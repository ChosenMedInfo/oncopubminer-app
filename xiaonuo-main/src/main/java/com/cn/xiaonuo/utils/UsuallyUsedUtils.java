package com.cn.xiaonuo.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuallyUsedUtils {

	/**
	 * @func 获取点号连接符号的日期字符串！
	 * @author Quan Xu
	 * @date 2021-05-10
	 * 
	 * @return yyyy.MM.dd！
	 */
	public static String GetDateWithDot() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		String currentDateStr = formatter.format(date);
		return currentDateStr;
	}
	
	/**
	 * @func 获取没有任何连接符号的日期字符串！
	 * @author Quan Xu
	 * @date 2021-04-13
	 * 
	 * @return yyyyMMdd！
	 */
	public static String GetDateWithOutAnySplitter() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String currentDateStr = formatter.format(date);
		return currentDateStr;
	}
	
	/**
	 * @func 获取字符串类型的时间戳：yyyy-MM-dd HH:mm:ss！
	 * @author Quan Xu
	 * @date 2021-03-05
	 * 
	 * @return yyyy-MM-dd HH:mm:ss！
	 */
	public static String GetStandardDateTimeStr() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTimeStr = formatter.format(date);
		return currentDateTimeStr;
	}
	
	/**
	 * @func 获取字符串类型的时间戳：yyyy-MM-dd HH:mm:ss！
	 * @author Quan Xu
	 * @date 2021-03-05
	 * 
	 * @return yyyy-MM-dd HH:mm:ss！
	 */
	public static String GetStandardDateTimeStr(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTimeStr = formatter.format(date);
		return currentDateTimeStr;
	}
	
	/**
	 * @func 获取字符串类型的时间戳：yyyyMMdd-HHmmss！
	 * @author Quan Xu
	 * @date 2021-03-05
	 * 
	 * @return yyyyMMdd-HHmmss
	 */
	public static String GetDateTimeNumWithShortLineStr() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String currentDateTimeStr = formatter.format(date);
		return currentDateTimeStr;
	}
	
	/**
	 * @func 获取字符串类型的UUID！
	 * @author Quan Xu
	 * @date 2021-03-05
	 * 
	 * @return 06bd17eaf27b4586a42b71a083836d3b
	 */
	public static String GetUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * @func 路径标准化处理！
	 * @author Quan Xu
	 * @date 2021-03-22
	 * 
	 * @return
	 */
	public static String FormatPath(String strPath) {
		return strPath.replaceAll("\\\\", "/").replaceAll("//", "/");
	}
	
	/**
	 * @func 属性名转化为数据库表字段名，如“fdGeneRefgene”转化为“fd_gene_refgene”！
	 * @author Quan Xu
	 * @date 2021-03-24
	 * 
	 * @return
	 */
	public static String BeanFieldNameToMysqlFieldName(String strBeanFieldName) {
		String pattern = "\\B[A-Z]";
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(strBeanFieldName);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_"+matcher.group().toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * @desc 打印字符串数组列表内容！
	 * @author Quan Xu
	 * @date 2021-04-12
	 * 
	 * @param strArrList
	 * @return
	 */
	public static String PrintArrayListString(List<String[]> strArrList) {
		String strOut = "";
		for(String[] strArr : strArrList) {
			strOut += "[";
			for(int i = 0; i < strArr.length; i++) {
				strOut += strArr[i] + ";";
			}
			strOut += "]";
		}
		strOut = strOut.replaceAll(";]", "]");
		return strOut;
	}
	
	/**
	 * @desc 对字符串列表进行去重与排序！
	 * @author Quan Xu
	 * @date 2021-05-21
	 */
	public static List<String> SortStrList(List<String> strList) {
		List<String> strListFinal = new ArrayList<String>();
		Map<String, Boolean> mapStr = new TreeMap<String, Boolean>();
		for(String str : strList) {
			mapStr.put(str, true);
		}
		for(String str : mapStr.keySet()) {
			strListFinal.add(str);
		}
		return strListFinal;
	}
	
	/**
	 * @desc 对字符串列表进行去重与排序，并连接成字符串！
	 * @author Quan Xu
	 * @date 2021-05-21
	 */
	public static String SortStrListAndGetStr(List<String> strList, String strSplitter) {
		Map<String, Boolean> mapStr = new TreeMap<String, Boolean>();
		String strFinal = "";
		for(String str : strList) {
			mapStr.put(str, true);
		}
		for(String str : mapStr.keySet()) {
			strFinal = strFinal.equals("") ? str : strFinal + strSplitter + str;
		}
		return strFinal;
	}
	
}
