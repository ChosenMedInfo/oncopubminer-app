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
package com.cn.xiaonuo.modular.formsitems.param;

import java.util.List;

import com.cn.xiaonuo.core.pojo.base.param.BaseParam;
import com.cn.xiaonuo.modular.formsitemsoptions.entity.FormsItemsOptions;

import lombok.Data;


/**
 * @func 表单项列表参数类
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:19
*/
@Data
public class FormsItemsParam extends BaseParam {

	/**
	 * @func 主键id
	 */
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
	 * @func 表单id
	 */
	private Long formId;

	/**
	 * @func 表项名称
	 */
	private String itemName;
	
	/**
	 * @func 字段名称
	 */
	private String itemFieldName;

	/**
	 * @func 表项类型（字典 0text 1radio ...）
	 */
	private Integer itemType;

	/**
	 * @func 表项默认值
	 */
	private String itemDefault;

	/**
	 * @func 最大长度（0表示不限制）
	 */
	private Integer itemMaxLength;
	
	/**
	 * @func 最多个数（0表示不限制）
	 */
	private Integer itemMaxCount;

	/**
	 * @func 是否必填（字典 0否 1是）
	 */
	private Integer itemRequired;

	/**
	 * @func 提示信息
	 */
	private String itemTips;

	/**
	 * @func 排序序号
	 */
	private Integer itemSort;

	/**
	 * @func 选项类型（字典 0无 1自定义 2癌种 3药物 4基因 5变异 ...）
	 */
	private Integer itemOptionType;
	
	// ============================================================== //
	
	/**
	 * @func 查询item的时候，同步带上item的options列表：对象列表！
	 */
	private List<FormsItemsOptions> formsItemOptionsList;
	
	/**
	 * @func 添加/修改item的时候，同步带上item的options列表：字符串列表！
	 */
	private List<String> itemOptionList;
	
}
