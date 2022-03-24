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
package com.cn.xiaonuo.modular.publications.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.publications.entity.Publications;
import com.cn.xiaonuo.modular.publications.enums.PublicationsExceptionEnum;
import com.cn.xiaonuo.modular.publications.mapper.PublicationsMapper;
import com.cn.xiaonuo.modular.publications.param.PublicationsParam;
import com.cn.xiaonuo.modular.publications.service.PublicationsService;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @func 文献列表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-14 16:33:06
 */
@Service
public class PublicationsServiceImpl extends ServiceImpl<PublicationsMapper, Publications> implements PublicationsService {

	@Override
	public PageResult<Publications> page(PublicationsParam publicationsParam) {
		
		QueryWrapper<Publications> queryWrapper = new QueryWrapper<>();
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			queryWrapper.lambda().eq(Publications::getGroupName, sysLoginUser.getAccountGroup());
		}else {
			queryWrapper.lambda().eq(Publications::getGroupName, "OncoPubMiner");
		}
		
		if (ObjectUtil.isNotNull(publicationsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getId())) {
				queryWrapper.lambda().eq(Publications::getId, publicationsParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getStatus())) {
				queryWrapper.lambda().like(Publications::getStatus, publicationsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getCreateUserName())) {
				queryWrapper.lambda().like(Publications::getCreateUserName, publicationsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getUpdateUserName())) {
				queryWrapper.lambda().like(Publications::getUpdateUserName, publicationsParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getGroupId())) {
				queryWrapper.lambda().like(Publications::getGroupId, publicationsParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getGroupName())) {
				queryWrapper.lambda().like(Publications::getGroupName, publicationsParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPmid())) {
				queryWrapper.lambda().like(Publications::getPmid, publicationsParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPmcid())) {
				queryWrapper.lambda().like(Publications::getPmcid, publicationsParam.getPmcid());
			}

			// 根据DOI 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getDoi())) {
				queryWrapper.lambda().like(Publications::getDoi, publicationsParam.getDoi());
			}

			// 根据文献标题 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubTitle())) {
				queryWrapper.lambda().like(Publications::getPubTitle, publicationsParam.getPubTitle());
			}

			// 根据文献作者 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubAuthors())) {
				queryWrapper.lambda().like(Publications::getPubAuthors, publicationsParam.getPubAuthors());
			}

			// 根据期刊名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubJournal())) {
				queryWrapper.lambda().like(Publications::getPubJournal, publicationsParam.getPubJournal());
			}

			// 根据发表年份 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubYear())) {
				queryWrapper.lambda().like(Publications::getPubYear, publicationsParam.getPubYear());
			}

			// 根据2020年度影响因子 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubIf2020())) {
				queryWrapper.lambda().like(Publications::getPubIf2020, publicationsParam.getPubIf2020());
			}

			// 根据文献引用方式 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubCitation())) {
				queryWrapper.lambda().like(Publications::getPubCitation, publicationsParam.getPubCitation());
			}
			
			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubCitation())) {
				queryWrapper.lambda().like(Publications::getPubCitation, publicationsParam.getPubCitation());
			}

		}
		
		// 排序！
		if(publicationsParam.getSortBy() != null && publicationsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(publicationsParam.getSortBy());
			if(publicationsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(Publications::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<Publications> list(PublicationsParam publicationsParam) {
		
		LambdaQueryWrapper<Publications> queryWrapper = new LambdaQueryWrapper<>();
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			queryWrapper.eq(Publications::getGroupName, sysLoginUser.getAccountGroup());
		}else {
			queryWrapper.eq(Publications::getGroupName, "OncoPubMiner");
		}
		
		if (ObjectUtil.isNotNull(publicationsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getId())) {
				queryWrapper.eq(Publications::getId, publicationsParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getStatus())) {
				queryWrapper.like(Publications::getStatus, publicationsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getCreateUserName())) {
				queryWrapper.like(Publications::getCreateUserName, publicationsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getUpdateUserName())) {
				queryWrapper.like(Publications::getUpdateUserName, publicationsParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getGroupId())) {
				queryWrapper.like(Publications::getGroupId, publicationsParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getGroupName())) {
				queryWrapper.like(Publications::getGroupName, publicationsParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPmid())) {
				queryWrapper.like(Publications::getPmid, publicationsParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPmcid())) {
				queryWrapper.like(Publications::getPmcid, publicationsParam.getPmcid());
			}

			// 根据DOI 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getDoi())) {
				queryWrapper.like(Publications::getDoi, publicationsParam.getDoi());
			}

			// 根据文献标题 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubTitle())) {
				queryWrapper.like(Publications::getPubTitle, publicationsParam.getPubTitle());
			}

			// 根据文献作者 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubAuthors())) {
				queryWrapper.like(Publications::getPubAuthors, publicationsParam.getPubAuthors());
			}

			// 根据期刊名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubJournal())) {
				queryWrapper.like(Publications::getPubJournal, publicationsParam.getPubJournal());
			}

			// 根据发表年份 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubYear())) {
				queryWrapper.like(Publications::getPubYear, publicationsParam.getPubYear());
			}

			// 根据2020年度影响因子 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubIf2020())) {
				queryWrapper.like(Publications::getPubIf2020, publicationsParam.getPubIf2020());
			}

			// 根据文献引用方式 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubCitation())) {
				queryWrapper.like(Publications::getPubCitation, publicationsParam.getPubCitation());
			}
			
			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsParam.getPubAbstract())) {
				queryWrapper.like(Publications::getPubCitation, publicationsParam.getPubAbstract());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(Publications::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(PublicationsParam publicationsParam) {
		Publications publications = new Publications();
		BeanUtil.copyProperties(publicationsParam, publications);
		
		// 添加用户组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		publications.setGroupName(sysLoginUser.getAccountGroup());
		
		// 添加创建者！
		publications.setCreateUserName(sysLoginUser.getName());
		
		this.save(publications);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<PublicationsParam> publicationsParamList) {
		publicationsParamList.forEach(publicationsParam -> {
			Publications publications = this.queryPublications(publicationsParam);
			this.removeById(publications.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(PublicationsParam publicationsParam) {
		Publications publications = this.queryPublications(publicationsParam);
		BeanUtil.copyProperties(publicationsParam, publications);
		this.updateById(publications);
	}

	@Override
	public Publications detail(PublicationsParam publicationsParam) {
		return this.queryPublications(publicationsParam);
	}

	/**
	 * @func 获取文献列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 16:33:06
	 */
	private Publications queryPublications(PublicationsParam publicationsParam) {
		Publications publications = this.getById(publicationsParam.getId());
		if (ObjectUtil.isNull(publications)) {
			throw new ServiceException(PublicationsExceptionEnum.NOT_EXIST);
		}
		return publications;
	}
	
}
