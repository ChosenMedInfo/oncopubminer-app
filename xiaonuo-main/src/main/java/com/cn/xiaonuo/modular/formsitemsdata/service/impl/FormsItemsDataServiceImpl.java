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
package com.cn.xiaonuo.modular.formsitemsdata.service.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.formsitems.entity.FormsItems;
import com.cn.xiaonuo.modular.formsitems.service.FormsItemsService;
import com.cn.xiaonuo.modular.formsitemsdata.entity.FormsItemsData;
import com.cn.xiaonuo.modular.formsitemsdata.enums.FormsItemsDataExceptionEnum;
import com.cn.xiaonuo.modular.formsitemsdata.mapper.FormsItemsDataMapper;
import com.cn.xiaonuo.modular.formsitemsdata.param.FormsItemsDataParam;
import com.cn.xiaonuo.modular.formsitemsdata.service.FormsItemsDataService;
import com.cn.xiaonuo.modular.librariespublications.entity.LibrariesPublications;
import com.cn.xiaonuo.modular.librariespublications.service.LibrariesPublicationsService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.modular.others.controller.BaseController;
import com.cn.xiaonuo.utils.SettingBase;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;

/**
 * @func 表单项采集数据service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-20 23:12:02
 */
@Service
public class FormsItemsDataServiceImpl extends ServiceImpl<FormsItemsDataMapper, FormsItemsData> implements FormsItemsDataService {

	@Resource
	private BaseController baseController;
	
	@Resource
	private FormsItemsService formsItemsService;
	
	@Resource
	private LibrariesPublicationsService librariesPublicationsService;
	
	@Resource
	private LogsService logsService;
	
	private static final Log log = Log.get();
	
	@Override
	public PageResult<FormsItemsData> page(FormsItemsDataParam formsItemsDataParam) {
		QueryWrapper<FormsItemsData> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(formsItemsDataParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getId())) {
				queryWrapper.lambda().eq(FormsItemsData::getId, formsItemsDataParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getStatus())) {
				queryWrapper.lambda().like(FormsItemsData::getStatus, formsItemsDataParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getCreateUserName())) {
				queryWrapper.lambda().like(FormsItemsData::getCreateUserName, formsItemsDataParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getUpdateUserName())) {
				queryWrapper.lambda().like(FormsItemsData::getUpdateUserName, formsItemsDataParam.getUpdateUserName());
			}

			// 根据 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getGroupId())) {
				queryWrapper.lambda().like(FormsItemsData::getGroupId, formsItemsDataParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getGroupName())) {
				queryWrapper.lambda().like(FormsItemsData::getGroupName, formsItemsDataParam.getGroupName());
			}

			// 根据项目id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getProjectId())) {
				queryWrapper.lambda().like(FormsItemsData::getProjectId, formsItemsDataParam.getProjectId());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getLibraryId())) {
				queryWrapper.lambda().like(FormsItemsData::getLibraryId, formsItemsDataParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getPmid())) {
				queryWrapper.lambda().like(FormsItemsData::getPmid, formsItemsDataParam.getPmid());
			}

			// 根据PMCID（如果采集的是全文的话就有） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getPmcid())) {
				queryWrapper.lambda().like(FormsItemsData::getPmcid, formsItemsDataParam.getPmcid());
			}

			// 根据表单id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getFormId())) {
				queryWrapper.lambda().like(FormsItemsData::getFormId, formsItemsDataParam.getFormId());
			}

			// 根据表项id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getItemId())) {
				queryWrapper.lambda().like(FormsItemsData::getItemId, formsItemsDataParam.getItemId());
			}

			// 根据表项采集值 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getItemValue())) {
				queryWrapper.lambda().like(FormsItemsData::getItemValue, formsItemsDataParam.getItemValue());
			}

			// 根据表项值类型（字典 0采集 1审核） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getItemValueType())) {
				queryWrapper.lambda().like(FormsItemsData::getItemValueType, formsItemsDataParam.getItemValueType());
			}

		}
		
		// 排序！
		if(formsItemsDataParam.getSortBy() != null && formsItemsDataParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(formsItemsDataParam.getSortBy());
			if(formsItemsDataParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(FormsItemsData::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<FormsItemsData> list(FormsItemsDataParam formsItemsDataParam) {
		LambdaQueryWrapper<FormsItemsData> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(formsItemsDataParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getId())) {
				queryWrapper.eq(FormsItemsData::getId, formsItemsDataParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getStatus())) {
				queryWrapper.like(FormsItemsData::getStatus, formsItemsDataParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getCreateUserName())) {
				queryWrapper.like(FormsItemsData::getCreateUserName, formsItemsDataParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getUpdateUserName())) {
				queryWrapper.like(FormsItemsData::getUpdateUserName, formsItemsDataParam.getUpdateUserName());
			}

			// 根据 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getGroupId())) {
				queryWrapper.like(FormsItemsData::getGroupId, formsItemsDataParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getGroupName())) {
				queryWrapper.like(FormsItemsData::getGroupName, formsItemsDataParam.getGroupName());
			}

			// 根据项目id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getProjectId())) {
				queryWrapper.like(FormsItemsData::getProjectId, formsItemsDataParam.getProjectId());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getLibraryId())) {
				queryWrapper.like(FormsItemsData::getLibraryId, formsItemsDataParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getPmid())) {
				queryWrapper.like(FormsItemsData::getPmid, formsItemsDataParam.getPmid());
			}

			// 根据PMCID（如果采集的是全文的话就有） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getPmcid())) {
				queryWrapper.like(FormsItemsData::getPmcid, formsItemsDataParam.getPmcid());
			}

			// 根据表单id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getFormId())) {
				queryWrapper.like(FormsItemsData::getFormId, formsItemsDataParam.getFormId());
			}

			// 根据表项id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getItemId())) {
				queryWrapper.like(FormsItemsData::getItemId, formsItemsDataParam.getItemId());
			}

			// 根据表项采集值 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getItemValue())) {
				queryWrapper.like(FormsItemsData::getItemValue, formsItemsDataParam.getItemValue());
			}

			// 根据表项值类型（字典 0采集 1审核） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsDataParam.getItemValueType())) {
				queryWrapper.like(FormsItemsData::getItemValueType, formsItemsDataParam.getItemValueType());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(FormsItemsData::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FormsItemsDataParam formsItemsDataParam) {
		FormsItemsData formsItemsData = new FormsItemsData();
		BeanUtil.copyProperties(formsItemsDataParam, formsItemsData);
		this.save(formsItemsData);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FormsItemsDataParam> formsItemsDataParamList) {
		formsItemsDataParamList.forEach(formsItemsDataParam -> {
			FormsItemsData formsItemsData = this.queryFormsItemsData(formsItemsDataParam);
			this.removeById(formsItemsData.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FormsItemsDataParam formsItemsDataParam) {
		FormsItemsData formsItemsData = this.queryFormsItemsData(formsItemsDataParam);
		BeanUtil.copyProperties(formsItemsDataParam, formsItemsData);
		this.updateById(formsItemsData);
	}

	@Override
	public FormsItemsData detail(FormsItemsDataParam formsItemsDataParam) {
		return this.queryFormsItemsData(formsItemsDataParam);
	}

	/**
	 * @func 获取表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	private FormsItemsData queryFormsItemsData(FormsItemsDataParam formsItemsDataParam) {
		FormsItemsData formsItemsData = this.getById(formsItemsDataParam.getId());
		if (ObjectUtil.isNull(formsItemsData)) {
			throw new ServiceException(FormsItemsDataExceptionEnum.NOT_EXIST);
		}
		return formsItemsData;
	}

	/**
	 * @func CIViC数据更新！
	 *
	 * @author Quan Xu
	 * @date 2021-10-18 11:10:00
	 */
	@Override
	public Boolean updateCivicData(HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		String strLogStarter       = ">>> [更新CIViC数据] ";
		String strFileNameWithPath = baseController.GetRootPath() + SettingBase.strPathCivicNightlyDatasets + UsuallyUsedUtils.GetDateTimeNumWithShortLineStr() + "_CIViC_nightly.tsv";
		Map<String, Boolean> mapAllLibraryPubRelation = new TreeMap<String, Boolean>(); // 2021-11-22（V0004-01.00.01-10）：用于收录和统计全部的文献数量！
		int intInsertedPubNum = 0; // 2021-11-22（V0004-01.00.01-10）：用于统计此次新插入的文献数量！
		log.info(strLogStarter + "CIViC数据待下载到的路径：‘" + strFileNameWithPath + "’..");
		
		// 2021-11-22（V0004-01.00.01-09）：// 判断操作系统类型：Windows下不备份LIMS数据文件！
		if(!baseController.GetRootPath().startsWith("/")) { // 不是Linux操作系统！
			String strDesc = "操作取消：Windows下更新CIViC数据，以免重复操作导致数据重复或被覆盖！";
			log.info(strLogStarter + strDesc);
			return false;
		}
		
		// 1. 首先，下载最新的文件！
		try {
			log.info(strLogStarter + "数据下载中..");
			FileUtils.copyURLToFile(new URL(SettingBase.strUrlCivicNightlyEvidence), new File(strFileNameWithPath));
			String strDesc = "CIViC数据下载成功：‘" + SettingBase.strUrlCivicNightlyEvidence + "’..";
			log.info(strLogStarter + strDesc);
			
			// 记录日志！
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeDownloadCivic);
			logsParam.setOperateDesc(strDesc);
			logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
			logsService.log(logsParam, session, request);
			
		} catch (Exception e) {
			e.printStackTrace();
			String strDesc = "CIViC数据下载失败：‘" + SettingBase.strUrlCivicNightlyEvidence + "’..";
			log.info(strLogStarter + strDesc);
			
			// 记录日志！
			LogsParam logsParam = new LogsParam();
			logsParam.setOperateType(SettingBase.strOperateTypeDownloadCivic);
			logsParam.setOperateDesc(strDesc);
			logsParam.setOperateSuccess(SettingBase.intSuccessFalse);
			logsService.log(logsParam, session, request);
			
			// 返回！
			return false;
			
		}
		
		// 2. 清除已有的CIViC数据！
		LambdaQueryWrapper<FormsItemsData> lqw = new LambdaQueryWrapper<FormsItemsData>();
		//lqw.eq(FormsItemsData::getCreateUserName, SettingBase.strUserCommon);
		lqw.eq(FormsItemsData::getCreateUserName, SettingBase.strUserCivic); // 2021-11-22（V0004-01.00.01-10）：新增，将CIViC数据独立于common，未来可能整合其他数据源的数据！
		this.remove(lqw);
		
		// 2021-11-22（V0004-01.00.01-10）：已有文献不再删除，做增量更新，减少不必要的重复操作！
//		// 3. 清除已有的文库-文献数据！
//		LambdaQueryWrapper<LibrariesPublications> lqw1 = new LambdaQueryWrapper<LibrariesPublications>();
//		lqw1.eq(LibrariesPublications::getGroupName, SettingBase.strGroupCommon);
//		librariesPublicationsService.remove(lqw1);
		
		// 4. 查询并存储全部可能涉及的表项！
		Map<String, Long> mapCivicFormsRelatedItemId = new TreeMap<String, Long>();
		for(String strEviType : SettingBase.mapCivicEviType2FormId.keySet()) {
			LambdaQueryWrapper<FormsItems> lqwForItemSearch = new LambdaQueryWrapper<FormsItems>();
			lqwForItemSearch.eq(FormsItems::getFormId, SettingBase.mapCivicEviType2FormId.get(strEviType));
			List<FormsItems> fiList = formsItemsService.list(lqwForItemSearch);
			for(FormsItems fi : fiList) {
				mapCivicFormsRelatedItemId.put(strEviType + "|" + fi.getItemName(), fi.getId()); // "证据类型|表项名称 -> 表项id"！
			}
		}
		
		// 5. 读取文件内容！
		log.info(strLogStarter + "读取文件内容..");
		int intTotalCivicEviLines = 0;
		Map<String, Boolean> mapCheckLibraryPubRelation = new TreeMap<String, Boolean>();
		Map<String, Integer> mapCheckEviTypeDataCount   = new TreeMap<String, Integer>(); // 记录每一类对应的证据数量！
		Map<Long, Boolean> mapIdLibraryQueried = new TreeMap<Long, Boolean>(); // 避免每次都要查询！
		List<String> strLines = FileUtil.readUtf8Lines(strFileNameWithPath);
		for(int i = 0; i < strLines.size(); i++) {
			log.info(strLogStarter + "行号i: “" + i + "”， 开始处理...");
			
			// 判断文件字段是否与配置的一致，不一致需要提示管理员审核并在必要时升级系统！
			if(i == 0 && !strLines.get(i).equals(SettingBase.strFieldNamesCivicEvidence)) {
				
				String strDesc = "此次下载的CIViC数据，其字段名称与设定的字段不一致，请联系管理员审核并在必要时升级系统！";
				log.info(strLogStarter + strDesc);
				
				// 记录日志！
				LogsParam logsParam = new LogsParam();
				logsParam.setOperateType(SettingBase.strOperateTypeUpdateCivic);
				logsParam.setOperateDesc(strDesc);
				logsParam.setOperateSuccess(SettingBase.intSuccessFalse);
				logsService.log(logsParam, session, request);
				
				// 返回！
				return false;
			}else if(i == 0) {
				String strDesc = "此次下载的CIViC数据，其字段名称与设定的字段一致，继续后续的数据读取与处理操作！";
				log.info(strLogStarter + strDesc);
				
				// 标题行的作用结束了，跳过标题行！
				continue;
			}
			
			// 数据读取与处理！
			String[] strArrLineData = strLines.get(i).split("\t");
			String strEviType = strArrLineData[SettingBase.intFieldNumCivicEviType];
			mapCheckEviTypeDataCount.put(strEviType, mapCheckEviTypeDataCount.containsKey(strEviType) ? mapCheckEviTypeDataCount.get(strEviType) + 1 : 1);
			String strBatchIdForThisLineData = UsuallyUsedUtils.GetDateTimeNumWithShortLineStr() + "-" + IdWorker.getId();
			log.info(strLogStarter + "行号i: “" + i + "”， 总字段数: “" + strArrLineData.length + "”...");
			Long idForm    = SettingBase.mapCivicEviType2FormId.get(strEviType);
			Long idProject = SettingBase.mapCivicEviType2ProjectId.get(strEviType);
			Long idLibrary = SettingBase.mapCivicEviType2LibraryId.get(strEviType);
			Map<String, Integer> mapCivicFormItemName2ColNum = SettingBase.mapCivicEviType2Map.get(strEviType);
			if(!strArrLineData[SettingBase.intFieldNumCivicSourceType].equals(SettingBase.strFieldNumCivicSourceType)) continue; // 非PubMed来源的证据，跳过！
			Long thisLinePMID = Long.parseLong(strArrLineData[SettingBase.intFieldNumCivicSourceId]); // 获取PMID！
			intTotalCivicEviLines++;
			List<FormsItemsData> formsItemsDataList = new ArrayList<FormsItemsData>(); // 临时存储全部数据对象，便于后续一次性持久化！
			for(int j = 0; j < strArrLineData.length; j++) {
				for(String strItemName : mapCivicFormItemName2ColNum.keySet()) {
					if(mapCivicFormItemName2ColNum.get(strItemName) == j) { // 判断当前字段是否匹配上！
						// 先获取表项id！
						String strThisKey = strEviType + "|" + strItemName;
						Long longIdFormItem = mapCivicFormsRelatedItemId.containsKey(strThisKey) ? mapCivicFormsRelatedItemId.get(strThisKey) : 0L;
						// 然后插入新的数据！
						FormsItemsData formsItemsData = new FormsItemsData();
						formsItemsData.setId(IdWorker.getId());
						formsItemsData.setStatus(0);
						//formsItemsData.setCreateUserName(SettingBase.strUserCommon);
						formsItemsData.setCreateUserName(SettingBase.strUserCivic); // 2021-11-22（V0004-01.00.01-10）：新增，将CIViC数据独立于common，未来可能整合其他数据源的数据！
						formsItemsData.setGroupName(SettingBase.strGroupCommon);
						formsItemsData.setProjectId(idProject);
						formsItemsData.setLibraryId(idLibrary);
						formsItemsData.setPmid(thisLinePMID);
						formsItemsData.setFormId(idForm);
						formsItemsData.setItemId(longIdFormItem);
						formsItemsData.setItemName(strItemName);
						formsItemsData.setItemValue(strArrLineData[j]); // 当前字段的数据！
						formsItemsData.setItemValueType(1); // 来自CIViC的数据都是审核过的！
						formsItemsData.setBatchId(strBatchIdForThisLineData);
						formsItemsDataList.add(formsItemsData); // 添加该数据！
					}
				}
			}
			this.saveBatch(formsItemsDataList); // 批量持久化：这一步比较耗时！！！
			log.info(strLogStarter + "行号i: “" + i + "”， 此次添加的数据行数（formsItemsData行）为“" + formsItemsDataList.size() + "”...");
			
			// 2021-11-22（V0004-01.00.01-10）：先获取全部已有文献！
			log.info(strLogStarter + "行号i: “" + i + "”， 获取全部已有文献：");
			if(mapIdLibraryQueried.containsKey(idLibrary)) {
				log.info(strLogStarter + "行号i: “" + i + "”，该文库对应的文献已经获取了，不需要再次查询...");
			}else {
				log.info(strLogStarter + "行号i: “" + i + "”，该文库对应的文献尚未获取，执行查询...");
				LambdaQueryWrapper<LibrariesPublications> lqwForPubListSearch = new LambdaQueryWrapper<LibrariesPublications>();
				lqwForPubListSearch.eq(LibrariesPublications::getLibraryId, idLibrary);
				List<LibrariesPublications> alreadyIncludedPubList = librariesPublicationsService.list(lqwForPubListSearch);
				for(LibrariesPublications lp : alreadyIncludedPubList) {
					mapCheckLibraryPubRelation.put(idLibrary + "|" + lp.getPmid(), true);
				}
				mapIdLibraryQueried.put(idLibrary, true); // 已经查了，避免后续再查！
			}
			
			// 添加文库-文献关联数据！
			String strThisLibraryPubRelation = idLibrary + "|" + thisLinePMID;
			mapAllLibraryPubRelation.put(strThisLibraryPubRelation, true); // 2021-11-22（V0004-01.00.01-10）：用于收录和统计全部的文献数量！
			if(!mapCheckLibraryPubRelation.containsKey(strThisLibraryPubRelation)) {
				log.info(strLogStarter + "行号i: “" + i + "”，文库对应当前文献不存在，执行添加...");
				intInsertedPubNum++;
				LibrariesPublications librariesPublication = new LibrariesPublications();
				librariesPublication.setGroupName(SettingBase.strGroupCommon);
				librariesPublication.setLibraryId(idLibrary);
				librariesPublication.setPmid(thisLinePMID);
				librariesPublicationsService.save(librariesPublication);
				mapCheckLibraryPubRelation.put(strThisLibraryPubRelation, true);
			}
		}
		
		// 记录日志！
		String strLogDesc = "此次数据更新涉及的新文献有“" + intInsertedPubNum + "”篇（增量更新，总量为“" + mapAllLibraryPubRelation.size() + "”篇），添加的CIViC证据行数为：“" + intTotalCivicEviLines + "”（替换更新），其中：";
		for(String strEviType : mapCheckEviTypeDataCount.keySet()) {
			strLogDesc += "“" + strEviType + "”：“" + mapCheckEviTypeDataCount.get(strEviType) + "”行，";
		}
		if(strLogDesc.endsWith("，")) strLogDesc = strLogDesc.substring(0, strLogDesc.length() - 1);
		strLogDesc += "。";
		log.info(strLogStarter + strLogDesc);
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeUpdateCivic);
		logsParam.setOperateDesc(strLogDesc);
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 2021-11-25（V0007-01.00.04-07）：为防止出现重复的关系对，这里执行一步去重操作！
		librariesPublicationsService.dedup(session, request);
		
		// 返回！
		return true;
		
	}

	/**
	 * @func 统计全部批次数量！
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 23:28:00
	 */
	@Override
	public Integer getBatchCountAll() {
		return this.baseMapper.getBatchCountAll();
	}
	
	/**
	 * @func 统计批次数量！
	 *
	 * @author Quan Xu
	 * @date 2021-10-21 14:56:00
	 */
	@Override
	public Integer getBatchCount(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strUserGroup = "guestGroup", strUserAccount = "guest" + IdWorker.getId(), strAccountType = "2"; // 默认阅读账号（2）！
		if(sysLoginUser != null) {
			strUserGroup = sysLoginUser.getAccountGroup();
			strUserAccount = sysLoginUser.getAccount();
			strAccountType = sysLoginUser.getAccountType();
		}else { // 用户没有登录的情况下，去session中找！
			String sessionUserAccount = (String)session.getAttribute("sessionUserAccount");
			if(sessionUserAccount != null) { // session中存在的话，取出！
				strUserAccount = sessionUserAccount;
			}else { // session中不存在的话，设置！
				session.setAttribute("sessionUserAccount", strUserAccount);
			}
		}
		return this.baseMapper.getBatchCount(formsItemsDataParam.getProjectId(), strUserGroup, formsItemsDataParam.getPmid(), strAccountType, strUserAccount);
	}

	/**
	 * @func 获取批次数据，用于下载！
	 *
	 * @author Quan Xu
	 * @date 2021-10-21 16:03:00
	 */
	@Override
	public List<String> getBatchDataForDownload(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strUserGroup = "guestGroup", strUserAccount = "guest" + IdWorker.getId(), strAccountType = "2"; // 默认阅读账号（2）！
		if(sysLoginUser != null) {
			strUserGroup = sysLoginUser.getAccountGroup();
			strUserAccount = sysLoginUser.getAccount();
			strAccountType = sysLoginUser.getAccountType();
		}else { // 用户没有登录的情况下，去session中找！
			String sessionUserAccount = (String)session.getAttribute("sessionUserAccount");
			if(sessionUserAccount != null) { // session中存在的话，取出！
				strUserAccount = sessionUserAccount;
			}else { // session中不存在的话，设置！
				session.setAttribute("sessionUserAccount", strUserAccount);
			}
		}
		return this.baseMapper.getBatchDataForDownload(formsItemsDataParam.getProjectId(), strUserGroup, formsItemsDataParam.getPmid(), strAccountType, strUserAccount);
	}
	
}
