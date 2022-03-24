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
package com.cn.xiaonuo.modular.librariespublications.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.xiaonuo.core.annotion.Permission;
import com.cn.xiaonuo.core.context.login.LoginContextHolder;
import com.cn.xiaonuo.core.pojo.login.SysLoginUser;
import com.cn.xiaonuo.core.pojo.page.PageResult;
import com.cn.xiaonuo.core.pojo.response.ResponseData;
import com.cn.xiaonuo.core.pojo.response.SuccessResponseData;
import com.cn.xiaonuo.modular.librariespublications.entity.LibrariesPublications;
import com.cn.xiaonuo.modular.librariespublications.param.LibrariesPublicationsParam;
import com.cn.xiaonuo.modular.librariespublications.service.LibrariesPublicationsService;
import com.cn.xiaonuo.modular.others.controller.BaseController;
import com.cn.xiaonuo.modular.publications.service.PublicationsService;
import com.cn.xiaonuo.utils.SettingBase;

import cn.hutool.log.Log;

/**
 * @func 文库文献关联表控制器
 *
 * @author Quan Xu
 * @date 2021-09-17 16:38:11
 */
@Controller
public class LibrariesPublicationsController {

	private String PATH_PREFIX = "librariesPublications/";

	@Resource
	private PublicationsService publicationsService;
	
	@Resource
	private LibrariesPublicationsService librariesPublicationsService;
	
	@Resource
	private BaseController baseController;
	
	// 日志对象！
	private static final Log log = Log.get();

	/**
	 * @func 文库文献关联表页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@GetMapping("/librariesPublications/index")
	public String index() {
		return PATH_PREFIX + "index.html";
	}

	/**
	 * @func 文库文献关联表表单页面
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@GetMapping("/librariesPublications/form")
	public String form() {
		return PATH_PREFIX + "form.html";
	}

	/**
	 * @func 查询文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@GetMapping("/librariesPublications/page")
	public PageResult<LibrariesPublications> page(LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		return librariesPublicationsService.page(librariesPublicationsParam);
	}
	
	/**
	 * @func 添加文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@PostMapping("/librariesPublications/add")
	public ResponseData add(@RequestBody @Validated(LibrariesPublicationsParam.add.class) LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		librariesPublicationsService.add(librariesPublicationsParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 删除文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@PostMapping("/librariesPublications/delete")
	public ResponseData delete(@RequestBody @Validated(LibrariesPublicationsParam.delete.class) List<LibrariesPublicationsParam> librariesPublicationsParamList, HttpSession session, HttpServletRequest request) {
		librariesPublicationsService.delete(librariesPublicationsParamList);
		return new SuccessResponseData();
	}

	/**
	 * @func 编辑文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@PostMapping("/librariesPublications/edit")
	public ResponseData edit(@RequestBody @Validated(LibrariesPublicationsParam.edit.class) LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		librariesPublicationsService.edit(librariesPublicationsParam);
		return new SuccessResponseData();
	}

	/**
	 * @func 查看文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@GetMapping("/librariesPublications/detail")
	public ResponseData detail(@Validated(LibrariesPublicationsParam.detail.class) LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(librariesPublicationsService.detail(librariesPublicationsParam));
	}

	/**
	 * @func 文库文献关联表列表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@GetMapping("/librariesPublications/list")
	public ResponseData list(LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(librariesPublicationsService.list(librariesPublicationsParam));
	}
	
	/**
	 * @func 添加文库文献关联表
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:38:11
	 */
	@ResponseBody
	@PostMapping("/librariesPublications/addBatch")
	public ResponseData addBatch(@RequestBody @Validated(LibrariesPublicationsParam.add.class) List<LibrariesPublicationsParam> librariesPublicationsParamList, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strGroupName = sysLoginUser.getAccountGroup();
		for(LibrariesPublicationsParam librariesPublicationsParam : librariesPublicationsParamList) {
			String[] strArrPmids = librariesPublicationsParam.getStrPmids().replaceAll(" ", "").split(",");
			for(String strPmid : strArrPmids) {
				Long pmid = Long.parseLong(strPmid);
//				// 先判断文献是否在文献表中存在：不同的情况有不同的处理办法！
//				QueryWrapper<Publications> qwp = new QueryWrapper<Publications>();
//				qwp.lambda().eq(Publications::getPmid, librariesPublicationsParam.getPmid());
//				qwp.lambda().eq(Publications::getGroupName, strGroupName); // 当前组的文献！
//				List<Publications> pList = publicationsService.list(qwp);
//				Publications p = pList.size() == 1 ? pList.get(0) : new Publications();
//				BeanUtil.copyProperties(librariesPublicationsParam, p);
//				p.setCreateUserName(sysLoginUser.getName());
//				p.setGroupName(strGroupName); // 设置为当前组！
//				if(pList.size() != 1) {
//					// 先清除已有的文献信息！
//					for(Publications pub : pList) {
//						publicationsService.removeById(pub.getId()); // 对于本组该文献存在多条的情况，删除所有的文献！
//					}
//					// 然后添加当前文献！
//					publicationsService.save(p);
//				}else {
//					// 只有一条文献的情况下，更新之！
//					p.setId(pList.get(0).getId());
//					publicationsService.updateById(p);
//				}
				// 添加关联！
				librariesPublicationsParam.setPmid(pmid);
				librariesPublicationsParam.setGroupName(strGroupName);
				librariesPublicationsService.add(librariesPublicationsParam);
			}
			
		}
		updatePublicationDetail(); // 异步更新文献信息！
		return new SuccessResponseData();
	}
	
	/**
	 * @func 统计特定文献对应的各类文库数量
	 *
	 * @author Quan Xu
	 * @date 2021-09-17 16:58:00
	 */
	@ResponseBody
	@PostMapping("/librariesPublications/statCollectionsFromPub")
	public ResponseData statCollectionsFromPub(@RequestBody LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();
		String strGroupName = sysLoginUser.getAccountGroup();
		List<LibrariesPublications> lpList = librariesPublicationsService.list(librariesPublicationsParam);
		librariesPublicationsParam.setGroupName(strGroupName);
		List<LibrariesPublications> lpListCurrent = librariesPublicationsService.list(librariesPublicationsParam);
		librariesPublicationsParam.setGroupName(SettingBase.strGroupCommon);
		List<LibrariesPublications> lpListCommon = librariesPublicationsService.list(librariesPublicationsParam);
		int intCommon = lpListCommon.size(), intCurrent = lpListCurrent.size(), intTotal = lpList.size();
		return new SuccessResponseData(intCurrent + "," + intCommon + "," + intTotal);
	}
	
	/**
	 * @func 更新文献详情
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 14:40:00
	 */
	@ResponseBody
	@GetMapping("/librariesPublications/updatePublicationDetail")
	public ResponseData updatePublicationDetail(LibrariesPublicationsParam librariesPublicationsParam, HttpSession session, HttpServletRequest request) {
		updatePublicationDetail(); // 异步更新文献信息！
		return new SuccessResponseData();
	}
	
	/**
	 * @func 更新文献详情
	 *
	 * @author Quan Xu
	 * @date 2021-11-19 14:40:00
	 */
	public void updatePublicationDetail() {
		log.info("updatePublicationDetail()...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					log.info("updatePublicationDetail() 内部运行中...");
					// 遍历全部标题文献！
					LambdaQueryWrapper<LibrariesPublications> qw = new LambdaQueryWrapper<LibrariesPublications>();
					qw.gt(LibrariesPublications::getId, 0);
					qw.orderByDesc(LibrariesPublications::getId); // 2021-12-09（V0013-01.00.10-09）：按照id倒序排列，确保最新添加的文献最先处理！
					List<LibrariesPublications> lpList = librariesPublicationsService.list(qw);
					int intSuccess = 0, intTotal = 0;
					for(LibrariesPublications lp : lpList) {
						if(
							lp.getPubTitle() == null || lp.getPubTitle().trim().equals("")
							|| lp.getPubAuthors() == null || lp.getPubTitle().trim().equals("")
							|| lp.getPubJournal() == null || lp.getPubJournal().trim().equals("")
							|| lp.getPubYear() == null || lp.getPubYear().trim().equals("")
						) { // 2021-12-03（V0011-01.00.08-11）：还得判断处标题外的其他字段是否为空！
							intTotal++;
							String strCmdUrl = baseController.OncoPubMinerApiUrl + baseController.OncoPubMinerApiParamId + lp.getPmid();
							log.info("[URL] " + strCmdUrl);
							RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<String> responseEntity = restTemplate.getForEntity(strCmdUrl, String.class);
							//log.info("[Return] " + responseEntity.getBody());
							JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
							JSONObject jsonObjectData = jsonObject.getJSONObject("data");
							JSONArray jsonArrPassages = jsonObjectData.getJSONArray("passages");
							if(jsonArrPassages != null && jsonArrPassages.size() > 0) {
								JSONObject jsonObjectDetail = jsonArrPassages.getJSONObject(0);
								JSONObject jsonObjectInfons = jsonObjectDetail.getJSONObject("infons");
								JSONArray jsonArrAuthors = jsonObjectInfons.getJSONArray("authors");
								String strAuthors = "";
								for(int i = 0; i < jsonArrAuthors.size(); i++) {
									String strThisAuthor = (String)jsonArrAuthors.get(i);
									strAuthors = strAuthors.equals("") ? strThisAuthor : strAuthors + "; " + strThisAuthor;
								}
								
								log.info("[TITLE] " + jsonObjectDetail.getString("text"));
								lp.setPmcid(jsonObjectInfons.getString("article_id_pmc"));
								lp.setDoi(jsonObjectInfons.getString("article_id_doi"));
								lp.setPubTitle(jsonObjectDetail.getString("text"));
								lp.setPubAuthors(strAuthors);
								lp.setPubJournal(jsonObjectInfons.getString("journal_name"));
								lp.setPubYear(jsonObjectInfons.getString("year"));
								lp.setPubIf2020(jsonObjectInfons.getFloat("if2020"));
								lp.setPubCitation(jsonObjectInfons.getString("journal"));
								//lp.setPubAbstract(pubAbstract);
								librariesPublicationsService.updateById(lp); // 执行更新！
								intSuccess++;
								
							}else {
								log.info("[文献信息获取失败] passages字段信息为空！！！");
							}
							
						}
					}
					log.info("总共待更新：“" + intTotal + "”，成功更新：“" + intSuccess + "”...");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	/**
	 * @func 去除文库文献关联表中重复的关系对
	 *
	 * @author Quan Xu
	 * @date 2021-11-25 13:28:00
	 */
	@Permission // 要求只有管理员才能执行！
	@ResponseBody
	@GetMapping("/librariesPublications/dedup")
	public ResponseData dedup(HttpSession session, HttpServletRequest request) {
		return new SuccessResponseData(librariesPublicationsService.dedup(session, request));
	}

}
