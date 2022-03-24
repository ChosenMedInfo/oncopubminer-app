package com.cn.xiaonuo.utils;

import java.util.Map;
import java.util.TreeMap;

public class SettingBase {

	// 用户权限相关的提示信息！
	public static final String strPermissionTipsNeedSignIn    = "Need sign in first!";
	public static final String strPermissionTipsGroupAdmin    = "Only group admin allowed!";
	public static final String strPermissionTipsSealedContais = "Sealed form exist, cannot be deleted!";
	
	// 用户组相关变量！
	public static final String strUserCivic             = "CIViC"; // 2021-11-22（V0004-01.00.01-10）：新增，将CIViC数据独立于common，未来可能整合其他数据源的数据！
	public static final String strUserCommon            = "common";
	public static final String strGroupCommon           = "commonGroup";
	public static final String strGroupSuffix           = "Group";
	public static final String strGroupFormNameSplitter = "-";
	
	// 文件夹名！
	public static final String strDirNameDatasets = "datasets";
	public static final String strDirNameExport   = "export";
	public static final String strDirNameImgs     = "imgs";
	public static final String strDirNameUpload   = "upload";
	
	public static final String strDirNameDatasetsPtc = "PubTatorCentral";
	public static final String strDirPathDatasetsPtc = strDirNameDatasets + "/" + strDirNameDatasetsPtc + "/";
	
	// API URLs！
	public static final String strUrlNcbiEutilitiesBase = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
	public static final String strUrlNcbiEutilitiesPubMedEsearch = strUrlNcbiEutilitiesBase + "esearch.fcgi?db=pubmed&usehistory=y&term=";
	
	public static final String strPubMedEsummaryParamQueryKey = "query_key";
	public static final String strPubMedEsummaryParamWebEnv   = "WebEnv";
	public static final String strUrlNcbiEutilitiesPubMedEsummary = strUrlNcbiEutilitiesBase + "esummary.fcgi?db=pubmed";
	
	public static final String strDataExportCommentsContent1 = "#######################################################################################";
	public static final String strDataExportCommentsContent2 = "# OncoPubMiner - A Platform for Oncology Publication Mining";
	public static final String strDataExportCommentsContent3 = "# -------------------------------------------------------------------------------------";
	
	// 日志记录相关！
	public static final String strEmailReceiver = "medinfoservice@chosenmedtech.com";
	
	public static final Integer intSuccessTrue  = 0;
	public static final Integer intSuccessFalse = 0;
	
	public static final String strOperateTypeRelationDedup = "文库文献关系去重";
	
	public static final String strOperateTypeDownloadCivic = "CIViC下载";
	public static final String strOperateTypeUpdateCivic   = "CIViC更新";
	
	public static final String strOperateTypeExportData    = "数据导出";
	public static final String strOperateTypeAddData       = "数据提交";
	
	public static final String strOperateTypeAddForm       = "表单新增";
	public static final String strOperateTypeCopyForm      = "表单拷贝";
	public static final String strOperateTypeDelForm       = "表单删除";
	public static final String strOperateTypeEditForm      = "表单编辑";
	public static final String strOperateTypeSealForm      = "表单封存";
	
	public static final String strOperateTypeAddFormItem   = "表项新增";
	public static final String strOperateTypeDelFormItem   = "表项删除";
	public static final String strOperateTypeEditFormItem  = "表项编辑";
	
	public static final String strOperateTypeAddProject    = "项目新增";
	public static final String strOperateTypeDelProject    = "项目删除";
	public static final String strOperateTypeEditProject   = "项目编辑";
	public static final String strOperateTypeSealProject   = "项目封存";
	
	public static final String strOperateTypeDonePub       = "文献Done标注";
	
	public static final String strOperateTypeAddLibrary    = "文库新增";
	public static final String strOperateTypeDelLibrary    = "文库删除";
	public static final String strOperateTypeEditLibrary   = "文库编辑";
	public static final String strOperateTypeSealLibrary   = "文库封存";
	
	public static final String strOperateTypeFuncSuggest   = "文献推荐";
	public static final String strOperateTypeFuncComment   = "文献备注";
	
	public static final String strOperateTypeErrorReport   = "错误报告";
	public static final String strOperateTypeFeedback      = "用户反馈";
	
	// CIViC数据相关！
	public static final String strUrlCivicNightlyGenes      = "https://civicdb.org/downloads/nightly/nightly-GeneSummaries.tsv";
	public static final String strUrlCivicNightlyVariants   = "https://civicdb.org/downloads/nightly/nightly-VariantSummaries.tsv";
	public static final String strUrlCivicNightlyEvidence   = "https://civicdb.org/downloads/nightly/nightly-ClinicalEvidenceSummaries.tsv";
	public static final String strUrlCivicNightlyVaGroups   = "https://civicdb.org/downloads/nightly/nightly-VariantGroupSummaries.tsv";
	public static final String strUrlCivicNightlyAssertions = "https://civicdb.org/downloads/nightly/nightly-AssertionSummaries.tsv";
	
	public static final String strDirCivicNightlyDatasets   = "CIViC";
	public static final String strPathCivicNightlyDatasets  = strDirNameDatasets + "/" + strDirCivicNightlyDatasets + "/";
	
	public static final String strFieldNamesCivicV1Evidence = "gene	entrez_id	variant	disease	doid	phenotypes	drugs	drug_interaction_type	evidence_type	evidence_direction	evidence_level	clinical_significance	evidence_statement	citation_id	source_type	asco_abstract_id	citation	nct_ids	rating	evidence_status	evidence_id	variant_id	gene_id	chromosome	start	stop	reference_bases	variant_bases	representative_transcript	chromosome2	start2	stop2	representative_transcript2	ensembl_version	reference_build	variant_summary	variant_origin	last_review_date	evidence_civic_url	variant_civic_url	gene_civic_url	is_flagged";
	public static final String strFieldNamesCivicEvidence   = strFieldNamesCivicV1Evidence;

	public static final Integer intFieldNumCivicEviType     = 8; // 用于根据该字段序号，获取数据，以判断证据类型！
	public static final Integer intFieldNumCivicSourceType  = 14;
	public static final Integer intFieldNumCivicSourceId    = 13;
	public static final String strFieldNumCivicSourceType   = "PubMed"; // 数据来源类型：只获取PubMed来源的数据！
	
	public static final String strFieldNameCivicEvidence01  = "Gene Entrez Name";
	public static final String strFieldNameCivicEvidence02  = "Variant Name";
	public static final String strFieldNameCivicEvidence03  = "Source Type";
	public static final String strFieldNameCivicEvidence04  = "Source ID";
	public static final String strFieldNameCivicEvidence05  = "Variant Origin";
	public static final String strFieldNameCivicEvidence06  = "Evidence Type";
	public static final String strFieldNameCivicEvidence07  = "Clinical Significance";
	public static final String strFieldNameCivicEvidence08  = "Disease";
	public static final String strFieldNameCivicEvidence09  = "Evidence Statement";
	public static final String strFieldNameCivicEvidence10  = "Evidence Level";
	public static final String strFieldNameCivicEvidence11  = "Evidence Direction";
	public static final String strFieldNameCivicEvidence12  = "Drug Names";
	public static final String strFieldNameCivicEvidence13  = "Associated Phenotypes";
	public static final String strFieldNameCivicEvidence14  = "Rating";
	public static final String strFieldNameCivicEvidence15  = "Additional Comments";
	public final static Map<String, Integer> mapCivicFormItemName2ColNumDiagnostic; // 1. Diagnostic！
	static {
		mapCivicFormItemName2ColNumDiagnostic = new TreeMap<String, Integer>();
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence01, 0);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence02, 2);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence03, intFieldNumCivicSourceType);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence04, intFieldNumCivicSourceId);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence05, 36);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence06, intFieldNumCivicEviType);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence07, 11);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence08, 3);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence09, 12);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence10, 10);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence11, 9);
		//mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence12, 6); // Diagnostic相比Predictive少了该字段！
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence13, 5);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence14, 18);
		mapCivicFormItemName2ColNumDiagnostic.put(strFieldNameCivicEvidence15, 38);
	}
	public final static Map<String, Integer> mapCivicFormItemName2ColNumFunctional; // 2. Functional！
	static {
		mapCivicFormItemName2ColNumFunctional = new TreeMap<String, Integer>();
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence01, 0);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence02, 2);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence03, intFieldNumCivicSourceType);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence04, intFieldNumCivicSourceId);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence05, 36);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence06, intFieldNumCivicEviType);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence07, 11);
		//mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence08, 3); // Functional相比Predictive少了该字段！
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence09, 12);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence10, 10);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence11, 9);
		//mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence12, 6); // Functional相比Predictive少了该字段！
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence13, 5);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence14, 18);
		mapCivicFormItemName2ColNumFunctional.put(strFieldNameCivicEvidence15, 38);
	}
	public final static Map<String, Integer> mapCivicFormItemName2ColNumOncogenic; // 3. Oncogenic！
	static {
		mapCivicFormItemName2ColNumOncogenic = new TreeMap<String, Integer>();
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence01, 0);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence02, 2);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence03, intFieldNumCivicSourceType);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence04, intFieldNumCivicSourceId);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence05, 36);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence06, intFieldNumCivicEviType);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence07, 11);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence08, 3);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence09, 12);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence10, 10);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence11, 9);
		//mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence12, 6); // Oncogenic相比Predictive少了该字段！
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence13, 5);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence14, 18);
		mapCivicFormItemName2ColNumOncogenic.put(strFieldNameCivicEvidence15, 38);
	}
	public final static Map<String, Integer> mapCivicFormItemName2ColNumPredictive; // 4. Predictive！
	static {
		mapCivicFormItemName2ColNumPredictive = new TreeMap<String, Integer>();
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence01, 0);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence02, 2);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence03, intFieldNumCivicSourceType);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence04, intFieldNumCivicSourceId);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence05, 36);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence06, intFieldNumCivicEviType);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence07, 11);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence08, 3);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence09, 12);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence10, 10);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence11, 9);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence12, 6); // 这个需要结合“drug_interaction_type”字段（序号7）进行数据拆分！
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence13, 5);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence14, 18);
		mapCivicFormItemName2ColNumPredictive.put(strFieldNameCivicEvidence15, 38); // 备注信息在下载文件中不存在，添加其中的证据ID吧，顺便加上时间信息（序号37）！
	}
	public final static Map<String, Integer> mapCivicFormItemName2ColNumPredisposing; // 5. Predisposing！
	static {
		mapCivicFormItemName2ColNumPredisposing = new TreeMap<String, Integer>();
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence01, 0);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence02, 2);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence03, intFieldNumCivicSourceType);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence04, intFieldNumCivicSourceId);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence05, 36);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence06, intFieldNumCivicEviType);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence07, 11);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence08, 3);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence09, 12);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence10, 10);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence11, 9);
		//mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence12, 6); // Predisposing相比Predictive少了该字段！
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence13, 5);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence14, 18);
		mapCivicFormItemName2ColNumPredisposing.put(strFieldNameCivicEvidence15, 38);
	}
	public final static Map<String, Integer> mapCivicFormItemName2ColNumPrognostic; // 6. Prognostic！
	static {
		mapCivicFormItemName2ColNumPrognostic = new TreeMap<String, Integer>();
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence01, 0);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence02, 2);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence03, intFieldNumCivicSourceType);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence04, intFieldNumCivicSourceId);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence05, 36);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence06, intFieldNumCivicEviType);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence07, 11);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence08, 3);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence09, 12);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence10, 10);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence11, 9);
		//mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence12, 6); // Prognostic相比Predictive少了该字段！
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence13, 5);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence14, 18);
		mapCivicFormItemName2ColNumPrognostic.put(strFieldNameCivicEvidence15, 38);
	}
	public final static String strCivicEviType01 = "Diagnostic";
	public final static String strCivicEviType02 = "Functional";
	public final static String strCivicEviType03 = "Oncogenic";
	public final static String strCivicEviType04 = "Predictive";
	public final static String strCivicEviType05 = "Predisposing";
	public final static String strCivicEviType06 = "Prognostic";
	public final static Map<String, Map<String, Integer>> mapCivicEviType2Map; // “Evidence Type”字段值对应的Map！
	static {
		mapCivicEviType2Map = new TreeMap<String, Map<String, Integer>>();
		mapCivicEviType2Map.put(strCivicEviType01, mapCivicFormItemName2ColNumDiagnostic);
		mapCivicEviType2Map.put(strCivicEviType02, mapCivicFormItemName2ColNumFunctional);
		mapCivicEviType2Map.put(strCivicEviType03, mapCivicFormItemName2ColNumOncogenic);
		mapCivicEviType2Map.put(strCivicEviType04, mapCivicFormItemName2ColNumPredictive);
		mapCivicEviType2Map.put(strCivicEviType05, mapCivicFormItemName2ColNumPredisposing);
		mapCivicEviType2Map.put(strCivicEviType06, mapCivicFormItemName2ColNumPrognostic);
	}
	public final static Map<String, Long> mapCivicEviType2FormId; // “Evidence Type”字段值对应的表单的id！
	static {
		mapCivicEviType2FormId = new TreeMap<String, Long>();
		mapCivicEviType2FormId.put(strCivicEviType01, 1449977717844312066L); // 该Long值需要根据commonGroup的CIViC相应表单的id来修改！
		mapCivicEviType2FormId.put(strCivicEviType02, 1441421274039881730L); // 该Long值需要根据commonGroup的CIViC相应表单的id来修改！
		mapCivicEviType2FormId.put(strCivicEviType03, 1449978051195011074L); // 该Long值需要根据commonGroup的CIViC相应表单的id来修改！
		mapCivicEviType2FormId.put(strCivicEviType04, 1441274726664597505L); // 该Long值需要根据commonGroup的CIViC相应表单的id来修改！
		mapCivicEviType2FormId.put(strCivicEviType05, 1449977908454457346L); // 该Long值需要根据commonGroup的CIViC相应表单的id来修改！
		mapCivicEviType2FormId.put(strCivicEviType06, 1441419097649090561L); // 该Long值需要根据commonGroup的CIViC相应表单的id来修改！
	}
	public final static Map<String, Long> mapCivicEviType2LibraryId; // “Evidence Type”字段值对应的文库的id！
	static {
		mapCivicEviType2LibraryId = new TreeMap<String, Long>();
		mapCivicEviType2LibraryId.put(strCivicEviType01, 1449981515069358081L); // 该Long值需要根据commonGroup的CIViC相应文库的id来修改！
		mapCivicEviType2LibraryId.put(strCivicEviType02, 1449981586158616577L); // 该Long值需要根据commonGroup的CIViC相应文库的id来修改！
		mapCivicEviType2LibraryId.put(strCivicEviType03, 1449981623789912066L); // 该Long值需要根据commonGroup的CIViC相应文库的id来修改！
		mapCivicEviType2LibraryId.put(strCivicEviType04, 1449981668031430658L); // 该Long值需要根据commonGroup的CIViC相应文库的id来修改！
		mapCivicEviType2LibraryId.put(strCivicEviType05, 1449981734209159170L); // 该Long值需要根据commonGroup的CIViC相应文库的id来修改！
		mapCivicEviType2LibraryId.put(strCivicEviType06, 1449981767654539266L); // 该Long值需要根据commonGroup的CIViC相应文库的id来修改！
	}
	public final static Map<String, Long> mapCivicEviType2ProjectId; // “Evidence Type”字段值对应的项目的id！
	static {
		mapCivicEviType2ProjectId = new TreeMap<String, Long>();
		mapCivicEviType2ProjectId.put(strCivicEviType01, 1449982127337078785L); // 该Long值需要根据commonGroup的CIViC相应项目的id来修改！
		mapCivicEviType2ProjectId.put(strCivicEviType02, 1449982219267833857L); // 该Long值需要根据commonGroup的CIViC相应项目的id来修改！
		mapCivicEviType2ProjectId.put(strCivicEviType03, 1449982278252331009L); // 该Long值需要根据commonGroup的CIViC相应项目的id来修改！
		mapCivicEviType2ProjectId.put(strCivicEviType04, 1449982352336322561L); // 该Long值需要根据commonGroup的CIViC相应项目的id来修改！
		mapCivicEviType2ProjectId.put(strCivicEviType05, 1449982406619004929L); // 该Long值需要根据commonGroup的CIViC相应项目的id来修改！
		mapCivicEviType2ProjectId.put(strCivicEviType06, 1449982484821803010L); // 该Long值需要根据commonGroup的CIViC相应项目的id来修改！
	}
	
}
