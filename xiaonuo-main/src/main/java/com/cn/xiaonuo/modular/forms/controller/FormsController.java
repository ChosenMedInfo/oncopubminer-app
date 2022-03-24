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
package com.cn.xiaonuo.modular.forms.controller;

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
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
import com.cn.xiaonuo.modular.formsitems.service.FormsItemsService;
import com.cn.xiaonuo.modular.formsitemsoptions.entity.FormsItemsOptions;
import com.cn.xiaonuo.modular.formsitemsoptions.service.FormsItemsOptionsService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;
import com.cn.xiaonuo.utils.SettingBase;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;

/**
 * @func 数据收集表单列表控制器
 *
 * @author Quan Xu
 * @date 2021-07-08 10:34:51
 */
@Controller
public class FormsController {

	private String PATH_PREFIX = "forms/";

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
	 * @func 数据收集表单列表页面
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	@GetMapping("/forms/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 数据收集表单列表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	@GetMapping("/forms/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	@ResponseBody
	@GetMapping("/forms/page")
	public PageResult<Forms> page(FormsParam formsParam) {
		
		// 全局变量！
		String strLogStarter      = ">>> [数据收集表单列表_查询] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			// 确保检索不到结果即可！
			formsParam.setGroupId(0L);
			formsParam.setGroupName("###QUANXUISAHANDSOMEBOY###");
			log.info(strLogStarter + "未登录用户正在查询数据收集表单：");
		} else {
			// 只能检索组内表单！
			formsParam.setGroupId(sysLoginUser.getId());
			formsParam.setGroupName(sysLoginUser.getAccount() + SettingBase.strGroupSuffix);
			log.info(strLogStarter + sysLoginUser.getAccount() + "正在查询数据收集表单：");
		}
		
//		// 执行检索及检索结果的处理！
//		PageResult<Forms> pageResultForms    = formsService.page(formsParam);
//		PageResult<Forms> pageResultFormsNew = new PageResult<Forms>();
//		List<Forms> formsList = pageResultForms.getData();
//		List<Forms> formsListNew = new ArrayList<Forms>();
//		for(Forms form : formsList) {
//			FormsParam formsParamNew = new FormsParam();
//			BeanUtil.copyProperties(form, formsParamNew);
//			form.setFormName(form.getFormName().replace(form.getGroupName() + SettingBase.strGroupFormNameSplitter, "")); // 将后台添加的组名去掉！
//			formsListNew.add(form);
//		}
//		BeanUtil.copyProperties(pageResultForms, pageResultFormsNew);
//		pageResultFormsNew.setData(formsListNew);
//		log.info(strLogStarter + "查询结果数量：‘" + formsListNew.size() + "’..");
		
		// 返回处理后的结果！
		return formsService.page(formsParam);
		
	}

	/**
	 * @func 添加数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	@ResponseBody
	@PostMapping("/forms/add")
	public ResponseData add(@RequestBody @Validated(FormsParam.add.class) FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [数据收集表单列表_增加] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser != null && sysUser.getAccountType() != null && sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在增加数据收集表单：");
		
		// 相关变量完善！
		String strGroupName = sysLoginUser.getAccountGroup();
		String strFormName  = strGroupName + SettingBase.strGroupFormNameSplitter + formsParam.getFormName();
		
//		// 验证同名表单是否存在！
//		if(formsService.checkFormNameExist(strFormName).size() != 0) {
//			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Duplicate form name '" + formsParam.getFormName() + "'!");
//		}
		
		// 验证通过，执行添加！
		formsParam.setStatus(0);
		formsParam.setCreateUserName(sysLoginUser.getAccount());
		formsParam.setGroupName(strGroupName);
		//formsParam.setFormName(strFormName); // 表单名称需要带上组名，以免不同组内表单重名！
		formsParam.setFormName(formsParam.getFormName()); // 2021-11-19：不需要验证重复！
		
		// 执行添加！
		formsService.add(formsParam);
		log.info(strLogStarter + sysLoginUser.getAccount() + "添加成功：‘" + formsParam.toString() + "’..");
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeAddForm);
		logsParam.setOperateDesc("Form name: '" + strFormName + "', form desc: '" + formsParam.getFormDesc() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}
	
	/**
	 * @func 拷贝数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-24 22:03:00
	 */
	@ResponseBody
	@PostMapping("/forms/copy")
	public ResponseData copy(@RequestBody @Validated(FormsParam.add.class) FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [数据收集表单列表_拷贝] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser != null && sysUser.getAccountType() != null && sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在拷贝数据收集表单：");
		
		// 获取被拷贝表单信息！
		Forms formsOri = formsService.getById(formsParam.getId());
		
		// 相关变量完善！
		String strGroupName = sysLoginUser.getAccountGroup();
		String strFormName  = formsOri.getFormName() + SettingBase.strGroupFormNameSplitter + "copy" + SettingBase.strGroupFormNameSplitter + UsuallyUsedUtils.GetDateTimeNumWithShortLineStr();
		
		// 验证同名表单是否存在！
		if(formsService.checkFormNameExist(strFormName).size() != 0) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Duplicate form name '" + formsParam.getFormName() + "'!");
		}
		
		// 验证通过，执行表单基本信息添加！
		Long idFormNew = IdWorker.getId(); // 创建新的ID！
		FormsParam formsParamNew = new FormsParam();
		BeanUtil.copyProperties(formsOri, formsParamNew);
		formsParamNew.setId(idFormNew); // 采用全新生成的id！
		formsParamNew.setStatus(0); // 状态归0！
		formsParamNew.setCreateUserName(sysLoginUser.getAccount());
		formsParamNew.setGroupId(sysLoginUser.getId()); // 组id同为组管理员用户id！
		formsParamNew.setGroupName(strGroupName);
		formsParamNew.setFormName(strFormName); // 表单名称需要带上组名，以免不同组内表单重名！
		formsParamNew.setFormDesc("-"); // 清空表单说明内容！
		
		// 执行表单信息添加！
		formsService.add(formsParamNew);
		log.info(strLogStarter + sysLoginUser.getAccount() + "表单信息拷贝成功：‘" + formsParam.toString() + "’，下一步执行表项及选项拷贝..");
		
		// 检索原表单的表项及表项选项，并执行替换后，生成新的数据！
		QueryWrapper<FormsItems> fiqw = new QueryWrapper<FormsItems>();
		fiqw.lambda().eq(FormsItems::getFormId, formsParam.getId());
		List<FormsItems> fiList = formsItemsService.list(fiqw);
		int intTotalOptions = 0;
		for(FormsItems fi : fiList) {
			// 表项拷贝！
			Long idItemNew = IdWorker.getId();
			FormsItems formsItemsNew = new FormsItems(); // 经尝试，这里不能用Params的方式执行添加，这会导致id设置之后与原值不等！！！具体原因未能成功排查！
			BeanUtil.copyProperties(fi, formsItemsNew);
			formsItemsNew.setId(idItemNew); // 新的itemId！
			formsItemsNew.setFormId(formsParamNew.getId()); // 新的formId！
			formsItemsNew.setCreateUserName(sysLoginUser.getAccount());
			formsItemsService.saveOrUpdate(formsItemsNew);
			
			// 表项选项拷贝！
			QueryWrapper<FormsItemsOptions> fioqw = new QueryWrapper<FormsItemsOptions>();
			fioqw.lambda().eq(FormsItemsOptions::getItemId, fi.getId());
			List<FormsItemsOptions> fioList = formsItemsOptionsService.list(fioqw);
			intTotalOptions += fioList.size();
			for(FormsItemsOptions fio : fioList) {
				Long idItemOptionsNew = IdWorker.getId();
				FormsItemsOptions formsItemsOptionsNew = new FormsItemsOptions();
				BeanUtil.copyProperties(fio, formsItemsOptionsNew);
				formsItemsOptionsNew.setId(idItemOptionsNew); // 新的optionId！
				formsItemsOptionsNew.setItemId(formsItemsNew.getId()); // 新的itemId！
				formsItemsOptionsNew.setCreateUserName(sysLoginUser.getAccount());
				formsItemsOptionsService.saveOrUpdate(formsItemsOptionsNew);
			}
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "表项相关数据拷贝成功，表项共计‘" + fiList.size() + "’个，表项选项数据共计‘" + intTotalOptions + "’个..");
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeCopyForm);
		logsParam.setOperateDesc("New form name: '" + strFormName + "', items count: '" + fiList.size() + "', options count: '" + intTotalOptions + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}

	/**
	 * @func 删除数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	@ResponseBody
	@PostMapping("/forms/delete")
	public ResponseData delete(@RequestBody @Validated(FormsParam.delete.class) List<FormsParam> formsParamList, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [数据收集表单列表_删除] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在删除数据收集表单：");
		
		// 验证通过，执行添加！
		String strFormList = ""; // 记录删除的表单的名称！
		for(FormsParam formsParam : formsParamList) {
			Forms forms = formsService.getById(formsParam.getId());
			BeanUtil.copyProperties(forms, formsParam);
			String strFormNameAfterDeleted = formsParam.getFormName() + SettingBase.strGroupFormNameSplitter + UsuallyUsedUtils.GetDateTimeNumWithShortLineStr();
			formsParam.setFormName(strFormNameAfterDeleted); // 更改一下名称，避免删除后无法再添加同名表单！
			formsParam.setUpdateUserName(sysLoginUser.getAccount());
			formsParam.setStatus(1); // 标记为删除状态！
			formsService.edit(formsParam); // 逻辑删除！
			strFormList = strFormList.equals("") ? formsParam.getFormName() : strFormList + " | " + formsParam.getFormName();
			
			// 记录日志！
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeDelForm);
			logsParam.setOperateDesc("Form id: '" + forms.getId() + "', form name: '" + forms.getFormName() + "', after deleted name: '" + strFormNameAfterDeleted + "'..");
			logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
			logsService.log(logsParam, session, request);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "删除成功：‘" + strFormList + "’..");
		
		// 返回！
		return new SuccessResponseData();
		
	}

	/**
	 * @func 编辑数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	@ResponseBody
	@PostMapping("/forms/edit")
	public ResponseData edit(@RequestBody @Validated(FormsParam.edit.class) FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [数据收集表单列表_编辑] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在编辑数据收集表单：");
		
		// 相关变量完善！
		String strGroupName = sysLoginUser.getAccountGroup();
		String strFormName  = strGroupName + SettingBase.strGroupFormNameSplitter + formsParam.getFormName();
		
		// 验证同名表单是否存在！
		List<Forms> listCheckFormNameExist = formsService.checkFormNameExist(strFormName);
		if(listCheckFormNameExist.size() > 1 || (listCheckFormNameExist.size() == 1 && listCheckFormNameExist.get(0).getId() != formsParam.getId())) {
			
			// 记录日志！
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeEditForm);
			logsParam.setOperateDesc("Form id: '" + formsParam.getId() + "', form name: '" + strFormName + "' edit failure: 'form name duplicated'..");
			logsParam.setOperateSuccess(SettingBase.intSuccessFalse);
			logsService.log(logsParam, session, request);
			
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Duplicate form name '" + formsParam.getFormName() + "'!");
		}
		
		// 验证通过，执行添加！
		formsParam.setStatus(0);
		formsParam.setCreateUserName(sysLoginUser.getAccount());
		formsParam.setUpdateUserName(sysLoginUser.getAccount());
		formsParam.setGroupName(strGroupName);
		formsParam.setFormName(strFormName); // 表单名称需要带上组名，以免不同组内表单重名！
		
		// 执行添加！
		formsService.edit(formsParam);
		log.info(strLogStarter + sysLoginUser.getAccount() + "编辑成功：‘" + formsParam.toString() + "’..");
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeEditForm);
		logsParam.setOperateDesc("Form id: '" + formsParam.getId() + "', after edit form name: '" + strFormName + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}
	
	/**
	 * @func 数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:39:00
	 */
	@ResponseBody
	@GetMapping("/forms/list")
	public ResponseData list(FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsService.list(formsParam));
	}
	
	/**
	 * @func 数据收集表单封存
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:39:00
	 */
	@ResponseBody
	@GetMapping("/forms/seal")
	public ResponseData seal(FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter      = ">>> [数据收集表单_封存] ";
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 判断是否登录：该功能需要组管理员用户登录后方可使用！
		if(sysLoginUser == null) {
			return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsNeedSignIn);
		} else {
			SysUser sysUser = sysUserService.getById(sysLoginUser.getId());
			if(sysUser.getAccountType() != 1) {
				
				// 记录日志！
				LogsParam logsParam = new LogsParam();
				logsParam.setOperateType(SettingBase.strOperateTypeSealForm);
				logsParam.setOperateDesc("Form id: '" + formsParam.getId() + "', seal failure: '" + SettingBase.strPermissionTipsGroupAdmin + "'..");
				logsParam.setOperateSuccess(SettingBase.intSuccessFalse);
				logsService.log(logsParam, session, request);
				
				return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, SettingBase.strPermissionTipsGroupAdmin);
			}
		}
		log.info(strLogStarter + sysLoginUser.getAccount() + "正在封存数据收集表单：");
		
		// 更新状态！
		Forms forms = formsService.getById(formsParam.getId());
		forms.setStatus(2);
		formsService.updateById(forms);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeSealForm);
		logsParam.setOperateDesc("Form id: '" + formsParam.getId() + "', form name: '" + forms.getFormName() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}
	
	/**
	 * @func 数据收集表单详情
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 22:50:00
	 */
	@ResponseBody
	@GetMapping("/forms/detail")
	public ResponseData detail(FormsParam formsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsService.getById(formsParam.getId()));
	}

}
