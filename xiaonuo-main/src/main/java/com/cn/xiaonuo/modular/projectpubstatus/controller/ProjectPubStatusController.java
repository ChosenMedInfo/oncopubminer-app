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
package com.cn.xiaonuo.modular.projectpubstatus.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.modular.projectpubstatus.entity.ProjectPubStatus;
import com.cn.xiaonuo.modular.projectpubstatus.param.ProjectPubStatusParam;
import com.cn.xiaonuo.modular.projectpubstatus.service.ProjectPubStatusService;
import com.cn.xiaonuo.modular.projects.entity.Projects;
import com.cn.xiaonuo.modular.projects.service.ProjectsService;
import com.cn.xiaonuo.utils.SettingBase;

import cn.hutool.core.bean.BeanUtil;

/**
 * @func 项目文献状态表控制器
 *
 * @author Quan Xu
 * @date 2021-09-28 22:55:16
 */
@Controller
public class ProjectPubStatusController {

	private String PATH_PREFIX = "projectPubStatus/";

	@Resource
	private ProjectsService projectsService;
	
	@Resource
	private ProjectPubStatusService projectPubStatusService;
	
	@Resource
	private LogsService logsService;

	/**
	 * @func 项目文献状态表页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-28 22:55:16
	 */
	@Permission
	@GetMapping("/projectPubStatus/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 项目文献状态表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-28 22:55:16
	 */
	@GetMapping("/projectPubStatus/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 项目文献状态表-Done标注
	 *
	 * @author Quan Xu
	 * @date 2021-09-28 23:03:00
	 */
	@ResponseBody
	@PostMapping("/projectPubStatus/done")
	public ResponseData done(@RequestBody @Validated(ProjectPubStatusParam.add.class) ProjectPubStatusParam projectPubStatusParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		ProjectPubStatus projectPubStatus = new ProjectPubStatus();
		BeanUtil.copyProperties(projectPubStatusParam, projectPubStatus);
		projectPubStatus.setCreateTime(new Date());
		projectPubStatus.setCreateUser(sysLoginUser.getId());
		projectPubStatus.setCreateUserName(sysLoginUser.getAccount());
		projectPubStatus.setStatus(1);
		projectPubStatusService.saveOrUpdate(projectPubStatus);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeDonePub);
		logsParam.setOperateDesc("ID: '" + projectPubStatusParam.getId() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}
	
	/**
	 * @func 项目文献状态表-状态获取
	 *
	 * @author Quan Xu
	 * @date 2021-09-28 23:03:00
	 */
	@ResponseBody
	@GetMapping("/projectPubStatus/getStatus")
	public ResponseData getStatus(ProjectPubStatusParam projectPubStatusParam, HttpSession session, HttpServletRequest request) {
		ProjectPubStatus projectPubStatus = projectPubStatusService.getById(projectPubStatusParam.getId());
		String strResult = "-";
		if(projectPubStatus != null) {
			strResult = projectPubStatus.getStatus() + "";
		}else {
			// 需要判断当前用户是否关联上了当前项目！
			Projects project = projectsService.getById(projectPubStatusParam.getProjectId());
			if(project != null) strResult = "0"; // 有关联上项目，说明未提交！
		}
		return new SuccessResponseData(strResult);
	}

}
