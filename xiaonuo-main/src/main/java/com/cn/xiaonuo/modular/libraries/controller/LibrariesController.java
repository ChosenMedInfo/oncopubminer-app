/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

XiaoNuo采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改XiaoNuo源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/xiaonuo-layui
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/xiaonuo-layui
6.若您的项目无法满足以上几点，可申请商业授权，获取XiaoNuo商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package com.cn.xiaonuo.modular.libraries.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ErrorResponseData;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.libraries.entity.Libraries;
import com.cn.xiaonuo.modular.libraries.param.LibrariesParam;
import com.cn.xiaonuo.modular.libraries.service.LibrariesService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.modular.projects.entity.Projects;
import com.cn.xiaonuo.modular.projects.service.ProjectsService;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;
import com.cn.xiaonuo.utils.SettingBase;

import cn.hutool.log.Log;

/**
 * @func 文库列表控制器
 *
 * @author Quan Xu
 * @date 2021-09-14 15:23:57
 */
@Controller
public class LibrariesController {

	private String PATH_PREFIX = "libraries/";

	@Resource
	private LibrariesService librariesService;
	
	@Resource
	private ProjectsService projectsService;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private LogsService logsService;
	
	// 日志对象！
	private static final Log log = Log.get();

	/**
	 * @func 文库列表页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@Permission
	@GetMapping("/libraries/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 文库列表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@GetMapping("/libraries/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@ResponseBody
	@GetMapping("/libraries/page")
	public PageResult<Libraries> page(LibrariesParam librariesParam, HttpSession session, HttpServletRequest request) {
		return librariesService.page(librariesParam);
	}

	/**
	 * @func 添加文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@ResponseBody
	@PostMapping("/libraries/add")
	public ResponseData add(@RequestBody @Validated(LibrariesParam.add.class) LibrariesParam librariesParam, HttpSession session, HttpServletRequest request) {
		librariesService.add(librariesParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeAddLibrary);
		logsParam.setOperateDesc("Collection name: '" + librariesParam.getLibraryName() + "', collection desc: '" + librariesParam.getLibraryDesc() + "', remote keywords: '" + librariesParam.getKwsRemote() + "', local keywords: '" + librariesParam.getKwsLocal() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}

	/**
	 * @func 删除文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@ResponseBody
	@PostMapping("/libraries/delete")
	public ResponseData delete(@RequestBody @Validated(LibrariesParam.delete.class) List<LibrariesParam> librariesParamList, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [文献集合_删除] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strTips = "";
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在删除文献集合：");
		
		// 执行删除！
		for(LibrariesParam lp : librariesParamList) {
			Libraries l = librariesService.getById(lp.getId()); // 查询信息！
			Integer checkSuccess = 1; // 默认失败！
			String strOperateDesc = "Collection name: '" + l.getLibraryName() + "', collection desc: '" + l.getLibraryDesc() + "', remote keywords: '" + l.getKwsRemote() + "', local keywords: '" + l.getKwsLocal() + "'";
			
			// 判断当前文库是否关联有未删除的项目！
			LambdaQueryWrapper<Projects> lqw = new LambdaQueryWrapper<Projects>();
			lqw.eq(Projects::getLibraryId, lp.getId());
			lqw.ne(Projects::getStatus, "1"); // 未删除的！
			List<Projects> projectList = projectsService.list(lqw);
			String strThisTip = "";
			if(projectList != null && projectList.size() != 0) {
				String strProjects = "";
				for(Projects p : projectList) {
					strProjects = strProjects.equals("") ? p.getProjectName() : strProjects + "; " + p.getProjectName();
				}
				strOperateDesc += ", filure reason: related undeleted project(s) exists: '" + strProjects + "'!";
				strThisTip = "Collection '" + l.getLibraryName() + "' deleted failed: related project(s) exists: '" + strProjects + "!";
			}else { // 没有关联项目，执行删除！
				l.setStatus(1);
				librariesService.updateById(l); // 执行逻辑删除！
				checkSuccess = 0; // 成功！
				strOperateDesc += "..";
				strThisTip = "Collection '" + l.getLibraryName() + "' deleted success!";
			}
			strTips = strTips.equals("") ? strThisTip : strTips + "\n" + strThisTip;
			
			// 记录日志！
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeDelLibrary);
			logsParam.setOperateDesc(strOperateDesc);
			logsParam.setOperateSuccess(checkSuccess);
			logsService.log(logsParam, session, request);
		}
		
		return new SuccessResponseData(strTips);
	}

	/**
	 * @func 编辑文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@ResponseBody
	@PostMapping("/libraries/edit")
	public ResponseData edit(@RequestBody @Validated(LibrariesParam.edit.class) LibrariesParam librariesParam, HttpSession session, HttpServletRequest request) {
		librariesService.edit(librariesParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeEditLibrary);
		logsParam.setOperateDesc("After edit: collection name: '" + librariesParam.getLibraryName() + "', collection desc: '" + librariesParam.getLibraryDesc() + "', remote keywords: '" + librariesParam.getKwsRemote() + "', local keywords: '" + librariesParam.getKwsLocal() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}

	/**
	 * @func 文库列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	@ResponseBody
	@GetMapping("/libraries/list")
	public ResponseData list(LibrariesParam librariesParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(librariesService.list(librariesParam));
	}
	
	/**
	 * @func 文库列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-10-12 15:52:00
	 */
	@ResponseBody
	@GetMapping("/libraries/listByPmidAndGroupName")
	public ResponseData listByPmidAndGroupName(Long pmid, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		return new SuccessResponseData(librariesService.listByPmidAndGroupName(pmid, sysLoginUser.getAccountGroup()));
	}
	
	/**
	 * @func 文库列表封存
	 *
	 * @author Quan Xu
	 * @date 2021-09-29 13:39:00
	 */
	@ResponseBody
	@GetMapping("/libraries/seal")
	public ResponseData seal(LibrariesParam librariesParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [文库列表_封存] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在封存文库列表：");
		
		// 更新状态！
		Libraries libraries = librariesService.getById(librariesParam.getId());
		libraries.setStatus(2);
		librariesService.updateById(libraries);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeSealLibrary);
		logsParam.setOperateDesc("Collection name: '" + libraries.getLibraryName() + "', collection desc: '" + libraries.getLibraryDesc() + "', remote keywords: '" + libraries.getKwsRemote() + "', local keywords: '" + libraries.getKwsLocal() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}
	
}
