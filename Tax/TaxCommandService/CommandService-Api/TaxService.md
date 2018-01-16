1.1 查询完税凭证主任务

http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryTaskMainProofByRes
SIT环境: 

请求报文：
{
	"managerName":"蓝天科技",   //管理方名称，模糊匹配
	"submitTimeStart":"2018-01-08", //起始日期
	"submitTimeEnd":"2018-01-10",   //结束日期	
	"currentNum":1, //当前页数	
	"pageSize":5	//分页显示数目
}

返回报文:
{
    "errorcode": "0",
    "errormsg": "success",
    "data": {
        "currentNum": 1,
        "pageSize": 5,
        "totalNum": 2,
        "rowList": [
            {
                "id": 58,   //主键ID
                "taskNo": "TAX20180109085135826",   //任务编号   
                "managerNo": "GL170001",    //管理方编号
                "managerName": "蓝天科技",  //管理方名称
                "headcount": null,  //总人数
                "chineseNum": null, //中方人数
                "foreignerNum": null,   //外方人数
                "status": "04", //任务状态,00:草稿01:已提交/处理中02:被退回03:已完成04:已失效
                "statusName": "已失效",
                "remark": null, //备注
                "createdTime": 1515459049000,   //创建时间
                "modifiedTime": 1515459049000,  //修改时间
                "createdBy": "admin",   //创建人
                "modifiedBy": "yuantq", //修改人
                "active": true  //是否可用
            },
            {
                "id": 57,
                "taskNo": "MAIN201712221314520",
                "managerNo": "GL170001",
                "managerName": "蓝天科技",
                "headcount": 1,
                "chineseNum": 1,
                "foreignerNum": 0,
                "status": "01",
                "statusName": "处理中",
                "remark": null,
                "createdTime": 1515400273000,
                "modifiedTime": 1515400273000,
                "createdBy": "admin",
                "modifiedBy": "adminMain",
                "active": true
            }
        ]
    }
}

1.2 新建完税凭证主任务

http方法: post
api地址:
开发环境: http://localhost:7019/tax/addTaskProof
SIT环境: 

请求报文:
{
	"managerNo": "GL170001",	//管理方编号
	"managerName": "蓝天科技",	   //管理方名称
	"declareAccount": null	    //申报账户(非必填)
}

返回报文:
{
    "errorcode": "0",
    "errormsg": "success",
    "data": true
}

1.3 批量提交完税凭证任务

http方法: post
api地址:
开发环境: http://localhost:7019/tax/updateTaskProof
SIT环境: 

请求报文：
{
	"mainProofIds": [59, 58],	
	"status": "01"	
}

返回报文：
{
    "errorcode": "0",   //批量提交的主任务ID数组
    "errormsg": "success",  //任务状态，00:草稿01:已提交/处理中02:被退回03:已完成04:已失效
    "data": true
}
 
1.3 批量失效完税凭证任务

http方法: post
api地址:
开发环境: http://localhost:7019/tax/invalidTaskProof
SIT环境: 

请求报文：
{
	"mainProofIds": [59, 58]	    //批量提交的主任务ID数组
}
返回报文：
{
    "errorcode": "0",
    "errormsg": "success",
    "data": true
} 


1.4 根据完税凭证主任务ID查询完税凭证子任务
http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryTaskSubProofByMainId/{mainProofId}/
SIT环境: 

请求报文:
{
}

返回报文：
{
    "errorcode": "0",
    "errormsg": "success",
    "data": [
        {
            "id": 133,  //主键ID
            "taskNo": "TAX20180106132044",     //任务编号
            "taskMainProofId": 54,  //完税凭证主任务ID
            "declareAccount": "中智上海财务咨询公司大库",   //申报账户
            "city": null,   //城市
            "taxOrganization": null,    //税务机构
            "headcount": 1, //总人数
            "chineseNum": 1,    //中方人数
            "foreignerNum": 0,  //外方人数
            "sendStatus": null, //寄送状态
            "status": "01", //任务状态,00:草稿01:已提交/处理中02:被退回03:已完成04:已失效
            "statusName": "处理中",
            "createdBy": "adminMain",   //创建人
            "createdTime": 1515215999000    //创建时间
        },
        {
            "id": 134,
            "taskNo": "TAX20180106132044",
            "taskMainProofId": 54,
            "declareAccount": "蓝天科技上海独立户",
            "city": null,
            "taxOrganization": null,
            "headcount": 1,
            "chineseNum": 1,
            "foreignerNum": 0,
            "sendStatus": null,
            "status": "02",
            "statusName": "被退回",
            "createdBy": "adminMain",
            "createdTime": 1515215999000
        }
    ]
}

1.5 根据完税凭证子任务ID复制被驳回的完税凭证子任务信息

http方法: post
api地址:
开发环境: http://localhost:7019/tax/copyProofInfoBySubId/{subProofId}/
SIT环境: 

请求报文:
{}

返回报文:
{
    "errorcode": "0",
    "errormsg": "success",
    "data": true
}

1.6 完税凭证主任务详细信息查询
http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryTaskMainProofByRes
SIT环境: 

请求报文：
{
	"id": "62",     //完税凭证主任务主键ID
	"declareAccount": ""    //申报账户
}

返回报文：
{
    "errorcode": "0",
    "errormsg": "success",
    "data": {
        "currentNum": null,
        "pageSize": null,
        "totalNum": null,
        "rowList": [
            {
                "id": 62,   //主键ID
                "taskNo": "MAIN201712221314520",    //任务编号
                "managerNo": "GL170001",    //管理方编号
                "managerName": "蓝天科技",  //管理方名称
                "headcount": 1, //总人数
                "chineseNum": 3,    //中方人数
                "foreignerNum": 0,  //外方人数
                "status": "00", //任务状态,00:草稿01:已提交/处理中02:被退回03:已完成04:已失效
                "statusName": "草稿",
                "remark": null, //备注
                "createdTime": 1515740010000,   //创建时间
                "modifiedTime": 1515740010000,  //修改时间
                "createdBy": "admin",   //创建人
                "modifiedBy": "adminMain",  //修改人
                "active": true  //是否可用
            }
        ]
    }
}

1.7 完税凭证子任务详细信息查询
http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryTaskSubProofByRes
SIT环境: 

请求报文：
{
	"id": "150", 	//完税凭证子任务主键ID
	"declareAccount": "蓝天科技上海独立户"   //申报账户
}

返回报文:
{
    "errorcode": "0",
    "errormsg": "success",
    "data": {
        "currentNum": null,
        "pageSize": null,
        "totalNum": null,
        "rowList": [
            {
                "id": 150,  //主键ID
                "taskNo": "SUB201712221314520", //任务编号
                "taskMainProofId": 62,  //完税凭证主任务ID
                "declareAccount": "蓝天科技上海独立户",  //申报账户
                "headcount": 1, //总人数
                "chineseNum": 1,    //中方人数
                "foreignerNum": 0,  //外方人数
                "status": "00", //任务状态,00:草稿01:已提交/处理中02:被退回03:已完成04:已失效
                "statusName": "草稿",
                "createdBy": "adminMain",   //创建人
                "createdTime": 1515740010000,   //创建时间
                "modifiedBy": "adminMain",  //修改人
                "modifiedTime": 1515740010000,  //修改时间
                "managerNo": null,  //管理方编号
                "managerName": null,    //管理方名称
                "period": null, //个税期间
                "taskType": "02",   //任务类型(01:自动,02:人工)
                "page": null,
                "combined": false,     //是否为合并任务
                "active": true  //是否可用
            }
        ]
    }
}

1.8 完税凭证详细页面保存数据
http方法: post
api地址:
开发环境: http://localhost:7019/tax/saveSubProofDetail
SIT环境: 


请求报文：
{
	"detailType":"main",	//数据保存类型，main:主任务，sub:子任务
	"oldDeleteIds":[63,63], //删除的明细ID
	"taskId":"150",	//任务ID
	"taskSubProofDetailDTOList":[	//保存或修改的数据
		{
			"active":null,	//是否可用
			"createdBy":null,	//创建人
			"createdTime":1515400159000,	//创建时间
			"declareAccount":"蓝天科技苏州独立户",	//申报账户
			"employeeName":"刘文华",	//雇员名称
			"employeeNo":"17A13513",	//雇员编号
			"id":63,	//申报明细主键ID  (新增的雇员不需要)
			"idNo":"321110197108239090",	//证件号
			"idType":"01",	//证件类型 01；身份证
			"incomeEnd":null,	//所得期间止
			"incomeForTax":null,	//应纳税所得额
			"incomeStart":null,	//所得期间起
			"incomeSubject":null,	//所得项目	01：工资薪金所得
			"modifiedBy":null,	//修改人
			"modifiedTime":null,	//修改时间
			"taskSubProofId":144,	//完税凭证子任务ID
			"withholdedAmount":null	//扣缴税额
		}
	]
}

返回报文:
{
    "errorcode": "0",
    "errormsg": "success",
    "data": true
}

1.9 完税凭证申报明细查询
http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryTaskSubProofDetail
SIT环境: 

请求报文:
{
	"detailType": "main",   //数据保存类型，main:主任务，sub:子任务
	"id": "61", 	   //任务ID
	"employeeNo": "",	    //雇员编号
	"employeeName": ""	    //雇员姓名
}


返回报文:
{
    "errorcode": "0",
    "errormsg": "success",
    "data": {
        "currentNum": null,
        "pageSize": null,
        "totalNum": null,
        "rowList": [
            {
                "id": 66,   //主键ID
                "taskSubProofId": 149,  //完税凭证子任务ID
                "employeeNo": "17A13012",  //雇员编号 
                "employeeName": "李晓",   //雇员姓名
                "idType": "01", //证件类型	01:身份或者能
                "idTypeName": "居民身份证",
                "idNo": "321000199010101234",   //证件号
                "declareAccount": "蓝天科技上海独立户",  //申报账户
                "incomeSubject": "01",  //所得项目	01：工资薪金所得
                "incomeSubjectName": "工资薪金所得",
                "incomeStart": 1509465600000,   //所得期间起
                "incomeEnd": 1512057600000, //所得期间止
                "incomeForTax": 5960,   //应纳税所得额
                "withholdedAmount": 436,    //扣缴税额
                "createdTime": 1515739906000,   //创建时间
                "modifiedTime": null,   //修改时间
                "createdBy": null,  //创建人
                "modifiedBy": null, //修改人
                "active": null  //是否可用
            }
        ]
    }
}


1.10 完税凭证添加雇员列表查询
http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryEmployee
SIT环境: 

请求报文：
{
	"taskId":"62",	    //任务ID
	"declareAccount":"",	   //申报账户
	"detailType":"main",	    //详细页面类型 main：主任务详细页面，sub：子任务详细页面
	"employeeName":"",	    //雇员姓名
	"employeeNo":"",	//雇员编号
	"pageSize":5,	    //每页显示条数
	"currentNum":1	    //当前页数
}

返回报文：
{
    "errorcode": "0",
    "errormsg": "success",
    "data": {
        "currentNum": 1,    //当前页数
        "pageSize": 5,  //每页显示条数
        "totalNum": 5,  //总数目
        "rowList": [
            {
                "id": 1,    //ID
                "employeeNo": "17A13012",   //雇员编号
                "employeeName": "李晓",   //雇员姓名
                "idType": "01", //证件类型 01：身份证
                "idTypeName": "居民身份证",
                "idNo": "321000199010101234",   //证件号
                "managerNo": "GL170001",    //管理方编号  
                "managerName": "蓝天科技",  //管理方名称
                "declareAccount": "蓝天科技上海独立户"   //申报账户
            },
            {
                "id": 2,
                "employeeNo": "17A13497",
                "employeeName": "张名",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "323891199003103290",
                "managerNo": "GL170001",
                "managerName": "蓝天科技",
                "declareAccount": "中智上海财务咨询公司大库"
            },
            {
                "id": 3,
                "employeeNo": "17A13513",
                "employeeName": "刘文华",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "321110197108239090",
                "managerNo": "GL170001",
                "managerName": "蓝天科技",
                "declareAccount": "蓝天科技苏州独立户"
            },
            {
                "id": 3,
                "employeeNo": "17A13542",
                "employeeName": "蒋文文",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "322140199211116510",
                "managerNo": "GL170001",
                "managerName": "蓝天科技",
                "declareAccount": "无锡供应商A大库"
            },
            {
                "id": 3,
                "employeeNo": "17A13101",
                "employeeName": "许强",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "320171198810106787",
                "managerNo": "GL170001",
                "managerName": "蓝天科技",
                "declareAccount": "蓝天科技无锡独立户"
            }
        ]
    }
}

1.11 雇员申报记录查询
http方法: post
api地址:
开发环境: http://localhost:7019/tax/queryTaxBatchDetail
SIT环境: 

请求报文：
{
	"idType": "01", //证件类型 01:身份证
	"idNo": "321000199010101234", //证件号
	"incomeSubject": "01",	//所得项目 01：
	"submitTimeStart": null, //个税期间起,年月+没有1号 如:"2018-01-01"
	"submitTimeEnd": null	//个税期间止
}

返回报文：
{
    "errorcode": "0",
    "errormsg": "success",
    "data": {
        "currentNum": null,
        "pageSize": null,
        "totalNum": null,
        "rowList": [
            {
                "id": 16,   //主键ID
                "calculationBatchId": 4,    //计算批次ID（为空，则为合并数据）
                "versionNo": 1, //版本号（重新计算版本号加1）
                "employeeNo": "YY000001",   //雇员编号
                "employeeName": "陆逊",   //雇员姓名
                "idType": "01",     //证件类型 01：身份证
                "idTypeName": "居民身份证", //证件类型中文名
                "idNo": "321000199010101234",   //证件号
                "declareAccount": "declare000001",  //申报账户
                "payAccount": "pay000001",  //缴纳账户
                "period": 1514649600000,    //个税期间
                "incomeSubjectName": "工资薪金所得",  //所得项目 01：工资薪金所得
                "incomeSubject": "01",  //所得项目中文名
                "incomeTotal": 12000,   //收入额
                "incomeDutyfree": null, //免税所得
                "deductRetirementInsurance": 960,   //基本养老保险费（税前扣除项目）
                "deductMedicalInsurance": 240,  //基本医疗保险费（税前扣除项目）
                "deductDlenessInsurance": 60,   //失业保险费（税前扣除项目）
                "deductProperty": null, //财产原值（税前扣除项目）
                "deductHouseFund": 840, //住房公积金（税前扣除项目）
                "deductTakeoff": null,  //允许扣除的税费（税前扣除项目）
                "deductOther": null,    //其他（税前扣除项目）
                "deductTotal": 2550,    //合计（税前扣除项目）
                "deduction": 3500,  //减除费用
                "donation": null,   //准予扣除的捐赠额  
                "incomeForTax": 5950,   //应纳税所得额
                "taxRate": "20",    //税率
                "quickCalDeduct": 555,  //速算扣除数
                "taxAmount": 635,   //应纳税额
                "taxDeduction": 200,    //减免税额
                "taxWithholdAmount": 435,   //应扣缴税额
                "taxWithholdedAmount": null,    //已扣缴税额
                "taxRemedyOrReturn": 435,   //应补（退）税额
                "createdTime": 1514946488000,   //创建时间
                "modifiedTime": 1514946488000, //修改时间 
                "defer": false, //是否暂缓
                "declare": true,    //是否申报
                "tranfer": true,    //是否划款
                "pay": true //是否缴纳
            },
            {
                "id": 18,
                "calculationBatchId": null,
                "versionNo": null,
                "employeeNo": "17A13012",
                "employeeName": "李晓",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "321000199010101234",
                "declareAccount": null,
                "payAccount": null,
                "period": 1509465600000,
                "incomeSubjectName": "工资薪金所得",
                "incomeSubject": "01",
                "incomeTotal": 1000,
                "incomeDutyfree": 100,
                "deductRetirementInsurance": 10,
                "deductMedicalInsurance": 10,
                "deductDlenessInsurance": 10,
                "deductProperty": 10,
                "deductHouseFund": 10,
                "deductTakeoff": 10,
                "deductOther": 10,
                "deductTotal": 200,
                "deduction": 20,
                "donation": 10,
                "incomeForTax": 10,
                "taxRate": "10",
                "quickCalDeduct": 1,
                "taxAmount": 1,
                "taxDeduction": 1,
                "taxWithholdAmount": 1,
                "taxWithholdedAmount": 1,
                "taxRemedyOrReturn": 1,
                "createdTime": 1513669394000,
                "modifiedTime": 1513669394000,
                "defer": null,
                "declare": true,
                "tranfer": true,
                "pay": true
            },
            {
                "id": 23,
                "calculationBatchId": null,
                "versionNo": null,
                "employeeNo": "17A13012",
                "employeeName": "李晓",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "321000199010101234",
                "declareAccount": null,
                "payAccount": null,
                "period": 1512057600000,
                "incomeSubjectName": "工资薪金所得",
                "incomeSubject": "01",
                "incomeTotal": 1000,
                "incomeDutyfree": 100,
                "deductRetirementInsurance": 10,
                "deductMedicalInsurance": 10,
                "deductDlenessInsurance": 10,
                "deductProperty": 10,
                "deductHouseFund": 10,
                "deductTakeoff": 10,
                "deductOther": 10,
                "deductTotal": 200,
                "deduction": 20,
                "donation": 10,
                "incomeForTax": 10,
                "taxRate": "10",
                "quickCalDeduct": 1,
                "taxAmount": 1,
                "taxDeduction": 1,
                "taxWithholdAmount": 1,
                "taxWithholdedAmount": 1,
                "taxRemedyOrReturn": 1,
                "createdTime": 1513669394000,
                "modifiedTime": 1513669394000,
                "defer": null,
                "declare": true,
                "tranfer": true,
                "pay": true
            },
            {
                "id": 24,
                "calculationBatchId": null,
                "versionNo": null,
                "employeeNo": "17A13012",
                "employeeName": "李晓",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "321000199010101234",
                "declareAccount": null,
                "payAccount": null,
                "period": 1514736000000,
                "incomeSubjectName": "工资薪金所得",
                "incomeSubject": "01",
                "incomeTotal": 1000,
                "incomeDutyfree": 100,
                "deductRetirementInsurance": 10,
                "deductMedicalInsurance": 10,
                "deductDlenessInsurance": 10,
                "deductProperty": 10,
                "deductHouseFund": 10,
                "deductTakeoff": 10,
                "deductOther": 10,
                "deductTotal": 200,
                "deduction": 20,
                "donation": 10,
                "incomeForTax": 10,
                "taxRate": "10",
                "quickCalDeduct": 1,
                "taxAmount": 1,
                "taxDeduction": 1,
                "taxWithholdAmount": 1,
                "taxWithholdedAmount": 1,
                "taxRemedyOrReturn": 1,
                "createdTime": 1513669394000,
                "modifiedTime": 1513669394000,
                "defer": null,
                "declare": true,
                "tranfer": true,
                "pay": true
            },
            {
                "id": 25,
                "calculationBatchId": null,
                "versionNo": null,
                "employeeNo": "17A13012",
                "employeeName": "李晓",
                "idType": "01",
                "idTypeName": "居民身份证",
                "idNo": "321000199010101234",
                "declareAccount": null,
                "payAccount": null,
                "period": 1506787200000,
                "incomeSubjectName": "工资薪金所得",
                "incomeSubject": "01",
                "incomeTotal": 1000,
                "incomeDutyfree": 100,
                "deductRetirementInsurance": 10,
                "deductMedicalInsurance": 10,
                "deductDlenessInsurance": 10,
                "deductProperty": 10,
                "deductHouseFund": 10,
                "deductTakeoff": 10,
                "deductOther": 10,
                "deductTotal": 200,
                "deduction": 20,
                "donation": 10,
                "incomeForTax": 10,
                "taxRate": "10",
                "quickCalDeduct": 1,
                "taxAmount": 1,
                "taxDeduction": 1,
                "taxWithholdAmount": 1,
                "taxWithholdedAmount": 1,
                "taxRemedyOrReturn": 1,
                "createdTime": 1513669394000,
                "modifiedTime": 1513669394000,
                "defer": null,
                "declare": true,
                "tranfer": true,
                "pay": true
            }
        ]
    }
}























 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 