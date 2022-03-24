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
package com.cn.xiaonuo.modular.projects.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.forms.entity.Forms;
import com.cn.xiaonuo.modular.forms.service.FormsService;
import com.cn.xiaonuo.modular.libraries.entity.Libraries;
import com.cn.xiaonuo.modular.libraries.service.LibrariesService;
import com.cn.xiaonuo.modular.projects.entity.Projects;
import com.cn.xiaonuo.modular.projects.enums.ProjectsExceptionEnum;
import com.cn.xiaonuo.modular.projects.mapper.ProjectsMapper;
import com.cn.xiaonuo.modular.projects.param.ProjectsParam;
import com.cn.xiaonuo.modular.projects.service.ProjectsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 项目列表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-07 17:00:34
 */
@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects> implements ProjectsService {

	@Autowired
	private FormsService formsService;
	
	@Autowired
	private LibrariesService librariesService;
	
	@Override
	public PageResult<Projects> page(ProjectsParam projectsParam) {
		
		QueryWrapper<Projects> queryWrapper = new QueryWrapper<>();
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			if(!sysLoginUser.getAccountType().equals("1")) {
				queryWrapper.lambda().and(
					wq -> wq.and(
						//i -> i.eq(Projects::getGroupName, sysLoginUser.getAccountGroup()).eq(Projects::getStatus, "2") // 当前用户组，非管理员，状态为封存！
						i -> i.eq(Projects::getGroupName, sysLoginUser.getAccountGroup()).eq(Projects::getStatus, "2").like(Projects::getMemberIds, sysLoginUser.getId()) // 2021-12-14（V0018-01.01.02-06）：当前用户组，非管理员，状态为封存，自己参与的项目！！！
					)
					.or(
						i -> i.eq(Projects::getGroupName, "commonGroup").eq(Projects::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}else {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(Projects::getGroupName, sysLoginUser.getAccountGroup()).ne(Projects::getStatus, "1") // 当前用户组，管理员，状态不能为已删除！
					)
					.or(
						i -> i.eq(Projects::getGroupName, "commonGroup").eq(Projects::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}
		}else {
			queryWrapper.lambda().eq(Projects::getGroupName, "commonGroup");
			queryWrapper.lambda().eq(Projects::getStatus, "2"); // 这里需要注意的是，对于commonGroup，只能显示已经封存的！
		}
		
		if (ObjectUtil.isNotNull(projectsParam)) {

			// 根据项目名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getProjectName())) {
				queryWrapper.lambda().like(Projects::getProjectName, projectsParam.getProjectName());
			}

			// 根据项目说明 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getProjectDesc())) {
				queryWrapper.lambda().like(Projects::getProjectDesc, projectsParam.getProjectDesc());
			}

			// 根据表单id 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getFormId())) {
				queryWrapper.lambda().eq(Projects::getFormId, projectsParam.getFormId());
			}

			// 根据表单名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getFormName())) {
				queryWrapper.lambda().like(Projects::getFormName, projectsParam.getFormName());
			}
			
			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getLibraryId())) {
				queryWrapper.lambda().eq(Projects::getLibraryId, projectsParam.getLibraryId());
			}

			// 根据文库名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getLibraryName())) {
				queryWrapper.lambda().like(Projects::getLibraryName, projectsParam.getLibraryName());
			}

			// 根据项目成员 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getMembers())) {
				queryWrapper.lambda().like(Projects::getMembers, projectsParam.getMembers());
			}

		}
		
		// 排序！
		if(projectsParam.getSortBy() != null && projectsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(projectsParam.getSortBy());
			if(projectsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(Projects::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<Projects> list(ProjectsParam projectsParam) {
		
		LambdaQueryWrapper<Projects> queryWrapper = new LambdaQueryWrapper<>();
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			queryWrapper.and(
				wq -> wq.and(
					i -> i.eq(Projects::getGroupName, sysLoginUser.getAccountGroup()).ne(Projects::getStatus, "1") // 当前用户组，状态不能为已删除！
				)
				.or(
					i -> i.eq(Projects::getGroupName, "commonGroup").eq(Projects::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
				)
			);
		}else {
			queryWrapper.eq(Projects::getGroupName, "commonGroup");
			queryWrapper.eq(Projects::getStatus, "2"); // 这里需要注意的是，对于commonGroup，只能显示已经封存的！
		}
		
		if (ObjectUtil.isNotNull(projectsParam)) {

			// 根据项目名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getProjectName())) {
				queryWrapper.like(Projects::getProjectName, projectsParam.getProjectName());
			}

			// 根据项目说明 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getProjectDesc())) {
				queryWrapper.like(Projects::getProjectDesc, projectsParam.getProjectDesc());
			}

			// 根据表单id 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getFormId())) {
				queryWrapper.eq(Projects::getFormId, projectsParam.getFormId());
			}

			// 根据表单名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getFormName())) {
				queryWrapper.like(Projects::getFormName, projectsParam.getFormName());
			}
			
			// 根据文库id 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getLibraryId())) {
				queryWrapper.eq(Projects::getLibraryId, projectsParam.getLibraryId());
			}

			// 根据文库名称 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getLibraryName())) {
				queryWrapper.like(Projects::getLibraryName, projectsParam.getLibraryName());
			}

			// 根据项目成员 模糊查询
			if (ObjectUtil.isNotEmpty(projectsParam.getMembers())) {
				queryWrapper.like(Projects::getMembers, projectsParam.getMembers());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(Projects::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(ProjectsParam projectsParam) {
		Projects projects = new Projects();
		BeanUtil.copyProperties(projectsParam, projects);
		
		// 添加用户组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		projects.setGroupName(sysLoginUser.getAccountGroup());
		
		// 添加创建者！
		projects.setCreateUserName(sysLoginUser.getName());
		
		// 表单信息！
		if(ObjectUtil.isNotEmpty(projectsParam.getFormId())) {
			Forms forms = formsService.getById(projectsParam.getFormId());
			if(forms != null) {
				projects.setFormName(forms.getFormName());
			}
		}
		
		// 文库信息！
		if(ObjectUtil.isNotEmpty(projectsParam.getLibraryId())) {
			Libraries libraries = librariesService.getById(projectsParam.getLibraryId());
			if(libraries != null) {
				projects.setLibraryName(libraries.getLibraryName());
			}
		}
		
		// 状态！
		if(projectsParam.getStatus() != null && projectsParam.getStatus() == 2) {
			projects.setStatus(2);
		}
		
		this.save(projects);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<ProjectsParam> projectsParamList) {
		projectsParamList.forEach(projectsParam -> {
			Projects projects = this.queryProjects(projectsParam);
			this.removeById(projects.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(ProjectsParam projectsParam) {
		Projects projects = this.queryProjects(projectsParam);
		BeanUtil.copyProperties(projectsParam, projects);
		
		// 表单信息！
		if(ObjectUtil.isNotEmpty(projectsParam.getFormId())) {
			Forms forms = formsService.getById(projectsParam.getFormId());
			if(forms != null) {
				projects.setFormName(forms.getFormName());
			}
		}
		
		// 文库信息！
		if(ObjectUtil.isNotEmpty(projectsParam.getLibraryId())) {
			Libraries libraries = librariesService.getById(projectsParam.getLibraryId());
			if(libraries != null) {
				projects.setLibraryName(libraries.getLibraryName());
			}
		}
		
		if(projectsParam.getStatus() != null && projectsParam.getStatus() == 2) {
			projects.setStatus(2);
		}
		this.updateById(projects);
	}

	@Override
	public Projects detail(ProjectsParam projectsParam) {
		return this.queryProjects(projectsParam);
	}

	/**
	 * @func 获取项目列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-07 17:00:34
	 */
	private Projects queryProjects(ProjectsParam projectsParam) {
		Projects projects = this.getById(projectsParam.getId());
		if (ObjectUtil.isNull(projects)) {
			throw new ServiceException(ProjectsExceptionEnum.NOT_EXIST);
		}
		return projects;
	}
	
}
