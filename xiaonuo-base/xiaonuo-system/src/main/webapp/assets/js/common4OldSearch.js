/** EasyWeb iframe v3.1.8 date:2020-05-04 License By http://xiaonuo.vip */
var sysLoginUser;
//var strApiUrlRoot = "http://192.168.2.2:9001/"; // LYY本地！
var strApiUrlRoot = "http://1.119.131.198:9001/"; // GPU服务器映射地址！
var strApiKwSearching = strApiUrlRoot + "search?q="; // 关键字检索！
var strApiKwQuery     = strApiUrlRoot + "keyword?q="; // 关键字搜索！
var strApiCaSearching = strApiUrlRoot + "cancer?q="; // 癌症标准词搜索！
var strApiGeSearching = strApiUrlRoot + "gene?q="; // 基因标准词搜索！
var strApiDrSearching = strApiUrlRoot + "chemical?q="; // 化合物标准词搜索！
var strApiIdSearching = strApiUrlRoot + "id?q="; // id搜索：PMID和PMCID都可以！
var strApiCbSearching = strApiUrlRoot + "cited_by?q="; // Cited By：PMID和PMCID都可以！
var strApiSiSearching = strApiUrlRoot + "cited_by?q="; // Similar：PMID和PMCID都可以！
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
//function getCuttentLoginUser(){
//	var thisLoginUser, $ = layui.jquery;
//	$.ajax({
//		type: "get",
//		url: "/getLoginUser",
//		async: false,
//		contentType: "application/json; charset=utf-8",
//		dataType: "json",
//		success: function (res) {
//			if(res.success){
//				thisLoginUser = res.data;
//			}
//		}
//	});
//	return thisLoginUser;
//}
function getCurrentLoginUser(){
	var thisUser, $ = layui.jquery;
	$.ajax({
		type: "get",
		url: "/sysUser/currentLoginUserInfo",
		async: false,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (res) {
			if(res != null && res.account != null){
				thisUser = res;
			}
		}
	});
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
		max: objMax, // 待替换更新！
		
		language: 'en',
		filterable: true,
		tips: 'please select..',
		searchTips: 'Enter key words',
		delay: 0,
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
			if(val.length < 3) return cb([]); // 至少输入3个字符！
			// 根据选项类别检索不同的接口！
			if(itemOptionType == 2){
				cb(funcGetCancerList(val));
			}else if(itemOptionType == 3){
				cb(funcGetDrugList(val));
			}else if(itemOptionType == 4){
				cb(funcGetGeneList(val));
			}else if(itemOptionType == 5){
				cb(funcGetAlterationList(val));
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
			//for(var i = 0; i < res.data.length; i++){
			//	objResult.push({name: res.data[i], value: res.data[i]});
			//}
			objResult = res; // 测试！
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
			//for(var i = 0; i < res.data.length; i++){
			//	objResult.push({name: res.data[i], value: res.data[i]});
			//}
			objResult = res; // 测试！
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
			//for(var i = 0; i < res.data.length; i++){
			//	objResult.push({name: res.data[i], value: res.data[i]});
			//}
			objResult = res; // 测试！
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
			//for(var i = 0; i < res.data.length; i++){
			//	objResult.push({name: res.data[i], value: res.data[i]});
			//}
			objResult = res; // 测试！
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
			type: _.toLower(ann.infons.type),
			pmid: strPmid, // 2021-11-08：新增PMID！
			pmcid: strPmcid ? strPmcid : "" // 2021-11-08：新增PMCID！
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
			hits++;
		},
		element: "m"
	}); // Unescape HTML tags

	let nodeHTML = node.html();
	let fixedHTML = nodeHTML && nodeHTML.replace(/&lt;(\/?[a-z]+)&gt;/g, '<$1>');
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

// 获取期刊名称！
function getJournal(data) {
	return data && data.passages[0].infons.journal_name || data.passages[0].infons.journal || 'Journal Unavailable';
}

// 获取发表年份信息！
function getYear(data) {
	return data && data.passages[0].infons.year || 'unknown';
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
function copyText(text) { // 复制内容到剪贴板的底层功能！
    var textarea = document.createElement("textarea");
    var currentFocus = document.activeElement;
    document.body.appendChild(textarea);
    textarea.value = text;
    textarea.focus();
    if (textarea.setSelectionRange)
        textarea.setSelectionRange(0, textarea.value.length);
    else
        textarea.select();
    try {
        var flag = document.execCommand("copy");
    } catch(eo){
        var flag = false;
    }
    document.body.removeChild(textarea);
    currentFocus.focus();
    return flag;
}
function funcCopyIntoClipboard(othis){ // 复制内容到剪贴板！
	var strToBeCopyText = $(othis).html().replace(strCopyIconHtml, '');
    var flag = copyText(strToBeCopyText);//这个必须在DOM对象的事件线程中执行
    layer.msg(flag ? "'" + strToBeCopyText + "' successfully copied!" : "'" + strToBeCopyText + "' copy failed!");
    if(flag) layer.close(indexEntityClick);
}
function funcSearchThisTerm(othis){ // 检索当前词条！
	layer.close(indexEntityClick); // 关闭实体弹窗！
	var strToBeSearchText = $(othis).html().replace(strSearchIconHtml, '');
	if($("#idLitKeyword").length == 0){
		//console.log("idLitKeyword 不存在！");
		localStorage.setItem('paramKw', strToBeSearchText);
		localStorage.setItem('paramMd', 'local');
		window.open("/lrSearch"); // 新窗口打开页面！
	}else{
		//console.log("idLitKeyword 存在！");
		$("#idLitKeyword").val(strToBeSearchText);
		$("#idSubmit").click(); // 执行检索！
	}
}
function funcEntityClick(){
	$("m").unbind("click", funcEntityClickObj); // 为防止多次调用该方法导致的重复监听，这里需要先去除之前的监听！
	$('m').bind('click', funcEntityClickObj = function (e) {
		//layer.msg('111 type:' + e.target.dataset.type + ';identifier:' + e.target.dataset.identifier)
		//console.log(e);
		
		let thisSymbol = "", thisIdentifier = "", thisType = "", thisOffset = "", thisPmid = "", thisPmcid = "";
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
			title: 'Operate on "' + thisTargetDatasetType + '": "' + e.target.innerText + '":',
			shadeClose: true,
			closeBtn: 0,
			//skin: 'layui-layer-rim', //加上边框
			area: ['420px'], // 宽设置，高度自适应！
			content: 
				'<div class="layui-btn-container divEntityPrompt">' +
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;">' + strCopyIconHtml + e.target.innerText + '</button>' + // 原词条拷贝！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this)">' + strSearchIconHtml + e.target.innerText + '</button>' + // 原词条检索！
				
				'	<br>' +
				(thisSymbol !== 'undefined' ?
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;">' + strCopyIconHtml + thisSymbol + '</button>' + // 标准词条拷贝！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this)">' + strSearchIconHtml + thisSymbol + '</button>' // 标准词条检索！
				: ''
				) +
				
				'	<br>' +
				(thisIdentifier.indexOf('undefined') > -1 || thisIdentifier == '-' || thisIdentifier.indexOf('@-') > -1 ? '' :
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcCopyIntoClipboard(this)" style="margin-left: 0;">' + strCopyIconHtml + thisIdentifier + '</button>' + // 标准词条ID拷贝！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcSearchThisTerm(this)">' + strSearchIconHtml + thisIdentifier + '</button>' // 标准词条ID检索！
				) +
				
				'	<br>' +
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcErrorReport(\'' + thisPmid + '\', \'' + thisPmcid + '\', \'Category\', \'' + thisTargetDatasetType + '\', \'' + thisOffset + '\')" style="margin-left: 0;"><i class="layui-icon layui-icon-release"></i> Report Category Error (\'' + thisTargetDatasetType + '\')</button>' + // 报告分类错误报告！
				'	<button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="funcErrorReport(\'' + thisPmid + '\', \'' + thisPmcid + '\', \'Normalization\', \'' + e.target.innerText + '\', \'' + thisOffset + '\')"><i class="layui-icon layui-icon-release"></i> Report Normalization Error (\'' + e.target.innerText + '\')</button>' + // 标准化错误报告！
				'</div>'
		});
	});
}
function funcErrorReport(strPmid, strPmcid, strErrType, strErrContent, strErrOffset){
	var indexReport = layer.load(3, {shade: [0.01, '#393D49']});
	$.ajax({
		type: "post",
		url: "/errorReport/add",
		dataType: "json",
		async: true,
		data: JSON.stringify({pmid: strPmid, pmcid: strPmcid, errorType: strErrType, errorContent: strErrContent, errorOffset: strErrOffset}),
		contentType: "application/json; charset=utf-8",
		success: function(result){
			layer.close(indexReport); // 关闭loading弹窗！
			layer.msg(result.message); // 给出报告的返回提示信息！
			layer.close(indexEntityClick); // 关闭当前实体弹窗！
		}
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

//搜索框内值变动的情况下，进行搜索！
function initSearch(passages, intMatchingTab){
	let val = intMatchingTab == 0 ? $('#filterText').val() : $('#filterTextFuzzy').val();
	let clear = intMatchingTab == 0 ? $('#summary .filter-controls i') : $('#summaryFuzzy .filter-controls i');
	if (val !== '') {
		clear.show();
	} else {
		clear.hide();
	}
	loadSummaryConcepts(passages, intMatchingTab)
}

//清除检索框中的关键词，恢复未检索状态：即显示全部实体！
function clearFilter() {
	$('#filterText').val('');
	$('#filterText').trigger('change');
}

// 显示/隐藏分组类型列表，以便选择分组类型！
function group_toggle(intMatchingTab) {
	let $group = intMatchingTab == 0 ? $('#groupControlsShow') : $('#groupControlsShowFuzzy');
	if ($group.is(":hidden")) {
		$group.show();
	} else {
		$group.hide();
	}
}

//显示/隐藏排序方式列表，以便选择排序方式！
function sort_toggle(intMatchingTab) {
	let $sort = intMatchingTab == 0 ? $('#sortControlsShow') : $('#sortControlsShowFuzzy');
	if ($sort.is(":hidden")) {
		$sort.show();
	} else {
		$sort.hide();
	}
}

// 设置分组类型！
function setGroup(ele, index, passages, intMatchingTab) {
	let $group = intMatchingTab == 0 ? $('#groupControlsShow') : $('#groupControlsShowFuzzy');
	$group.children('.selected').removeClass('selected')
	$(ele).addClass('selected')
	$group.next('.selected-value').text(groups[index])
	$group.hide()
	loadSummaryConcepts(passages, intMatchingTab)
}

//设置排序方式！
function setSort(ele, index, passages, intMatchingTab) {
	let $sort = intMatchingTab == 0 ? $('#sortControlsShow') : $('#sortControlsShowFuzzy');
	$sort.children('.selected').removeClass('selected')
	$(ele).addClass('selected')
	$sort.next('.selected-value').text(sorts[index].niceField || sorts[index].field)
	$sort.hide()
	loadSummaryConcepts(passages, intMatchingTab)
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
function goToEntities(entityId, sectionName, intMatchingTab) {
	if (!entityId) return;
	let sectionSelector = sectionName ? "[data-section='" + sectionName + "']" : "";
	let thisTabLabel = (intMatchingTab == 0 ? '#idDivTab1' : '#idDivTab2') + " " + sectionSelector;
	//console.log("‘" + thisTabLabel + "’");
	let entityNodes = $(thisTabLabel + " m[data-identifier='" + entityId + "']"); // 1. Scroll to first
	//console.log(entityNodes);
	scrollToNode(entityNodes); // 2. Animate (remove+add class) // 跳转到特定节点：当前概念在文献中第一次出现的地方！
	animate(entityNodes); // 3. Mark all selected nodes
	entityNodes.hasClass('selected') ? entityNodes.removeClass('selected') : entityNodes.addClass('selected');
}

// 点击概念，进行跳转！
function gotoAnn(ele, ann, intMatchingTab) {
	let summaryItem = $(ele); //FIXME Only set as selected when successfully selected nodes (sometimes does not select)
	summaryItem.hasClass('selected') ? summaryItem.removeClass('selected') : summaryItem.addClass('selected');
	//console.log(ann.identifier);
	goToEntities(ann.identifier, ann.section, intMatchingTab);
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
function loadSummaryConcepts(passages, intMatchingTab) {
	let tableView = intMatchingTab == 0 ? $('#summary .annotations-table-view').empty() : $('#summaryFuzzy .annotations-table-view').empty(); // 首先清空该模块内部代码：为此次加载生成的HTML腾空间！
	let $group = intMatchingTab == 0 ? $('#groupControlsShow') : $('#groupControlsShowFuzzy'); // 实体分组类别列表对象！
	let $sort = intMatchingTab == 0 ? $('#sortControlsShow') : $('#sortControlsShowFuzzy');; // 实体排序方式列表对象！
	let group = groups[$group.children('.selected').attr('data-index')]; // 获取当前选中的分组！
	let sort = sorts[$sort.children('.selected').attr('data-index')]; // 获取当前选中的排序方式！
	//console.log("++++++++++");
	//console.log(passages);
	let concepts = getConcepts(extractMentions(passages), group, sort, intMatchingTab == 0 ? $('#filterText').val() : $('#filterTextFuzzy').val()); // 获取全部的概念！
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
					gotoAnn(e.target, ann, intMatchingTab); // 点击概念，进行跳转！
				})
				td.append(a);
				td.append('&nbsp;');
				td.append('<span title="Number of mentions" class="ann-count">(' + ann.count + ')</span>');
				tr.append(td);
				if ($group.next().text() !== 'type') {
					tr.append('<td style="text-align: right;">' + ann.type + '</td>');
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