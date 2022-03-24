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
package com.cn.xiaonuo.modular.formsitemsdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.formsitemsdata.entity.FormsItemsData;
import com.cn.xiaonuo.modular.formsitemsdata.param.FormsItemsDataParam;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @func 表单项采集数据service接口
 *
 * @author Quan Xu
 * @date 2021-09-20 23:12:02
 */
public interface FormsItemsDataService extends IService<FormsItemsData> {

	/**
	 * @func 查询表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	PageResult<FormsItemsData> page(FormsItemsDataParam formsItemsDataParam);

	/**
	 * @func 表单项采集数据列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	List<FormsItemsData> list(FormsItemsDataParam formsItemsDataParam);

	/**
	 * @func 添加表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	void add(FormsItemsDataParam formsItemsDataParam);

	/**
	 * @func 删除表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	void delete(List<FormsItemsDataParam> formsItemsDataParamList);

	/**
	 * @func 编辑表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	void edit(FormsItemsDataParam formsItemsDataParam);

	/**
	 * @func 查看表单项采集数据
	 *
	 * @author Quan Xu
	 * @date 2021-09-20 23:12:02
	 */
	FormsItemsData detail(FormsItemsDataParam formsItemsDataParam);
	
	/**
	 * @func CIViC数据更新！
	 *
	 * @author Quan Xu
	 * @date 2021-10-18 11:10:00
	 */
	Boolean updateCivicData(HttpSession session, HttpServletRequest request);
	
	/**
	 * @func 统计全部批次数量！
	 *
	 * @author Quan Xu
	 * @date 2021-11-15 23:29:00
	 */
	Integer getBatchCountAll();
	
	/**
	 * @func 统计批次数量！
	 *
	 * @author Quan Xu
	 * @date 2021-10-21 14:56:00
	 */
	Integer getBatchCount(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request);
	
	/**
	 * @func 获取批次数据，用于下载！
	 *
	 * @author Quan Xu
	 * @date 2021-10-21 16:03:00
	 */
	List<String> getBatchDataForDownload(FormsItemsDataParam formsItemsDataParam, HttpSession session, HttpServletRequest request);
	 
}
