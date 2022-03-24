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
package com.cn.xiaonuo.modular.funccomments.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.funccomments.entity.FuncComments;
import com.cn.xiaonuo.modular.funccomments.param.FuncCommentsParam;
import com.cn.xiaonuo.modular.funccomments.service.FuncCommentsService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.utils.SettingBase;

/**
 * @func 文献备注表控制器
 *
 * @author Quan Xu
 * @date 2021-09-18 11:42:14
 */
@Controller
public class FuncCommentsController {

	private String PATH_PREFIX = "funcComments/";

	@Resource
	private FuncCommentsService funcCommentsService;
	
	@Resource
	private LogsService logsService;

	/**
	 * @func 文献备注表页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:14
	 */
	@GetMapping("/funcComments/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 文献备注表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:14
	 */
	@GetMapping("/funcComments/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询文献备注表
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:14
	 */
	@ResponseBody
	@GetMapping("/funcComments/page")
	public PageResult<FuncComments> page(FuncCommentsParam funcCommentsParam, HttpSession session, HttpServletRequest request) {
		return funcCommentsService.page(funcCommentsParam);
	}

	/**
	 * @func 添加文献备注表
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:14
	 */
	@ResponseBody
	@PostMapping("/funcComments/add")
	public ResponseData add(@RequestBody @Validated(FuncCommentsParam.add.class) FuncCommentsParam funcCommentsParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strGroupName = sysLoginUser.getAccountGroup();
		funcCommentsParam.setCreateUserName(sysLoginUser.getAccount());
		funcCommentsParam.setGroupName(strGroupName);
		funcCommentsService.add(funcCommentsParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeFuncComment);
		logsParam.setOperateDesc("Commented PMID: '" + funcCommentsParam.getPmid() + "', comments content: '" + funcCommentsParam.getComments() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		return new SuccessResponseData();
	}
	
	/**
	 * @func 统计文献备注
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 17:11:00
	 */
	@ResponseBody
	@PostMapping("/funcComments/stat")
	public ResponseData stat(@RequestBody FuncCommentsParam funcCommentsParam, HttpSession session, HttpServletRequest request) {
		
		// 检索当前文献的所有备注数量！
		QueryWrapper<FuncComments> qwfc = new QueryWrapper<FuncComments>();
		qwfc.lambda().eq(FuncComments::getPmid, funcCommentsParam.getPmid());
		
		// 返回！
		return new SuccessResponseData(funcCommentsService.list(qwfc).size());
		
	}

}
