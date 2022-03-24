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
package com.cn.xiaonuo.modular.formsitems.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.formsitems.entity.FormsItems;
import com.cn.xiaonuo.modular.formsitems.param.FormsItemsParam;
import java.util.List;

/**
 * @func 表单项列表service接口
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:19
 */
public interface FormsItemsService extends IService<FormsItems> {

	/**
	 * @func 查询表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	PageResult<FormsItems> page(FormsItemsParam formsItemsParam);

	/**
	 * @func 表单项列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	List<FormsItems> list(FormsItemsParam formsItemsParam);

	/**
	 * @func 添加表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	void add(FormsItemsParam formsItemsParam);

	/**
	 * @func 删除表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	void delete(List<FormsItemsParam> formsItemsParamList);

	/**
	 * @func 编辑表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	void edit(FormsItemsParam formsItemsParam);

	/**
	 * @func 查看表单项列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 13:19:19
	 */
	FormsItems detail(FormsItemsParam formsItemsParam);
	 
}
