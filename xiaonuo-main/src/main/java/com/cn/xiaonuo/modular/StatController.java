package com.cn.xiaonuo.modular;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.forms.entity.Forms;
import com.cn.xiaonuo.modular.forms.service.FormsService;
import com.cn.xiaonuo.modular.formsitemsdata.service.FormsItemsDataService;
import com.cn.xiaonuo.modular.libraries.entity.Libraries;
import com.cn.xiaonuo.modular.libraries.service.LibrariesService;
import com.cn.xiaonuo.modular.projects.entity.Projects;
import com.cn.xiaonuo.modular.projects.service.ProjectsService;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;

import cn.hutool.json.JSONObject;

@Controller
public class StatController {

	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private LibrariesService librariesService;
	
	@Resource
	private FormsService formsService;
	
	@Resource
	private ProjectsService projectsService;
	
	@Resource
	private FormsItemsDataService formsItemsDataService;
	
	/**
	 * @desc Stat
	 *
	 * @author quanxu
	 * @date 2021-11-15 23:10
	 */
	@ResponseBody
	@GetMapping("/stat/index")
	public ResponseData stat(HttpSession session, HttpServletRequest request) {
		
		// 创建json对象！
		JSONObject jsonObject = new JSONObject();
		
		// 团队数量统计！
		QueryWrapper<SysUser> qw1 = new QueryWrapper<SysUser>();
		qw1.lambda().eq(SysUser::getAccountType, "1"); // 管理账号！
		List<SysUser> sysUserList = sysUserService.list(qw1);
		jsonObject.set("statTeams", sysUserList.size());
		
		// 文献集合数量统计！
		QueryWrapper<Libraries> qw2 = new QueryWrapper<Libraries>();
		qw2.lambda().gt(Libraries::getId, 0); // 全部数据！
		List<Libraries> libraryList = librariesService.list(qw2);
		jsonObject.set("statCollections", libraryList.size());
		
		// 表单数量统计！
		QueryWrapper<Forms> qw3 = new QueryWrapper<Forms>();
		qw3.lambda().gt(Forms::getId, 0); // 全部数据！
		List<Forms> formList = formsService.list(qw3);
		jsonObject.set("statForms", formList.size());
		
		// 项目数量统计！
		QueryWrapper<Projects> qw4 = new QueryWrapper<Projects>();
		qw4.lambda().gt(Projects::getId, 0); // 全部数据！
		List<Projects> projectList = projectsService.list(qw4);
		jsonObject.set("statProjects", projectList.size());
		
		// 数据集数量统计！
		jsonObject.set("statDatasets", formsItemsDataService.getBatchCountAll());
		
		// 返回！
		return new SuccessResponseData(jsonObject);
		
	}
	
}
