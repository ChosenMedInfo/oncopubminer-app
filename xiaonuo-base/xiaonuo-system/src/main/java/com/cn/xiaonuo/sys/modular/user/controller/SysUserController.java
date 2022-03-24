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
package com.cn.xiaonuo.sys.modular.user.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.xiaonuo.core.annotion.BusinessLog;
import com.cn.xiaonuo.core.annotion.DataScope;
import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ErrorResponseData;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.sys.modular.auth.service.AuthService;
import com.cn.xiaonuo.sys.modular.emp.param.SysEmpParam;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.param.SysUserParam;
import com.cn.xiaonuo.sys.modular.user.result.SysUserResult;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;

/**
 * 系统用户控制器
 *
 * @author xuyuxiang
 * @date 2020/3/19 21:14
 */
@Controller
public class SysUserController {

	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private AuthService authService;

	/**
	 * 系统用户页面
	 *
	 * @author xuyuxiang
	 * @date 2020/11/17 16:40
	 */
	@Permission
	@GetMapping("/sysUser/index")
	public String index() {
		return "system/sysUser/index.html";
	}

	/**
	 * 系统用户表单页面
	 *
	 * @author xuyuxiang
	 * @date 2020/11/17 16:40
	 */
	@GetMapping("/sysUser/form")
	public String form() {
		return "system/sysUser/form.html";
	}

	/**
	 * 系统用户附属信息表单页面
	 *
	 * @author xuyuxiang
	 * @date 2020/11/17 16:40
	 */
	@GetMapping("/sysUser/extForm")
	public String extForm() {
		return "system/sysUser/extForm.html";
	}

	/**
	 * 系统用户授权角色页面
	 *
	 * @author xuyuxiang
	 * @date 2020/11/17 16:40
	 */
	@GetMapping("/sysUser/grantRole")
	public String grantRole() {
		return "system/sysUser/grantRole.html";
	}

	/**
	 * 系统用户授权数据页面
	 *
	 * @author xuyuxiang
	 * @date 2020/11/17 16:40
	 */
	@GetMapping("/sysUser/grantData")
	public String grantData() {
		return "system/sysUser/grantData.html";
	}

	/**
	 * 查询系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/3/20 21:00
	 */
	@Permission
	@ResponseBody
	@DataScope
	@GetMapping("/sysUser/page")
	public PageResult<SysUserResult> page(SysUserParam sysUserParam) {
		return sysUserService.page(sysUserParam);
	}
	
	/**
     * @desc 查询GroupUser
     *
     * @author Quan Xu
     * @date 2021-09-14
     */
	@ResponseBody
	@DataScope
	@GetMapping("/sysUser/pageMember")
	public PageResult<SysUserResult> pageMember(SysUserParam sysUserParam) {
		return sysUserService.pageMember(sysUserParam);
	}
	
	/**
	 * 查询系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/3/20 21:00
	 */
	@ResponseBody
	@GetMapping("/sysUser/listMember")
	public ResponseData listMember(SysUserParam sysUserParam) {
		return new SuccessResponseData(sysUserService.listMember(sysUserParam));
	}
	
	/**
	 * 增加系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/3/23 16:28
	 */
	@Permission
	@ResponseBody
	@DataScope
	@PostMapping("/sysUser/add")
	@BusinessLog(title = "系统用户_增加", opType = LogAnnotionOpTypeEnum.ADD)
	public ResponseData add(@RequestBody @Validated(SysUserParam.add.class) SysUserParam sysUserParam) {
		sysUserService.add(sysUserParam);
		return new SuccessResponseData();
	}
	
	/**
	 * 增加系统用户
	 *
	 * @author quanxu
	 * @date 2021-06-26 14:07
	 */
	@ResponseBody
	@PostMapping("/sysUser/addMember")
	@BusinessLog(title = "系统用户_增加成员", opType = LogAnnotionOpTypeEnum.ADD)
	public ResponseData addMember(@RequestBody SysUserParam sysUserParam) {
		// 获取当前登录用户！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null) return new ErrorResponseData(ResponseData.DEFAULT_ERROR_CODE, "Please sign in first!");
		SysUser sysUserLogin = sysUserService.getById(sysLoginUser.getId());
		// 默认员工表信息！
		SysEmpParam sysEmpParam = new SysEmpParam();
		sysEmpParam.setOrgId(1399252936032702465L);
		sysEmpParam.setOrgName("其他部门");
		List<Long> posIdList = new ArrayList<Long>();
		posIdList.add(1399252553419902977L);
		sysEmpParam.setPosIdList(posIdList);
		sysEmpParam.setOrgId(1407886712392597505L);
		sysEmpParam.setOrgName("外部人员");
		// 创建用户！
		//sysUserParam.setAccount(account);
		//sysUserParam.setPassword(password);
		sysUserParam.setNickName(sysUserParam.getAccount());
		sysUserParam.setName(sysUserParam.getAccount());
		sysUserParam.setSex(1);
		//sysUserParam.setEmail(email);
		sysUserParam.setStatus(0);
		sysUserParam.setPhone("-");;
		sysUserParam.setTel("-");
		sysUserParam.setInstitute(sysUserLogin.getInstitute());
		sysUserParam.setAccountType(2); // 添加的成员都是阅读账号！
		sysUserParam.setAccountGroup(sysUserLogin.getAccount() + "Group");
		sysUserParam.setSysEmpParam(sysEmpParam);
		sysUserService.add(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 删除系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/3/23 16:28
	 */
	@Permission
	@ResponseBody
	@DataScope
	@PostMapping("/sysUser/delete")
	@BusinessLog(title = "系统用户_删除", opType = LogAnnotionOpTypeEnum.DELETE)
	public ResponseData delete(@RequestBody @Validated(SysUserParam.delete.class) List<SysUserParam> sysUserParamList) {
		sysUserService.delete(sysUserParamList);
		return new SuccessResponseData();
	}
	
	/**
	 * 删除成员用户
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 09:55
	 */
	@ResponseBody
	@PostMapping("/sysUser/delMember")
	@BusinessLog(title = "成员用户_删除", opType = LogAnnotionOpTypeEnum.DELETE)
	public ResponseData delMember(@RequestBody SysUserParam sysUserParam) {
		// 获取当前操作用户的用户组名，并判断是否为组管理员，且待删除用户在当前用户组！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccountType().equals("1")) { // 操作前提1：当前登录用户是组管理员！
			SysUser sysUser = sysUserService.getById(sysUserParam.getId());
			String strGroupName = sysUser == null ? "-" : sysUser.getAccountGroup();
			if(strGroupName.equals(sysLoginUser.getAccountGroup())) { // 操作前提2：组名相同！
				sysUser.setStatus(2); // 逻辑删除！
				sysUserService.saveOrUpdate(sysUser);
			}
		}else {
			return new ErrorResponseData(500, "No permission!");
		}
		return new SuccessResponseData();
	}
	
	/**
	 * 重置成员用户密码
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 10:45
	 */
	@ResponseBody
	@PostMapping("/sysUser/resetPassMember")
	@BusinessLog(title = "重置成员用户密码", opType = LogAnnotionOpTypeEnum.EDIT)
	public ResponseData resetPassMember(@RequestBody SysUserParam sysUserParam) {
		// 获取当前操作用户的用户组名，并判断是否为组管理员，且待删除用户在当前用户组！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccountType().equals("1")) { // 操作前提1：当前登录用户是组管理员！
			SysUser sysUser = sysUserService.getById(sysUserParam.getId());
			String strGroupName = sysUser == null ? "-" : sysUser.getAccountGroup();
			if(strGroupName.equals(sysLoginUser.getAccountGroup())) { // 操作前提2：组名相同！
		        sysUser.setPassword(BCrypt.hashpw(sysUserParam.getPassword(), BCrypt.gensalt())); // 2021-11-19：更新尼玛！
		        sysUser.setEmail(sysUserParam.getEmail()); // 2021-11-19：更新邮箱！
		        sysUserService.updateById(sysUser); // 执行更新！
			}
		}else {
			return new ErrorResponseData(500, "No permission!");
		}
		return new SuccessResponseData();
	}

	/**
	 * 编辑系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/3/23 16:28
	 */
	@Permission
	@ResponseBody
	@DataScope
	@PostMapping("/sysUser/edit")
	@BusinessLog(title = "系统用户_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
	public ResponseData edit(@RequestBody @Validated(SysUserParam.edit.class) SysUserParam sysUserParam) {
		// 2021-04-06：处理修改用户信息后，用户无法登陆系统的问题！
		SysUser sysUser = sysUserService.getById(sysUserParam);
		sysUserParam.setPassword(sysUser.getPassword());
		// 执行编辑！
		sysUserService.edit(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 查看系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/3/23 16:28
	 */
	@ResponseBody
	@GetMapping("/sysUser/detail")
	public ResponseData detail(@Validated(SysUserParam.detail.class) SysUserParam sysUserParam) {
		return new SuccessResponseData(sysUserService.detail(sysUserParam));
	}

	/**
	 * 修改状态
	 *
	 * @author xuyuxiang
	 * @date 2020/5/25 14:32
	 */
	@Permission
	@ResponseBody
	@PostMapping("/sysUser/changeStatus")
	@BusinessLog(title = "系统用户_修改状态", opType = LogAnnotionOpTypeEnum.CHANGE_STATUS)
	public ResponseData changeStatus(@RequestBody @Validated(SysUserParam.changeStatus.class) SysUserParam sysUserParam) {
		sysUserService.changeStatus(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 授权角色
	 *
	 * @author xuyuxiang
	 * @date 2020/3/28 16:05
	 */
	@Permission
	@ResponseBody
	@DataScope
	@PostMapping("/sysUser/grantRole")
	@BusinessLog(title = "系统用户_授权角色", opType = LogAnnotionOpTypeEnum.GRANT)
	public ResponseData grantRole(@RequestBody @Validated(SysUserParam.grantRole.class) SysUserParam sysUserParam) {
		sysUserService.grantRole(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 授权数据
	 *
	 * @author xuyuxiang
	 * @date 2020/3/28 16:05
	 */
	@Permission
	@ResponseBody
	@DataScope
	@PostMapping("/sysUser/grantData")
	@BusinessLog(title = "系统用户_授权数据", opType = LogAnnotionOpTypeEnum.GRANT)
	public ResponseData grantData(@RequestBody @Validated(SysUserParam.grantData.class) SysUserParam sysUserParam) {
		sysUserService.grantData(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 更新信息
	 *
	 * @author xuyuxiang
	 * @date 2020/4/1 14:27
	 */
	@ResponseBody
	@PostMapping("/sysUser/updateInfo")
	@BusinessLog(title = "系统用户_更新信息", opType = LogAnnotionOpTypeEnum.UPDATE)
	public ResponseData updateInfo(@RequestBody @Validated(SysUserParam.updateInfo.class) SysUserParam sysUserParam) {
		sysUserService.updateInfo(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 修改密码
	 *
	 * @author xuyuxiang
	 * @date 2020/4/1 14:42
	 */
	@ResponseBody
	@PostMapping("/sysUser/updatePwd")
	@BusinessLog(title = "系统用户_修改密码", opType = LogAnnotionOpTypeEnum.UPDATE)
	public ResponseData updatePwd(@RequestBody @Validated(SysUserParam.updatePwd.class) SysUserParam sysUserParam) {
		sysUserService.updatePwd(sysUserParam);
		authService.logout(); // 修改密码之后退出登录！
		return new SuccessResponseData("Success! Please login with the new password!");
	}

	/**
	 * 拥有角色
	 *
	 * @author xuyuxiang
	 * @date 2020/3/28 14:46
	 */
	@Permission
	@ResponseBody
	@GetMapping("/sysUser/ownRole")
	@BusinessLog(title = "系统用户_拥有角色", opType = LogAnnotionOpTypeEnum.DETAIL)
	public ResponseData ownRole(@Validated(SysUserParam.detail.class) SysUserParam sysUserParam) {
		return new SuccessResponseData(sysUserService.ownRole(sysUserParam));
	}

	/**
	 * 拥有数据
	 *
	 * @author xuyuxiang
	 * @date 2020/3/28 14:46
	 */
	@Permission
	@ResponseBody
	@GetMapping("/sysUser/ownData")
	@BusinessLog(title = "系统用户_拥有数据", opType = LogAnnotionOpTypeEnum.DETAIL)
	public ResponseData ownData(@Validated(SysUserParam.detail.class) SysUserParam sysUserParam) {
		return new SuccessResponseData(sysUserService.ownData(sysUserParam));
	}

	/**
	 * 重置密码
	 *
	 * @author xuyuxiang
	 * @date 2020/4/1 14:42
	 */
	@Permission
	@ResponseBody
	@PostMapping("/sysUser/resetPwd")
	@BusinessLog(title = "系统用户_重置密码", opType = LogAnnotionOpTypeEnum.UPDATE)
	public ResponseData resetPwd(@RequestBody @Validated(SysUserParam.resetPwd.class) SysUserParam sysUserParam) {
		sysUserService.resetPwd(sysUserParam);
		return new SuccessResponseData();
	}
	
	/**
	 * @desc 扮演账户
	 *
	 * @author Quan Xu
	 * @date 2021-06-21 16:13:00
	 */
	@Permission
	@ResponseBody
	@PostMapping("/sysUser/impersonate")
	@BusinessLog(title = "系统用户_扮演账号", opType = LogAnnotionOpTypeEnum.OTHER)
	public ResponseData impersonate(@RequestBody @Validated(SysUserParam.resetPwd.class) SysUserParam sysUserParam) {
		
		// 获取用户信息！
		SysUser sysUser = sysUserService.getById(sysUserParam.getId());
		
		// 扮演账号登录！
		authService.doLogin(sysUser);
		
		// 返回提示信息！
		return new SuccessResponseData();
		
	}

	/**
	 * 修改头像
	 *
	 * @author xuyuxiang
	 * @date 2020/6/28 15:19
	 */
	@ResponseBody
	@PostMapping("/sysUser/updateAvatar")
	@BusinessLog(title = "系统用户_修改头像", opType = LogAnnotionOpTypeEnum.UPDATE)
	public ResponseData updateAvatar(@RequestBody @Validated(SysUserParam.updateAvatar.class) SysUserParam sysUserParam) {
		sysUserService.updateAvatar(sysUserParam);
		return new SuccessResponseData();
	}

	/**
	 * 导出系统用户
	 *
	 * @author xuyuxiang
	 * @date 2020/6/30 16:07
	 */
	@Permission
	@ResponseBody
	@GetMapping("/sysUser/export")
	@BusinessLog(title = "系统用户_导出", opType = LogAnnotionOpTypeEnum.EXPORT)
	public void export(SysUserParam sysUserParam) {
		sysUserService.export(sysUserParam);
	}


	/**
	 * 用户选择器
	 *
	 * @author xuyuxiang
	 * @date 2020/7/3 13:17
	 */
	@Permission
	@ResponseBody
	@GetMapping("/sysUser/selector")
	public ResponseData selector(SysUserParam sysUserParam) {
		return new SuccessResponseData(sysUserService.selector(sysUserParam));
	}
	
	/**
	 * @func 获取当前登录用户信息
	 *
	 * @author quanxu
	 * @date 2021-03-18
	 */
	@ResponseBody
	@GetMapping("/sysUser/currentLoginUserInfo")
	public SysLoginUser currentLoginUserInfo() {
		return LoginContextHolder.me().getSysLoginUserWithoutException();
	}
	
}
