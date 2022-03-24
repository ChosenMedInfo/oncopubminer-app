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
package com.cn.xiaonuo.modular.errorreport.service.impl;

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
import com.cn.xiaonuo.modular.errorreport.entity.ErrorReport;
import com.cn.xiaonuo.modular.errorreport.enums.ErrorReportExceptionEnum;
import com.cn.xiaonuo.modular.errorreport.mapper.ErrorReportMapper;
import com.cn.xiaonuo.modular.errorreport.param.ErrorReportParam;
import com.cn.xiaonuo.modular.errorreport.service.ErrorReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 错误报告表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-11-08 14:19:51
 */
@Service
public class ErrorReportServiceImpl extends ServiceImpl<ErrorReportMapper, ErrorReport> implements ErrorReportService {

	@Override
	public PageResult<ErrorReport> page(ErrorReportParam errorReportParam) {
		QueryWrapper<ErrorReport> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(errorReportParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getId())) {
				queryWrapper.lambda().eq(ErrorReport::getId, errorReportParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getStatus())) {
				queryWrapper.lambda().like(ErrorReport::getStatus, errorReportParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getCreateUserName())) {
				queryWrapper.lambda().like(ErrorReport::getCreateUserName, errorReportParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getUpdateUserName())) {
				queryWrapper.lambda().like(ErrorReport::getUpdateUserName, errorReportParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getGroupId())) {
				queryWrapper.lambda().like(ErrorReport::getGroupId, errorReportParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getGroupName())) {
				queryWrapper.lambda().like(ErrorReport::getGroupName, errorReportParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getPmid())) {
				queryWrapper.lambda().like(ErrorReport::getPmid, errorReportParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getPmcid())) {
				queryWrapper.lambda().like(ErrorReport::getPmcid, errorReportParam.getPmcid());
			}

			// 根据错误类别 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getErrorType())) {
				queryWrapper.lambda().like(ErrorReport::getErrorType, errorReportParam.getErrorType());
			}

			// 根据错误内容 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getErrorContent())) {
				queryWrapper.lambda().like(ErrorReport::getErrorContent, errorReportParam.getErrorContent());
			}

			// 根据错误位置 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getErrorOffset())) {
				queryWrapper.lambda().like(ErrorReport::getErrorOffset, errorReportParam.getErrorOffset());
			}

		}
		
		// 排序！
		if(errorReportParam.getSortBy() != null && errorReportParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(errorReportParam.getSortBy());
			if(errorReportParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(ErrorReport::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<ErrorReport> list(ErrorReportParam errorReportParam) {
		LambdaQueryWrapper<ErrorReport> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(errorReportParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getId())) {
				queryWrapper.eq(ErrorReport::getId, errorReportParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getStatus())) {
				queryWrapper.like(ErrorReport::getStatus, errorReportParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getCreateUserName())) {
				queryWrapper.like(ErrorReport::getCreateUserName, errorReportParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getUpdateUserName())) {
				queryWrapper.like(ErrorReport::getUpdateUserName, errorReportParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getGroupId())) {
				queryWrapper.like(ErrorReport::getGroupId, errorReportParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getGroupName())) {
				queryWrapper.like(ErrorReport::getGroupName, errorReportParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getPmid())) {
				queryWrapper.like(ErrorReport::getPmid, errorReportParam.getPmid());
			}

			// 根据PMCID 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getPmcid())) {
				queryWrapper.like(ErrorReport::getPmcid, errorReportParam.getPmcid());
			}

			// 根据错误类别 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getErrorType())) {
				queryWrapper.like(ErrorReport::getErrorType, errorReportParam.getErrorType());
			}

			// 根据错误内容 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getErrorContent())) {
				queryWrapper.like(ErrorReport::getErrorContent, errorReportParam.getErrorContent());
			}

			// 根据错误位置 模糊查询
			if (ObjectUtil.isNotEmpty(errorReportParam.getErrorOffset())) {
				queryWrapper.like(ErrorReport::getErrorOffset, errorReportParam.getErrorOffset());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(ErrorReport::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(ErrorReportParam errorReportParam) {
		ErrorReport errorReport = new ErrorReport();
		BeanUtil.copyProperties(errorReportParam, errorReport);
		
		// 添加用户组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		errorReport.setGroupName(sysLoginUser == null ? "guestGroup" : (sysLoginUser.getAccountGroup() == null ? sysLoginUser.getName() + "Group" : sysLoginUser.getAccountGroup()));
		
		// 添加创建者！
		errorReport.setCreateUserName(sysLoginUser == null ? "guest" : sysLoginUser.getName());
		
		this.save(errorReport);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<ErrorReportParam> errorReportParamList) {
		errorReportParamList.forEach(errorReportParam -> {
			ErrorReport errorReport = this.queryErrorReport(errorReportParam);
			this.removeById(errorReport.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(ErrorReportParam errorReportParam) {
		ErrorReport errorReport = this.queryErrorReport(errorReportParam);
		BeanUtil.copyProperties(errorReportParam, errorReport);
		this.updateById(errorReport);
	}

	@Override
	public ErrorReport detail(ErrorReportParam errorReportParam) {
		return this.queryErrorReport(errorReportParam);
	}

	/**
	 * @func 获取错误报告表
	 *
	 * @author Quan Xu
	 * @date 2021-11-08 14:19:51
	 */
	private ErrorReport queryErrorReport(ErrorReportParam errorReportParam) {
		ErrorReport errorReport = this.getById(errorReportParam.getId());
		if (ObjectUtil.isNull(errorReport)) {
			throw new ServiceException(ErrorReportExceptionEnum.NOT_EXIST);
		}
		return errorReport;
	}
	
}
