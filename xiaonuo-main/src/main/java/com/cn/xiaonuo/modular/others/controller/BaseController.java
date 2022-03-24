package com.cn.xiaonuo.modular.others.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseController {

	// 系统根路径！
	@Value("${currentsystem.rootpath}")
	private String CurrentSystemRootPath;
	
	// 系统版本号！
	@Value("${currentsystem.version}")
	private String CurrentSystemVersion;
	
	// 接口URL！
	@Value("${oncopubminer.api-url}")
	public String OncoPubMinerApiUrl;
	
	// 接口ID搜索参数！
	@Value("${oncopubminer.api-param-id}")
	public String OncoPubMinerApiParamId;
	
	/**
	 * @func 向外提供根路径的获取：对配置的路径进行验证，确保提供的路径最后带有斜杠！
	 * @author Quan Xu
	 * @date 2021-03-05
	 * 
	 * @return
	 */
	public String GetRootPath() {
		return CurrentSystemRootPath.endsWith("/") ? CurrentSystemRootPath : CurrentSystemRootPath + "/";
	}
	
	/**
	 * @func 向外提供版本信息获取！
	 * @author Quan Xu
	 * @date 2021-06-24
	 * 
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getVersion")
	public String GetVersion() {
		return CurrentSystemVersion;
	}
	
}
