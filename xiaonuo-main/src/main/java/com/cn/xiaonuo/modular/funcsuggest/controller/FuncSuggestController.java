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
package com.cn.xiaonuo.modular.funcsuggest.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.funcsuggest.entity.FuncSuggest;
import com.cn.xiaonuo.modular.funcsuggest.param.FuncSuggestParam;
import com.cn.xiaonuo.modular.funcsuggest.service.FuncSuggestService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.utils.SettingBase;

/**
 * @func 文献推荐表控制器
 *
 * @author Quan Xu
 * @date 2021-09-18 11:42:11
 */
@Controller
public class FuncSuggestController {

	@Resource
	private FuncSuggestService funcSuggestService;
	
	@Resource
	private LogsService logsService;

	/**
	 * @func 添加文献推荐表
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:11
	 */
	@ResponseBody
	@PostMapping("/funcSuggest/add")
	public ResponseData add(@RequestBody @Validated(FuncSuggestParam.add.class) FuncSuggestParam funcSuggestParam, HttpSession session, HttpServletRequest request) {
		
		// 获取用户及组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strGroupName = sysLoginUser.getAccountGroup();
		
		// 检索看看当前用户是否已经推荐了当前文献！
		QueryWrapper<FuncSuggest> qwfs = new QueryWrapper<FuncSuggest>();
		qwfs.lambda().eq(FuncSuggest::getCreateUserName, sysLoginUser.getAccount());
		qwfs.lambda().eq(FuncSuggest::getPmid, funcSuggestParam.getPmid());
		List<FuncSuggest> fsList = funcSuggestService.list(qwfs);
		
		// 没有推荐：那就加上推荐！
		int intCurrentUser = 0;
		if(fsList.size() == 0) {
			FuncSuggest fs = new FuncSuggest();
			fs.setCreateUserName(sysLoginUser.getAccount());
			fs.setGroupName(strGroupName);
			fs.setPmid(funcSuggestParam.getPmid());
			funcSuggestService.save(fs);
			intCurrentUser = 1;
		}
		// 有推荐，那就删除之！
		else {
			funcSuggestService.remove(qwfs);
		}
		
		// 检索当前文献的所有推荐！
		QueryWrapper<FuncSuggest> qwfsAll = new QueryWrapper<FuncSuggest>();
		qwfsAll.lambda().eq(FuncSuggest::getPmid, funcSuggestParam.getPmid());
		List<FuncSuggest> fsListAll = funcSuggestService.list(qwfsAll);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeFuncSuggest);
		logsParam.setOperateDesc("Suggested PMID: '" + funcSuggestParam.getPmid() + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回修改之后的数量！
		return new SuccessResponseData(intCurrentUser + "," + fsListAll.size());
		
	}

	/**
	 * @func 查看文献推荐表
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:11
	 */
	@ResponseBody
	@PostMapping("/funcSuggest/detail")
	public ResponseData detail(@RequestBody FuncSuggestParam funcSuggestParam, HttpSession session, HttpServletRequest request) {
		
		// 获取用户及组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		
		// 检索当前文献的所有star数量！
		QueryWrapper<FuncSuggest> qwfs1 = new QueryWrapper<FuncSuggest>();
		qwfs1.lambda().eq(FuncSuggest::getPmid, funcSuggestParam.getPmid());
		List<FuncSuggest> fsList1 = funcSuggestService.list(qwfs1);
		
		// 检索当前用于是否对当前文献进行了star！
		QueryWrapper<FuncSuggest> qwfs2 = new QueryWrapper<FuncSuggest>();
		qwfs2.lambda().eq(FuncSuggest::getPmid, funcSuggestParam.getPmid());
		qwfs2.lambda().eq(FuncSuggest::getCreateUserName, sysLoginUser.getAccount());
		List<FuncSuggest> fsList2 = funcSuggestService.list(qwfs2);
		
		// 返回！
		return new SuccessResponseData(fsList2.size() + "," + fsList1.size());
		
	}

}
