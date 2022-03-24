package com.cn.xiaonuo.sys.core.log;

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
					if(content.indexOf("本地ip") > -1 || content.indexOf("1.119.131.194") > -1) {
						// 不发送邮件的情况！
						System.out.println("本地IP/局域网IP，或者求臻医学IP访问，不发送邮件！");
					}else {
						MailUtil.sendHtml("medinfoservice@chosenmedtech.com", title, content);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
}
