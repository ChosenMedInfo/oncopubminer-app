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
package com.cn.xiaonuo.modular.publicationsview.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.xiaonuo.core.annotion.BusinessLog;
import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.librariespublications.param.LibrariesPublications4PubPage;
import com.cn.xiaonuo.modular.publicationsview.param.PublicationsViewParam;
import com.cn.xiaonuo.modular.publicationsview.service.PublicationsViewService;

/**
 * @func 文献视图控制器
 *
 * @author Quan Xu
 * @date 2021-11-25 10:16:01
 */
@Controller
public class PublicationsViewController {

	private String PATH_PREFIX = "publicationsView/";

	@Resource
	private PublicationsViewService publicationsViewService;

	/**
	 * @func 文献视图页面
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:16:01
	 */
	@Permission
	@GetMapping("/publicationsView/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 文献视图表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:16:01
	 */
	@GetMapping("/publicationsView/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询文献视图
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:16:01
	 */
	@ResponseBody
	@GetMapping("/publicationsView/pubPage")
	@BusinessLog(title = "文献视图_查询", opType = LogAnnotionOpTypeEnum.QUERY)
	public PageResult<LibrariesPublications4PubPage> pubPage(PublicationsViewParam publicationsViewParam) {
		return publicationsViewService.pubPage(publicationsViewParam);
	}

	/**
	 * @func 查看文献视图
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:16:01
	 */
	@ResponseBody
	@GetMapping("/publicationsView/detail")
	@BusinessLog(title = "文献视图_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
	public ResponseData detail(@Validated(PublicationsViewParam.detail.class) PublicationsViewParam publicationsViewParam) {
		return new SuccessResponseData(publicationsViewService.detail(publicationsViewParam));
	}

	/**
	 * @func 文献视图列表
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:16:01
	 */
	@ResponseBody
	@GetMapping("/publicationsView/list")
	@BusinessLog(title = "文献视图_列表", opType = LogAnnotionOpTypeEnum.QUERY)
	public ResponseData list(PublicationsViewParam publicationsViewParam) {
		return new SuccessResponseData(publicationsViewService.list(publicationsViewParam));
	}

}
