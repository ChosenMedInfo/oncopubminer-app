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
package com.cn.xiaonuo.modular.forms.service.impl;

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
import com.cn.xiaonuo.modular.forms.entity.Forms;
import com.cn.xiaonuo.modular.forms.enums.FormsExceptionEnum;
import com.cn.xiaonuo.modular.forms.mapper.FormsMapper;
import com.cn.xiaonuo.modular.forms.param.FormsParam;
import com.cn.xiaonuo.modular.forms.service.FormsService;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @func 数据收集表单列表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-07-08 10:34:51
 */
@Service
public class FormsServiceImpl extends ServiceImpl<FormsMapper, Forms> implements FormsService {

	@Override
	public PageResult<Forms> page(FormsParam formsParam) {
		
		QueryWrapper<Forms> queryWrapper = new QueryWrapper<>();
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			if(!sysLoginUser.getAccountType().equals("1")) {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(Forms::getGroupName, sysLoginUser.getAccountGroup()).eq(Forms::getStatus, "2") // 当前用户组，非管理员，状态需要为封存的状态！
					)
					.or(
						i -> i.eq(Forms::getGroupName, "commonGroup").eq(Forms::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}else {
				queryWrapper.lambda().and(
					wq -> wq.and(
						i -> i.eq(Forms::getGroupName, sysLoginUser.getAccountGroup()).ne(Forms::getStatus, "1") // 当前用户组，管理员，状态需要为非删除状态！
					)
					.or(
						i -> i.eq(Forms::getGroupName, "commonGroup").eq(Forms::getStatus, "2") // 对于commonGroup，只能显示已经封存的！
					)
				);
			}
		}else {
			queryWrapper.lambda().eq(Forms::getGroupName, "commonGroup");
			queryWrapper.lambda().eq(Forms::getStatus, "2"); // 这里需要注意的是，对于commonGroup，只能显示已经封存的！
		}
		
		if (ObjectUtil.isNotNull(formsParam)) {

			// 根据表单名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsParam.getFormName())) {
				queryWrapper.lambda().like(Forms::getFormName, formsParam.getFormName());
			}

			// 根据表单说明 模糊查询
			if (ObjectUtil.isNotEmpty(formsParam.getFormDesc())) {
				queryWrapper.lambda().like(Forms::getFormDesc, formsParam.getFormDesc());
			}

		}
		
		// 排序！
		if(formsParam.getSortBy() != null && formsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(formsParam.getSortBy());
			if(formsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(Forms::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<Forms> list(FormsParam formsParam) {
		
		LambdaQueryWrapper<Forms> queryWrapper = new LambdaQueryWrapper<>();
		
		// 状态设置：只能罗列已封存的，这里跟page有所区别！
		queryWrapper.eq(Forms::getStatus, "2");
		
		// 用户组限制！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		if(sysLoginUser != null && sysLoginUser.getAccount() != null && !sysLoginUser.getAccount().equals("")) {
			queryWrapper.and(
				wq -> wq.eq(Forms::getGroupName, sysLoginUser.getAccountGroup()).or().eq(Forms::getGroupName, "commonGroup")
			);
		}else {
			queryWrapper.eq(Forms::getGroupName, "commonGroup");
		}
		
		if (ObjectUtil.isNotNull(formsParam)) {

			// 根据表单名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsParam.getFormName())) {
				queryWrapper.like(Forms::getFormName, formsParam.getFormName());
			}

			// 根据表单说明 模糊查询
			if (ObjectUtil.isNotEmpty(formsParam.getFormDesc())) {
				queryWrapper.like(Forms::getFormDesc, formsParam.getFormDesc());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(Forms::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FormsParam formsParam) {
		Forms forms = new Forms();
		BeanUtil.copyProperties(formsParam, forms);
		
		// 添加用户组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		forms.setGroupName(sysLoginUser.getAccountGroup());
		
		this.save(forms);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FormsParam> formsParamList) {
		formsParamList.forEach(formsParam -> {
			Forms forms = this.queryForms(formsParam);
			this.removeById(forms.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FormsParam formsParam) {
		Forms forms = this.queryForms(formsParam);
		BeanUtil.copyProperties(formsParam, forms);
		this.updateById(forms);
	}

	@Override
	public Forms detail(FormsParam formsParam) {
		return this.queryForms(formsParam);
	}

	/**
	 * @func 获取数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	private Forms queryForms(FormsParam formsParam) {
		Forms forms = this.getById(formsParam.getId());
		if (ObjectUtil.isNull(forms)) {
			throw new ServiceException(FormsExceptionEnum.NOT_EXIST);
		}
		return forms;
	}

	/**
	 * @func 验证同名表单是否存在！
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 11:58:00
	 */
	@Override
	public List<Forms> checkFormNameExist(String strFormName) {
		FormsParam formsParam = new FormsParam();
		formsParam.setFormName(strFormName);
		return list(formsParam);
	}
	
}
