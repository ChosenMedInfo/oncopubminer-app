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
package com.cn.xiaonuo.sys.modular.timer.tasks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cn.xiaonuo.core.timer.TimerTaskRunner;

import cn.hutool.log.Log;

/**
 * @func 定时更新LIMS样本信息表
 *
 * @author quanxu
 * @date 2021-06-02
 */
@Component
public class UpdateCivicDataRunner implements TimerTaskRunner {

	private static final Log log = Log.get();
	
	@Value("${server.port}")
	public String serverPort;
	
	@Override
	public void action() {
		String strCmdUrl = "http://localhost:" + serverPort + "/formsItemsData/updateCivicData";
		log.info("[UpdateCivicDataRunner] 定时器执行：更新CIViC数据：调用接口“" + strCmdUrl + "”实现：");
		log.info("[UpdateCivicDataRunner] 20220208，该定时器暂时取消，请在需要的时候手动更新：“/formsItemsData/updateCivicData”..");
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> responseEntity = restTemplate.getForEntity(strCmdUrl, String.class);
//		log.info("[UpdateCivicDataRunner] " + responseEntity.getBody());
	}

}
