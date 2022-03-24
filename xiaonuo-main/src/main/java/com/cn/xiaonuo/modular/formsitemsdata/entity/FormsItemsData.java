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
package com.cn.xiaonuo.modular.formsitemsdata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.xiaonuo.core.pojo.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @func 表单项采集数据
 *
 * @author Quan Xu
 * @date 2021-09-20 23:12:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("lr_forms_items_data")
public class FormsItemsData extends BaseEntity {

	/**
	 * @func 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * @func 状态（字典 0正常 1删除）
	 */
	private Integer status;

	/**
	 * @func 创建者名称
	 */
	private String createUserName;

	/**
	 * @func 更新者名称
	 */
	private String updateUserName;

	/**
	 * @func 
	 */
	private Long groupId;

	/**
	 * @func 用户组名称
	 */
	private String groupName;

	/**
	 * @func 项目id
	 */
	private Long projectId;

	/**
	 * @func 文库id
	 */
	private Long libraryId;

	/**
	 * @func 文献id
	 */
	private Long pmid;

	/**
	 * @func PMCID（如果采集的是全文的话就有）
	 */
	private String pmcid;

	/**
	 * @func 表单id
	 */
	private Long formId;

	/**
	 * @func 表项id
	 */
	private Long itemId;
	
	/**
	 * @func 表项名称
	 */
	private String itemName;

	/**
	 * @func 表项采集值
	 */
	private String itemValue;

	/**
	 * @func 表项值类型（字典 0采集 1审核）
	 */
	private Integer itemValueType;
	
	/**
	 * @func 批次id
	 */
	private String batchId;
	
}
