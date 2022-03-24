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
package com.cn.xiaonuo.modular.formsitemsdata.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.formsitems.entity.FormsItems;
import com.cn.xiaonuo.modular.formsitems.service.FormsItemsService;
import com.cn.xiaonuo.modular.formsitemsdata.entity.FormsItemsData;
import com.cn.xiaonuo.modular.formsitemsdata.param.FormsItemsDataParam;
import com.cn.xiaonuo.modular.formsitemsdata.service.FormsItemsDataService;
import com.cn.xiaonuo.modular.librariespublications.controller.LibrariesPublicationsController;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.modular.others.controller.BaseController;
import com.cn.xiaonuo.modular.projects.entity.Projects;
import com.cn.xiaonuo.modular.projects.service.ProjectsService;
import com.cn.xiaonuo.sys.modular.file.util.DownloadUtil;
import com.cn.xiaonuo.utils.SettingBase;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;

/**
 * @func 表单项采集数据控制器
 *
 * @author Quan Xu
 * @date 2021-09-20 23:12:02
 */
@Controller
public class FormsItemsDataController {

	private String PATH_PREFIX = "formsItemsData/";

	@Resource
	private FormsItemsService formsItemsService;
	
	@Resource
	private FormsItemsDataService formsItemsDataService;
	
	@Resource
	private ProjectsService projectsService;
	
	@Resource
	private LogsService logsService;
	
	@Resource
	private BaseController baseController;
	
	@Resource
	private LibrariesPublicationsController librariesPublicationsController;
	
	private static final Log log = Log.get();

	/**
	 * @func 表单项采集数据页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@Permission
	@GetMapping("/formsItemsData/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 表单项采集数据表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@GetMapping("/formsItemsData/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@ResponseBody
	@GetMapping("/formsItemsData/page")
	public PageResult<FormsItemsData> page(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		return formsItemsDataService.page(formsItemsDataParam);
	}
	
	/**
	 * @func 统计表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-24 10:59:00
	 */
	@ResponseBody
	@GetMapping("/formsItemsData/getBatchCount")
	public Integer getBatchCount(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		return formsItemsDataService.getBatchCount(formsItemsDataParam, session, request);
	}
	
	/**
	 * @func 导出表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-23 23:33:00
	 */
	@ResponseBody
	@GetMapping("/formsItemsData/exportData")
	public void exportData(FormsItemsDataParam formsItemsDataParam, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		
		// 处理！
		List<String> strListDataFinal   = generateToBeExportData(formsItemsDataParam, response, session, request); // 最终要导出的数据列表！
		String strFileName              = UsuallyUsedUtils.GetDateTimeNumWithShortLineStr() + "_oncopubminer_data.tsv";
		String statFilesPath            = baseController.GetRootPath() + SettingBase.strDirNameExport;
		String strFileNameWithPath      = statFilesPath + "/" + strFileName;
		
		// 生成文件！
		FileUtil.writeLines(strListDataFinal, strFileNameWithPath, "utf-8", false);
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeExportData);
		//logsParam.setOperateDesc("Number of data lines: '" + intLineNum + "', file path: '" + strFileNameWithPath + "'..");
		logsParam.setOperateDesc("Number of lines in data file: '" + strListDataFinal.size() + "', file path: '" + strFileNameWithPath + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		File file = new File(strFileNameWithPath);
		byte[] fileBytes = FileUtil.readBytes(file);
		DownloadUtil.download(strFileName, fileBytes, response);
		
	}
	
	/**
	 * @func 获取表单项采集数据：用于表格展示！
	 *
	 * @author Quan Xu
	 * @date 2021-11-30 14:06:00
	 */
	@ResponseBody
	@GetMapping("/formsItemsData/listData")
	public ResponseData listData(FormsItemsDataParam formsItemsDataParam, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(generateToBeExportData(formsItemsDataParam, response, session, request));
	}

	/**
	 * @func 添加表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@ResponseBody
	@PostMapping("/formsItemsData/add")
	public ResponseData add(@RequestBody @Validated(FormsItemsDataParam.add.class) List<FormsItemsDataParam> formsItemsDataParamList, HttpSession session, HttpServletRequest request) {
		
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strUserGroup = "guestGroup", strUserAccount = "guest" + IdWorker.getId();
		Integer intItemValueType = 0; // 默认采集数据！
		if(sysLoginUser != null) {
			strUserGroup = sysLoginUser.getAccountGroup();
			strUserAccount = sysLoginUser.getAccount();
			intItemValueType = sysLoginUser.getAccountType().equals("1") ? 1 : 0;
		}else { // 用户没有登录的情况下，去session中找！
			String sessionUserAccount = (String)session.getAttribute("sessionUserAccount");
			if(sessionUserAccount != null) { // session中存在的话，取出！
				strUserAccount = sessionUserAccount;
			}else { // session中不存在的话，设置！
				session.setAttribute("sessionUserAccount", strUserAccount);
			}
		}
		String strBatchId = UsuallyUsedUtils.GetDateTimeNumWithShortLineStr() + "-" + IdWorker.getId(); // 创建本批次id！
		for(FormsItemsDataParam formsItemsDataParam : formsItemsDataParamList) {
			String strGroupName = strUserGroup;
			formsItemsDataParam.setCreateUserName(strUserAccount);
			formsItemsDataParam.setGroupName(strGroupName);
			formsItemsDataParam.setItemValueType(intItemValueType); // 组管理员提交的是审核数据，其他组员提交的是采集数据！
			formsItemsDataParam.setBatchId(strBatchId); // 添加批次id！
			
			// 获取表项名称！
			FormsItems formsItems = formsItemsService.getById(formsItemsDataParam.getItemId());
			formsItemsDataParam.setItemName(formsItems.getItemName());
			
			// 去除表项值前后的空格！
			String strItemValue = formsItemsDataParam.getItemValue();
			strItemValue = strItemValue.trim();
			formsItemsDataParam.setItemValue(strItemValue);
			
			formsItemsDataService.add(formsItemsDataParam);
		}
		
		// 记录日志！
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeAddData);
		logsParam.setOperateDesc("Batch id: '" + strBatchId + "'..");
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return new SuccessResponseData();
		
	}

	/**
	 * @func 删除表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@ResponseBody
	@PostMapping("/formsItemsData/deleteByBatchId")
	public ResponseData deleteByBatchId(@RequestBody FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		System.out.println("strBatchId: “" + formsItemsDataParam.getBatchId() + "”..");
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		LambdaQueryWrapper<FormsItemsData> lqw = new LambdaQueryWrapper<FormsItemsData>();
		lqw.eq(FormsItemsData::getBatchId, formsItemsDataParam.getBatchId());
		lqw.eq(FormsItemsData::getCreateUser, sysLoginUser.getId());
		List<FormsItemsData> formsItemsDataList = formsItemsDataService.list(lqw);
		System.out.println("检索得到的数量：“" + formsItemsDataList.size() + "”..");
		for(FormsItemsData fid : formsItemsDataList) {
			System.out.println("删除数据“" + fid.toString() + "”..");
			formsItemsDataService.removeById(fid.getId());
		}
		return new SuccessResponseData();
	}

	/**
	 * @func 编辑表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@Permission
	@ResponseBody
	@PostMapping("/formsItemsData/edit")
	public ResponseData edit(@RequestBody @Validated(FormsItemsDataParam.edit.class) FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		formsItemsDataService.edit(formsItemsDataParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 查看表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@ResponseBody
	@GetMapping("/formsItemsData/detail")
	public ResponseData detail(@Validated(FormsItemsDataParam.detail.class) FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsItemsDataService.detail(formsItemsDataParam));
	}
	
	/**
	 * @func 查看表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-22 11:34:00
	 */
	@ResponseBody
	@GetMapping("/formsItemsData/getItemData")
	public ResponseData getItemData(@Validated(FormsItemsDataParam.detail.class) FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strGroupName = sysLoginUser.getAccountGroup();
		QueryWrapper<FormsItemsData> qw = new QueryWrapper<FormsItemsData>();
		
		//qw.lambda().eq(FormsItemsData::getGroupName, strGroupName);
		qw.lambda().and(wq -> wq
			.eq(FormsItemsData::getGroupName, strGroupName)
			.or()
			.eq(FormsItemsData::getGroupName, "commonGroup")
		);
		
		if(formsItemsDataParam.getProjectId() != null) qw.lambda().eq(FormsItemsData::getProjectId, formsItemsDataParam.getProjectId());
		if(formsItemsDataParam.getLibraryId() != null) qw.lambda().eq(FormsItemsData::getLibraryId, formsItemsDataParam.getLibraryId());
		qw.lambda().eq(FormsItemsData::getPmid, formsItemsDataParam.getPmid());
		qw.lambda().eq(FormsItemsData::getFormId, formsItemsDataParam.getFormId());
		qw.lambda().eq(FormsItemsData::getItemId, formsItemsDataParam.getItemId());
		if(sysLoginUser == null || !sysLoginUser.getAccountType().equals("1")) { // 非组管理员用户，只能查看自己或公共组管理员的数据！
			qw.lambda().and(wq -> wq
				.eq(FormsItemsData::getCreateUser, sysLoginUser.getId())
				.or()
				//.eq(FormsItemsData::getCreateUserName, "common")
				.eq(FormsItemsData::getCreateUserName, SettingBase.strUserCivic) // 2021-12-03（V0011-01.00.08-07）：之前改了用户，不是叫common了，改civic了！
			); 
		}
		return new SuccessResponseData(formsItemsDataService.list(qw));
	}

	/**
	 * @func 表单项采集数据列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	@Permission
	@ResponseBody
	@GetMapping("/formsItemsData/list")
	public ResponseData list(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(formsItemsDataService.list(formsItemsDataParam));
	}
	
	/**
	 * @func CIViC数据更新！
	 * @desc 20220208，加上Permission限制，以免被随意调用：只有superadmin才能调用了！
	 *
	 * @author Quan Xu
	 * @date 2021-10-18 11:10:00
	 */
	@Permission
	@ResponseBody
	@GetMapping("/formsItemsData/updateCivicData")
	public ResponseData updateCivicData(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					log.info("进入到异步处理了，准备更新CIViC数据..");
					formsItemsDataService.updateCivicData(session, request);
					log.info("CIViC数据更新完成，准备更新文献信息了..");
					librariesPublicationsController.updatePublicationDetail(); // 异步更新文献信息！
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		return new SuccessResponseData("异步执行中...");
	}
	
	// ============================================================================================================================== //
	// ============================================================================================================================== //
	// ============================================================================================================================== //
	
	/**
	 * @desc 生成用于下载的数据列表！
	 * @author Quan Xu
	 * @date 2021-11-30
	 */
	public List<String> generateToBeExportData(FormsItemsDataParam formsItemsDataParam, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strUserGroup = "guestGroup", strUserAccount = "guest" + IdWorker.getId();
		if(sysLoginUser != null) {
			strUserGroup = sysLoginUser.getAccountGroup();
			strUserAccount = sysLoginUser.getAccount();
		}else { // 用户没有登录的情况下，去session中找！
			String sessionUserAccount = (String)session.getAttribute("sessionUserAccount");
			if(sessionUserAccount != null) { // session中存在的话，取出！
				strUserAccount = sessionUserAccount;
			}else { // session中不存在的话，设置！
				session.setAttribute("sessionUserAccount", strUserAccount);
			}
		}
//		Map<String, String> mapCheckSampleBatchData = new TreeMap<String, String>();
//		Map<String, Boolean> mapAllBatchId          = new TreeMap<String, Boolean>(); // 收录所有的批次id！
		
		//String strTitleLine             = "Submit Id";
		String strTitleLine             = "Submit Id\tPMID"; // 2021-11-30（V0008-01.00.05-08）：固定加上PMID！
		String strTitleLineLastColNames = "Submitter\tSubmit Time";
		List<String> strListDataFinal   = new ArrayList<String>(); // 最终要导出的数据列表！
		
		// 加入待导出文件内容之前的备注信息！
		Projects project = projectsService.getById(formsItemsDataParam.getProjectId());
		String strProjectAndPubInfo = "";
		if(project != null) {
			strProjectAndPubInfo = "# Data exported for project '" + project.getProjectName() + "'";
			if(formsItemsDataParam.getPmid() != null) strProjectAndPubInfo += " and publication 'PMID:" + formsItemsDataParam.getPmid() + "'";
		}
		strListDataFinal.add(SettingBase.strDataExportCommentsContent1);
		strListDataFinal.add(SettingBase.strDataExportCommentsContent2);
		if(!strProjectAndPubInfo.equals("")) {
			strListDataFinal.add(SettingBase.strDataExportCommentsContent3);
			strListDataFinal.add(strProjectAndPubInfo);
			strListDataFinal.add("# Data exported by '" + strUserAccount + "' (in group '" + strUserGroup + "')");
			strListDataFinal.add("# Data exported on " + new Date());
		}
		strListDataFinal.add(SettingBase.strDataExportCommentsContent1);
		
		// 检索数据库！
		List<String> strList = formsItemsDataService.getBatchDataForDownload(formsItemsDataParam, session, request);
		Map<String, Boolean> mapCheckItemNamesAdded = new TreeMap<String, Boolean>();
		String strBatchIdPointer = "";
		String strLineData = "";
		String strLineSubmitterAndTime = "";
		List<String> strLineDataList = new ArrayList<String>();
		for(String str : strList) {
			String[] strArr = str.split("######");
			
			// 完善标题行，并防止itemName重复添加！
			if(!mapCheckItemNamesAdded.containsKey(strArr[1])) {
				strTitleLine += "\t" + strArr[1];
				mapCheckItemNamesAdded.put(strArr[1], true);
			}
			
			// 2021-11-30（V0008-01.00.05-08）：访客用户信息处理！
			String strSubmitter = strArr[3];
			strSubmitter = strSubmitter.startsWith("guest") ? "N/A" : strSubmitter;
			
			// 添加数据行！
			//if(strLineData.equals("")) strLineData = strArr[0];
			if(strLineData.equals("")) strLineData = strArr[0] + "\t" + strArr[5]; // 2021-11-30（V0008-01.00.05-08）：固定加上PMID！
			
			//if(strLineSubmitterAndTime.equals("")) strLineSubmitterAndTime = strArr[3] + "\t" + strArr[4] + " (UTC+8)";
			if(strLineSubmitterAndTime.equals("")) strLineSubmitterAndTime = strSubmitter + "\t" + strArr[4] + " (UTC+8)"; // 2021-11-30（V0008-01.00.05-08）：访客信息隐藏！
			
			if(strBatchIdPointer.equals("") || strBatchIdPointer.equals(strArr[0])) {
				strLineData += "\t" + strArr[2]; // 加上表项数据值！
			}else {
				
				strLineData += "\t" + strLineSubmitterAndTime;
				strLineDataList.add(strLineData);
				
				//strLineData = strArr[0] + "\t" + strArr[2]; // 重置！
				strLineData = strArr[0] + "\t" + strArr[5] + "\t" + strArr[2]; // 2021-11-30（V0008-01.00.05-08）：固定加上PMID！
				
				//strLineSubmitterAndTime = strArr[3] + "\t" + strArr[4] + " (UTC+8)"; // 重置！
				strLineSubmitterAndTime = strSubmitter + "\t" + strArr[4] + " (UTC+8)"; // 2021-11-30（V0008-01.00.05-08）：访客信息隐藏！
				
			}
			strBatchIdPointer = strArr[0]; // 指针更新！
		}
		strTitleLine += "\t" + strTitleLineLastColNames; // 标题行完善！
		strListDataFinal.add(strTitleLine); // 先加入标题行！
		strLineData += "\t" + strLineSubmitterAndTime; // 完善最后一行数据！
		strLineDataList.add(strLineData); // 加入最后的一行数据！
		strListDataFinal.addAll(strLineDataList); // 再加入全部数据行！
		
		// 返回！
		return strListDataFinal;
		
	}

}
