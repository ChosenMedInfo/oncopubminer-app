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
package com.cn.xiaonuo.modular.errorreport.controller;

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
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.errorreport.entity.ErrorReport;
import com.cn.xiaonuo.modular.errorreport.param.ErrorReportParam;
import com.cn.xiaonuo.modular.errorreport.service.ErrorReportService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.utils.SettingBase;

/**
 * @func 错误报告表控制器
 *
 * @author Quan Xu
 * @date 2021-11-08 14:19:51
 */
@Controller
public class ErrorReportController {

	private String PATH_PREFIX = "errorReport/";

	@Resource
	private ErrorReportService errorReportService;
	
	@Resource
	private LogsService logsService;
	
	/**
	 * @func 错误报告表页面
	 *
	 * @author Quan Xu
	 * @date 2021-11-08 14:19:51
	 */
	@Permission
	@GetMapping("/errorReport/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 查询错误报告表
	 *
	 * @author Quan Xu
	 * @date 2021-11-08 14:19:51
	 */
	@Permission
	@ResponseBody
	@GetMapping("/errorReport/page")
	public PageResult<ErrorReport> page(ErrorReportParam errorReportParam) {
		return errorReportService.page(errorReportParam);
	}

	/**
	 * @func 添加错误报告表
	 *
	 * @author Quan Xu
	 * @date 2021-11-08 14:19:51
	 */
	@ResponseBody
	@PostMapping("/errorReport/add")
	public ResponseData add(@RequestBody @Validated(ErrorReportParam.add.class) ErrorReportParam errorReportParam, HttpSession session, HttpServletRequest request) {
		errorReportService.add(errorReportParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeErrorReport);
		logsParam.setOperateDesc("Error type: '" + errorReportParam.getErrorType() + "', error content: '" + errorReportParam.getErrorContent() + "', error offset: '" + errorReportParam.getErrorOffset() + "', PMID: '" + errorReportParam.getPmid() + "', PMCID: '" + errorReportParam.getPmcid() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}

}