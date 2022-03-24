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
package com.cn.xiaonuo.modular.formsitemsoptions.controller;

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

import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.formsitemsoptions.entity.FormsItemsOptions;
import com.cn.xiaonuo.modular.formsitemsoptions.param.FormsItemsOptionsParam;
import com.cn.xiaonuo.modular.formsitemsoptions.service.FormsItemsOptionsService;

/**
 * @func 表单项选项列表控制器
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:16
 */
@Controller
public class FormsItemsOptionsController {

	private String PATH_PREFIX = "formsItemsOptions/";

	@Resource
	private FormsItemsOptionsService formsItemsOptionsService;

	/**
	 * @func 表单项选项列表页面
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@Permission
	@GetMapping("/formsItemsOptions/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 表单项选项列表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@GetMapping("/formsItemsOptions/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询表单项选项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@Permission
	@ResponseBody
	@GetMapping("/formsItemsOptions/page")
	public PageResult<FormsItemsOptions> page(FormsItemsOptionsParam formsItemsOptionsParam, HttpSession session, HttpServletRequest request) {
		return formsItemsOptionsService.page(formsItemsOptionsParam);
	}

	/**
	 * @func 添加表单项选项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@Permission
	@ResponseBody
	@PostMapping("/formsItemsOptions/add")
	public ResponseData add(@RequestBody @Validated(FormsItemsOptionsParam.add.class) FormsItemsOptionsParam formsItemsOptionsParam, HttpSession session, HttpServletRequest request) {
		formsItemsOptionsService.add(formsItemsOptionsParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 删除表单项选项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@Permission
	@ResponseBody
	@PostMapping("/formsItemsOptions/delete")
	public ResponseData delete(@RequestBody @Validated(FormsItemsOptionsParam.delete.class) List<FormsItemsOptionsParam> formsItemsOptionsParamList, HttpSession session, HttpServletRequest request) {
		formsItemsOptionsService.delete(formsItemsOptionsParamList);
		return new SuccessResponseData();
	}

	/**
	 * @func 编辑表单项选项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@Permission
	@ResponseBody
	@PostMapping("/formsItemsOptions/edit")
	public ResponseData edit(@RequestBody @Validated(FormsItemsOptionsParam.edit.class) FormsItemsOptionsParam formsItemsOptionsParam, HttpSession session, HttpServletRequest request) {
		formsItemsOptionsService.edit(formsItemsOptionsParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 查看表单项选项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@Permission
	@ResponseBody
	@GetMapping("/formsItemsOptions/detail")
	public ResponseData detail(@Validated(FormsItemsOptionsParam.detail.class) FormsItemsOptionsParam formsItemsOptionsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsItemsOptionsService.detail(formsItemsOptionsParam));
	}

	/**
	 * @func 表单项选项列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	@ResponseBody
	@GetMapping("/formsItemsOptions/list")
	public ResponseData list(FormsItemsOptionsParam formsItemsOptionsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsItemsOptionsService.list(formsItemsOptionsParam));
	}

}
