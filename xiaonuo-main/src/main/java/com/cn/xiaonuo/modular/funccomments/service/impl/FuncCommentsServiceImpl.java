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
package com.cn.xiaonuo.modular.funccomments.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.xiaonuo.utils.UsuallyUsedUtils;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.factory.PageFactory;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.funccomments.entity.FuncComments;
import com.cn.xiaonuo.modular.funccomments.enums.FuncCommentsExceptionEnum;
import com.cn.xiaonuo.modular.funccomments.mapper.FuncCommentsMapper;
import com.cn.xiaonuo.modular.funccomments.param.FuncCommentsParam;
import com.cn.xiaonuo.modular.funccomments.service.FuncCommentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 文献备注表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-09-18 11:42:14
 */
@Service
public class FuncCommentsServiceImpl extends ServiceImpl<FuncCommentsMapper, FuncComments> implements FuncCommentsService {

	@Override
	public PageResult<FuncComments> page(FuncCommentsParam funcCommentsParam) {
		QueryWrapper<FuncComments> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(funcCommentsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getId())) {
				queryWrapper.lambda().eq(FuncComments::getId, funcCommentsParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getStatus())) {
				queryWrapper.lambda().like(FuncComments::getStatus, funcCommentsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getCreateUserName())) {
				queryWrapper.lambda().like(FuncComments::getCreateUserName, funcCommentsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getUpdateUserName())) {
				queryWrapper.lambda().like(FuncComments::getUpdateUserName, funcCommentsParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getGroupId())) {
				queryWrapper.lambda().like(FuncComments::getGroupId, funcCommentsParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getGroupName())) {
				queryWrapper.lambda().like(FuncComments::getGroupName, funcCommentsParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getPmid())) {
				queryWrapper.lambda().like(FuncComments::getPmid, funcCommentsParam.getPmid());
			}

			// 根据备注内容 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getComments())) {
				queryWrapper.lambda().like(FuncComments::getComments, funcCommentsParam.getComments());
			}

		}
		
		// 排序！
		if(funcCommentsParam.getSortBy() != null && funcCommentsParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(funcCommentsParam.getSortBy());
			if(funcCommentsParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(FuncComments::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<FuncComments> list(FuncCommentsParam funcCommentsParam) {
		LambdaQueryWrapper<FuncComments> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(funcCommentsParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getId())) {
				queryWrapper.eq(FuncComments::getId, funcCommentsParam.getId());
			}

			// 根据状态（字典 0正常 1删除 2封存） 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getStatus())) {
				queryWrapper.like(FuncComments::getStatus, funcCommentsParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getCreateUserName())) {
				queryWrapper.like(FuncComments::getCreateUserName, funcCommentsParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getUpdateUserName())) {
				queryWrapper.like(FuncComments::getUpdateUserName, funcCommentsParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getGroupId())) {
				queryWrapper.like(FuncComments::getGroupId, funcCommentsParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getGroupName())) {
				queryWrapper.like(FuncComments::getGroupName, funcCommentsParam.getGroupName());
			}

			// 根据PMID 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getPmid())) {
				queryWrapper.like(FuncComments::getPmid, funcCommentsParam.getPmid());
			}

			// 根据备注内容 模糊查询
			if (ObjectUtil.isNotEmpty(funcCommentsParam.getComments())) {
				queryWrapper.like(FuncComments::getComments, funcCommentsParam.getComments());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(FuncComments::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FuncCommentsParam funcCommentsParam) {
		FuncComments funcComments = new FuncComments();
		BeanUtil.copyProperties(funcCommentsParam, funcComments);
		this.save(funcComments);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FuncCommentsParam> funcCommentsParamList) {
		funcCommentsParamList.forEach(funcCommentsParam -> {
			FuncComments funcComments = this.queryFuncComments(funcCommentsParam);
			this.removeById(funcComments.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FuncCommentsParam funcCommentsParam) {
		FuncComments funcComments = this.queryFuncComments(funcCommentsParam);
		BeanUtil.copyProperties(funcCommentsParam, funcComments);
		this.updateById(funcComments);
	}

	@Override
	public FuncComments detail(FuncCommentsParam funcCommentsParam) {
		return this.queryFuncComments(funcCommentsParam);
	}

	/**
	 * @func 获取文献备注表
	 *
	 * @author Quan Xu
	 * @date 2021-09-18 11:42:14
	 */
	private FuncComments queryFuncComments(FuncCommentsParam funcCommentsParam) {
		FuncComments funcComments = this.getById(funcCommentsParam.getId());
		if (ObjectUtil.isNull(funcComments)) {
			throw new ServiceException(FuncCommentsExceptionEnum.NOT_EXIST);
		}
		return funcComments;
	}
	
}
