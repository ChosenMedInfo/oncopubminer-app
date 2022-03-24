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
package com.cn.xiaonuo.modular.formsitems.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.formsitems.entity.FormsItems;
import com.cn.xiaonuo.modular.formsitems.enums.FormsItemsExceptionEnum;
import com.cn.xiaonuo.modular.formsitems.mapper.FormsItemsMapper;
import com.cn.xiaonuo.modular.formsitems.param.FormsItemsParam;
import com.cn.xiaonuo.modular.formsitems.service.FormsItemsService;
import com.cn.xiaonuo.modular.formsitemsoptions.entity.FormsItemsOptions;
import com.cn.xiaonuo.modular.formsitemsoptions.param.FormsItemsOptionsParam;
import com.cn.xiaonuo.modular.formsitemsoptions.service.FormsItemsOptionsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.annotation.Resource;

/**
 * @func 表单项列表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:19
 */
@Service
public class FormsItemsServiceImpl extends ServiceImpl<FormsItemsMapper, FormsItems> implements FormsItemsService {

	@Resource
	private FormsItemsOptionsService formsItemsOptionsService;
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<FormsItems> page(FormsItemsParam formsItemsParam) {
		QueryWrapper<FormsItems> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(formsItemsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getId())) {
				queryWrapper.lambda().eq(FormsItems::getId, formsItemsParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getStatus())) {
				queryWrapper.lambda().eq(FormsItems::getStatus, formsItemsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getCreateUserName())) {
				queryWrapper.lambda().like(FormsItems::getCreateUserName, formsItemsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getUpdateUserName())) {
				queryWrapper.lambda().like(FormsItems::getUpdateUserName, formsItemsParam.getUpdateUserName());
			}

			// 根据表单id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getFormId())) {
				queryWrapper.lambda().eq(FormsItems::getFormId, formsItemsParam.getFormId());
			}

			// 根据表项名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemName())) {
				queryWrapper.lambda().like(FormsItems::getItemName, formsItemsParam.getItemName());
			}

			// 根据表项类型（字典 0text 1radio ...） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemType())) {
				queryWrapper.lambda().like(FormsItems::getItemType, formsItemsParam.getItemType());
			}

			// 根据表项默认值 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemDefault())) {
				queryWrapper.lambda().like(FormsItems::getItemDefault, formsItemsParam.getItemDefault());
			}

			// 根据最大长度（0表示不限制） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemMaxLength())) {
				queryWrapper.lambda().like(FormsItems::getItemMaxLength, formsItemsParam.getItemMaxLength());
			}

			// 根据是否必填（字典 0否 1是） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemRequired())) {
				queryWrapper.lambda().like(FormsItems::getItemRequired, formsItemsParam.getItemRequired());
			}

			// 根据提示信息 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemTips())) {
				queryWrapper.lambda().like(FormsItems::getItemTips, formsItemsParam.getItemTips());
			}

			// 根据排序序号 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemSort())) {
				queryWrapper.lambda().like(FormsItems::getItemSort, formsItemsParam.getItemSort());
			}

			// 根据选项类型（字典 0无 1自定义 2癌种 3药物 4基因 5变异 ...） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemOptionType())) {
				queryWrapper.lambda().like(FormsItems::getItemOptionType, formsItemsParam.getItemOptionType());
			}

		}
		
		// 排序！
		if(formsItemsParam.getSortBy() != null && formsItemsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(formsItemsParam.getSortBy());
			if(formsItemsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByAsc(FormsItems::getItemSort, FormsItems::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormsItems> list(FormsItemsParam formsItemsParam) {
		LambdaQueryWrapper<FormsItems> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(formsItemsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getId())) {
				queryWrapper.eq(FormsItems::getId, formsItemsParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getStatus())) {
				queryWrapper.eq(FormsItems::getStatus, formsItemsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getCreateUserName())) {
				queryWrapper.like(FormsItems::getCreateUserName, formsItemsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getUpdateUserName())) {
				queryWrapper.like(FormsItems::getUpdateUserName, formsItemsParam.getUpdateUserName());
			}

			// 根据表单id 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getFormId())) {
				queryWrapper.eq(FormsItems::getFormId, formsItemsParam.getFormId());
			}

			// 根据表项名称 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemName())) {
				queryWrapper.like(FormsItems::getItemName, formsItemsParam.getItemName());
			}

			// 根据表项类型（字典 0text 1radio ...） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemType())) {
				queryWrapper.like(FormsItems::getItemType, formsItemsParam.getItemType());
			}

			// 根据表项默认值 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemDefault())) {
				queryWrapper.like(FormsItems::getItemDefault, formsItemsParam.getItemDefault());
			}

			// 根据最大长度（0表示不限制） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemMaxLength())) {
				queryWrapper.like(FormsItems::getItemMaxLength, formsItemsParam.getItemMaxLength());
			}

			// 根据是否必填（字典 0否 1是） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemRequired())) {
				queryWrapper.like(FormsItems::getItemRequired, formsItemsParam.getItemRequired());
			}

			// 根据提示信息 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemTips())) {
				queryWrapper.like(FormsItems::getItemTips, formsItemsParam.getItemTips());
			}

			// 根据排序序号 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemSort())) {
				queryWrapper.like(FormsItems::getItemSort, formsItemsParam.getItemSort());
			}

			// 根据选项类型（字典 0无 1自定义 2癌种 3药物 4基因 5变异 ...） 模糊查询
			if (ObjectUtil.isNotEmpty(formsItemsParam.getItemOptionType())) {
				queryWrapper.like(FormsItems::getItemOptionType, formsItemsParam.getItemOptionType());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByAsc(FormsItems::getItemSort, FormsItems::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FormsItemsParam formsItemsParam) {
		FormsItems formsItems = new FormsItems();
		Long itemId = IdWorker.getId();
		BeanUtil.copyProperties(formsItemsParam, formsItems);
		formsItems.setId(itemId); // 注意，这个需要放到copyProperties之后！！！
		this.save(formsItems);
		
		// 2021-08-31：同步新增选项！
		if(formsItemsParam.getItemOptionList() != null && formsItemsParam.getItemOptionList().size() > 0) {
			for(String strOptionValue : formsItemsParam.getItemOptionList()) {
				FormsItemsOptionsParam formsItemsOptionsParam = new FormsItemsOptionsParam();
				formsItemsOptionsParam.setCreateUserName(LoginContextHolder.me().getSysLoginUserWithoutException().getAccount());
				formsItemsOptionsParam.setItemId(itemId);
				formsItemsOptionsParam.setOptionValue(strOptionValue);
				formsItemsOptionsService.add(formsItemsOptionsParam);
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FormsItemsParam> formsItemsParamList) {
		
		formsItemsParamList.forEach(formsItemsParam -> {
			FormsItems formsItems = this.queryFormsItems(formsItemsParam);
			this.removeById(formsItems.getId());
		});
		
		// 2021-08-31：同步删除选项！
		for(FormsItemsParam fip : formsItemsParamList) {
			LambdaQueryWrapper<FormsItemsOptions> lwq = new LambdaQueryWrapper<FormsItemsOptions>();
			lwq.eq(FormsItemsOptions::getItemId, fip.getId());
			formsItemsOptionsService.remove(lwq);
		}
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FormsItemsParam formsItemsParam) {
		FormsItems formsItems = this.queryFormsItems(formsItemsParam);
		BeanUtil.copyProperties(formsItemsParam, formsItems);
		this.updateById(formsItems);
		
		// 2021-08-31：先删除已有选项！
		LambdaQueryWrapper<FormsItemsOptions> lwq = new LambdaQueryWrapper<FormsItemsOptions>();
		lwq.eq(FormsItemsOptions::getItemId, formsItemsParam.getId());
		formsItemsOptionsService.remove(lwq);
		
		// 2021-08-31：接着添加新增选项！
		if(formsItemsParam.getItemOptionList() != null && formsItemsParam.getItemOptionList().size() > 0) {
			for(String strOptionValue : formsItemsParam.getItemOptionList()) {
				FormsItemsOptionsParam formsItemsOptionsParam = new FormsItemsOptionsParam();
				formsItemsOptionsParam.setCreateUserName(LoginContextHolder.me().getSysLoginUserWithoutException().getAccount());
				formsItemsOptionsParam.setItemId(formsItemsParam.getId());
				formsItemsOptionsParam.setOptionValue(strOptionValue);
				formsItemsOptionsService.add(formsItemsOptionsParam);
			}
		}
	}

	@Override
	public FormsItems detail(FormsItemsParam formsItemsParam) {
		return this.queryFormsItems(formsItemsParam);
	}

	/**
	 * @func 获取表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	private FormsItems queryFormsItems(FormsItemsParam formsItemsParam) {
		FormsItems formsItems = this.getById(formsItemsParam.getId());
		if (ObjectUtil.isNull(formsItems)) {
			throw new ServiceException(FormsItemsExceptionEnum.NOT_EXIST);
		}
		return formsItems;
	}
	
}
