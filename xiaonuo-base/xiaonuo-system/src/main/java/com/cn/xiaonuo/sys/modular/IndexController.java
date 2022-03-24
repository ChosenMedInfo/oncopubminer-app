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
package com.cn.xiaonuo.sys.modular;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.sys.core.log.LogManager;
import com.cn.xiaonuo.sys.core.log.MailUtils;
import com.cn.xiaonuo.sys.modular.auth.service.AuthService;
import com.cn.xiaonuo.sys.modular.log.entity.SysVisLog;
import com.cn.xiaonuo.sys.modular.user.entity.SysUser;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;

import cn.hutool.json.JSONUtil;

/**
 * 首页控制器
 *
 * @author xuyuxiang
 * @date 2020/3/18 11:20
 */
@Controller
public class IndexController {

	@Resource
	private AuthService authService;
	
	@Resource
	private SysUserService sysUserService;
	
	/**
	 * @desc 访问首页，提示语
	 *
	 * @author xuyuxiang
	 * @date 2020/4/8 19:27
	 */
	@GetMapping("/")
	public String index(Model model) {
		//sysUserService.checkLoginAndAutoRegisterAndAutoLogin(); // 访问首页不实现自动登录！
		
		//判断是否登录
		boolean hasLogin = LoginContextHolder.me().hasLogin();
		if(hasLogin) {
			
			SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
			model.addAttribute("loginUser", sysLoginUser);
			
//			// 2021-12-09（V0013-01.00.10-07）：发送提示邮件！
//			SysVisLog sysVisLog = LogManager.me().genBaseSysVisLog();
//	        MailUtils.sendLogMail("[OncoPubMiner - 首页访问] 有用户（" + sysLoginUser.getAccount() + "）正在访问系统首页", JSONUtil.toJsonPrettyStr(sysVisLog));
			
			// 根据用户角色的不同，走向不同的页面！
			String strIndex = "pubMiner/index.html";
			if(sysLoginUser.getAdminType() == 1) strIndex = "adminIndex.html";
			return strIndex;
			
		} else {
			
//			// 2021-12-09（V0013-01.00.10-07）：发送提示邮件！
//			SysVisLog sysVisLog = LogManager.me().genBaseSysVisLog();
//	        MailUtils.sendLogMail("[OncoPubMiner - 首页访问] 有用户（未登录）正在访问系统首页", JSONUtil.toJsonPrettyStr(sysVisLog));
			
			return "pubMiner/index.html"; // 未登录用户也进入首页！
			
		}
		
	}
	
	/**
	 * @desc 用示例的demoAdmin账号登录
	 *
	 * @author Quan Xu
	 * @date 2021-12-29 14:10
	 */
	@GetMapping("/demoGroupAdmin")
	public String demoGroupAdmin(Model model) {
		
		// 获取用户信息！
		SysUser sysUser = sysUserService.getById(1476073425276628994L);
		
		// 扮演账号登录！
		authService.doLogin(sysUser);
		
		// 进入首页！
		return "redirect:/";
		
	}
	
	/**
	 * @desc 用示例的demoMemberA账号登录
	 *
	 * @author Quan Xu
	 * @date 2021-12-29 14:10
	 */
	@GetMapping("/demoGroupMemberA")
	public String demoGroupMemberA(Model model) {
		
		// 获取用户信息！
		SysUser sysUser = sysUserService.getById(1476073619468709889L);
		
		// 扮演账号登录！
		authService.doLogin(sysUser);
		
		// 进入首页！
		return "redirect:/";
		
	}
	
	/**
	 * @desc 用示例的demoMemberB账号登录
	 *
	 * @author Quan Xu
	 * @date 2021-12-29 14:10
	 */
	@GetMapping("/demoGroupMemberB")
	public String demoGroupMemberB(Model model) {
		
		// 获取用户信息！
		SysUser sysUser = sysUserService.getById(1476073982888374274L);
		
		// 扮演账号登录！
		authService.doLogin(sysUser);
		
		// 进入首页！
		return "redirect:/";
		
	}
	
	/**
	 * @desc 用示例的demoMemberC账号登录
	 *
	 * @author Quan Xu
	 * @date 2021-12-29 14:10
	 */
	@GetMapping("/demoGroupMemberC")
	public String demoGroupMemberC(Model model) {
		
		// 获取用户信息！
		SysUser sysUser = sysUserService.getById(1476074097980076033L);
		
		// 扮演账号登录！
		authService.doLogin(sysUser);
		
		// 进入首页！
		return "redirect:/";
		
	}
	
	/**
	 * @desc 通过链接带上账号，登录系统
	 *
	 * @author Quan Xu
	 * @date 2022-03-17 14:51
	 */
	@ResponseBody
	@GetMapping("/opmUser")
	public SysLoginUser opmUser(String strAccount) {
		
		// 获取用户信息！
		QueryWrapper<SysUser> qw = new QueryWrapper<SysUser>();
		qw.lambda().eq(SysUser::getAccount, strAccount);
		List<SysUser> sysUserList = sysUserService.list(qw);
		if(sysUserList != null && sysUserList.size() == 1) {
			// 扮演账号登录！
			authService.doLogin(sysUserList.get(0));
			// 2021-12-09（V0013-01.00.10-07）：发送提示邮件！
			SysVisLog sysVisLog = LogManager.me().genBaseSysVisLog();
	        MailUtils.sendLogMail("[OncoPubMiner - LocalStorage登录] 有用户正在通过LocalStorage的Account登录", JSONUtil.toJsonPrettyStr(sysVisLog));
		}
		
		// 返回登录用户！
		return LoginContextHolder.me().getSysLoginUserWithoutException();
		
	}
	
	/**
	 * @desc 通过链接带上账号，登录系统
	 *
	 * @author Quan Xu
	 * @date 2022-03-21 16:33
	 */
	@GetMapping("/opmUserLogin")
	public String opmUserLogin(String strAccount) {
		
		// 获取用户信息！
		QueryWrapper<SysUser> qw = new QueryWrapper<SysUser>();
		qw.lambda().eq(SysUser::getAccount, strAccount);
		List<SysUser> sysUserList = sysUserService.list(qw);
		if(sysUserList != null && sysUserList.size() == 1) {
			// 扮演账号登录！
			authService.doLogin(sysUserList.get(0));
			// 2021-12-09（V0013-01.00.10-07）：发送提示邮件！
			SysVisLog sysVisLog = LogManager.me().genBaseSysVisLog();
	        MailUtils.sendLogMail("[OncoPubMiner - LocalStorage登录] 有用户正在通过LocalStorage的Account登录", JSONUtil.toJsonPrettyStr(sysVisLog));
		}
		
		// 进入首页！
		return "redirect:/";
		
	}
	
}
