package com.cn.xiaonuo.core;

import java.util.List;

import com.alibaba.fastjson.JSONPath;
import com.cn.xiaonuo.core.consts.SymbolConstant;
import com.cn.xiaonuo.core.context.constant.ConstantContextHolder;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

public class Tester {

	public static void main(String[] args) {
		
//		String strTest = "1441421274224431105\t20211021-142011-1451070792100450305\tGene Entrez Name\tTP53\tcommon\t2021-10-21 14:20:11";
//		String[] strArr = strTest.split("\\\t");
//		System.out.println("strTest: “" + strTest + "”, strArr.length：“" + strArr.length + "”");
//		for(String strAAA : strArr) {
//			System.out.println("--- strAAA: “" + strAAA + "”");
//		}
		
		// 2022-03-16：测试IP地址查询！
		String resultJson = SymbolConstant.DASH;
		try {
            //获取fkcoder定位api接口
            String strApi = "https://www.fkcoder.com/ip?ip=";
            String strIp = "66.249.64.45";
            System.out.println("strApi: “" + strApi + "”..");
            if (strApi != null && !strApi.equals("")) {
                HttpRequest http = HttpUtil.createGet(strApi + strIp);
                resultJson = http.timeout(3000).execute().body().toString().replace("\r", "").replace("\n", "");
                System.out.println("resultJson1: “" + resultJson + "”..");
            }
        } catch (Exception e) {
            resultJson = SymbolConstant.DASH;
        }
		System.out.println("resultJson: “" + resultJson + "”..");
		
		
	}
	
}
