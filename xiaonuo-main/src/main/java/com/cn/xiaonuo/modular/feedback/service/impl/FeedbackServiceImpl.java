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
package com.cn.xiaonuo.modular.feedback.service.impl;

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
import com.cn.xiaonuo.modular.feedback.entity.Feedback;
import com.cn.xiaonuo.modular.feedback.enums.FeedbackExceptionEnum;
import com.cn.xiaonuo.modular.feedback.mapper.FeedbackMapper;
import com.cn.xiaonuo.modular.feedback.param.FeedbackParam;
import com.cn.xiaonuo.modular.feedback.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @func 用户反馈表service接口实现类
 *
 * @author Quan Xu
 * @date 2021-11-15 15:25:19
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

	@Override
	public PageResult<Feedback> page(FeedbackParam feedbackParam) {
		QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(feedbackParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getId())) {
				queryWrapper.lambda().eq(Feedback::getId, feedbackParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getStatus())) {
				queryWrapper.lambda().like(Feedback::getStatus, feedbackParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getCreateUserName())) {
				queryWrapper.lambda().like(Feedback::getCreateUserName, feedbackParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getUpdateUserName())) {
				queryWrapper.lambda().like(Feedback::getUpdateUserName, feedbackParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getGroupId())) {
				queryWrapper.lambda().like(Feedback::getGroupId, feedbackParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getGroupName())) {
				queryWrapper.lambda().like(Feedback::getGroupName, feedbackParam.getGroupName());
			}

			// 根据反馈者姓名 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackName())) {
				queryWrapper.lambda().like(Feedback::getFeedbackName, feedbackParam.getFeedbackName());
			}

			// 根据反馈者邮箱 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackEmail())) {
				queryWrapper.lambda().like(Feedback::getFeedbackEmail, feedbackParam.getFeedbackEmail());
			}

			// 根据反馈者主题 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackSubject())) {
				queryWrapper.lambda().like(Feedback::getFeedbackSubject, feedbackParam.getFeedbackSubject());
			}

			// 根据反馈内容 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackMessage())) {
				queryWrapper.lambda().like(Feedback::getFeedbackMessage, feedbackParam.getFeedbackMessage());
			}

			// 根据操作IP 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getOperateIp())) {
				queryWrapper.lambda().like(Feedback::getOperateIp, feedbackParam.getOperateIp());
			}

			// 根据IP地址所在位置 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getOperateAddr())) {
				queryWrapper.lambda().like(Feedback::getOperateAddr, feedbackParam.getOperateAddr());
			}

		}
		
		// 排序！
		if(feedbackParam.getSortBy() != null && feedbackParam.getOrderBy() != null) {
			String mysqlFieldName = UsuallyUsedUtils.BeanFieldNameToMysqlFieldName(feedbackParam.getSortBy());
			if(feedbackParam.getOrderBy().toLowerCase().equals("asc")) {
				queryWrapper.orderByAsc(mysqlFieldName);
			}else {
				queryWrapper.orderByDesc(mysqlFieldName);
			}
		}else{
			// 默认排序！
			queryWrapper.lambda().orderByDesc(Feedback::getCreateTime);
		}
		
		return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
	}

	@Override
	public List<Feedback> list(FeedbackParam feedbackParam) {
		LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
		if (ObjectUtil.isNotNull(feedbackParam)) {

			// 根据主键id 查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getId())) {
				queryWrapper.eq(Feedback::getId, feedbackParam.getId());
			}

			// 根据状态（字典 0正常 1删除） 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getStatus())) {
				queryWrapper.like(Feedback::getStatus, feedbackParam.getStatus());
			}

			// 根据创建者名称 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getCreateUserName())) {
				queryWrapper.like(Feedback::getCreateUserName, feedbackParam.getCreateUserName());
			}

			// 根据更新者名称 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getUpdateUserName())) {
				queryWrapper.like(Feedback::getUpdateUserName, feedbackParam.getUpdateUserName());
			}

			// 根据用户组id 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getGroupId())) {
				queryWrapper.like(Feedback::getGroupId, feedbackParam.getGroupId());
			}

			// 根据用户组名称 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getGroupName())) {
				queryWrapper.like(Feedback::getGroupName, feedbackParam.getGroupName());
			}

			// 根据反馈者姓名 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackName())) {
				queryWrapper.like(Feedback::getFeedbackName, feedbackParam.getFeedbackName());
			}

			// 根据反馈者邮箱 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackEmail())) {
				queryWrapper.like(Feedback::getFeedbackEmail, feedbackParam.getFeedbackEmail());
			}

			// 根据反馈者主题 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackSubject())) {
				queryWrapper.like(Feedback::getFeedbackSubject, feedbackParam.getFeedbackSubject());
			}

			// 根据反馈内容 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getFeedbackMessage())) {
				queryWrapper.like(Feedback::getFeedbackMessage, feedbackParam.getFeedbackMessage());
			}

			// 根据操作IP 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getOperateIp())) {
				queryWrapper.like(Feedback::getOperateIp, feedbackParam.getOperateIp());
			}

			// 根据IP地址所在位置 模糊查询
			if (ObjectUtil.isNotEmpty(feedbackParam.getOperateAddr())) {
				queryWrapper.like(Feedback::getOperateAddr, feedbackParam.getOperateAddr());
			}

		}
		
		// 默认排序！
		queryWrapper.orderByDesc(Feedback::getCreateTime);
		
		return this.list(queryWrapper);
	}

	@Override
	public void add(FeedbackParam feedbackParam) {
		Feedback feedback = new Feedback();
		BeanUtil.copyProperties(feedbackParam, feedback);
		
		// 添加用户组信息！
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		feedback.setGroupName(sysLoginUser == null ? "guestGroup" : (sysLoginUser.getAccountGroup() == null ? sysLoginUser.getName() + "Group" : sysLoginUser.getAccountGroup()));
		
		// 添加创建者！
		feedback.setCreateUserName(sysLoginUser == null ? "guest" : sysLoginUser.getName());
		
		this.save(feedback);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(List<FeedbackParam> feedbackParamList) {
		feedbackParamList.forEach(feedbackParam -> {
			Feedback feedback = this.queryFeedback(feedbackParam);
			this.removeById(feedback.getId());
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(FeedbackParam feedbackParam) {
		Feedback feedback = this.queryFeedback(feedbackParam);
		BeanUtil.copyProperties(feedbackParam, feedback);
		this.updateById(feedback);
	}

	@Override
	public Feedback detail(FeedbackParam feedbackParam) {
		return this.queryFeedback(feedbackParam);
	}

	/**
	 * @func 获取用户反馈表
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 15:25:19
	 */
	private Feedback queryFeedback(FeedbackParam feedbackParam) {
		Feedback feedback = this.getById(feedbackParam.getId());
		if (ObjectUtil.isNull(feedback)) {
			throw new ServiceException(FeedbackExceptionEnum.NOT_EXIST);
		}
		return feedback;
	}
	
}
