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
package com.cn.xiaonuo.modular.formsitemsoptions.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.formsitemsoptions.entity.FormsItemsOptions;
import com.cn.xiaonuo.modular.formsitemsoptions.enums.FormsItemsOptionsExceptionEnum;
import com.cn.xiaonuo.modular.formsitemsoptions.mapper.FormsItemsOptionsMapper;
import com.cn.xiaonuo.modular.formsitemsoptions.param.FormsItemsOptionsParam;
import com.cn.xiaonuo.modular.formsitemsoptions.service.FormsItemsOptionsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 表单项选项列表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:16
 */
@Service
public class FormsItemsOptionsServiceImpl extends ServiceImpl<FormsItemsOptionsMapper, FormsItemsOptions> implements FormsItemsOptionsService {

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<FormsItemsOptions> page(FormsItemsOptionsParam formsItemsOptionsParam) {
		QueryWrapper<FormsItemsOptions> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(formsItemsOptionsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getId())) {
				queryWrapper.lambda().eq(FormsItemsOptions::getId, formsItemsOptionsParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getStatus())) {
				queryWrapper.lambda().eq(FormsItemsOptions::getStatus, formsItemsOptionsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getCreateUserName())) {
				queryWrapper.lambda().like(FormsItemsOptions::getCreateUserName, formsItemsOptionsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getUpdateUserName())) {
				queryWrapper.lambda().like(FormsItemsOptions::getUpdateUserName, formsItemsOptionsParam.getUpdateUserName());
			}

			// 根据表单项id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getItemId())) {
				queryWrapper.lambda().eq(FormsItemsOptions::getItemId, formsItemsOptionsParam.getItemId());
			}

			// 根据选项值 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getOptionValue())) {
				queryWrapper.lambda().like(FormsItemsOptions::getOptionValue, formsItemsOptionsParam.getOptionValue());
			}

			// 根据选项值说明 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getOptionDesc())) {
				queryWrapper.lambda().like(FormsItemsOptions::getOptionDesc, formsItemsOptionsParam.getOptionDesc());
			}

			// 根据是否选中（字典 0否 1是） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getOptionChecked())) {
				queryWrapper.lambda().like(FormsItemsOptions::getOptionChecked, formsItemsOptionsParam.getOptionChecked());
			}

		}
		
		// 排序！
		if(formsItemsOptionsParam.getSortBy() != null && formsItemsOptionsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(formsItemsOptionsParam.getSortBy());
			if(formsItemsOptionsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByAsc(FormsItemsOptions::getCreateTime, FormsItemsOptions::getId);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormsItemsOptions> list(FormsItemsOptionsParam formsItemsOptionsParam) {
		LambdaQueryWrapper<FormsItemsOptions> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(formsItemsOptionsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getId())) {
				queryWrapper.eq(FormsItemsOptions::getId, formsItemsOptionsParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getStatus())) {
				queryWrapper.eq(FormsItemsOptions::getStatus, formsItemsOptionsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getCreateUserName())) {
				queryWrapper.like(FormsItemsOptions::getCreateUserName, formsItemsOptionsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getUpdateUserName())) {
				queryWrapper.like(FormsItemsOptions::getUpdateUserName, formsItemsOptionsParam.getUpdateUserName());
			}

			// 根据表单项id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getItemId())) {
				queryWrapper.eq(FormsItemsOptions::getItemId, formsItemsOptionsParam.getItemId());
			}

			// 根据选项值 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getOptionValue())) {
				queryWrapper.like(FormsItemsOptions::getOptionValue, formsItemsOptionsParam.getOptionValue());
			}

			// 根据选项值说明 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getOptionDesc())) {
				queryWrapper.like(FormsItemsOptions::getOptionDesc, formsItemsOptionsParam.getOptionDesc());
			}

			// 根据是否选中（字典 0否 1是） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsOptionsParam.getOptionChecked())) {
				queryWrapper.like(FormsItemsOptions::getOptionChecked, formsItemsOptionsParam.getOptionChecked());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByAsc(FormsItemsOptions::getCreateTime, FormsItemsOptions::getId);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FormsItemsOptionsParam formsItemsOptionsParam) {
		FormsItemsOptions formsItemsOptions = new FormsItemsOptions();
		BeanUtil.copyProperties(formsItemsOptionsParam, formsItemsOptions);
		this.save(formsItemsOptions);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FormsItemsOptionsParam> formsItemsOptionsParamList) {
		formsItemsOptionsParamList.forEach(formsItemsOptionsParam -> {
			FormsItemsOptions formsItemsOptions = this.queryFormsItemsOptions(formsItemsOptionsParam);
			this.removeById(formsItemsOptions.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FormsItemsOptionsParam formsItemsOptionsParam) {
		FormsItemsOptions formsItemsOptions = this.queryFormsItemsOptions(formsItemsOptionsParam);
		BeanUtil.copyProperties(formsItemsOptionsParam, formsItemsOptions);
		this.updateById(formsItemsOptions);
	}

	@Override
	public FormsItemsOptions detail(FormsItemsOptionsParam formsItemsOptionsParam) {
		return this.queryFormsItemsOptions(formsItemsOptionsParam);
	}

	/**
	 * @func 获取表单项选项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:16
	 */
	private FormsItemsOptions queryFormsItemsOptions(FormsItemsOptionsParam formsItemsOptionsParam) {
		FormsItemsOptions formsItemsOptions = this.getById(formsItemsOptionsParam.getId());
		if (ObjectUtil.isNull(formsItemsOptions)) {
			throw new ServiceException(FormsItemsOptionsExceptionEnum.NOT_EXIST);
		}
		return formsItemsOptions;
	}
	
}
