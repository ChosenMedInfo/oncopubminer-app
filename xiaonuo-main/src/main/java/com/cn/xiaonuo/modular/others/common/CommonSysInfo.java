package com.cn.xiaonuo.modular.others.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.sys.modular.auth.service.AuthService;

@Component
public class CommonSysInfo {

	@Resource
	AuthService authService;
	
	@Resource
	HttpServletRequest request;
	
	/**
	 * @func 获取当前登录用户的id！
	 * @author quanxu
	 * @date 2021-01-07 18:04:00
	 * 
	 * @return
	 */
	public Long getCurrentLoginUserId() {
		
		return authService.getLoginUserByToken(authService.getTokenFromRequest(request)).getId();
		
	}
	
	/**
	 * @func 获取当前登录用户信息！
	 * @author quanxu
	 * @date 2021-01-07 18:04:00
	 * 
	 * @return
	 */
	public SysLoginUser getCurrentLoginUserInfo() {
		
		return authService.getLoginUserByToken(authService.getTokenFromRequest(request));
		
	}
	
}
