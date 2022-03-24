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
package com.cn.xiaonuo.modular.projects.controller;

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
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ErrorResponseData;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.forms.param.FormsParam;
import com.cn.xiaonuo.modular.libraries.param.LibrariesParam;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.modular.projects.entity.Projects;
import com.cn.xiaonuo.modular.projects.param.ProjectsParam;
import com.cn.xiaonuo.modular.projects.service.ProjectsService;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.param.SysUserParam;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;
import com.cn.xiaonuo.utils.SettingBase;

import cn.hutool.log.Log;

/**
 * @func 项目列表控制器
 *
 * @author Quan Xu
 * @date 2021-09-07 17:00:34
 */
@Controller
public class ProjectsController {

	private String PATH_PREFIX = "projects/";

	@Resource
	private ProjectsService projectsService;
	
	@Resource
	private LogsService logsService;
	
	@Resource
	private SysUserService sysUserService;
	
	// 日志对象！
	private static final Log log = Log.get();
	
	/**
	 * @func 项目列表页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@Permission
	@GetMapping("/projects/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 项目列表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@GetMapping("/projects/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询项目列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@ResponseBody
	@GetMapping("/projects/page")
	public PageResult<Projects> page(ProjectsParam projectsParam, HttpSession session, HttpServletRequest request) {
		return projectsService.page(projectsParam);
	}

	/**
	 * @func 添加项目列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@ResponseBody
	@PostMapping("/projects/add")
	public ResponseData add(@RequestBody @Validated(ProjectsParam.add.class) ProjectsParam projectsParam, HttpSession session, HttpServletRequest request) {
		Long id = IdWorker.getId();
		projectsParam.setId(id);
		projectsService.add(projectsParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeAddProject);
		logsParam.setOperateDesc("Project name: '" + projectsParam.getProjectName() + "', project desc: '" + projectsParam.getProjectDesc() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}

	/**
	 * @func 删除项目列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@ResponseBody
	@PostMapping("/projects/delete")
	public ResponseData delete(@RequestBody @Validated(ProjectsParam.delete.class) List<ProjectsParam> projectsParamList, HttpSession session, HttpServletRequest request) {
		
		// 记录日志！
		for(ProjectsParam pp : projectsParamList) {
			
			// 2021-12-29（V0022-01.01.06-08）：demoAdmin用户组的特定项目不允许被删除！由于项目没有删除，那么表单、文库也就被控制了不被删除了！
			if(pp.getId().equals(1476086455930454018L)) { // 待根据最终的项目进行修改！！！
				
				LogsParam logsParam = new LogsParam();
				logsParam.setOperateType(SettingBase.strOperateTypeDelProject);
				logsParam.setOperateDesc("用户尝试删除示例项目..");
				logsParam.setOperateSuccess(SettingBase.intSuccessFalse);
				logsService.log(logsParam, session, request);
				
				return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Current demo project CANNOT be deleted!");
			}
			
			Projects p = projectsService.getById(pp.getId());
			p.setStatus(1);
			projectsService.updateById(p); // 先查询后删除：逻辑删除！
			
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeDelProject);
			logsParam.setOperateDesc("Project name: '" + p.getProjectName() + "', project desc: '" + p.getProjectDesc() + "'..");
			logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
			logsService.log(logsParam, session, request);
		}
		
		return new SuccessResponseData();
	}

	/**
	 * @func 编辑项目列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@ResponseBody
	@PostMapping("/projects/edit")
	public ResponseData edit(@RequestBody @Validated(ProjectsParam.edit.class) ProjectsParam projectsParam, HttpSession session, HttpServletRequest request) {
		projectsService.edit(projectsParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeEditProject);
		logsParam.setOperateDesc("Project name after edit: '" + projectsParam.getProjectName() + "', project desc after edit: '" + projectsParam.getProjectDesc() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}

	/**
	 * @func 查看项目列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@ResponseBody
	@GetMapping("/projects/detail")
	public ResponseData detail(@Validated(ProjectsParam.detail.class) ProjectsParam projectsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(projectsService.detail(projectsParam));
	}

	/**
	 * @func 项目列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	@ResponseBody
	@GetMapping("/projects/list")
	public ResponseData list(ProjectsParam projectsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(projectsService.list(projectsParam));
	}
	
	/**
	 * @func 项目封存
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 14:04:00
	 */
	@ResponseBody
	@PostMapping("/projects/seal")
	public ResponseData seal(@RequestBody ProjectsParam projectsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [项目_封存] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在封存项目：");
		
		// 更新状态！
		Projects projects = projectsService.getById(projectsParam.getId());
		projects.setStatus(2);
		projectsService.updateById(projects);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeSealProject);
		logsParam.setOperateDesc("Project name: '" + projectsParam.getProjectName() + "', project desc: '" + projectsParam.getProjectDesc() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}
	
	/**
	 * @func 判断某个member是否允许删除
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 10:22:00
	 */
	@ResponseBody
	@PostMapping("/projects/checkDelMember")
	public ResponseData checkDelMember(@RequestBody SysUserParam sysUserParam, HttpSession session, HttpServletRequest request) {
		LambdaQueryWrapper<Projects> qw = new LambdaQueryWrapper<Projects>();
		qw.like(Projects::getMemberIds, sysUserParam.getId()); // 成员列表包含当前待删除成员！
		qw.ne(Projects::getStatus, 1); // 非删除状态！
		List<Projects> projectList = projectsService.list(qw);
		return new SuccessResponseData(projectList.size() > 0 ? "CNBD" : "CBD"); // 检索出结果了，表示不允许被删除，否则可以！
	}
	
	/**
	 * @func 判断某个library是否允许删除
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 12:38:00
	 */
	@ResponseBody
	@PostMapping("/projects/checkDelLibrary")
	public ResponseData checkDelLibrary(@RequestBody LibrariesParam librariesParam, HttpSession session, HttpServletRequest request) {
		LambdaQueryWrapper<Projects> qw = new LambdaQueryWrapper<Projects>();
		qw.eq(Projects::getLibraryId, librariesParam.getId());
		qw.ne(Projects::getStatus, 1); // 非删除状态！
		List<Projects> projectList = projectsService.list(qw);
		return new SuccessResponseData(projectList.size() > 0 ? "CNBD" : "CBD"); // 检索出结果了，表示不允许被删除，否则可以！
	}
	
	/**
	 * @func 判断某个form是否允许删除
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 12:38:00
	 */
	@ResponseBody
	@PostMapping("/projects/checkDelForm")
	public ResponseData checkDelForm(@RequestBody FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		LambdaQueryWrapper<Projects> qw = new LambdaQueryWrapper<Projects>();
		qw.eq(Projects::getFormId, formsParam.getId());
		qw.ne(Projects::getStatus, 1); // 非删除状态！
		List<Projects> projectList = projectsService.list(qw);
		return new SuccessResponseData(projectList.size() > 0 ? "CNBD" : "CBD"); // 检索出结果了，表示不允许被删除，否则可以！
	}

}
