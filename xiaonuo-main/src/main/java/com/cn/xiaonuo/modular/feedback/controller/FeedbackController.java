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
package com.cn.xiaonuo.modular.feedback.controller;

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
import com.cn.xiaonuo.core.util.IpAddressUtil;
import com.cn.xiaonuo.modular.feedback.entity.Feedback;
import com.cn.xiaonuo.modular.feedback.param.FeedbackParam;
import com.cn.xiaonuo.modular.feedback.service.FeedbackService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.utils.SettingBase;

/**
 * @func 用户反馈表控制器
 *
 * @author Quan Xu
 * @date 2021-11-15 15:25:19
 */
@Controller
public class FeedbackController {

	private String PATH_PREFIX = "feedback/";

	@Resource
	private FeedbackService feedbackService;
	
	@Resource
	private LogsService logsService;

	/**
	 * @func 用户反馈表页面
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	@Permission
	@GetMapping("/feedback/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 用户反馈表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	@GetMapping("/feedback/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询用户反馈表
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	@Permission
	@ResponseBody
	@GetMapping("/feedback/page")
	public PageResult<Feedback> page(FeedbackParam feedbackParam) {
		return feedbackService.page(feedbackParam);
	}

	/**
	 * @func 添加用户反馈表
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	@ResponseBody
	@PostMapping("/feedback/add")
	public ResponseData add(@RequestBody @Validated(FeedbackParam.add.class) FeedbackParam feedbackParam, HttpSession session, HttpServletRequest request) {
		
		// 添加IP相关信息！
		feedbackParam.setOperateIp(request.getRemoteHost());
		feedbackParam.setOperateAddr(IpAddressUtil.getAddressFkcoder(request));
		
		// 执行添加！
		feedbackService.add(feedbackParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeFeedback);
		logsParam.setOperateDesc("Name: '" + feedbackParam.getFeedbackName() + "', email: '" + feedbackParam.getFeedbackEmail() + "', subject: '" + feedbackParam.getFeedbackSubject() + "', message: '" + feedbackParam.getFeedbackMessage() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}

	/**
	 * @func 查看用户反馈表
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	@Permission
	@ResponseBody
	@GetMapping("/feedback/detail")
	public ResponseData detail(@Validated(FeedbackParam.detail.class) FeedbackParam feedbackParam) {
		return new SuccessResponseData(feedbackService.detail(feedbackParam));
	}

	/**
	 * @func 用户反馈表列表
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	@Permission
	@ResponseBody
	@GetMapping("/feedback/list")
	public ResponseData list(FeedbackParam feedbackParam) {
		return new SuccessResponseData(feedbackService.list(feedbackParam));
	}

}
