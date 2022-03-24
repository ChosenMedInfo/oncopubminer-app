package com.cn.xiaonuo.utils;

import org.springframework.scheduling.annotation.Async;

import cn.hutool.extra.mail.MailUtil;

public class MailUtils {

	/**
	 * @func 异步发送日志提示邮件！
	 * @author Quan Xu
	 * @date 2021-10-14
	 */
	@Async
	public static void sendLogMail(String title, String content) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MailUtil.sendHtml(SettingBase.strEmailReceiver, title, content);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
}
