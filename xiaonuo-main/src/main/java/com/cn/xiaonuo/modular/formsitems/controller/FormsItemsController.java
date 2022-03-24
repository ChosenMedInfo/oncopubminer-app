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
package com.cn.xiaonuo.modular.formsitems.controller;

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

import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ErrorResponseData;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.forms.entity.Forms;
import com.cn.xiaonuo.modular.forms.param.FormsParam;
import com.cn.xiaonuo.modular.forms.service.FormsService;
import com.cn.xiaonuo.modular.formsitems.entity.FormsItems;
import com.cn.xiaonuo.modular.formsitems.param.FormsItemsParam;
import com.cn.xiaonuo.modular.formsitems.service.FormsItemsService;
import com.cn.xiaonuo.modular.formsitemsoptions.param.FormsItemsOptionsParam;
import com.cn.xiaonuo.modular.formsitemsoptions.service.FormsItemsOptionsService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;
import com.cn.xiaonuo.utils.SettingBase;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;

/**
 * @func 表单项列表控制器
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:19
 */
@Controller
public class FormsItemsController {

	private String PATH_PREFIX = "formsItems/";

	@Resource
	private FormsService formsService;
	
	@Resource
	private FormsItemsService formsItemsService;
	
	@Resource
	private FormsItemsOptionsService formsItemsOptionsService;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private LogsService logsService;
	
	// 日志对象！
	private static final Log log = Log.get();

	/**
	 * @func 表单项列表页面
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@GetMapping("/formsItems/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 表单项列表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@GetMapping("/formsItems/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@ResponseBody
	@GetMapping("/formsItems/page")
	public PageResult<FormsItems> page(FormsItemsParam formsItemsParam) {
		return formsItemsService.page(formsItemsParam);
	}

	/**
	 * @func 添加表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@ResponseBody
	@PostMapping("/formsItems/add")
	public ResponseData add(@RequestBody @Validated(FormsItemsParam.add.class) FormsItemsParam formsItemsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [表单项列表_增加] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
			
			// 验证表单是否已经封存！
			Forms forms = formsService.getById(formsItemsParam.getFormId());
			if(forms == null) {
				return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Data collection form not exists!");
			}else if(forms.getStatus() == 2) {
				return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Data collection sealed, cannot be changed!");
			}
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在增加表单项列表：");
		
		// 验证通过，执行添加！
		formsItemsParam.setStatus(0);
		formsItemsParam.setCreateUserName(sysLoginUser.getAccount());
		
		// 执行添加！
		formsItemsService.add(formsItemsParam);
		log.info(strLogStarter + sysLoginUser.getAccount() + "添加成功：‘" + formsItemsParam.toString() + "’..");
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeAddFormItem);
		logsParam.setOperateDesc("Form item info: '" + formsItemsParam.toString() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}

	/**
	 * @func 删除表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@ResponseBody
	@PostMapping("/formsItems/delete")
	public ResponseData delete(@RequestBody @Validated(FormsItemsParam.delete.class) List<FormsItemsParam> formsItemsParamList, HttpSession session, HttpServletRequest request) {
		
		// 记录日志！
		for(FormsItemsParam fip : formsItemsParamList) {
			FormsItems fi = formsItemsService.getById(fip.getId());
			formsItemsService.removeById(fip.getId()); // 先查询后删除！
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeDelFormItem);
			logsParam.setOperateDesc("Deleted form item info: '" + fi.toString() + "'..");
			logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
			logsService.log(logsParam, session, request);
		}
		
		return new SuccessResponseData();
	}

	/**
	 * @func 编辑表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@ResponseBody
	@PostMapping("/formsItems/edit")
	public ResponseData edit(@RequestBody @Validated(FormsItemsParam.edit.class) FormsItemsParam formsItemsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [表单项列表_编辑] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
			
			// 验证表单是否已经封存！
			Forms forms = formsService.getById(formsItemsParam.getFormId());
			if(forms == null) {
				return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Data collection form not exists!");
			}else if(forms.getStatus() == 2) {
				return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Data collection sealed, cannot be changed!");
			}
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在编辑表单项列表：");
		
		// 执行编辑！
		formsItemsService.edit(formsItemsParam);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeEditFormItem);
		logsParam.setOperateDesc("Form item info after edit: '" + formsItemsParam.toString() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}

	/**
	 * @func 查看表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@ResponseBody
	@GetMapping("/formsItems/detail")
	public ResponseData detail(@Validated(FormsItemsParam.detail.class) FormsItemsParam formsItemsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsItemsService.detail(formsItemsParam));
	}

	/**
	 * @func 表单项列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	@ResponseBody
	@PostMapping("/formsItems/list")
	public ResponseData list(@RequestBody FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [表单项列表_列表] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		String strUser = "游客";
		if(sysLoginUser == null) {
			//return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			strUser = sysLoginUser.getAccount();
			//SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			//if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + strUser+ "正在获取表单（id：“" + formsParam.getId() + "”）表项列表：");
		
		// 验证通过，执行查询！
		FormsItemsParam formsItemsParam = new FormsItemsParam();
		formsItemsParam.setStatus(0); // 只查询未被删除的！
		formsItemsParam.setFormId(formsParam.getId());
		List<FormsItems> formsItemsList = formsItemsService.list(formsItemsParam);
		log.info(strLogStarter + strUser+ "表项数量为：“" + formsItemsList.size() + "”...");
		
		// 遍历items，获取每个items的选项！
		List<FormsItemsParam> formsItemsParamList = new ArrayList<FormsItemsParam>(); 
		for(FormsItems formsItem : formsItemsList) {
			FormsItemsParam formsItemsParamNew = new FormsItemsParam();
			BeanUtil.copyProperties(formsItem, formsItemsParamNew);
			if(formsItem.getItemOptionType() != null && formsItem.getItemOptionType() == 1) {
				FormsItemsOptionsParam formsItemsOptionsParam = new FormsItemsOptionsParam();
				formsItemsOptionsParam.setStatus(0);
				formsItemsOptionsParam.setItemId(formsItem.getId());
				formsItemsParamNew.setFormsItemOptionsList(formsItemsOptionsService.list(formsItemsOptionsParam));
			}
			formsItemsParamList.add(formsItemsParamNew);
		}
		
		// 返回！
		return new SuccessResponseData(formsItemsParamList);
		
	}

}
