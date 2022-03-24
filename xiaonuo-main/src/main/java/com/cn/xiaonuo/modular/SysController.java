package com.cn.xiaonuo.modular;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;

@Controller
public class SysController {

	@Resource
	private SysUserService sysUserService;
	
	/**
	 * @desc Register
	 *
	 * @author quanxu
	 * @date 2021-06-24 13:22
	 */
	@GetMapping("/register")
	public String register() {
		return "login/register.html";
	}
	
	/**
	 * @desc HOME
	 *
	 * @author quanxu
	 * @date 2021-06-24 12:01
	 */
	@GetMapping("/pubMiner")
	public String pubMiner(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/index.html";
	}
	
	/**
	 * @desc Index
	 *
	 * @author quanxu
	 * @date 2021-06-24 12:01
	 */
	@GetMapping("/lrIndex")
	public String lrIndex(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/index.html";
	}
	
	/**
	 * @desc Search
	 *
	 * @author quanxu
	 * @date 2021-06-24 16:57
	 */
	@GetMapping("/lrSearch")
	public String lrSearch(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/search.html";
	}
	
	/**
	 * @desc Search
	 *
	 * @author quanxu
	 * @date 2021-11-11 20:09
	 */
	@GetMapping("/lrSearchOld")
	public String lrSearchOld(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/searchOld.html";
	}
	
	/**
	 * @desc Library
	 *
	 * @author quanxu
	 * @date 2021-06-24 16:57
	 */
	@GetMapping("/lrLibrary")
	public String lrLibrary(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/library.html";
		}
	}
	
	/**
	 * @desc Library Form
	 *
	 * @author quanxu
	 * @date 2021-09-14 15:38
	 */
	@GetMapping("/lrLibraryForm")
	public String lrLibraryForm(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/libraryForm.html";
		}
	}
	
	/**
	 * @desc Members
	 *
	 * @author quanxu
	 * @date 2021-06-24 22:54
	 */
	@GetMapping("/lrMembers")
	public String lrMembers(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/members.html";
		}
	}
	
	/**
	 * @desc Member Form
	 *
	 * @author quanxu
	 * @date 2021-06-26 13:59
	 */
	@GetMapping("/lrMemberForm")
	public String lrMemberForm(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/memberForm.html";
		}
	}
	
	/**
	 * @desc Forms
	 *
	 * @author quanxu
	 * @date 2021-06-25 09:03
	 */
	@GetMapping("/lrForms")
	public String lrForms(Model model, HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/forms.html";
		}
	}
	
	/**
	 * @desc About
	 *
	 * @author quanxu
	 * @date 2021-06-24 12:01
	 */
	@GetMapping("/lrAbout")
	public String lrAbout(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/about.html";
	}
	
	/**
	 * @desc Help
	 *
	 * @author quanxu
	 * @date 2021-06-24 12:01
	 */
	@GetMapping("/lrHelp")
	public String lrHelp(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/help.html";
	}
	
	/**
	 * @desc Stat
	 *
	 * @author quanxu
	 * @date 2021-11-25 20:47
	 */
	@GetMapping("/lrStat")
	public String lrStat(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/stat.html";
	}
	
	/**
	 * @desc Details / Data Collecting
	 *
	 * @author quanxu
	 * @date 2021-08-29 11:35
	 */
	@GetMapping("/lrDetails")
	public String lrDetails(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/details.html";
	}
	
	/**
	 * @desc Details / Data Collecting
	 *
	 * @author quanxu
	 * @date 2021-11-13 23:20
	 */
	@GetMapping("/lrDetailsOld")
	public String lrDetailsOld(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/detailsOld.html";
	}
	
	/**
	 * @desc Projects
	 *
	 * @author quanxu
	 * @date 2021-09-04 15:13
	 */
	@GetMapping("/lrProjects")
	public String lrProjects(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null) {
			return "redirect:/"; // 未登录用户不允许访问页面！
		}else {
			return "pubMiner/projects.html";
		}
	}
	
	/**
	 * @desc Project Form
	 *
	 * @author quanxu
	 * @date 2021-09-07 17:14
	 */
	@GetMapping("/lrProjectForm")
	public String lrProjectForm(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/projectForm.html";
		}
	}
	
	/**
	 * @desc Publications
	 *
	 * @author quanxu
	 * @date 2021-09-14 16:35
	 */
	@GetMapping("/lrPublications")
	public String lrPublications(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/publications.html";
		}
	}
	
	/**
	 * @desc Publications
	 *
	 * @author quanxu
	 * @date 2021-09-16 17:34
	 */
	@GetMapping("/lrProjectPubs")
	public String lrProjectPubs(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/projectPubs.html";
		}
	}
	
	/**
	 * @desc Add Into Library
	 *
	 * @author quanxu
	 * @date 2021-09-17 16:12
	 */
	@GetMapping("/lrAddIntoLibrary")
	public String lrAddIntoLibrary(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) {
			return "redirect:/"; // 未登录用户及非组管理员用户不允许访问页面！
		}else {
			return "pubMiner/addIntoLibrary.html";
		}
	}
	
	/**
	 * @desc Select Collection to Read
	 *
	 * @author quanxu
	 * @date 2021-10-13 10:13
	 */
	@GetMapping("/lrSelectFormToRead")
	public String lrSelectFormToRead(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/selectFormToRead.html";
	}
	
	/**
	 * @desc Data Viewer
	 *
	 * @author quanxu
	 * @date 2021-11-30 14:14
	 */
	@GetMapping("/lrDataViewer")
	public String lrDataViewer(HttpServletRequest request) {
		sysUserService.checkLoginAndAutoRegisterAndAutoLogin(request);
		return "pubMiner/dataViewer.html";
	}

}
