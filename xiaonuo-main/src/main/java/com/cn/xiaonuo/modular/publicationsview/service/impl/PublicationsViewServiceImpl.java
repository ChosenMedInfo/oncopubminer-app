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
package com.cn.xiaonuo.modular.publicationsview.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.librariespublications.entity.LibrariesPublications;
import com.cn.xiaonuo.modular.librariespublications.param.LibrariesPublications4PubPage;
import com.cn.xiaonuo.modular.publicationsview.entity.PublicationsView;
import com.cn.xiaonuo.modular.publicationsview.enums.PublicationsViewExceptionEnum;
import com.cn.xiaonuo.modular.publicationsview.mapper.PublicationsViewMapper;
import com.cn.xiaonuo.modular.publicationsview.param.PublicationsViewParam;
import com.cn.xiaonuo.modular.publicationsview.service.PublicationsViewService;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.util.ObjectUtil;

/**
 * @func 文献视图service接口实现类
 *
 * @author Quan Xu
 * @date 2021-11-25 10:16:01
 */
@Service
public class PublicationsViewServiceImpl extends ServiceImpl<PublicationsViewMapper, PublicationsView> implements PublicationsViewService {

	@Override
	public PageResult<PublicationsView> page(PublicationsViewParam publicationsViewParam) {
		QueryWrapper<PublicationsView> queryWrapper = new QueryWrapper<>();
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			if(!sysLoginUser.getAccountType().equals("1")) {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(PublicationsView::getGroupName, sysLoginUser.getAccountGroup()).eq(PublicationsView::getStatus, "2") // 当前用户组，非管理员，状态需要为封存的状态！
					)
					.or(
						i -> i.eq(PublicationsView::getGroupName, "commonGroup").eq(PublicationsView::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}else {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(PublicationsView::getGroupName, sysLoginUser.getAccountGroup()).ne(PublicationsView::getStatus, "1") // 当前用户组，管理员，状态需要为非删除状态！
					)
					.or(
						i -> i.eq(PublicationsView::getGroupName, "commonGroup").eq(PublicationsView::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}
		}else {
			queryWrapper.lambda().eq(PublicationsView::getGroupName, "commonGroup");
			queryWrapper.lambda().eq(PublicationsView::getStatus, "2"); // 这里需要注意的是，对于commonGroup，只能显示已经封存的！
		}
		
		if (ObjectUtil.isNotNull(publicationsViewParam)) {

			// 根据主键id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getId())) {
				queryWrapper.lambda().like(PublicationsView::getId, publicationsViewParam.getId());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getGroupName())) {
				queryWrapper.lambda().like(PublicationsView::getGroupName, publicationsViewParam.getGroupName());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getLibraryId())) {
				queryWrapper.lambda().like(PublicationsView::getLibraryId, publicationsViewParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPmid())) {
				queryWrapper.lambda().like(PublicationsView::getPmid, publicationsViewParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPmcid())) {
				queryWrapper.lambda().like(PublicationsView::getPmcid, publicationsViewParam.getPmcid());
			}

			// 根据DOI 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getDoi())) {
				queryWrapper.lambda().like(PublicationsView::getDoi, publicationsViewParam.getDoi());
			}

			// 根据文献标题 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubTitle())) {
				queryWrapper.lambda().like(PublicationsView::getPubTitle, publicationsViewParam.getPubTitle());
			}

			// 根据文献作者 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubAuthors())) {
				queryWrapper.lambda().like(PublicationsView::getPubAuthors, publicationsViewParam.getPubAuthors());
			}

			// 根据期刊名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubJournal())) {
				queryWrapper.lambda().like(PublicationsView::getPubJournal, publicationsViewParam.getPubJournal());
			}

			// 根据发表年份 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubYear())) {
				queryWrapper.lambda().like(PublicationsView::getPubYear, publicationsViewParam.getPubYear());
			}

			// 根据2020年度影响因子 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubIf2020())) {
				queryWrapper.lambda().like(PublicationsView::getPubIf2020, publicationsViewParam.getPubIf2020());
			}

			// 根据文献引用方式 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubCitation())) {
				queryWrapper.lambda().like(PublicationsView::getPubCitation, publicationsViewParam.getPubCitation());
			}

			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubAbstract())) {
				queryWrapper.lambda().like(PublicationsView::getPubAbstract, publicationsViewParam.getPubAbstract());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getStatus())) {
				queryWrapper.lambda().like(PublicationsView::getStatus, publicationsViewParam.getStatus());
			}

		}
		
		// 排序！
		if(publicationsViewParam.getSortBy() != null && publicationsViewParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(publicationsViewParam.getSortBy());
			if(publicationsViewParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(PublicationsView::getId);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<PublicationsView> list(PublicationsViewParam publicationsViewParam) {
		LambdaQueryWrapper<PublicationsView> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(publicationsViewParam)) {

			// 根据主键id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getId())) {
				queryWrapper.like(PublicationsView::getId, publicationsViewParam.getId());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getGroupName())) {
				queryWrapper.like(PublicationsView::getGroupName, publicationsViewParam.getGroupName());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getLibraryId())) {
				queryWrapper.like(PublicationsView::getLibraryId, publicationsViewParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPmid())) {
				queryWrapper.like(PublicationsView::getPmid, publicationsViewParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPmcid())) {
				queryWrapper.like(PublicationsView::getPmcid, publicationsViewParam.getPmcid());
			}

			// 根据DOI 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getDoi())) {
				queryWrapper.like(PublicationsView::getDoi, publicationsViewParam.getDoi());
			}

			// 根据文献标题 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubTitle())) {
				queryWrapper.like(PublicationsView::getPubTitle, publicationsViewParam.getPubTitle());
			}

			// 根据文献作者 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubAuthors())) {
				queryWrapper.like(PublicationsView::getPubAuthors, publicationsViewParam.getPubAuthors());
			}

			// 根据期刊名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubJournal())) {
				queryWrapper.like(PublicationsView::getPubJournal, publicationsViewParam.getPubJournal());
			}

			// 根据发表年份 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubYear())) {
				queryWrapper.like(PublicationsView::getPubYear, publicationsViewParam.getPubYear());
			}

			// 根据2020年度影响因子 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubIf2020())) {
				queryWrapper.like(PublicationsView::getPubIf2020, publicationsViewParam.getPubIf2020());
			}

			// 根据文献引用方式 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubCitation())) {
				queryWrapper.like(PublicationsView::getPubCitation, publicationsViewParam.getPubCitation());
			}

			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubAbstract())) {
				queryWrapper.like(PublicationsView::getPubAbstract, publicationsViewParam.getPubAbstract());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getStatus())) {
				queryWrapper.like(PublicationsView::getStatus, publicationsViewParam.getStatus());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(PublicationsView::getId);
		
		return this.list(queryWrapper);
	}

	@Override
	public PublicationsView detail(PublicationsViewParam publicationsViewParam) {
		return this.queryPublicationsView(publicationsViewParam);
	}

	/**
	 * @func 获取文献视图
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:16:01
	 */
	private PublicationsView queryPublicationsView(PublicationsViewParam publicationsViewParam) {
		PublicationsView publicationsView = this.getById(publicationsViewParam.getId());
		if (ObjectUtil.isNull(publicationsView)) {
			throw new ServiceException(PublicationsViewExceptionEnum.NOT_EXIST);
		}
		return publicationsView;
	}

	/**
	 * @func 查询文库中的文献
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 10:50:00
	 */
	@Override
	public PageResult<LibrariesPublications4PubPage> pubPage(PublicationsViewParam publicationsViewParam) {
		QueryWrapper<PublicationsView> queryWrapper = new QueryWrapper<>();
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			if(!sysLoginUser.getAccountType().equals("1")) {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(PublicationsView::getGroupName, sysLoginUser.getAccountGroup()).eq(PublicationsView::getStatus, "2") // 当前用户组，非管理员，状态需要为封存的状态！
					)
					.or(
						i -> i.eq(PublicationsView::getGroupName, "commonGroup").eq(PublicationsView::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}else {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(PublicationsView::getGroupName, sysLoginUser.getAccountGroup()).ne(PublicationsView::getStatus, "1") // 当前用户组，管理员，状态需要为非删除状态！
					)
					.or(
						i -> i.eq(PublicationsView::getGroupName, "commonGroup").eq(PublicationsView::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}
		}else {
			queryWrapper.lambda().eq(PublicationsView::getGroupName, "commonGroup");
			queryWrapper.lambda().eq(PublicationsView::getStatus, "2"); // 这里需要注意的是，对于commonGroup，只能显示已经封存的！
		}
		
		if (ObjectUtil.isNotNull(publicationsViewParam)) {

			// 根据主键id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getId())) {
				queryWrapper.lambda().like(PublicationsView::getId, publicationsViewParam.getId());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getGroupName())) {
				queryWrapper.lambda().like(PublicationsView::getGroupName, publicationsViewParam.getGroupName());
			}

			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getLibraryId())) {
				queryWrapper.lambda().like(PublicationsView::getLibraryId, publicationsViewParam.getLibraryId());
			}

			// 根据文献id 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPmid())) {
				queryWrapper.lambda().like(PublicationsView::getPmid, publicationsViewParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPmcid())) {
				queryWrapper.lambda().like(PublicationsView::getPmcid, publicationsViewParam.getPmcid());
			}

			// 根据DOI 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getDoi())) {
				queryWrapper.lambda().like(PublicationsView::getDoi, publicationsViewParam.getDoi());
			}

			// 根据文献标题 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubTitle())) {
				queryWrapper.lambda().like(PublicationsView::getPubTitle, publicationsViewParam.getPubTitle());
			}

			// 根据文献作者 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubAuthors())) {
				queryWrapper.lambda().like(PublicationsView::getPubAuthors, publicationsViewParam.getPubAuthors());
			}

			// 根据期刊名称 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubJournal())) {
				queryWrapper.lambda().like(PublicationsView::getPubJournal, publicationsViewParam.getPubJournal());
			}

			// 根据发表年份 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubYear())) {
				queryWrapper.lambda().like(PublicationsView::getPubYear, publicationsViewParam.getPubYear());
			}

			// 根据2020年度影响因子 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubIf2020())) {
				queryWrapper.lambda().like(PublicationsView::getPubIf2020, publicationsViewParam.getPubIf2020());
			}

			// 根据文献引用方式 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubCitation())) {
				queryWrapper.lambda().like(PublicationsView::getPubCitation, publicationsViewParam.getPubCitation());
			}

			// 根据文献摘要 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getPubAbstract())) {
				queryWrapper.lambda().like(PublicationsView::getPubAbstract, publicationsViewParam.getPubAbstract());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(publicationsViewParam.getStatus())) {
				queryWrapper.lambda().like(PublicationsView::getStatus, publicationsViewParam.getStatus());
			}

		}
		
		// 排序！
		QueryWrapper<PublicationsView> queryWrapperForOrder = new QueryWrapper<>();
		if(publicationsViewParam.getSortBy() != null && publicationsViewParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(publicationsViewParam.getSortBy());
			if(publicationsViewParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapperForOrder.orderByAsc(mysqlFieldName);
			}else {
				queryWrapperForOrder.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapperForOrder.lambda().orderByDesc(PublicationsView::getPmid, PublicationsView::getGroupName);
		}
		
		// 执行查询并返回！
		return new PageResult<>(this.baseMapper.pubPage(PageFactory.defaultPage(), queryWrapper, queryWrapperForOrder));
	}
	
}
