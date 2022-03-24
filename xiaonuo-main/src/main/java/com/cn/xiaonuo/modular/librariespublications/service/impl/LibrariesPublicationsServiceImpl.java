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
package com.cn.xiaonuo.modular.librariespublications.service.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.librariespublications.entity.LibrariesPublications;
import com.cn.xiaonuo.modular.librariespublications.enums.LibrariesPublicationsExceptionEnum;
import com.cn.xiaonuo.modular.librariespublications.mapper.LibrariesPublicationsMapper;
import com.cn.xiaonuo.modular.librariespublications.param.LibrariesPublicationsParam;
import com.cn.xiaonuo.modular.librariespublications.service.LibrariesPublicationsService;
import com.cn.xiaonuo.modular.logs.param.LogsParam;
import com.cn.xiaonuo.modular.logs.service.LogsService;
import com.cn.xiaonuo.utils.SettingBase;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @func 文库文献关联表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-17 16:38:11
 */
@Service
public class LibrariesPublicationsServiceImpl extends ServiceImpl<LibrariesPublicationsMapper, LibrariesPublications> implements LibrariesPublicationsService {

	@Resource
	private LogsService logsService;
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<LibrariesPublications> page(LibrariesPublicationsParam librariesPublicationsParam) {
		QueryWrapper<LibrariesPublications> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(librariesPublicationsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getId())) {
				queryWrapper.lambda().eq(LibrariesPublications::getId, librariesPublicationsParam.getId());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getGroupName())) {
				queryWrapper.lambda().like(LibrariesPublications::getGroupName, librariesPublicationsParam.getGroupName());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getLibraryId())) {
				queryWrapper.lambda().like(LibrariesPublications::getLibraryId, librariesPublicationsParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPmid())) {
				queryWrapper.lambda().like(LibrariesPublications::getPmid, librariesPublicationsParam.getPmid());
			}
			
			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPmcid())) {
				queryWrapper.lambda().like(LibrariesPublications::getPmcid, librariesPublicationsParam.getPmcid());
			}

			// 根据DOI 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getDoi())) {
				queryWrapper.lambda().like(LibrariesPublications::getDoi, librariesPublicationsParam.getDoi());
			}

			// 根据文献标题 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubTitle())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubTitle, librariesPublicationsParam.getPubTitle());
			}

			// 根据文献作者 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubAuthors())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubAuthors, librariesPublicationsParam.getPubAuthors());
			}

			// 根据期刊名称 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubJournal())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubJournal, librariesPublicationsParam.getPubJournal());
			}

			// 根据发表年份 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubYear())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubYear, librariesPublicationsParam.getPubYear());
			}

			// 根据2020年度影响因子 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubIf2020())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubIf2020, librariesPublicationsParam.getPubIf2020());
			}

			// 根据文献引用方式 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubCitation())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubCitation, librariesPublicationsParam.getPubCitation());
			}
			
			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubAbstract())) {
				queryWrapper.lambda().like(LibrariesPublications::getPubCitation, librariesPublicationsParam.getPubAbstract());
			}

		}
		
		// 排序！
		if(librariesPublicationsParam.getSortBy() != null && librariesPublicationsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(librariesPublicationsParam.getSortBy());
			if(librariesPublicationsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(LibrariesPublications::getPmid, LibrariesPublications::getGroupName);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LibrariesPublications> list(LibrariesPublicationsParam librariesPublicationsParam) {
		LambdaQueryWrapper<LibrariesPublications> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(librariesPublicationsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getId())) {
				queryWrapper.eq(LibrariesPublications::getId, librariesPublicationsParam.getId());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getGroupName())) {
				queryWrapper.like(LibrariesPublications::getGroupName, librariesPublicationsParam.getGroupName());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getLibraryId())) {
				queryWrapper.like(LibrariesPublications::getLibraryId, librariesPublicationsParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPmid())) {
				queryWrapper.like(LibrariesPublications::getPmid, librariesPublicationsParam.getPmid());
			}
			
			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(librariesPublicationsParam.getPubAbstract())) {
				queryWrapper.like(LibrariesPublications::getPubCitation, librariesPublicationsParam.getPubAbstract());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(LibrariesPublications::getPmid, LibrariesPublications::getGroupName);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(LibrariesPublicationsParam librariesPublicationsParam) {
		LibrariesPublications librariesPublications = new LibrariesPublications();
		BeanUtil.copyProperties(librariesPublicationsParam, librariesPublications);
		List<LibrariesPublications> lbList = this.list(librariesPublicationsParam);
		if(lbList.size() == 0) {
			this.save(librariesPublications);
		}else if(lbList.size() > 1) {
			for(int i = 1; i < lbList.size(); i++) { // 保留第一个：从第二个开始！
				this.removeById(lbList.get(i).getId()); // 删除多余的！
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<LibrariesPublicationsParam> librariesPublicationsParamList) {
		librariesPublicationsParamList.forEach(librariesPublicationsParam -> {
			LibrariesPublications librariesPublications = this.queryLibrariesPublications(librariesPublicationsParam);
			this.removeById(librariesPublications.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(LibrariesPublicationsParam librariesPublicationsParam) {
		LibrariesPublications librariesPublications = this.queryLibrariesPublications(librariesPublicationsParam);
		BeanUtil.copyProperties(librariesPublicationsParam, librariesPublications);
		this.updateById(librariesPublications);
	}

	@Override
	public LibrariesPublications detail(LibrariesPublicationsParam librariesPublicationsParam) {
		return this.queryLibrariesPublications(librariesPublicationsParam);
	}

	/**
	 * @func 获取文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	private LibrariesPublications queryLibrariesPublications(LibrariesPublicationsParam librariesPublicationsParam) {
		LibrariesPublications librariesPublications = this.getById(librariesPublicationsParam.getId());
		if (ObjectUtil.isNull(librariesPublications)) {
			throw new ServiceException(LibrariesPublicationsExceptionEnum.NOT_EXIST);
		}
		return librariesPublications;
	}

	@Override
	public String dedup(HttpSession session, HttpServletRequest request) {
		
		// 全局变量！
		int intRemoved = 0;
		
		// 查询并处理！
		LambdaQueryWrapper<LibrariesPublications> lqwForPubListSearch = new LambdaQueryWrapper<LibrariesPublications>();
		lqwForPubListSearch.gt(LibrariesPublications::getId, 0);
		List<LibrariesPublications> allList = this.list(lqwForPubListSearch);
		Map<String, Boolean> mapRelations = new TreeMap<String, Boolean>();
		for(LibrariesPublications lp : allList) {
			String strKey = lp.getLibraryId() + "|" + lp.getPmid();
			if(mapRelations.containsKey(strKey)) {
				this.removeById(lp.getId()); // 已经存在的话，去掉已有的！
				intRemoved++;
			}else {
				mapRelations.put(strKey, true);
			}
		}
		
		// 记录日志！
		String strLog = "此轮对LibrariesPublications表进行遍历去重处理操作，检索得到全部关系对“" + allList.size() + "”对，去除了“" + intRemoved + "”对，保留了“" + mapRelations.size() + "”对唯一的关系对...";
		LogsParam logsParam = new LogsParam();
		logsParam.setOperateType(SettingBase.strOperateTypeRelationDedup);
		logsParam.setOperateDesc(strLog);
		logsParam.setOperateSuccess(SettingBase.intSuccessTrue);
		logsService.log(logsParam, session, request);
		
		// 返回！
		return strLog;
		
	}

}
