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
package com.cn.xiaonuo.modular.forms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.forms.entity.Forms;
import com.cn.xiaonuo.modular.forms.param.FormsParam;
import java.util.List;

/**
 * @func 数据收集表单列表service接口
 *
 * @author Quan Xu
 * @date 2021-07-08 10:34:51
 */
public interface FormsService extends IService<Forms> {

	/**
	 * @func 查询数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	PageResult<Forms> page(FormsParam formsParam);

	/**
	 * @func 数据收集表单列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	List<Forms> list(FormsParam formsParam);

	/**
	 * @func 添加数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	void add(FormsParam formsParam);

	/**
	 * @func 删除数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	void delete(List<FormsParam> formsParamList);

	/**
	 * @func 编辑数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	void edit(FormsParam formsParam);

	/**
	 * @func 查看数据收集表单列表
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 10:34:51
	 */
	Forms detail(FormsParam formsParam);
	
	/**
	 * @func 验证同名表单是否存在！
	 *
	 * @author Quan Xu
	 * @date 2021-07-08 11:58:00
	 */
	List<Forms> checkFormNameExist(String strFormName);
	 
}
