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
package com.cn.xiaonuo.sys.modular.auth.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.xiaonuo.core.consts.CommonConstant;
import com.cn.xiaonuo.core.context.constant.ConstantContextHolder;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.exception.AuthException;
import com.cn.xiaonuo.core.exception.enums.AuthExceptionEnum;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.core.util.HttpServletUtil;
import com.cn.xiaonuo.sys.modular.auth.service.AuthService;
import com.cn.xiaonuo.sys.modular.emp.param.SysEmpParam;
import com.cn.xiaonuo.sys.modular.user.param.SysUserParam;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;

/**
 * 登录控制器
 *
 * @author xuyuxiang
 * @date 2020/3/11 12:20
 */
@Controller
public class SysLoginController {

	@Resource
	private AuthService authService;
	
	@Resource
	private SysUserService sysUserService;

	/**
	 * 获取是否开启租户的标识
	 *
	 * @author xuyuxiang
	 * @date 2020/9/4
	 */
	@GetMapping("/getTenantOpen")
	public ResponseData getTenantOpen() {
		return new SuccessResponseData(ConstantContextHolder.getTenantOpenFlag());
	}

	/**
	 * 登录页面，GET请求
	 *
	 * @author xuyuxiang
	 * @date 2020/11/17 10:09
	 */
	@GetMapping("/login")
	public String loginPage() {
		//判断是否登录
		boolean hasLogin = LoginContextHolder.me().hasLogin();
		if(hasLogin) {
			return "redirect:/";
		} else {
			return "login/login.html";
		}
	}

	/**
	 * 账号密码登录接口，POST请求
	 *
	 * @author xuyuxiang
	 * @date 2020/3/11 15:52
	 */
	@ResponseBody
	@PostMapping("/loginPost")
	public ResponseData loginPost(@RequestBody Dict dict) {
		String account = dict.getStr("account");
		String password = dict.getStr("password");
		String tenantCode = dict.getStr("tenantCode");
		String captcha = dict.getStr("captcha");
		//如果系统开启了多租户开关，则添加租户的临时缓存
		if (ConstantContextHolder.getTenantOpenFlag()) {
			authService.cacheTenantInfo(tenantCode);
		}
		//如果开启了验证码，则先校验验证码
		if (ConstantContextHolder.getCaptchaFlag()) {
			String sessionCaptcha = Convert.toStr(HttpServletUtil.getRequest().getSession().getAttribute(CommonConstant.CAPTCHA_SESSION_KEY));
			if (ObjectUtil.isEmpty(captcha) || !captcha.equalsIgnoreCase(sessionCaptcha)) {
				throw new AuthException(AuthExceptionEnum.VALID_CODE_ERROR);
			}
		}
		String token = authService.login(account, password);
		return new SuccessResponseData(token);
	}
	
	/**
	 * 注册账号，POST请求
	 *
	 * @author Quan Xu
	 * @date 2021-06-25 20:46
	 */
	@ResponseBody
	@PostMapping("/registerPost")
	public ResponseData registerPost(@RequestBody Dict dict) {
		String account = dict.getStr("account");
		String password = dict.getStr("password");
		String email = dict.getStr("email");
		String institute = dict.getStr("institute");
		String captcha = dict.getStr("captcha");
		// 校验验证码！
		String sessionCaptcha = Convert.toStr(HttpServletUtil.getRequest().getSession().getAttribute(CommonConstant.CAPTCHA_SESSION_KEY));
		if (ObjectUtil.isEmpty(captcha) || !captcha.equalsIgnoreCase(sessionCaptcha)) {
			throw new AuthException(AuthExceptionEnum.VALID_CODE_ERROR);
		}
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
		SysUserParam sysUserParam = new SysUserParam();
		sysUserParam.setAccount(account);
		sysUserParam.setPassword(password);
		sysUserParam.setNickName(account);
		sysUserParam.setName(account);
		sysUserParam.setSex(1);
		sysUserParam.setEmail(email);
		sysUserParam.setStatus(0);
		sysUserParam.setPhone("-");;
		sysUserParam.setTel("-");
		sysUserParam.setInstitute(institute);
		sysUserParam.setAccountType(1); // 注册用户默认都是管理账号，可以自行创建用户组和数据收集表单！
		sysUserParam.setAccountGroup(account + "Group");
		sysUserParam.setSysEmpParam(sysEmpParam);
		sysUserService.add(sysUserParam);
		// 创建完成后，用当前新建的用户进行登录！
		String token = authService.login(account, password);
		return new SuccessResponseData(token);
	}

	/**
	 * 退出登录页面，GET请求
	 *
	 * @author xuyuxiang
	 * @date 2020/3/16 15:02
	 */
	@GetMapping("/logout")
	public String logoutPage() {
		authService.logout();
		return "redirect:/";
	}

	/**
	 * 退出登录接口，POST请求
	 *
	 * @author xuyuxiang
	 * @date 2020/3/16 15:02
	 */
	@ResponseBody
	@PostMapping("/logout")
	public ResponseData logout() {
		authService.logout();
		return new SuccessResponseData();
	}

	/**
	 * 获取当前登录用户信息
	 *
	 * @author xuyuxiang
	 * @date 2020/3/23 17:57
	 */
	@ResponseBody
	@GetMapping("/getLoginUser")
	public ResponseData getLoginUser() {
		return new SuccessResponseData(LoginContextHolder.me().getSysLoginUserUpToDate());
	}

	/**
	 * 获取验证码
	 *
	 * @author xuyuxiang
	 * @date 2020/3/23 17:57
	 */
	@ResponseBody
	@GetMapping("/getCaptcha")
	public void getCaptcha() throws IOException {
		//定义图形验证码的长、宽、验证码字符数、干扰线宽度
		ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(300, 100, 4, 4);
		HttpSession session = HttpServletUtil.getRequest().getSession();
		session.setAttribute(CommonConstant.CAPTCHA_SESSION_KEY, captcha.getCode());
		HttpServletResponse response = HttpServletUtil.getResponse();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLUtil.encode("captcha.jpg") + "\"");
		response.addHeader("Content-Length", "" + captcha.getImageBytes().length);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setContentType("application/octet-stream;charset=UTF-8");
		ServletOutputStream outputStream = response.getOutputStream();
		captcha.write(outputStream);
	}
	
}
