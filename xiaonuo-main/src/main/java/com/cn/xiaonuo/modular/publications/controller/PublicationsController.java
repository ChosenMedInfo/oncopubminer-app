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
package com.cn.xiaonuo.modular.publications.controller;

import java.util.ArrayList;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.librariespublications.entity.LibrariesPublications;
import com.cn.xiaonuo.modular.librariespublications.param.LibrariesPublicationsParam;
import com.cn.xiaonuo.modular.librariespublications.service.LibrariesPublicationsService;
import com.cn.xiaonuo.modular.publications.entity.Publications;
import com.cn.xiaonuo.modular.publications.param.PublicationsParam;
import com.cn.xiaonuo.modular.publications.service.PublicationsService;

/**
 * @func 文献列表控制器
 *
 * @author Quan Xu
 * @date 2021-09-14 16:33:06
 */
@Controller
public class PublicationsController {

	private String PATH_PREFIX = "publications/";

	@Resource
	private PublicationsService publicationsService;
	
	@Resource
	private LibrariesPublicationsService librariesPublicationsService;

	/**
	 * @func 文献列表页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@GetMapping("/publications/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 文献列表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@GetMapping("/publications/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@ResponseBody
	@GetMapping("/publications/page")
	public PageResult<Publications> page(PublicationsParam publicationsParam, HttpSession session, HttpServletRequest request) {
		return publicationsService.page(publicationsParam);
	}
	
	/**
	 * @func 查询文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 18:46:00
	 */
	@ResponseBody
	@GetMapping("/publications/searchByCollectionId")
	public PageResult<Publications> searchByCollectionId(LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strGroupName = sysLoginUser.getAccountGroup();
		PageResult<LibrariesPublications> pageLP = librariesPublicationsService.page(librariesPublicationsParam);
		PageResult<Publications> pageP = new PageResult<Publications>();
		List<LibrariesPublications> listLP = pageLP.getData();
		List<Publications> listP = new ArrayList<Publications>();
		for(LibrariesPublications lp : listLP) {
			QueryWrapper<Publications> qwp = new QueryWrapper<Publications>();
			qwp.lambda().eq(Publications::getPmid, lp.getPmid());
			qwp.lambda().eq(Publications::getGroupName, strGroupName); // 当前组内！
			List<Publications> pList = publicationsService.list(qwp);
			if(pList.size() >= 1) {
				listP.add(pList.get(0));
			}else if(pList.size() == 0) {
				Publications p = new Publications();
				p.setPmid(lp.getPmid() + "");
				listP.add(p);
			}
		}
		pageP.setCode(pageLP.getCode());
		pageP.setCount(pageLP.getCount());
		pageP.setLimit(pageLP.getLimit());
		pageP.setData(listP);
		pageP.setMsg(pageLP.getMsg());
		pageP.setPage(pageLP.getPage());
		return pageP;
	}

	/**
	 * @func 添加文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@ResponseBody
	@PostMapping("/publications/add")
	public ResponseData add(@RequestBody @Validated(PublicationsParam.add.class) PublicationsParam publicationsParam, HttpSession session, HttpServletRequest request) {
		publicationsService.add(publicationsParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 删除文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@ResponseBody
	@PostMapping("/publications/delete")
	public ResponseData delete(@RequestBody @Validated(PublicationsParam.delete.class) List<PublicationsParam> publicationsParamList, HttpSession session, HttpServletRequest request) {
		publicationsService.delete(publicationsParamList);
		return new SuccessResponseData();
	}

	/**
	 * @func 编辑文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@ResponseBody
	@PostMapping("/publications/edit")
	public ResponseData edit(@RequestBody @Validated(PublicationsParam.edit.class) PublicationsParam publicationsParam, HttpSession session, HttpServletRequest request) {
		publicationsService.edit(publicationsParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 查看文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@ResponseBody
	@GetMapping("/publications/detail")
	public ResponseData detail(@Validated(PublicationsParam.detail.class) PublicationsParam publicationsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(publicationsService.detail(publicationsParam));
	}

	/**
	 * @func 文献列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	@ResponseBody
	@GetMapping("/publications/list")
	public ResponseData list(PublicationsParam publicationsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(publicationsService.list(publicationsParam));
	}
	
	/**
	 * @func 文献检索
	 *
	 * @author Quan Xu
	 * @date 2021-09-16 17:49:00
	 */
	@ResponseBody
	@GetMapping("/publications/search")
	public ResponseData search(PublicationsParam publicationsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(publicationsService.list(publicationsParam));
	}

}
