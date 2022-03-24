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
package com.cn.xiaonuo.modular.libraries.service.impl;

import java.util.Arrays;
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
import com.cn.xiaonuo.modular.libraries.entity.Libraries;
import com.cn.xiaonuo.modular.libraries.enums.LibrariesExceptionEnum;
import com.cn.xiaonuo.modular.libraries.mapper.LibrariesMapper;
import com.cn.xiaonuo.modular.libraries.param.LibrariesParam;
import com.cn.xiaonuo.modular.libraries.service.LibrariesService;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @func 文库列表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-14 15:23:57
 */
@Service
public class LibrariesServiceImpl extends ServiceImpl<LibrariesMapper, Libraries> implements LibrariesService {

	@Override
	public PageResult<Libraries> page(LibrariesParam librariesParam) {
		
		QueryWrapper<Libraries> queryWrapper = new QueryWrapper<>();
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			if(!sysLoginUser.getAccountType().equals("1")) {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(Libraries::getGroupName, sysLoginUser.getAccountGroup()).eq(Libraries::getStatus, "2") // 当前用户组，非管理员，状态需要为封存的状态！
					)
					.or(
						i -> i.eq(Libraries::getGroupName, "commonGroup").eq(Libraries::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}else {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(Libraries::getGroupName, sysLoginUser.getAccountGroup()).ne(Libraries::getStatus, "1") // 当前用户组，管理员，状态需要为非删除状态！
					)
					.or(
						i -> i.eq(Libraries::getGroupName, "commonGroup").eq(Libraries::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}
		}else {
			queryWrapper.lambda().eq(Libraries::getGroupName, "commonGroup");
			queryWrapper.lambda().eq(Libraries::getStatus, "2"); // 这里需要注意的是，对于commonGroup，只能显示已经封存的！
		}
		
		if (ObjectUtil.isNotNull(librariesParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(librariesParam.getId())) {
				queryWrapper.lambda().eq(Libraries::getId, librariesParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getStatus())) {
				queryWrapper.lambda().like(Libraries::getStatus, librariesParam.getStatus());
			}

			// 根据文库名称 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getLibraryName())) {
				queryWrapper.lambda().like(Libraries::getLibraryName, librariesParam.getLibraryName());
			}

			// 根据文库说明 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getLibraryDesc())) {
				queryWrapper.lambda().like(Libraries::getLibraryDesc, librariesParam.getLibraryDesc());
			}
			
			// 根据远程检索用关键词组 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getKwsRemote())) {
				queryWrapper.lambda().like(Libraries::getKwsRemote, librariesParam.getKwsRemote());
			}
			
			// 根据本地检索用关键词组 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getKwsLocal())) {
				queryWrapper.lambda().like(Libraries::getKwsLocal, librariesParam.getKwsLocal());
			}
			
			// 根据updateUserName是否为null进行检索！
			if (ObjectUtil.isNotEmpty(librariesParam.getUpdateUserName())) {
				String[] strArrIds = librariesParam.getUpdateUserName().split(",");
				queryWrapper.lambda().in(Libraries::getId, Arrays.asList(strArrIds));
			}

		}
		
		// 排序！
		if(librariesParam.getSortBy() != null && librariesParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(librariesParam.getSortBy());
			if(librariesParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(Libraries::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<Libraries> list(LibrariesParam librariesParam) {
		
		LambdaQueryWrapper<Libraries> queryWrapper = new LambdaQueryWrapper<>();
		
		// 状态设置：只能罗列已封存的，这里跟page有所区别！
		queryWrapper.eq(Libraries::getStatus, "2");
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser != null && sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			queryWrapper.eq(Libraries::getGroupName, sysLoginUser.getAccountGroup());
		}else {
			queryWrapper.eq(Libraries::getGroupName, "commonGroup"); // 未登录用户，查看commonGroup的collection！
		}
		
		if (ObjectUtil.isNotNull(librariesParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(librariesParam.getId())) {
				queryWrapper.eq(Libraries::getId, librariesParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getStatus())) {
				queryWrapper.like(Libraries::getStatus, librariesParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getCreateUserName())) {
				queryWrapper.like(Libraries::getCreateUserName, librariesParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getUpdateUserName())) {
				queryWrapper.like(Libraries::getUpdateUserName, librariesParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getGroupId())) {
				queryWrapper.like(Libraries::getGroupId, librariesParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getGroupName())) {
				queryWrapper.like(Libraries::getGroupName, librariesParam.getGroupName());
			}

			// 根据文库名称 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getLibraryName())) {
				queryWrapper.like(Libraries::getLibraryName, librariesParam.getLibraryName());
			}

			// 根据文库说明 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getLibraryDesc())) {
				queryWrapper.like(Libraries::getLibraryDesc, librariesParam.getLibraryDesc());
			}
			
			// 根据远程检索用关键词组 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getKwsRemote())) {
				queryWrapper.like(Libraries::getKwsRemote, librariesParam.getKwsRemote());
			}
			
			// 根据本地检索用关键词组 模糊查询
			if (ObjectUtil.isNotEmpty(librariesParam.getKwsLocal())) {
				queryWrapper.like(Libraries::getKwsLocal, librariesParam.getKwsLocal());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(Libraries::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(LibrariesParam librariesParam) {
		Libraries libraries = new Libraries();
		BeanUtil.copyProperties(librariesParam, libraries);
		
		// 添加用户组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		libraries.setGroupName(sysLoginUser.getAccountGroup());
		
		// 添加创建者！
		libraries.setCreateUserName(sysLoginUser.getName());
		
		this.save(libraries);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<LibrariesParam> librariesParamList) {
		librariesParamList.forEach(librariesParam -> {
			Libraries libraries = this.queryLibraries(librariesParam);
			this.removeById(libraries.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(LibrariesParam librariesParam) {
		Libraries libraries = this.queryLibraries(librariesParam);
		BeanUtil.copyProperties(librariesParam, libraries);
		this.updateById(libraries);
	}

	@Override
	public Libraries detail(LibrariesParam librariesParam) {
		return this.queryLibraries(librariesParam);
	}

	/**
	 * @func 获取文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	private Libraries queryLibraries(LibrariesParam librariesParam) {
		Libraries libraries = this.getById(librariesParam.getId());
		if (ObjectUtil.isNull(libraries)) {
			throw new ServiceException(LibrariesExceptionEnum.NOT_EXIST);
		}
		return libraries;
	}
	
	/**
	 * @func 通过文献id和用户组，查看不包含该文献的文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-10-12 16:00:00
	 */
	public List<Libraries> listByPmidAndGroupName(Long pmid, String groupName) {
		return this.baseMapper.listByPmidAndGroupName(pmid, groupName);
	}
	
}
