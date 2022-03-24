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
package com.cn.xiaonuo.modular.libraries.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.modular.libraries.entity.Libraries;
import com.cn.xiaonuo.modular.libraries.param.LibrariesParam;
import java.util.List;

/**
 * @func 文库列表service接口
 *
 * @author Quan Xu
 * @date 2021-09-14 15:23:57
 */
public interface LibrariesService extends IService<Libraries> {

	/**
	 * @func 查询文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	PageResult<Libraries> page(LibrariesParam librariesParam);

	/**
	 * @func 文库列表列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	List<Libraries> list(LibrariesParam librariesParam);

	/**
	 * @func 添加文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	void add(LibrariesParam librariesParam);

	/**
	 * @func 删除文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	void delete(List<LibrariesParam> librariesParamList);

	/**
	 * @func 编辑文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	void edit(LibrariesParam librariesParam);

	/**
	 * @func 查看文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-14 15:23:57
	 */
	Libraries detail(LibrariesParam librariesParam);
	
	/**
	 * @func 通过文献id和用户组，查看不包含该文献的文库列表
	 *
	 * @author Quan Xu
	 * @date 2021-10-12 16:00:00
	 */
	List<Libraries> listByPmidAndGroupName(Long pmid, String groupName);
	 
}
