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
package com.cn.xiaonuo.modular.funcsuggest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.funcsuggest.entity.FuncSuggest;
import com.cn.xiaonuo.modular.funcsuggest.enums.FuncSuggestExceptionEnum;
import com.cn.xiaonuo.modular.funcsuggest.mapper.FuncSuggestMapper;
import com.cn.xiaonuo.modular.funcsuggest.param.FuncSuggestParam;
import com.cn.xiaonuo.modular.funcsuggest.service.FuncSuggestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 文献推荐表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-18 11:42:11
 */
@Service
public class FuncSuggestServiceImpl extends ServiceImpl<FuncSuggestMapper, FuncSuggest> implements FuncSuggestService {

	@Override
	public PageResult<FuncSuggest> page(FuncSuggestParam funcSuggestParam) {
		QueryWrapper<FuncSuggest> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(funcSuggestParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getId())) {
				queryWrapper.lambda().eq(FuncSuggest::getId, funcSuggestParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getStatus())) {
				queryWrapper.lambda().like(FuncSuggest::getStatus, funcSuggestParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getCreateUserName())) {
				queryWrapper.lambda().like(FuncSuggest::getCreateUserName, funcSuggestParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getUpdateUserName())) {
				queryWrapper.lambda().like(FuncSuggest::getUpdateUserName, funcSuggestParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getGroupId())) {
				queryWrapper.lambda().like(FuncSuggest::getGroupId, funcSuggestParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getGroupName())) {
				queryWrapper.lambda().like(FuncSuggest::getGroupName, funcSuggestParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getPmid())) {
				queryWrapper.lambda().like(FuncSuggest::getPmid, funcSuggestParam.getPmid());
			}

		}
		
		// 排序！
		if(funcSuggestParam.getSortBy() != null && funcSuggestParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(funcSuggestParam.getSortBy());
			if(funcSuggestParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(FuncSuggest::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<FuncSuggest> list(FuncSuggestParam funcSuggestParam) {
		LambdaQueryWrapper<FuncSuggest> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(funcSuggestParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getId())) {
				queryWrapper.eq(FuncSuggest::getId, funcSuggestParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getStatus())) {
				queryWrapper.like(FuncSuggest::getStatus, funcSuggestParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getCreateUserName())) {
				queryWrapper.like(FuncSuggest::getCreateUserName, funcSuggestParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getUpdateUserName())) {
				queryWrapper.like(FuncSuggest::getUpdateUserName, funcSuggestParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getGroupId())) {
				queryWrapper.like(FuncSuggest::getGroupId, funcSuggestParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getGroupName())) {
				queryWrapper.like(FuncSuggest::getGroupName, funcSuggestParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(funcSuggestParam.getPmid())) {
				queryWrapper.like(FuncSuggest::getPmid, funcSuggestParam.getPmid());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(FuncSuggest::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FuncSuggestParam funcSuggestParam) {
		FuncSuggest funcSuggest = new FuncSuggest();
		BeanUtil.copyProperties(funcSuggestParam, funcSuggest);
		this.save(funcSuggest);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FuncSuggestParam> funcSuggestParamList) {
		funcSuggestParamList.forEach(funcSuggestParam -> {
			FuncSuggest funcSuggest = this.queryFuncSuggest(funcSuggestParam);
			this.removeById(funcSuggest.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FuncSuggestParam funcSuggestParam) {
		FuncSuggest funcSuggest = this.queryFuncSuggest(funcSuggestParam);
		BeanUtil.copyProperties(funcSuggestParam, funcSuggest);
		this.updateById(funcSuggest);
	}

	@Override
	public FuncSuggest detail(FuncSuggestParam funcSuggestParam) {
		return this.queryFuncSuggest(funcSuggestParam);
	}

	/**
	 * @func 获取文献推荐表
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:11
	 */
	private FuncSuggest queryFuncSuggest(FuncSuggestParam funcSuggestParam) {
		FuncSuggest funcSuggest = this.getById(funcSuggestParam.getId());
		if (ObjectUtil.isNull(funcSuggest)) {
			throw new ServiceException(FuncSuggestExceptionEnum.NOT_EXIST);
		}
		return funcSuggest;
	}
	
}
