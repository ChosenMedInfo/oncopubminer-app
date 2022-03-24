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
package com.cn.xiaonuo.modular.publicationsview.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


/**
 * @func 文献视图
 *
 * @author Quan Xu
 * @date 2021-11-25 10:16:01
 */
@Data
@TableName("view_publications_view")
public class PublicationsView {


	/**
	 * @func 主键id
	 */
	private Long id;

	/**
	 * @func 用户组id
	 */
	private String groupName;

	/**
	 * @func 文库id
	 */
	private Long libraryId;

	/**
	 * @func 文献id
	 */
	private Long pmid;

	/**
	 * @func PMCID
	 */
	private String pmcid;

	/**
	 * @func DOI
	 */
	private String doi;

	/**
	 * @func 文献标题
	 */
	private String pubTitle;

	/**
	 * @func 文献作者
	 */
	private String pubAuthors;

	/**
	 * @func 期刊名称
	 */
	private String pubJournal;

	/**
	 * @func 发表年份
	 */
	private String pubYear;

	/**
	 * @func 2020年度影响因子
	 */
	private String pubIf2020;

	/**
	 * @func 文献引用方式
	 */
	private String pubCitation;

	/**
	 * @func 文献摘要
	 */
	private String pubAbstract;

	/**
	 * @func 状态（字典 0正常 1删除 2封存）
	 */
	private Integer status;
}
