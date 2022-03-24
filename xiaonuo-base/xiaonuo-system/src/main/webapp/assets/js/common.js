/** EasyWeb iframe v3.1.8 date:2020-05-04 License By http://xiaonuo.vip */
var sysLoginUser;
//var strApiUrlRoot = "http://192.168.2.2:9001/"; // LYY本地！
//var strApiUrlRoot = "http://1.119.131.198:9001/"; // GPU服务器映射地址！
var strApiUrlRoot = "https://oncopubapi.chosenmedinfo.com/"; // 域名版！
//var strApiUrlRoot = "https://oncopubapibeta.chosenmedinfo.com/"; // 域名测试版！
var strApiKwSearching = strApiUrlRoot + "search?q="; // 关键字检索！
var strApiKwQuery     = strApiUrlRoot + "keyword?q="; // 关键字搜索！
var strApiCaSearching = strApiUrlRoot + "cancer?q="; // 癌症标准词搜索！
var strApiGeSearching = strApiUrlRoot + "gene?q="; // 基因标准词搜索！
var strApiDrSearching = strApiUrlRoot + "chemical?q="; // 化合物标准词搜索！
var strApiIdSearching = strApiUrlRoot + "id?q="; // id搜索：PMID和PMCID都可以！
var strApiCbSearching = strApiUrlRoot + "cited_by?q="; // Cited By：PMID和PMCID都可以！
var strApiSiSearching = strApiUrlRoot + "similar?q="; // Similar：PMID和PMCID都可以！
var strApiReSearching = strApiUrlRoot + "ref?q="; // Similar：PMID和PMCID都可以！
var strApiStatNumber  = strApiUrlRoot + "stat"; // 统计文献及实体数量！
var strApiPubUpdates  = strApiUrlRoot + "pub_updates"; // 2021-12-29（V0022-01.01.06-10）：文献更新信息！
var strApiDocument    = strApiUrlRoot + "api/doc?docExpansion=list"; // 2021-12-29（V0022-01.01.06-11）：API文档地址！

// 2021-12-29（V0022-01.01.06-11）：导航条新增API链接！
var objApiNav = document.getElementById("idNavApiDoc");
if(objApiNav) objApiNav.innerHTML = '<a href="' + strApiDocument + '" target="_blank">API</a>';

layui.config({ // common.js是配置layui扩展模块的目录，每个页面都需要引入
	version: '318',   // 更新组件缓存，设为true不缓存，也可以设一个固定值
	base: getProjectUrl() + 'assets/module/',
	baseServer: getProjectUrl(),
	// 请求完成后预处理
	ajaxSuccessBefore: function (res, url, obj) {
		if(obj.param.dataType === "html") {
			return true;
		} else {
			return handleNetworkError(res);
		}
	}
}).extend({
	steps: 'steps/steps',
	notice: 'notice/notice',
	cascader: 'cascader/cascader',
	dropdown: 'dropdown/dropdown',
	fileChoose: 'fileChoose/fileChoose',
	Split: 'Split/Split',
	Cropper: 'Cropper/Cropper',
	tagsInput: 'tagsInput/tagsInput',
	citypicker: 'city-picker/city-picker',
	introJs: 'introJs/introJs',
	zTree: 'zTree/zTree',
	iconPicker: 'iconPicker/iconPicker',
	xnUtil: 'xnUtil/xnUtil'
}).use(['layer', 'admin', 'table', 'xnUtil'], function () {
	var $ = layui.jquery;
	var admin = layui.admin;
	var xnUtil = layui.xnUtil;
	var table = layui.table;
	
	// quanxu, 2021-01-18: 设置默认主题！
	var defaultTheme = admin.getTempData("defaultTheme", true);
	if(defaultTheme === undefined){
		admin.changeTheme('theme-white');
	}
	
	// quanxu, 2021-06-21：获取用户！
	sysLoginUser = layui.data(admin.setter.tableName).loginUser;
	
	//表格重载时ajaxSuccessBefore无法捕获ajax结果，使用此处判断
	$.ajaxSetup({
		timeout : 1000000, //超时时间设置，单位毫秒，默认1000秒
		complete: function (XMLHttpRequest, textStatus) {
			if(XMLHttpRequest.responseJSON !== null && XMLHttpRequest.responseJSON !== undefined) {
				if(!XMLHttpRequest.responseJSON.success) {
					// 登录已过期，请重新登录
					if(XMLHttpRequest.responseJSON.code === 1011008) {
						window.location.href = "/";
					}
				}
			}
		}
	});
	
	// 页面载入就检查按钮权限
	xnUtil.renderPerm();
	
});

/** 获取当前项目的根路径，通过获取layui.js全路径截取assets之前的地址 */
function getProjectUrl() {
	var layuiDir = layui.cache.dir;
	if (!layuiDir) {
		var js = document.scripts, last = js.length - 1, src;
		for (var i = last; i > 0; i--) {
			if (js[i].readyState === 'interactive') {
				src = js[i].src;
				break;
			}
		}
		var jsPath = src || js[last].src;
		layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
	}
	return layuiDir.substring(0, layuiDir.indexOf('assets'));
}

function supportPreview(suffix) {
	var result = [];
	result.push('pdf');
	result.push('doc');
	result.push('docx');
	result.push('xls');
	result.push('xlsx');
	result.push('ppt');
	result.push('pptx');
	result.push('jpg');
	result.push('png');
	result.push('jpeg');
	result.push('tif');
	result.push('bmp');
	result.push('gif');
	result.push('txt');
	return result.indexOf(suffix) !== -1;
}

// 网络错误处理
function handleNetworkError(res) {
	//关闭加载层
	layui.layer.closeAll('loading');
	if(res.code !== 0) {
		if(res.success !== null && res.success !== undefined) {
			if(!res.success) {
				// 登录已过期，请重新登录
				if(res.code === 1011008 || res.code === 1011004) {
					window.location.href = "/";
				} else {
					if(res.message) {
						layui.notice.msg(res.message, {icon: 2});
					} else {
						layui.notice.msg("服务器出现异常，请联系管理员", {icon: 2});
					}
					return false;
				}
			}
		} else {
			if(res.code === 500) {
				if(res.msg === "error") {
					layui.notice.msg("服务器出现异常，请联系管理员", {icon: 2});
					return false;
				}
			}

			if(res.code === 404) {
				if(res.msg === "error") {
					layui.notice.msg("资源路径不存在，请检查请求地址", {icon: 2});
					return false;
				}
			}
		}
	} else {
		//网络错误
		if(res.msg === "timeout") {
			layui.notice.msg("请求超时，请检查网络状态", {icon: 2});
			return false;
		}
		if(res.msg === "error") {
			layui.notice.msg("网络错误，请检查网络连接", {icon: 2});
			return false;
		}
	}
	return true;
}

// 2021-03-18：获取当前登录用户信息，用于在前端进行部分验证！
function getCurrentLoginUser(){
	var thisUser = null, $ = layui.jquery;
	$.ajax({
		type: "get",
		url: "/sysUser/currentLoginUserInfo",
		async: false,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (res) {
			if(res != null && res.account != null){
				thisUser = res;
				localStorage.setItem('localStorageUserAccount', thisUser.account); // 2022-03-17（V0029-01.02.06-08）：更新当前登录用户信息！
			}else{
				// 2022-03-17（V0029-01.02.06-08）：未登录状态下，尝试从localstorage中获取用户信息！
				let localStorageUserAccount = localStorage.getItem('localStorageUserAccount');
				if(localStorageUserAccount != null){
					$.ajax({
						type: "get",
						url: "/opmUser",
						data: {strAccount: localStorageUserAccount},
						async: false,
						contentType: "application/json; charset=utf-8",
						dataType: "json",
						success: function (res) {
							if(res != null && res.account != null){
								thisUser = res;
								localStorage.setItem('localStorageUserAccount', thisUser.account); // 2022-03-17（V0029-01.02.06-08）：更新当前登录用户信息！
							}
						}
					});
				}
			}
		},
		error: function () {
			// 2022-03-17（V0029-01.02.06-08）：未登录状态下，尝试从localstorage中获取用户信息！
			let localStorageUserAccount = localStorage.getItem('localStorageUserAccount');
			if(localStorageUserAccount != null){
				$.ajax({
					type: "get",
					url: "/opmUser",
					data: {strAccount: localStorageUserAccount},
					async: false,
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					success: function (res) {
						if(res != null && res.account != null){
							thisUser = res;
							localStorage.setItem('localStorageUserAccount', thisUser.account); // 2022-03-17（V0029-01.02.06-08）：更新当前登录用户信息！
						}
					}
				});
			}
		}
	});
	
	// 2022-03-17（V0029-01.02.06-08）：根据是否成功登录，进行相应操作！
	if(thisUser == null){
		// 未登录，删除未授权链接！
		$("#idNavLibrary").remove();
		$("#idNavMembers").remove();
		$("#idNavForms").remove();
		$("#idNavProjects").remove();
	}else{
		// 登录相关信息！
		$("#idNavUser").addClass("drop-down").html(
			'<a href="javascript:;" class="classAccountUrl" title="Copy account URL to login elsewhere!">' + thisUser.account + '</a>' +
			'<ul>' +
			'	<li><a href="javascript:;" class="classAccountUrl" title="Copy account URL to login elsewhere!"><i class="bx bx-copy"></i> AccountUrl</a></li>' +
			'	<li><a href="javascript:;" id="idModify"><i class="bx bx-edit-alt"></i> Modify</a></li>' +
			'	<li><a href="javascript:;" id="idLogout"><i class="bx bx-log-out"></i> Sign Out</a></li>' +
			'</ul>'
		);
		// 拷贝账号登录链接！
		$(".classAccountUrl").click(function(){
			copyText(this, window.location.host + "/opmUserLogin?strAccount=" + thisUser.account)
		});
		// 监听修改链接点击事件！
		$("#idModify").click(function(){
			var layIndex = admin.open({
				title: '<i class="bx bx-edit-alt"></i> Modify',
				url: '/other/updatePasswordHtml',
				area: '800px',
				offset: '100px', // 距离顶部位置！
				data: { data: thisUser }, // 传递数据到表单页面
				end: function () {
					var layerData = admin.getLayerData(layIndex, 'formOk');
					if (layerData) { // 判断表单操作成功标识
						location.reload();
					}
				},
				success: function (layero, dIndex) {
					// 弹窗超出范围不出现滚动条
					//$(layero).children('.layui-layer-content').css('overflow', 'visible');
					//$(layero).find('[lay-submit]').focus();
				}
			});
		});
		// 监听登出链接点击事件！
		$("#idLogout").click(function(){
			localStorage.removeItem('localStorageUserAccount'); // 退出登录的情况下，需要清除本地缓存的用户信息！
			window.location.href = "/logout"; // 执行登出！
		});
		// 根据不同的角色，显示不同的按钮！
		$("#idNavProjects").show();
		if(thisUser.accountType == 1){ // 管理账号！
			// 显示权限相关的链接！
			$("#idNavLibrary").show();
			$("#idNavMembers").show();
			$("#idNavForms").show();
		}else if(thisUser.accountType == 2){ // 阅读账号！
			// 非组管理员账号，删除未授权链接！
			$("#idNavLibrary").remove();
			$("#idNavMembers").remove();
			$("#idNavForms").remove();
		}
	}
	
	// Mobile Navigation！
	if ($('.nav-menu').length) {
		var $mobile_nav = $('.nav-menu').clone().prop({
			class: 'mobile-nav d-lg-none'
		});
		$('body').append($mobile_nav);
		$('body').prepend('<button type="button" class="mobile-nav-toggle d-lg-none"><i class="icofont-navigation-menu"></i></button>');
		$('body').append('<div class="mobile-nav-overly"></div>');

		$(document).on('click', '.mobile-nav-toggle', function(e) {
			$('body').toggleClass('mobile-nav-active');
			$('.mobile-nav-toggle i').toggleClass('icofont-navigation-menu icofont-close');
			$('.mobile-nav-overly').toggle();
		});

		$(document).on('click', '.mobile-nav .drop-down > a', function(e) {
			e.preventDefault();
			$(this).next().slideToggle(300);
			$(this).parent().toggleClass('active');
		});

		$(document).click(function(e) {
			var container = $(".mobile-nav, .mobile-nav-toggle");
			if (!container.is(e.target) && container.has(e.target).length === 0) {
				if ($('body').hasClass('mobile-nav-active')) {
					$('body').removeClass('mobile-nav-active');
					$('.mobile-nav-toggle i').toggleClass('icofont-navigation-menu icofont-close');
					$('.mobile-nav-overly').fadeOut();
				}
			}
		});
	} else if ($(".mobile-nav, .mobile-nav-toggle").length) {
		$(".mobile-nav, .mobile-nav-toggle").hide();
	}
	
	// 返回！
	return thisUser;
}

/* 验证报告相关日期字符串格式 */
function funcCheckValidReportDateFormatStr(strDate){
	var regex = /^\d{4}\.\d{2}\.\d{2}$/i;
	if(strDate == '-' || regex.test(strDate)){
		return true;
	}else{
		return false;
	}
}

/* 生成当前年月日标准表达式 */
function funcGetCurrentDate(){
	var date = new Date();
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0' + (date.getMonth()+1) : date.getMonth() + 1) + '-';
	D = (date.getDate()+1 < 10 ? '0' + date.getDate() : date.getDate());
	return Y + M + D;
}

/* 生成当前年月日时分秒标准表达式 */
function funcGetCurrentDateTime(){
	var date = new Date();
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0' + (date.getMonth()+1) : date.getMonth() + 1) + '-';
	D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + " ";
	h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
	m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
	s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
	return Y + M + D + h + m + s;
}

/* xmSelect渲染对象默认设置 */
function funcGenerateXmSelectRenderObj(objEl, objName, objVerify, objMax, itemOptionType){
	var thisObj = {
		el: "#" + objEl, // 待替换更新！
		
		//name:  currentFormId + '-' + id, // 待替换更新！
		name:  objName, // 待替换更新！
		
		//layVerify: itemRequired === 1 ? 'required' : '', // 待替换更新！
		layVerify: objVerify, // 待替换更新！
		
		//max: itemMaxLength, // 待替换更新！
		//max: objMax, // 待替换更新！
		max: objMax != null && objMax != "" && objMax != 0 ? objMax : 10000, // 2022-01-13（V0025-01.02.02-05）：考虑为空或者为0的情况！
		
		direction: 'down', // 2021-12-13：解决底部空间不足时，高度无法展开，导致选项背景透明的问题！
		language: 'en',
		filterable: true,
		tips: 'please select..',
		searchTips: 'Enter key words',
		delay: 500, // 2021-11-30（V0008-01.00.05-12）：延迟500ms！
		paging: true,
		pageSize: 10,
		pageEmptyShow: false,
		autoRow: true,
		toolbar: {
			show: false,
			list: ['ALL', 'REVERSE', 'CLEAR']
		},
		theme: {
			color: '#5FB878'
		},
		data: [],
		maxMethod(){
			layui.layer.tips('Select up to ' + objMax + ' items!', $('#' + objEl), {
				tips: [4, "#ff691b"],
				time: 2000,
			});
		},
		
		// 2021-10-20：加入远程搜索功能！
		remoteSearch: true,
		remoteMethod: function(val, cb, show){
			if(!val) return cb([]);
			//if(val.length < 3) return cb([]); // 至少输入3个字符！
			if(val.length < 3 && itemOptionType < 6) return cb([]); // 2021-11-30（V0008-01.00.05-11）：人员搜索不限定最少3个字符，其他的都限定！
			// 根据选项类别检索不同的接口！
			if(itemOptionType == 2){
				cb(funcGetCancerList(val));
			}else if(itemOptionType == 3){
				cb(funcGetDrugList(val));
			}else if(itemOptionType == 4){
				cb(funcGetGeneList(val));
			}else if(itemOptionType == 5){
				cb(funcGetAlterationList(val));
			}else if(itemOptionType == 6){ // 2021-11-30（V0008-01.00.05-11）：解决人员搜索不成功的问题！
				cb(funcGetMemberList(val));
			}else if(itemOptionType == 7){ // 2021-12-03（V0011-01.00.08-09）：数据采集表单检索！
				cb(funcGetFormList(val));
			}else{
				cb([]); // 其他，返回空！
			}
		},
	};
	
	return thisObj;
}

/* 获取癌种列表 */
function funcGetCancerList(kw){
	var objResult = [], $ = layui.jquery;
	kw = kw ? kw : "XXXXXXXXXXXX";
	$.ajax({
		type:'get',
		url: strApiCaSearching + kw,
		//url: '/assets/module/hjf-ptc/data/cancerList.json', // 测试！
		dataType: 'json',
	    contentType: "application/json; charset=utf-8",
		async: false,
		success: function (res) {
			for(var i = 0; i < res.data.length; i++){
				objResult.push({name: res.data[i], value: res.data[i]});
			}
			//objResult = res; // 测试！
		}
	});
	return objResult;
}

/* 获取药物列表 */
function funcGetDrugList(kw){
	var objResult = [], $ = layui.jquery;
	kw = kw ? kw : "XXXXXXXXXXXX";
	$.ajax({
		type:'get',
		url: strApiDrSearching + kw,
		//url: '/assets/module/hjf-ptc/data/drugList.json', // 测试！
		dataType: 'json',
	    contentType: "application/json; charset=utf-8",
		async: false,
		success: function (res) {
			for(var i = 0; i < res.data.length; i++){
				objResult.push({name: res.data[i], value: res.data[i]});
			}
			//objResult = res; // 测试！
		}
	});
	return objResult;
}

/* 获取基因列表 */
function funcGetGeneList(kw){
	var objResult = [], $ = layui.jquery;
	kw = kw ? kw : "XXXXXXXXXXXX";
	$.ajax({
		type:'get',
		url: strApiGeSearching + kw,
		//url: '/assets/module/hjf-ptc/data/geneList.json', // 测试！
		dataType: 'json',
	    contentType: "application/json; charset=utf-8",
		async: false,
		success: function (res) {
			for(var i = 0; i < res.data.length; i++){
				objResult.push({name: res.data[i], value: res.data[i]});
			}
			//objResult = res; // 测试！
		}
	});
	return objResult;
}

/* 获取变异列表 */
function funcGetAlterationList(kw){
	var objResult = [], $ = layui.jquery;
	kw = kw ? kw : "XXXXXXXXXXXX";
	$.ajax({
		type:'get',
		url: strApiGeSearching + kw,
		//url: '/assets/module/hjf-ptc/data/alterationList.json', // 测试！
		dataType: 'json',
	    contentType: "application/json; charset=utf-8",
		async: false,
		success: function (res) {
			for(var i = 0; i < res.data.length; i++){
				objResult.push({name: res.data[i], value: res.data[i]});
			}
			//objResult = res; // 测试！
		}
	});
	return objResult;
}

/* 获取人员列表 */
function funcGetMemberList(kw){
	var objResult = [], $ = layui.jquery;
	kw = kw ? kw : "XXXXXXXXXXXX";
	$.ajax({
		type:'get',
		url: '/sysUser/listMember?searchValue=' + kw,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		async: false,
		success: function (res) {
			for(var i = 0; i < res.data.length; i++){
				objResult.push({name: res.data[i].name, value: res.data[i].id});
			}
		}
	});
	return objResult;
}

/* 获取数据采集表单 */
function funcGetFormList(kw){
	var objResult = [], $ = layui.jquery;
	kw = kw ? kw : "";
	$.ajax({
		type:'get',
		url: '/forms/list?formName=' + kw,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		async: false,
		success: function (res) {
			for(var i = 0; i < res.data.length; i++){
				objResult.push({name: res.data[i].formName, value: res.data[i].id});
			}
		}
	});
	return objResult;
}

/* 根据 itemOptionType 获取 xmData */
function funcFromItemOptionType2XmData(intItemOptionType, kw){
	var xmData = [];
	if(intItemOptionType == 2){ // Cancer List!
    	var objArr = funcGetCancerList(kw);
    	if(objArr && objArr.length > 0){
    		for(var i = 0; i < objArr.length; i++){
    			xmData.push({name: objArr[i].name, value: objArr[i].value});
    		}
    	}
    }else if(intItemOptionType == 3){ // Drug List!
    	var objArr = funcGetDrugList(kw);
    	if(objArr && objArr.length > 0){
    		for(var i = 0; i < objArr.length; i++){
    			xmData.push({name: objArr[i].name, value: objArr[i].value});
    		}
    	}
    }else if(intItemOptionType == 4){ // Gene List!
    	var objArr = funcGetGeneList(kw);
    	if(objArr && objArr.length > 0){
    		for(var i = 0; i < objArr.length; i++){
    			xmData.push({name: objArr[i].name, value: objArr[i].value});
    		}
    	}
    }else if(intItemOptionType == 5){ // Alteration List!
    	var objArr = funcGetAlterationList(kw);
    	if(objArr && objArr.length > 0){
    		for(var i = 0; i < objArr.length; i++){
    			xmData.push({name: objArr[i].name, value: objArr[i].value});
    		}
    	}
    }
	return xmData;
}

/* 获取默认值之间的分隔符 */
function funcGetSplitter4DefaultValues(){
	return "<<<SPLITTER4DEFAULTVALS>>>";
}

/* 生成导航条内容 */
function funcGenerateNavHtml(thisNav){
	var thisLoginUser = getCuttentLoginUser();
	// 定义变量！
	var thisHtml = "";
	// 首页！
	if(thisNav == 'lrIndex'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a href="lrIndex">Home</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a href="lrIndex">Home</a>' +
		'			</li>';
	}
	// 检索页！
	if(thisNav == 'lrSearch'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a href="lrSearch">Search</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a href="lrSearch">Search</a>' +
		'			</li>';
	}
	// 文库页！
	if(thisNav == 'lrLibrary'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a ew-href="lrLibrary">Library</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a ew-href="lrLibrary">Library</a>' +
		'			</li>';
	}
	// 成员页！
	if(thisNav == 'lrMembers'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a ew-href="lrMembers">Members</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a ew-href="lrMembers">Members</a>' +
		'			</li>';
	}
	// 表单页！
	if(thisNav == 'lrForms'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a ew-href="lrForms">Forms</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a ew-href="lrForms">Forms</a>' +
		'			</li>';
	}
	// 项目页！
	if(thisNav == 'lrProjects'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a ew-href="lrProjects">Projects</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a ew-href="lrProjects">Projects</a>' +
		'			</li>';
	}
	// 帮助页！
	if(thisNav == 'lrHelp'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a ew-href="lrHelp">Tutorial</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a ew-href="lrHelp">Tutorial</a>' +
		'			</li>';
	}
	// 关于页！
	if(thisNav == 'lrAbout'){
		thisHtml +=
		'			<li class="layui-nav-item layui-this">' +
		'				<a ew-href="lrAbout">About</a>' +
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item">' +
		'				<a ew-href="lrAbout">About</a>' +
		'			</li>';
	}
	// 用户！
	if(thisLoginUser){
		thisHtml +=
		'			<li class="layui-nav-item" lay-unselect>' +
		'				<a>' + thisLoginUser.name + ' &nbsp;</a>' + 
		'				<dl class="layui-nav-child">' + 
		'					<dd lay-unselect><a ew-event="psw" data-url="/other/updatePasswordHtml" style="text-align: left;"><i class="layui-icon">&#xe673;</i>&nbsp;&nbsp; Modify</a></dd>' + 
		'					<hr>' + 
		'					<dd lay-unselect><a ew-event="logout" data-url="/logout" style="text-align: left;"><i class="layui-icon">&#xe682;</i>&nbsp;&nbsp; Sign Out</a></dd>' + 
		'				</dl>'
		'			</li>';
	}else{
		thisHtml +=
		'			<li class="layui-nav-item" lay-unselect>' +
		'				<a ew-href="login">Sign In &nbsp;</a>' +
		'			</li>';
	}
	// 返回！
	return thisHtml;
}

/* 数组元素去重后排序 */
Array.prototype.clearRepeat = function () {
	var res = [];
    var json = {};
    for (var i = 0; i < this.length; i++) {
    	if (!json[this[i]]) {
            res.push(this[i]);
            json[this[i]] = 1;
        }
    }
    return res.sort();
};

/* ### Annotation - Start ########################################### */

// 全局变量！
let hits = 0;
let groups = ['type', 'section', 'none']; // 分组方式！
let groupDescription = { // 各分组方式对应的简要提示信息！
	'type': 'Concept type',
	'section': 'Article section',
	'none': 'No group'
};
let sorts = [{ // 排序方式！
	field: 'count',
	order: 'desc',
	niceField: 'freq'
}, {
	field: 'offset',
	order: 'asc'
}];
let sectionsShowFull = {};

//=== 注释相关 - START ===

// 判断段落是否需要进行处理：判断结果用于过滤！
let ignorePassages = { 'ref': 1 };
let ignoreTitles = { 'references': 1 }; // IE11 has borderline support for Set
function shouldProcessPassage(passage) {
	let passageType = passage && passage.infons && passage.infons.type;
	let passageSection = _.toLower(passage && passage.infons && passage.infons.section);
	if (passageType in ignorePassages) return false; // 如果是在需要过滤的段落类型中，过滤掉（这里设置为过滤掉参考文献段落）！
	return !(passageType === 'title' && passageSection in ignoreTitles); // 如果段落type为title，且段落section属于待过滤的标题，过滤掉（这里设置为过滤掉参考文献段落）！
}

// 生成标题的html代码（search和detail页面都适用）！
function getTitleHtml(passage, bolNotAddStar, strPmid, strPmcid) {
	passage = passage || {};
	//let annotations = getAnnotations(passage);
	let annotations = getAnnotations(passage, strPmid, strPmcid);
	//console.log(annotations);
	let node = $('<div></div>');
	//console.log(passage.text);
	node.text(passage.text);
	annotateNode(node, annotations, bolNotAddStar);
	//console.log(node.html());
	return node.html();
}

// 生成普通文本的html代码（适用于search页面）！
function getTextHtml(passage, bolNotAddStar, strPmid, strPmcid) {
	passage = passage || {};
	//let annotations = getAnnotations(passage);
	let annotations = getAnnotations(passage, strPmid, strPmcid);
	let node = $('<span></span>');
	//console.log(passage.text);
	node.text(passage.text);
	//console.log(annotations);
	annotateNode(node, annotations, bolNotAddStar);
	//console.log(node.html());
	return node.html();
}

// 生成段落文本对应的html节点！
function getPassageNode(passage, publication, strPmid, strPmcid) {
	passage = passage || {};
	let infons = passage.infons || {};
	let passageType = infons.type;
	//let annotations = getAnnotations(passage); // The DOM node that we will annotate, then add to the webpage
	let annotations = getAnnotations(passage, strPmid, strPmcid); // The DOM node that we will annotate, then add to the webpage
	//console.log(annotations);

	let passageNode = $('<div class="passage"></div>');
	passageNode.text(passage.text || infons.note);
	passageNode.addClass(passageType); // Used by CSS to change display of titles, etc

	passageNode.attr("data-section", infons.section || passageType);
	passageNode.attr("data-passage-id", passage.offset);

	if (shouldAnnotatePassage(passage)) {
		annotateNode(passageNode, annotations, false); // half performance impact
	}

	return postprocessNode(passageNode, publication, passage);
}

// 获取实体注释结果！
function getAnnotations(passage, strPmid, strPmcid) {
	//console.log(passage.annotations);
	return _.map(passage.annotations, function (ann) {
		let location = ann.locations[0];
		return {
			offset: location.offset,
			start: location.offset - passage.offset, // start：在passage中的offset！
			length: location.length,
			identifier: ann.infons.identifier,
			symbol: ann.infons.symbol, // 2021-12-14（V0017-01.01.01-06）：解决实体点击弹出框中没有symbol的问题！
			type: _.toLower(ann.infons.type),
			pmid: strPmid, // 2021-11-08：新增PMID！
			pmcid: strPmcid ? strPmcid : "", // 2021-11-08：新增PMCID！
			doitems: ann.infons.do_items ? ann.infons.do_items : [] // 2022-02-22：新增DOID相关！
		};
	});
}

// 节点注释！
function annotateNode(node, locations, bolNotAddStar) {
	//console.log(locations);
	//console.log(node);
	node.markRanges(locations, {
		each: function each(n, range) {
			//console.log("************");
			//console.log(n);
			//console.log(range);
			// TODO old browser compatibility warning : $(n).addClass
			n.classList.add('concept-' + range.type);
			n.setAttribute('data-offset', range.offset);
			n.setAttribute("data-identifier", range.identifier);
			n.setAttribute("data-symbol", range.symbol); // 2021-10-29：加上标准词条，用于点击实体进行标准词条的拷贝！
			n.setAttribute("data-type", range.type);
			n.setAttribute("data-pmid", range.pmid); // 2021-11-08：新增PMID！
			n.setAttribute("data-pmcid", range.pmcid); // 2021-11-08：新增PMCID！
			
			// 2022-02-22：新增DOID相关！
			var thisDoItems = "";
			for(var di = 0; di < range.doitems.length; di++){
				//console.log(range.doitems[di].do_identifier);
				thisDoItems = thisDoItems === "" ? range.doitems[di].do_identifier + "|" + range.doitems[di].do_symbol : thisDoItems + "|||" + range.doitems[di].do_identifier + "|" + range.doitems[di].do_symbol;
			}
			n.setAttribute("data-doid", thisDoItems);
			
			hits++;
		},
		element: "m"
	}); // Unescape HTML tags

	let nodeHTML = node.html();
	
	// 2021-12-22（V0021-01.01.05-06）：HTML特殊字符处理！
	nodeHTML = nodeHTML && nodeHTML.replace(/&amp;/g, '&');
	nodeHTML = nodeHTML && nodeHTML.replace(/&gt;/g, '>');
	nodeHTML = nodeHTML && nodeHTML.replace(/&ge;/g, '≥');
	nodeHTML = nodeHTML && nodeHTML.replace(/&lt;/g, '＜');
	nodeHTML = nodeHTML && nodeHTML.replace(/&le;/g, '≤');
	
	let fixedHTML = nodeHTML && nodeHTML.replace(/&lt;(\/?[a-z]+)&gt;/g, '<$1>');
	//console.log(fixedHTML);
	fixedHTML = bolNotAddStar != null && bolNotAddStar ? fixedHTML : generateStarsHtml(locations) + " " + fixedHTML;
	//console.log(fixedHTML);
	node.html(fixedHTML);
}

// 判断段落是否需要注释！
let notAnnotatePassagesRe = [/(abbreviation|ethic|supplementary|acknowledgment|acknowledgement|funding|author|publisher|additional file|conflict|interest|disclosure)/g, /^(affiliation(s|)|journal(s| impact factor|)|grant(s|))$/g];
function shouldAnnotatePassage(passage) {
	let section = _.toLower(passage && passage.infons && passage.infons.section);
	return !(section && _.size(section) <= 40 && _.some(notAnnotatePassagesRe, function (re) {
		return section.match(re);
	}));
}

// 注释好的节点添加到DOM中去！
function annotate(nonTitleNodes) {
	let startHighlightingTime = Date.now(); // 用于计算渲染时间！
	let passageNodes = $('<div class="passages animated fadeIn"></div>');
	_.each(nonTitleNodes, function (p) {
		return passageNodes.append(p);
	});
	$('.abstract-show-true').find('.passages-wrapper').html(passageNodes);
	let processingTime = Date.now() - startHighlightingTime; // 计算渲染时间！
	console.info("Article with " + hits + " annotations rendered in " + processingTime + " ms.");
}

// 根据locations类别，返回星标HTML！
var strHtmlStar = '<i class="layui-icon layui-icon-rate-solid" style="color: red;"></i>';
var strHtmlStarHalf = '<i class="layui-icon layui-icon-rate-half" style="color: red;"></i>';
function generateStarsHtml(locationsArr){
	var starsObj = {};
	var starsHtml = "";
	for(var i = 0; i < locationsArr.length; i++){
		//console.log("+++ " + locationsArr[i].type);
		if(locationsArr[i].type.toLowerCase() == "gene"){
			starsObj.gene = true;
		}else if(locationsArr[i].type.toLowerCase() == "disease"){
			starsObj.disease = true;
		}else if(locationsArr[i].type.toLowerCase() == "chemical"){
			starsObj.chemical = true;
		}else if(locationsArr[i].type.toLowerCase() == "mutation"){
			starsObj.mutation = true;
		}else if(locationsArr[i].type.toLowerCase() == "clinsig"){
			starsObj.clinsig = true;
		}else if(locationsArr[i].type.toLowerCase() == "evidirt"){
			starsObj.eivdirt = true;
		}
	}
	// 四大类实体检出即加一颗星！
	if(starsObj.gene !== undefined) starsHtml += strHtmlStar;
	if(starsObj.disease !== undefined) starsHtml += strHtmlStar;
	if(starsObj.chemical !== undefined) starsHtml += strHtmlStar;
	if(starsObj.mutation !== undefined) starsHtml += strHtmlStar;
	// 剩下两类实体单独判断！
	if(starsObj.clinsig !== undefined && starsObj.eivdirt !== undefined){
		starsHtml += strHtmlStar; // 临床意义+证据方向均存在，加一颗星！
	}else if(starsObj.clinsig !== undefined && starsObj.eivdirt === undefined){
		starsHtml += strHtmlStarHalf; // 临床意义存在但证据方向不存在，加半颗星！
	}
	// 返回！
	return starsHtml;
}

//根据annotations类别，返回星标数量！
function countStarsNum(annotationsArr){
	var starsObj = {};
	var countStars = 0;
	for(var i = 0; i < annotationsArr.length; i++){
		//console.log("+++ " + locationsArr[i].type);
		if(annotationsArr[i].infons.type.toLowerCase() == "gene"){
			starsObj.gene = true;
		}else if(annotationsArr[i].infons.type.toLowerCase() == "disease"){
			starsObj.disease = true;
		}else if(annotationsArr[i].infons.type.toLowerCase() == "chemical"){
			starsObj.chemical = true;
		}else if(annotationsArr[i].infons.type.toLowerCase() == "mutation"){
			starsObj.mutation = true;
		}else if(annotationsArr[i].infons.type.toLowerCase() == "clinsig"){
			starsObj.clinsig = true;
		}else if(annotationsArr[i].infons.type.toLowerCase() == "evidirt"){
			starsObj.eivdirt = true;
		}
	}
	// 四大类实体检出即加一颗星！
	if(starsObj.gene !== undefined) countStars += 1;
	if(starsObj.disease !== undefined) countStars += 1;
	if(starsObj.chemical !== undefined) countStars += 1;
	if(starsObj.mutation !== undefined) countStars += 1;
	// 剩下两类实体单独判断！
	if(starsObj.clinsig !== undefined && starsObj.eivdirt !== undefined){
		countStars += 1; // 临床意义+证据方向均存在，加一颗星！
	}else if(starsObj.clinsig !== undefined && starsObj.eivdirt === undefined){
		countStars += 0.5; // 临床意义存在但证据方向不存在，加半颗星！
	}
	// 返回！
	return countStars;
}

// 根据统计的星的数量，生成相应的HTML！
function funcGenerateStarsHtmlFromCount(countStars){
	var starsHtml = "0";
	if(countStars == 0.5){
		starsHtml = strHtmlStarHalf;
	}else if(countStars == 1){
		starsHtml = strHtmlStar;
	}else if(countStars == 1.5){
		starsHtml = strHtmlStar + strHtmlStarHalf;
	}else if(countStars == 2){
		starsHtml = strHtmlStar + strHtmlStar;
	}else if(countStars == 2.5){
		starsHtml = strHtmlStar + strHtmlStar + strHtmlStarHalf;
	}else if(countStars == 3){
		starsHtml = strHtmlStar + strHtmlStar + strHtmlStar;
	}else if(countStars == 3.5){
		starsHtml = strHtmlStar + strHtmlStar + strHtmlStar + strHtmlStarHalf;
	}else if(countStars == 4){
		starsHtml = strHtmlStar + strHtmlStar + strHtmlStar + strHtmlStar;
	}else if(countStars == 4.5){
		starsHtml = strHtmlStar + strHtmlStar + strHtmlStar + strHtmlStar + strHtmlStarHalf;
	}else if(countStars == 5){
		starsHtml = strHtmlStar + strHtmlStar + strHtmlStar + strHtmlStar + strHtmlStar;
	}
	return starsHtml;
}

//=== 注释相关 - END ===

// === 获取文献元信息 - START ===

//获取作者列表！
function getCondensedAuthors(data) {
	if (_.size(data && data.passages[0].infons.authors) > 3) {
		var authors = data.passages[0].infons.authors;
		return "".concat(authors[0], ", ").concat(authors[1], " ... ").concat(_.nth(authors, -1)); // 作者数量多于3个的时候，只取第1/2及最后一个作者，其他作者中“...”代替！
	}else{
		return data && _.join(data.passages[0].infons.authors, ', ') || 'No authors listed'; // 作者数量少于3个的时候，全显示，逗号隔开！没有的话提示没有！
	}
	return getAuthors(data);
}
function getAllAuthors(data) { // 2021-12-29（V0022-01.01.06-05）：生成全部authors！
	return data && _.join(data.passages[0].infons.authors, ', ') || 'No authors listed'; // 全显示，逗号隔开！
}

// 获取期刊名称！
function getJournal(data) {
	return data && data.passages[0].infons.journal_name || data.passages[0].infons.journal || 'Journal Unavailable';
}

// 获取发表年份信息！
function getYear(data) {
	return data && data.passages[0].infons.year || 'Year Unknown';
}

// 2021-12-29（V0022-01.01.06-05）：获取影响因子！
function getIf2020(data) {
	return data && data.passages[0].infons.if2020 || 'N/A';
}

// 判断文档是否是被注释过的：依据为document是否含有“_id”字段（已废弃）！
function isAnnotated(data) {
	return !!(data && data._id);
}

// 判断文档是否是全文，即是否有“pmcid”字段（已废弃）！
function isAnnotedFullText(data) {
	return !!(data && data._id && data.pmcid);
}

// 获取BioC导出链接：注意需要替换！
function getExportUrl(data) {
	let url = 'https://www.ncbi.nlm.nih.gov/research/pubtator-api/publications/export/biocxml?'
	if (getPMCID(data)) {
		url += 'pmcids=' + getPMCID(data)
	} else {
		url += 'pmids=' + getPMID(data)
	}
	return url;
}

// 获取PMID！
function getPMID(data) {
	return data.pmid; // 这个是必须有的，所以这里不需要“|| null”！
}

// 获取PMID！
function getPMCID(data) {
	return data && data.pmcid || null;
}

// 获取关键词数组！
function getKeywords(data) {
	return data && data.keywords || [];
}

//=== 获取文献元信息 - END ===

//=== 节点处理 - START ===

//节点处理器映射！
let POST_PROCESSORS = {
	'fig_caption_title': post_process_figure,
	'fig_title_caption': post_process_figure,
	'fig': post_process_figure,
	'fig_caption': post_process_figure,
	'table_caption': post_process_tableCaption,
	//'table_caption_title': post_process_tableCaption,
	//'table_title_caption': post_process_tableCaption,
	//'table': post_process_table,
	'title_1': post_process_title,
	'title': post_process_title
};

// 节点处理！
function postprocessNode(passageNode, publication, passage) {
	let pp = POST_PROCESSORS[passage.infons.type];

	if (pp) {
		return pp(passageNode, publication, passage);
	}

	return passageNode;
}

// 为每个具有图形图例和图像文件名的段落创建一个新图形节点！
function post_process_figure(passageNode, publication, passage) {
	let file = passage && passage.infons && passage.infons.file;
	let figUrl = file ? "https://www.ncbi.nlm.nih.gov/pmc/articles/".concat(publication.pmcid, "/bin/").concat(file) : 'images/missing-figure.png';
	let fig_id = passage && passage.infons && passage.infons.id;
	let fig_text = passageNode.html();
	return $('<div class="figure row">' +
		'<div class="col l3 m12 s12 figure-img">' +
		//'<img alt="Figure" src="' + figUrl + '"></div>' +
		'<div class="figure-text col l9 m12 s12">' +
		'<b>' + (fig_id || 'Figure') + ': </b>' + fig_text + '</div></div>');
}

// 表格标题处理！
function post_process_tableCaption(passageNode, publication, passage) {
	let fig_id = passage && passage.infons && passage.infons.id;
	let fig_text = passageNode.html();
	return $("<div class=\"table-caption\"><b>".concat(fig_id || 'Table', ": </b>").concat(fig_text, "</div>"));
}

// 表格处理！
function post_process_table(passageNode) {
	let annotatedText = passageNode.html();
	let tableNode = $('<table class="stripped"><tbody></tbody></table>');

	_.each(annotatedText.split('\n'), function (str_row) {
		let rowNode = $('<tr></tr>');

		_.each(str_row.split('\t'), function (field) {
			return rowNode.append('<td>' + field + '</td>');
		});

		tableNode.append(rowNode);
	});

	passageNode.html(tableNode);
	return passageNode;
}

// 对标题节点进行处理：type为title或title_1的节点！
function post_process_title(passageNode) {
	passageNode.text(passageNode.text().toLowerCase());
	//passageNode.prepend('<i class="title-icon fas"></i>'); // 各标题前面加上图标！
	return passageNode;
}

// 参考文献处理：由于参考文献被过滤了，所以用不上了！
function post_process_ref(passageNode, publication, passage) {
	let ref = $('<div class="ref-wrapper"></div>');
	let infons = passage && passage.infons || {};
	ref.append(_.join(infons.authors, ', '));
	ref.append(infons.year ? '<span class="ref-year">' + infons.year + '.</span>' : '');
	ref.append(passageNode);
	ref.append(infons.volume ? "Vol." + infons.volume + "." : '');
	ref.append(infons.source ? infons.source + ", " : '');
	ref.append(infons.fpage && infons.lpage ? infons.fpage + "-" + infons.lpage : '');
	ref.append(infons['pub-id_pmid'] ? ' <a target="_blank" href="https://www.ncbi.nlm.nih.gov/pubmed/' + infons['pub-id_pmid'] + '">[Pubmed]</a>' : '');
	ref.append(infons['pub-id_doi'] ? ' <a target="_blank" href="http://dx.doi.org/' + infons['pub-id_doi'] + '">[Journal]</a>' : '');
	ref.append(infons.comment);
	return ref;
}

// 缩略词处理：这个也用不上了！
function post_process_abbreviation(passageNode, publication, passage) {
	let section = passage && passage.infons && passage.infons.section;
	if (_.toLower(section) === 'abbreviations') {
		if (publication.abbrCounter % 2 === 0) {
			passageNode.addClass("table-left-col");
		} else {
			passageNode.addClass("table-right-col");
		}
		publication.abbrCounter++;
	}
}

//=== 节点处理 - END ===

//=== 其他各种判断与处理 - START ===

// 判断当前文献是否是全文：根据标题段落的infons.type是否为front进行判断！
function isFullText(passages) {
	let fp = _.nth(passages);
	return !!(fp && fp.infons && fp.infons.type === 'front');
}

// 获取章节标题段落！
function getSectionTitles(passages) {
	return _.filter(passages, function (p) {
		return p.offset !== 0 && (p.infons.type === 'title_1' || p.infons.type === 'title');
	});
}

// 版块跳转！
function scrollToSection(passageId) {
	return passageId && scrollToNode($("[data-passage-id=" + passageId + "]"));
}

// 跳转到特定节点！
let scrollToNodeHeight = 50; // 适用于details页面的高度！
function scrollToNode(n) {
	let offset = n.length && n.offset().top;
	if (offset) $('html, body').scrollTop(offset - scrollToNodeHeight);
}

// 概念的显示与隐藏！
function concept_toggle(id) {
	let body = $('body')
	let target_class = 'show-' + id
	body.hasClass(target_class) ? body.removeClass(target_class) : body.addClass(target_class)
}

// 获取排序后的全部概念类别！
function getOrderedConcepts() {
	return _.sortBy(_.values(allConcepts), 'order');
}

//实体点击效果！
var funcEntityClickObj, indexEntityClick;
var strCopyIconHtml   = '<i class="layui-icon layui-icon-templeate-1"></i> ';
var strSearchIconHtml = '<i class="layui-icon layui-icon-search"></i> ';
var strCopyTextHtml   = '[COPY] ';
var strSearchTextHtml = '[QUERY] ';
function copyText(othis, text) { // 复制内容到剪贴板的底层功能！
    var textarea = document.createElement("textarea");
    var currentFocus = document.activeElement;
    var flag = false;
    
    //document.body.appendChild(textarea);
    othis.appendChild(textarea);
    
    textarea.value = text;
    textarea.focus();
    if (textarea.setSelectionRange)
        textarea.setSelectionRange(0, textarea.value.length);
    else
        textarea.select();
    try {
        flag = document.execCommand("copy");
    } catch(eo){
        flag = false;
    }
    
    //document.body.removeChild(textarea);
    othis.removeChild(textarea);
    
    currentFocus.focus();
    if(flag) layui.layer.msg(flag ? "'" + text + "' successfully copied!" : "'" + text + "' copy failed!");
    return flag;
}
function funcCopyIntoClipboard(othis){ // 复制内容到剪贴板！
	var strToBeCopyText = $(othis).html().replace(strCopyIconHtml, '').replace(strCopyTextHtml, '');
    var flag = copyText(othis, strToBeCopyText);//这个必须在DOM对象的事件线程中执行
    //if(flag) layer.close(indexEntityClick);
}
function funcSearchThisTerm(othis, paramEM){ // 检索当前词条！
	//layer.close(indexEntityClick); // 关闭实体弹窗！
	var strToBeSearchText = $(othis).html().replace(strSearchIconHtml, '').replace(strSearchTextHtml, '');
	if($("#idLitKeyword").length == 0){
		window.location.href = "/lrSearch?paramKw=" + strToBeSearchText + "&paramMd=local&paramEM=" + paramEM;
	}else{
		window.location.href = "/lrSearch?paramKw=" + strToBeSearchText + "&paramEM=" + paramEM;
	}
}
function funcEntityClick(){
	$("m").unbind("click", funcEntityClickObj); // 为防止多次调用该方法导致的重复监听，这里需要先去除之前的监听！
	$('m').bind('click', funcEntityClickObj = function (e) {
		//layer.msg('111 type:' + e.target.dataset.type + ';identifier:' + e.target.dataset.identifier)
		//console.log(e);
		
		let thisSymbol = "", thisIdentifier = "", thisType = "", thisOffset = "", thisPmid = "", thisPmcid = "", thisDoItems = "", thisDoItemsArr = [], thisDoItemsIDHtml = "", thisDoItemsTermHtml = "";
		for(let j = 0; j < e.target.attributes.length; j++){
			if(e.target.attributes[j].nodeName == 'data-identifier'){
				thisIdentifier = e.target.attributes[j].nodeValue;
			}else if(e.target.attributes[j].nodeName == 'data-type'){
				thisType = e.target.attributes[j].nodeValue.toLowerCase();
			}else if(e.target.attributes[j].nodeName == 'data-symbol'){
				thisSymbol = e.target.attributes[j].nodeValue;
			}else if(e.target.attributes[j].nodeName == 'data-offset'){
				thisOffset = e.target.attributes[j].nodeValue;
			}else if(e.target.attributes[j].nodeName == 'data-pmid'){
				thisPmid = e.target.attributes[j].nodeValue;
			}else if(e.target.attributes[j].nodeName == 'data-pmcid'){
				thisPmcid = e.target.attributes[j].nodeValue;
			}else if(e.target.attributes[j].nodeName == 'data-doid'){ // 2022-02-22：新增DOID相关！
				thisDoItems = e.target.attributes[j].nodeValue;
			}
		}
		
		// 2022-02-22：新增DOID相关！
		//console.log(JSON.stringify(thisDoItems));
		if(thisDoItems !== ""){
			thisDoItemsArr = thisDoItems.split("\\|\\|\\|");
			for(var di = 0; di < thisDoItemsArr.length; di++){
				//console.log(thisDoItemsArr[di]);
				var thisDoItemArr = thisDoItemsArr[di].split("\|");
				thisDoItemsIDHtml += 
					'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;" title="copy">' + strCopyIconHtml + thisDoItemArr[0] + '</button>'; // 标准词条拷贝！
					//'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this, 1)" title="search">' + strSearchIconHtml + thisDoItemArr[0] + '</button>'; // 标准词条检索！
				thisDoItemsTermHtml += 
					'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;" title="copy">' + strCopyIconHtml + thisDoItemArr[1] + '</button>' + // 标准词条拷贝！
					'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this, 1)" title="search">' + strSearchIconHtml + thisDoItemArr[1] + '</button>'; // 标准词条检索！
			}
		}
		
		if(thisType == 'cancer' || thisType == 'disease'){
			thisIdentifier = '@CA@' + thisIdentifier;
		}else if(thisType == 'drug' || thisType == 'chemical'){
			thisIdentifier = '@DR@' + thisIdentifier;
		}else if(thisType == 'gene'){
			thisIdentifier = '@GE@' + thisIdentifier;
		}else if(thisType == 'clinsig' || thisType == 'evidirt'){
			thisIdentifier = '-';
		}
		
		// 打开弹窗！
		let thisTargetDatasetType = e.target.dataset.type.toUpperCase();
		indexEntityClick = layer.open({
			type: 1,
			//title: 'Operate on "' + thisTargetDatasetType + '": "' + e.target.innerText + '":',
			title: false,
			shadeClose: true,
			closeBtn: 0,
			//skin: 'layui-layer-rim', //加上边框
			area: ['560px'], // 宽设置，高度自适应！
			content: 
				'<div class="layui-btn-container divEntityPrompt">' +
				'	<h3 style="font-size: 14px; color: grey;"><b>' + thisTargetDatasetType + '</b></h3>' +
				'	<h2><b>' + e.target.innerText + '</b></h2>' +
				'	<fieldset class="layui-elem-field layui-field-title"><legend>Mentioned Entity</legend></fieldset>' +
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;" title="copy">' + strCopyIconHtml + e.target.innerText + '</button>' + // 原词条拷贝！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this, 1)" title="search">' + strSearchIconHtml + e.target.innerText + '</button>' + // 原词条检索！
				
				'	<br>' +
				(thisSymbol !== 'undefined' && thisSymbol !== '-' ?
				'	<fieldset class="layui-elem-field layui-field-title"><legend>Standardized Entity</legend></fieldset>' +
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;" title="copy">' + strCopyIconHtml + thisSymbol + '</button>' + // 标准词条拷贝！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this, 1)" title="search">' + strSearchIconHtml + thisSymbol + '</button>' // 标准词条检索！
				: ''
				) +
				
				'	<br>' +
				(thisIdentifier.indexOf('undefined') > -1 || thisIdentifier == '-' || thisIdentifier.indexOf('@-') > -1 ? '' :
				'	<fieldset class="layui-elem-field layui-field-title"><legend>Standardized Identifier</legend></fieldset>' +
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;" title="copy">' + strCopyIconHtml + thisIdentifier + '</button>' + // 标准词条ID拷贝！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this, 0)" title="search">' + strSearchIconHtml + thisIdentifier + '</button>' // 标准词条ID检索！
				) +
				
				'	<br>' +
				(thisDoItemsIDHtml === '' ? '' : 
					'	<fieldset class="layui-elem-field layui-field-title"><legend>Disease Ontology</legend></fieldset>' +
					thisDoItemsIDHtml + thisDoItemsTermHtml
				) +
				
				'	<br>' +
				'	<fieldset class="layui-elem-field layui-field-title"><legend>Report bug</legend></fieldset>' +
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcErrorReport(\'' + thisPmid + '\', \'' + thisPmcid + '\', \'' + e.target.innerText + '\', \'Category\', \'' + thisTargetDatasetType + '\', \'' + thisOffset + '\')" style="margin-left: 0;" title="report"><i class="layui-icon layui-icon-release"></i> Category: \'' + thisTargetDatasetType + '\'</button>' + // 报告分类错误报告！
				(thisSymbol !== 'undefined' && thisSymbol !== '-' ?
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcErrorReport(\'' + thisPmid + '\', \'' + thisPmcid + '\', \'' + e.target.innerText + '\', \'Normalization\', \'' + thisSymbol + '\', \'' + thisOffset + '\')" title="report"><i class="layui-icon layui-icon-release"></i> Normalization: \'' + thisSymbol + '\'</button>' // 标准化错误报告！
				: '') + 
				'</div>'
		});
	});
}
function funcErrorReport(strPmid, strPmcid, strThisMention, strErrType, strErrContent, strErrOffset){
	layer.confirm('Are you sure to report: <br>The "<i>' + strErrType + ':</i> <b>' + strErrContent + '</b>" of <br>"<b>' + strThisMention + '</b>" is error?', {
		skin: 'layui-layer-admin',
		shade: .1,
		btn: ['Yes', 'Cancel'],
		title: 'Confirm'
	}, function (index) {
		
		var indexReport = layer.load(3, {shade: [0.3, '#393D49']});
		$.ajax({
			type: "post",
			url: "/errorReport/add",
			dataType: "json",
			async: true,
			data: JSON.stringify({pmid: strPmid, pmcid: strPmcid, errorType: strErrType, errorContent: strErrContent, errorOffset: strErrOffset}),
			contentType: "application/json; charset=utf-8",
			success: function(result){
				layer.close(indexReport); // 关闭loading弹窗！
				layer.msg(result.success ? "Thank you very much for your feedback, we will review and deal with this error as soon as possible!" : result.message); // 给出报告的返回提示信息！
				//layer.close(indexEntityClick); // 关闭当前实体弹窗！
			}
		});
		
	});
	
}

// 判断passage是否为摘要！
function funcJudgeAbstract(thisPassageObj){
	if((thisPassageObj.infons.section_type != null && thisPassageObj.infons.section_type.toLowerCase().indexOf("abstract") > -1) || (thisPassageObj.infons.type != null && thisPassageObj.infons.type.toLowerCase().indexOf("abstract") > -1)) {
		return true;
	}else{
		return false;
	}
}

//=== 其他各种判断与处理 - END ===

//=== 左侧实体汇总显示 - START ===

// 搜索框内值变动的情况下，进行搜索！
function initSearch(passages){
	let val = $('#filterText').val();
	let clear = $('#summary .fa-times');
	if (val !== '') {
		clear.show(); // 当搜索框中有内容的时候，显示叉号！
	} else {
		clear.hide(); // 当搜索框中没有内容的时候，隐藏叉号！
	}
	loadSummaryConcepts(passages)
}

// 清除检索框中的关键词，恢复未检索状态：即显示全部实体！
function clearFilter() {
	$('#filterText').val('');
	$('#filterText').trigger('change');
}

// 设置分组类型！
function setGroup(ele, index, passages) {
	let $group = $('#groupControlsShow');
	$group.children('.selected').removeClass('selected')
	$(ele).addClass('selected')
	$group.next('.selected-value').text(groups[index])
	$group.hide()
	loadSummaryConcepts(passages)
}

//设置排序方式！
function setSort(ele, index, passages) {
	let $sort = $('#sortControlsShow');
	$sort.children('.selected').removeClass('selected')
	$(ele).addClass('selected')
	$sort.next('.selected-value').text(sorts[index].niceField || sorts[index].field)
	$sort.hide()
	loadSummaryConcepts(passages)
}

// 排序！
function getOrder(type) {
	let concept = type && allConcepts[_.toLower(type)];
	return concept && concept.order;
}

// 实现实体组内部的概念排序！
function sortConcepts(conceptGroups) {
	return _.sortBy(conceptGroups, function (g) {
		return getOrder(g.name);
	});
}

// 从所有段落中抽取提及的概念！
function extractMentions(passages) {
	//console.log(passages);
	let mentions = _.transform(passages, function (r, p) {
		//console.log("----------");
		//console.log(p.annotations);
		_.each(p.annotations, function (m) {
			//console.log(m.text);
			let strThisType = m.infons && m.infons.type;
			if(strThisType.toLowerCase() == 'evidirt'){
				strThisType = "Evidence Direction";
			}else if(strThisType.toLowerCase() == 'clinsig'){
				strThisType = "Clinical Significance";
			}else if(strThisType.toLowerCase() == 'disease'){
				strThisType = "Cancer/Disease";
			}else if(strThisType.toLowerCase() == 'chemical'){
				strThisType = "Drug/Chemical";
			}else if(strThisType.toLowerCase() == 'gene'){
				strThisType = "Gene";
			}else if(strThisType.toLowerCase() == 'mutation'){
				strThisType = "Alteration";
			}
			r.push({
				identifier: m.infons && m.infons.identifier,
				type: strThisType,
				text: m.text,
				section: p.infons && (p.infons.section || p.infons.type),
				offset: m.locations && m.locations[0].offset
			});
		});
	}, []); // TODO export to another function to get only mentions with identifiers
	//console.log(mentions);
	return _.filter(mentions, 'identifier'); // 要求段落的annotations.infons.identifier存在，相应sentence.infons.identifier也存在，且注意大小写！
}

// 对提及的概念进行过滤（基于用于过滤的文本框中输入的文本内容进行）！
function filterMentions(mentions, query, paths) {
	query = _.lowerCase(query);
	return _.filter(mentions, function (m) {
		return _.some(paths, function (path) {
			return _.includes(_.lowerCase(_.get(m, path)), query);
		});
	});
}

// 获取概念名称！
function getName(mentions) {
	let mostCommonText = _.chain(mentions).map('text').countBy().toPairs().maxBy(_.last).value();
	return mostCommonText && mostCommonText[0];
}

// 概念名称格式化！
function cleanCatName(sectionName) {
	return _.replace(_.toLower(sectionName), ' ', '_'); // 小写转化，同时替换空格为下划线！
}

// 点击左侧概念，中间文献中该概念显示的动态效果！
function animate(n) {
	let ANIMATE_SHOW_CLASS = "rubberBand";
	let ANIMATE_CLASS = "animated";
	let animationEvent = function () {
		let t;
		let el = document.createElement("fakeelement");
		let animations = {
			"animation": "animationend",
			"OAnimation": "oAnimationEnd",
			"MozAnimation": "animationend",
			"WebkitAnimation": "webkitAnimationEnd"
		};

		for (t in animations) {
			if (el.style[t] !== undefined) {
				return animations[t];
			}
		}
	}();
	n.css("display", "inline-block");
	n.addClass(ANIMATE_CLASS);
	n.addClass(ANIMATE_SHOW_CLASS);
	n.one(animationEvent, function () {
		n.removeClass(ANIMATE_CLASS);
		n.removeClass(ANIMATE_SHOW_CLASS);
		n.css("display", "inline");
	});
}

// 概念在文献中的显示！
function goToEntities(entityId, sectionName) {
	if (!entityId) return;
	let sectionSelector = sectionName ? "[data-section='" + sectionName + "']" : "";
	let thisTabLabel = sectionSelector;
	//console.log("‘" + thisTabLabel + "’");
	let entityNodes = $(thisTabLabel + " m[data-identifier='" + entityId + "']"); // 1. Scroll to first
	//console.log(entityNodes);
	scrollToNode(entityNodes); // 2. Animate (remove+add class) // 跳转到特定节点：当前概念在文献中第一次出现的地方！
	animate(entityNodes); // 3. Mark all selected nodes
	entityNodes.hasClass('selected') ? entityNodes.removeClass('selected') : entityNodes.addClass('selected');
}

// 点击概念，进行跳转！
function gotoAnn(ele, ann) {
	let summaryItem = $(ele); //FIXME Only set as selected when successfully selected nodes (sometimes does not select)
	summaryItem.hasClass('selected') ? summaryItem.removeClass('selected') : summaryItem.addClass('selected');
	//console.log(ann.identifier);
	goToEntities(ann.identifier, ann.section);
}

// 获取提及概念的偏移位置！
function getOffset(mentions) {
	return _.chain(mentions).map('offset').min().value();
}

// 获取全部的概念！
function getConcepts(mentions, groupField, sortObj, filterText) {
	//console.log(mentions);
	let startTime = Date.now(); // 用于计算此次概念计算时间！
	let filterPaths = ['text', 'identifier', 'type'];

	// 如果用于过滤的文本框中有内容，就需要进行过滤，没有就不执行过滤！
	if (filterText) {
		mentions = filterMentions(mentions, filterText, filterPaths);
	}

	let groupedByConcept = _.groupBy(mentions, function (m) {
		return m.type + '@' + m.identifier + '@' + (m[groupField] || 'All');
	});

	let concepts = _.map(groupedByConcept, function (mentions, conceptId) {
		let idtype = conceptId.split('@', 3);
		let concept = {
			type: idtype[0],
			identifier: idtype[1],
			count: _.size(mentions),
			name: getName(mentions), // 获取概念名称！
			offset: getOffset(mentions) //text: _.join(_.uniq(_.map(m, 'text')), ', ')

		};
		concept[groupField] = idtype[2];
		return concept;
	});

	let groups = _.groupBy(concepts, groupField); // Sort inside each of the groups

	_.each(groups, function (groupConcepts, groupName) {
		groups[groupName] = _.orderBy(groupConcepts, [sortObj.field], [sortObj.order]);
	});

	let processingTime = Date.now() - startTime; // 计算此次概念计算时间！
	//console.info("Concepts (re)calculated in " + processingTime + " ms.");
	
	// 返回所有概念！
	return _.map(groups, function (v, k) {
		return {
			name: k,
			concepts: v
		};
	});
}

// 加载搜索、分组及排序后的实体，在页面左侧相应模块显示！
function loadSummaryConcepts(passages) {
	let tableView = $('#summary .annotations-table-view').empty(); // 首先清空该模块内部代码：为此次加载生成的HTML腾空间！
	let group     = groups[$("#idGroupControls").val()]; // 获取当前选中的分组！
	let sort      = sorts[$("#idSortControls").val()]; // 获取当前选中的排序方式！
	//console.log("++++++++++");
	//console.log(passages);
	let concepts = getConcepts(extractMentions(passages), group, sort, $('#filterText').val()); // 获取全部的概念！
	//console.log(extractMentions(passages));
	//console.log(concepts);
	if (group === 'type') {
		concepts = sortConcepts(concepts); // 实现实体组内部的概念排序！
	}
	if(concepts.length > 0){ // 存在匹配的实体！
		for (let category of concepts) {
			
			// 类别！
			let categoryItem = $('<div class="category full-' + !!sectionsShowFull[category.name] + '"' + ' data-section="' + cleanCatName(category.name) + '"></div>');
			categoryItem.append('<div class="table-section-name"><span class="fa category-icon"></span>' + category.name + '</div>');
			
			// 当前类别下的概念！
			let table = $('<table></table>');
			let tbody = $('<tbody></tbody>');
			for (let ann of category.concepts) {
				let tr = $('<tr></tr>');
				let td = $('<td></td>');
				let a = $('<a href="javascript:" class="ann-name">' + ann.name + '</a>');
				a.on('click', function (e) {
					gotoAnn(e.target, ann); // 点击概念，进行跳转！
				})
				td.append(a);
				td.append('&nbsp;');
				td.append('<span title="Number of mentions" class="ann-count">(' + ann.count + ')</span>');
				tr.append(td);
				if (group !== 'type') {
					tr.append('<td class="annType">' + ann.type + '</td>');
				}
				tbody.append(tr);
			}
			table.append(tbody);
			categoryItem.append(table);
			
			// 如果当前类别下的概念多余5个，显示more/less！
			if (category.concepts.length > 5) {
				let span = $('<span class="visibility-toggle">' + (sectionsShowFull[category.name] ? 'less' : 'more') + '</span>');
				span.on('click', function () {
					sectionsShowFull[category.name] = !sectionsShowFull[category.name];
					categoryItem.removeClass('full-' + !sectionsShowFull[category.name]).addClass('full-' + sectionsShowFull[category.name]);
					span.text(sectionsShowFull[category.name] ? 'less' : 'more');
				})
				categoryItem.append(span);
			}
			
			// 将当前类别的代码添加到DOM中！
			tableView.append(categoryItem);
		}
	}else{ // 不存在匹配的实体：给出提示！
		tableView.append("<i class='ann-name'>No matched bioconcept!</i><br><br>");
	}
}

//=== 左侧实体汇总显示 - START ===

/* ### Annotation - END ########################################### */

/* ### Footer Processing - END ########################################### */

var strHtmlFooterContent = 
	'<div class="footer-top">' +
	'	<div class="container">' +
	'		<div class="row">' +
	'' +
	'			<div class="col-lg-4 col-md-6 footer-info">' +
	'				<h3>OncoPubMiner <span class="systemVersion"></span> <span class="apiVersion"></span></h3>' +
	'				<p>' +
	'					Precision Oncology Text-Mining Group<br><br>' +
	'					C3-1, Jinghai Industrial Park<br>Beijing 100176, China<br><br>' +
	'					<strong>Phone:</strong> +86-010-56380035, 59544150<br>' +
	'					<strong>Email:</strong> medinfoservice(at)chosenmedtech.com<br>' +
	'				</p>' +
	'				<div class="social-links mt-3">' +
	'					<a href="javascript:;" class="tiktok"><svg t="1636966478156" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2802" width="18" height="18"><path d="M937.4 423.9c-84 0-165.7-27.3-232.9-77.8v352.3c0 179.9-138.6 325.6-309.6 325.6S85.3 878.3 85.3 698.4c0-179.9 138.6-325.6 309.6-325.6 17.1 0 33.7 1.5 49.9 4.3v186.6c-15.5-6.1-32-9.2-48.6-9.2-76.3 0-138.2 65-138.2 145.3 0 80.2 61.9 145.3 138.2 145.3 76.2 0 138.1-65.1 138.1-145.3V0H707c0 134.5 103.7 243.5 231.6 243.5v180.3l-1.2 0.1" p-id="2803" fill="#ffffff"></path></svg></a>' +
	'					<a href="javascript:;" class="wechat"><svg t="1636966410711" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2497" width="24" height="24"><path d="M664.250054 368.541681c10.015098 0 19.892049 0.732687 29.67281 1.795902-26.647917-122.810047-159.358451-214.077703-310.826188-214.077703-169.353083 0-308.085774 114.232694-308.085774 259.274068 0 83.708494 46.165436 152.460344 123.281791 205.78483l-30.80868 91.730191 107.688651-53.455469c38.558178 7.53665 69.459978 15.308661 107.924012 15.308661 9.66308 0 19.230993-0.470721 28.752858-1.225921-6.025227-20.36584-9.521864-41.723264-9.521864-63.862493C402.328693 476.632491 517.908058 368.541681 664.250054 368.541681zM498.62897 285.87389c23.200398 0 38.557154 15.120372 38.557154 38.061874 0 22.846334-15.356756 38.156018-38.557154 38.156018-23.107277 0-46.260603-15.309684-46.260603-38.156018C452.368366 300.994262 475.522716 285.87389 498.62897 285.87389zM283.016307 362.090758c-23.107277 0-46.402843-15.309684-46.402843-38.156018 0-22.941502 23.295566-38.061874 46.402843-38.061874 23.081695 0 38.46301 15.120372 38.46301 38.061874C321.479317 346.782098 306.098002 362.090758 283.016307 362.090758zM945.448458 606.151333c0-121.888048-123.258255-221.236753-261.683954-221.236753-146.57838 0-262.015505 99.348706-262.015505 221.236753 0 122.06508 115.437126 221.200938 262.015505 221.200938 30.66644 0 61.617359-7.609305 92.423993-15.262612l84.513836 45.786813-23.178909-76.17082C899.379213 735.776599 945.448458 674.90216 945.448458 606.151333zM598.803483 567.994292c-15.332197 0-30.807656-15.096836-30.807656-30.501688 0-15.190981 15.47546-30.477129 30.807656-30.477129 23.295566 0 38.558178 15.286148 38.558178 30.477129C637.361661 552.897456 622.099049 567.994292 598.803483 567.994292zM768.25071 567.994292c-15.213493 0-30.594809-15.096836-30.594809-30.501688 0-15.190981 15.381315-30.477129 30.594809-30.477129 23.107277 0 38.558178 15.286148 38.558178 30.477129C806.808888 552.897456 791.357987 567.994292 768.25071 567.994292z" p-id="2498" fill="#ffffff"></path></svg></a>' +
	'					<a href="javascript:;" class="qq"><svg t="1636967789314" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5540" width="18" height="18"><path d="M980.79827 694.105946c-21.144216-122.796973-109.844757-203.250162-109.844757-203.250162 12.647784-111.477622-33.792-131.26573-33.792-131.26573C827.392 14.668108 530.985514 20.67373 524.730811 20.839784 518.476108 20.67373 222.01427 14.668108 212.300108 359.590054c0 0-46.467459 19.788108-33.819676 131.26573 0 0-88.700541 80.453189-109.817081 203.250162 0 0-11.291676 207.484541 101.403676 25.40627 0 0 25.350919 69.161514 71.790703 131.26573 0 0-83.082378 28.256865-75.997405 101.625081 0 0-2.87827 81.836973 177.401081 76.218811 0 0 126.699243-9.852541 164.753297-63.515676l16.605405 0 0.276757 0 16.633081 0c38.026378 53.635459 164.725622 63.515676 164.725622 63.515676 180.224 5.618162 177.401081-76.218811 177.401081-76.218811 7.029622-73.368216-75.997405-101.625081-75.997405-101.625081 46.439784-62.104216 71.790703-131.26573 71.790703-131.26573C992.034595 901.590486 980.79827 694.105946 980.79827 694.105946z" p-id="5541" fill="#ffffff"></path></svg></a>' +
	'					<a href="javascript:;" class="docker"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20.17 9.82a4.76 4.76 0 0 0-.84.07 3.12 3.12 0 0 0-1.43-2.14l-.28-.16-.19.27a3.7 3.7 0 0 0-.51 1.19 2.84 2.84 0 0 0 .33 2.22 4.11 4.11 0 0 1-1.45.35H2.63a.63.63 0 0 0-.63.62 9.6 9.6 0 0 0 .58 3.39 5 5 0 0 0 2 2.6 8.86 8.86 0 0 0 4.42.95 13.27 13.27 0 0 0 2.42-.18 10.09 10.09 0 0 0 3.19-1.15A8.9 8.9 0 0 0 16.78 16a11.94 11.94 0 0 0 2.13-3.67h.19a3.08 3.08 0 0 0 2.23-.84 2.36 2.36 0 0 0 .59-.87l.08-.22-.2-.16a2.69 2.69 0 0 0-1.63-.42z"></path><path d="M5.61 9.35H3.85a.16.16 0 0 0-.16.15v1.58a.16.16 0 0 0 .16.15h1.76a.16.16 0 0 0 .16-.15V9.5a.16.16 0 0 0-.16-.15zm2.44 0H6.28a.16.16 0 0 0-.16.15v1.58a.16.16 0 0 0 .16.15h1.77a.15.15 0 0 0 .15-.15V9.5a.15.15 0 0 0-.15-.15zm2.47 0H8.75a.15.15 0 0 0-.15.15v1.58a.15.15 0 0 0 .15.15h1.77a.15.15 0 0 0 .15-.15V9.5a.15.15 0 0 0-.15-.15zm.67 0a.15.15 0 0 0-.19.15v1.58a.15.15 0 0 0 .15.15H13a.15.15 0 0 0 .15-.15V9.5a.15.15 0 0 0-.15-.15zM6.28 7.09H8a.16.16 0 0 1 .16.16v1.56A.16.16 0 0 1 8 9H6.28a.15.15 0 0 1-.15-.15V7.24a.15.15 0 0 1 .15-.15zm2.47 0h1.77a.15.15 0 0 1 .15.15v1.57a.16.16 0 0 1-.16.16H8.75a.15.15 0 0 1-.15-.15V7.24a.15.15 0 0 1 .15-.15zm2.44 0H13a.15.15 0 0 1 .15.15v1.57A.15.15 0 0 1 13 9h-1.81a.16.16 0 0 1-.19-.19V7.24a.15.15 0 0 1 .19-.15z"></path><rect x="11.04" y="4.82" width="2.07" height="1.88" rx=".15"></rect><path d="M13.65 9.35a.15.15 0 0 0-.15.15v1.58a.15.15 0 0 0 .15.15h1.77a.15.15 0 0 0 .15-.15V9.5a.15.15 0 0 0-.15-.15z"></path></svg></a>' +
	'					<a href="javascript:;" class="gitlab"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20.892 9.889a.664.664 0 0 0-.025-.087l-2.104-6.479a.84.84 0 0 0-.8-.57.822.822 0 0 0-.789.575l-2.006 6.175H8.834L6.826 3.327a.823.823 0 0 0-.786-.575h-.006a.837.837 0 0 0-.795.575L3.133 9.815c0 .005-.005.01-.007.016l-1.067 3.281a1.195 1.195 0 0 0 .435 1.34l9.227 6.706c.167.121.393.12.558-.003l9.229-6.703a1.2 1.2 0 0 0 .435-1.34l-1.051-3.223zM17.97 3.936l1.809 5.566H16.16l1.81-5.566zm-11.94 0 1.812 5.566H4.228L6.03 3.936zm-2.982 9.752a.253.253 0 0 1-.093-.284l.793-2.437 5.817 7.456-6.517-4.735zm1.499-3.239h3.601l2.573 7.916-6.174-7.916zm7.452 8.794-2.856-8.798h5.718l-1.792 5.515-1.07 3.283zm1.282-.877 2.467-7.588.106-.329h3.604l-5.586 7.156-.591.761zm7.671-4.678-6.519 4.733.022-.029 5.794-7.425.792 2.436a.25.25 0 0 1-.089.285z"></path></svg></a>' +
	'				</div>' +
	'			</div>' +
	'' +
	'			<div class="col-lg-2 col-md-6 footer-links">' +
	'				<h4>Useful Links</h4>' +
	'				<ul>' +
	'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/">Home</a></li>' +
	'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/lrSearch">Search</a></li>' +
	'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/lrDetails?fi=1441274726664597505&pid=33676426&t=0">Reading</a></li>' +
	//'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/lrStat">Statistics</a></li>' +
	'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/lrHelp">Tutorial</a></li>' +
	'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/lrAbout">About Us</a></li>' +
	'					<li><i class="bx bx-map-pin"></i>&nbsp;&nbsp;<a href="/login">Login</a></li>' +
	'				</ul>' +
	'			</div>' +
	'' +
	'			<div class="col-lg-3 col-md-6 footer-links">' +
	'				<h4>Our Services</h4>' +
	'				<ul>' +
	'					<li>Publication Search</li>' +
	'					<li>Library Management</li>' +
	'					<li>Team Building</li>' +
	'					<li>Form Customization</li>' +
	'					<li>Project Management</li>' +
	'					<li>Online Reading</li>' +
	'					<li>Data Review</li>' +
	'				</ul>' +
	'			</div>' +
	'' +
//	'			<div class="col-lg-3 col-md-6 footer-links">' +
//	'				<h4>&nbsp;</h4>' +
//	'				<ul>' +
//	'					<li>Form Customization</li>' +
//	'					<li>Project Management</li>' +
//	'					<li>Online Reading</li>' +
//	'					<li>Data Review</li>' +
//	'					<li>Dataset Download</li>' +
//	'				</ul>' +
//	'			</div>' +
//	'' +
	'			<div class="col-lg-3 col-md-6 footer-links">' +
	'				<h4>Visitor Traffic</h4>' +
	'				<script type="text/javascript" id="clustrmaps" src="//clustrmaps.com/map_v2.js?d=op9uCbIIYI3DBTi56xqNqk6PA76D1aB48M9cHtlPoFw&cl=ffffff&w=a"></script>' + // 样式已经被隐藏！
//	'				<script type="text/javascript" id="clstr_globe" src="//clustrmaps.com/globe.js?d=op9uCbIIYI3DBTi56xqNqk6PA76D1aB48M9cHtlPoFw"></script>' +
	'				<script type="text/javascript" src="//rf.revolvermaps.com/0/0/1.js?i=57e803e0f4b&amp;s=220&amp;m=0&amp;v=false&amp;r=false&amp;b=000000&amp;n=false&amp;c=ff0000" async="async"></script>' +
	'			</div>' +
	'' +
	'		</div>' +
	'	</div>' +
	'</div>' +
	'' +
	'<div class="container">' +
	'	<div class="copyright">' +
	'		Copyright &copy; Since 2021, <strong><span>OncoPubMiner</span></strong>. All Rights Reserved' +
	'	</div>' +
	'</div>';
$("#footer").html(strHtmlFooterContent);

//获取系统版本信息！
$.ajax({url:"/getVersion",async:true,success:function(result){
	$(".systemVersion").html("&nbsp;&nbsp;v" + result);
	$(".currentVersion").html("v" + result);
}});

//统计数量！
function funcGetStatInfo(){
	
	//统计数量 - 接口相关！
	$.ajax({
		type: "get",
		url: strApiStatNumber,
		dataType: "json",
		async: true,
		data: {},
		contentType: "application/json; charset=utf-8",
		success: function(result){
			if(result.code == 200){
				$("#idStatAbstracts").html(toThousands(result.data.statAbstracts));
				$("#idStatFullTexts").html(toThousands(result.data.statFullTexts));
				$("#idStatEntityPubPairs").html(toThousands(result.data.statEntityPubPairs));
				$(".apiVersion").html("(API: " + result.data.version + ")");
			}else{
				$("#idStatAbstracts").html(0);
				$("#idStatFullTexts").html(0);
				$("#idStatEntityPubPairs").html(0);
				$(".apiVersion").html("");
			}
		},
		error: function(result){
			$("#idStatAbstracts").html(0);
			$("#idStatFullTexts").html(0);
			$("#idStatEntityPubPairs").html(0);
			$(".apiVersion").html("");
		}
	});
	
	// 统计数量 - 本地系统！
	$.ajax({
		type: "get",
		url: "/stat/index",
		dataType: "json",
		async: true,
		data: {date: new Date()},
		contentType: "application/json; charset=utf-8",
		success: function(result){
			if(result.success){
				$("#idStatTeams").html(toThousands(result.data.statTeams));
				$("#idStatCollections").html(toThousands(result.data.statCollections));
				$("#idStatForms").html(toThousands(result.data.statForms));
				$("#idStatProjects").html(toThousands(result.data.statProjects));
				$("#idStatDatasets").html(toThousands(result.data.statDatasets));
			}else{
				$("#idStatTeams").html(0);
				$("#idStatCollections").html(0);
				$("#idStatForms").html(0);
				$("#idStatProjects").html(0);
				$("#idStatDatasets").html(0);
			}
		},
		error: function(result){
			$("#idStatTeams").html(0);
			$("#idStatCollections").html(0);
			$("#idStatForms").html(0);
			$("#idStatProjects").html(0);
			$("#idStatDatasets").html(0);
		}
	});
	
}

//数字加逗号！
function toThousands(num) {
	var num = (num || 0).toString(), result = '';
	while (num.length > 3) {
		result = ',' + num.slice(-3) + result;
		num = num.slice(0, num.length - 3);
	}
	if (num) { result = num + result; }
	return result;
}

// 系统简要描述内容！
var strHtmlStartNewParagraph = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//var strPlatformBriefDesc = 'OncoPubMiner is a one-stop oncology publication mining system. Daily updated articles are automatically annotated and converted into BioC format, and they are ready to be searched and read at any time. Its extremely flexible form customization function can meet almost all data structure requirements, ensuring that it can provide services to a wider range of users.';
var strPlatformBriefDesc = 'OncoPubMiner is a one-stop solution for mining oncology publications. Articles are automatically annotated and converted into BioC format on a daily basis, and they are available for searching and reading at any time. Its incredibly versatile form customization feature may accommodate practically any data format need, allowing it to serve a broader spectrum of users.';
$(".briefDesc").html(strPlatformBriefDesc);

/* ### Annotation - END ########################################### */

/* ### Tutorial & About - START ########################################### */

//监听页面滚动条！
function funcListenScroll(){
	let winPos = $(window).scrollTop();
	if(winPos > 246){
		$("#idDivLeft").addClass("leftFixed");
		$("#idDivRight").addClass("leftFixedRight");
	}else{
		$("#idDivLeft").removeClass("leftFixed");
		$("#idDivRight").removeClass("leftFixedRight");
	}
	
	// 判断footer的位置：确保左侧div不覆盖到footer之上！
	//console.log($("#footer").offset()['top']);
	let footerPos = document.getElementById("footer").offsetTop - winPos; // footer距离浏览器顶部的位置！
	let leftDivHeight = $("#idDivLeft").height();
	let leftDivBottomPos = leftDivHeight + 80 + 30; // 左侧div底部位置！
	if(footerPos < leftDivBottomPos){
		$("#idDivLeft").hide();
	}else{
		$("#idDivLeft").show();
	}
}

// 根据对象生成HTML！
function funcNo2Code(n){
	n++;
	if(n < 10){
		n = '0' + n;
	}
	return n;
}

// 锚链接跳转！
function topMao(e){
	document.querySelector("#" + e).scrollIntoView(true);
	var target = $("#" + e);
	var top = target.offset().top-80;// 距离顶部80像素！
	if(top > 0) {
		$('html,body').animate({scrollTop: top}, 1000);// 带jq动画的跳转！
	}
}

/* 获取窗口高度 */
function getClientHeight(){
	var clientHeight=0;
	if(document.body.clientHeight&&document.documentElement.clientHeight){
		var clientHeight = (document.body.clientHeight<document.documentElement.clientHeight)?document.body.clientHeight:document.documentElement.clientHeight;
	}else{
		var clientHeight = (document.body.clientHeight>document.documentElement.clientHeight)?document.body.clientHeight:document.documentElement.clientHeight;
	}
	return clientHeight;
}

/* ### Tutorial & About - END ########################################### */

/* ### 百度网站访问统计 - START ########################################### */

var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?b78ab130e04fa84e94aaae2689092a34";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

/* ### 百度网站访问统计 - END ########################################### */

/* 获取URL中的参数 */
function getQueryVariable(variable){
	var query = window.location.search.substring(1);
	query = decodeURIComponent(query); // 2021-09-22：解码！
	var vars = query.split("&");
	for (var i=0;i<vars.length;i++) {
		var pair = vars[i].split("=");
		if(pair[0] == variable){ return pair[1] == "undefined" || pair[1] == "" ? null : pair[1]; }
	}
	return(null);
}

/* 判断是否是整数 */
function isInteger(obj) {
	if(!(/(^[1-9]\d*$)/.test(obj))){
	    return false;
	}else{
		return true;
	}
}