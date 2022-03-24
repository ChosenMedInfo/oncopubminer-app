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
package com.cn.xiaonuo.modular.projectpubstatus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.projectpubstatus.entity.ProjectPubStatus;
import com.cn.xiaonuo.modular.projectpubstatus.enums.ProjectPubStatusExceptionEnum;
import com.cn.xiaonuo.modular.projectpubstatus.mapper.ProjectPubStatusMapper;
import com.cn.xiaonuo.modular.projectpubstatus.param.ProjectPubStatusParam;
import com.cn.xiaonuo.modular.projectpubstatus.service.ProjectPubStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 项目文献状态表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-28 22:55:16
 */
@Service
public class ProjectPubStatusServiceImpl extends ServiceImpl<ProjectPubStatusMapper, ProjectPubStatus> implements ProjectPubStatusService {

	@Override
	public PageResult<ProjectPubStatus> page(ProjectPubStatusParam projectPubStatusParam) {
		QueryWrapper<ProjectPubStatus> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(projectPubStatusParam)) {

			// 根据主键id（各种id组合的字符串） 查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getId())) {
				queryWrapper.lambda().eq(ProjectPubStatus::getId, projectPubStatusParam.getId());
			}

			// 根据状态（字典 0已提交） 模糊查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getStatus())) {
				queryWrapper.lambda().like(ProjectPubStatus::getStatus, projectPubStatusParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getCreateUserName())) {
				queryWrapper.lambda().like(ProjectPubStatus::getCreateUserName, projectPubStatusParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getUpdateUserName())) {
				queryWrapper.lambda().like(ProjectPubStatus::getUpdateUserName, projectPubStatusParam.getUpdateUserName());
			}

		}
		
		// 排序！
		if(projectPubStatusParam.getSortBy() != null && projectPubStatusParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(projectPubStatusParam.getSortBy());
			if(projectPubStatusParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(ProjectPubStatus::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<ProjectPubStatus> list(ProjectPubStatusParam projectPubStatusParam) {
		LambdaQueryWrapper<ProjectPubStatus> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(projectPubStatusParam)) {

			// 根据主键id（各种id组合的字符串） 查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getId())) {
				queryWrapper.eq(ProjectPubStatus::getId, projectPubStatusParam.getId());
			}

			// 根据状态（字典 0已提交） 模糊查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getStatus())) {
				queryWrapper.like(ProjectPubStatus::getStatus, projectPubStatusParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getCreateUserName())) {
				queryWrapper.like(ProjectPubStatus::getCreateUserName, projectPubStatusParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectPubStatusParam.getUpdateUserName())) {
				queryWrapper.like(ProjectPubStatus::getUpdateUserName, projectPubStatusParam.getUpdateUserName());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(ProjectPubStatus::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(ProjectPubStatusParam projectPubStatusParam) {
		ProjectPubStatus projectPubStatus = new ProjectPubStatus();
		BeanUtil.copyProperties(projectPubStatusParam, projectPubStatus);
		this.save(projectPubStatus);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<ProjectPubStatusParam> projectPubStatusParamList) {
		projectPubStatusParamList.forEach(projectPubStatusParam -> {
			ProjectPubStatus projectPubStatus = this.queryProjectPubStatus(projectPubStatusParam);
			this.removeById(projectPubStatus.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(ProjectPubStatusParam projectPubStatusParam) {
		ProjectPubStatus projectPubStatus = this.queryProjectPubStatus(projectPubStatusParam);
		BeanUtil.copyProperties(projectPubStatusParam, projectPubStatus);
		this.updateById(projectPubStatus);
	}

	@Override
	public ProjectPubStatus detail(ProjectPubStatusParam projectPubStatusParam) {
		return this.queryProjectPubStatus(projectPubStatusParam);
	}

	/**
	 * @func 获取项目文献状态表
	 *
	 * @author Quan Xu
	 * @date 2021-09-28 22:55:16
	 */
	private ProjectPubStatus queryProjectPubStatus(ProjectPubStatusParam projectPubStatusParam) {
		ProjectPubStatus projectPubStatus = this.getById(projectPubStatusParam.getId());
		if (ObjectUtil.isNull(projectPubStatus)) {
			throw new ServiceException(ProjectPubStatusExceptionEnum.NOT_EXIST);
		}
		return projectPubStatus;
	}
	
}
