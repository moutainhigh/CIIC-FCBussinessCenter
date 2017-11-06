# 金钱豹REST API

### 0.获取管理方列表

0.1.1 获取管理方列表

> **GET** /management

*Request payload*
```
{

}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    manamentList: [
      {
        code: '管理方编码',
        name: '管理方名称',
      },
      ...
    ]
  }
}
```
0.2.1 获取客户列表

> **GET** /client

*Request payload*
```
{
  management: '管理方ID'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    clientList: [
      {
        code: '客户编码',
        name: '客户名称',
      },
      ...
    ]
  }
}
```
### 1. 薪资项

1.1.1 获取薪资项列表

> **GET** /payItem

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItemList: [
      {
        code: '薪资项编码',
        name: '薪资项名称',
        itemModel: '薪资项模板',
        client: '所属客户',
        type: '薪资项类型',
        group: '薪资组',
        dataType: '数据类型',
        isActive: '是否启用'
      },
      ...
    ]
  }
}
```

1.1.2 查询薪资项列表

> **POST** /payItem

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id',
  name: '薪资项名称',
  type: '薪资项类型',
  groupId: '薪资组id',
  ...//自定义查询条件
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItemList: [
      {
        code: '薪资项编码',
        name: '薪资项名称',
        itemModel: '薪资项模板',
        client: '所属客户',
        type: '薪资项类型',
        group: '薪资组',
        dataType: '数据类型',
        isActive: '是否启用'
      },
      ...
    ]
  }
}
```
1.2.1 获取薪资项名称列表

> **GET** /payItem/name

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItemList: [
      {
        code: '薪资项编码',
        name: '薪资项名称',
      },
      ...
    ]
  }
}
```
1.3.1 获取薪资项类型列表

> **GET** /payItem/type

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItemList: [
      {
        code: '薪资项编码',
        type: '薪资项类型',
      },
      ...
    ]
  }
}
```
1.4.1 新建薪资项

> **POST** /payItem

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
  modelId: '薪资项模板id',
  name: '薪资项名称',
  nickname: {
    baseName: '基本别称',
    batchName: '计算批次别称',
    ticketName: '工资单别称',
    reportName: '报表别称'
  },
  type: '薪资项类型',
  isModel: '是否绑定薪资项模板',
  group: '薪资组',
  dataType: '数据类型',
  defaultValue: '默认值',
  note: '备注',
  precision: '计算精度',
  decimal: '小数处理方式',
  useClx: '是否使用高級公式',
  formula: '公式',
  complxFormula: '复杂公式'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
     code: '薪资项编码'
  }
}
```
1.5.1 获取一个薪资项

> **GET** /payItem/{id}

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItem: {
       managementId: '管理方id',
       clientId: '客户id' ,
       modelId: '薪资项模板id',
       name: '薪资项名称',
       nickname: {
         baseName: '基本别称',
         batchName: '计算批次别称',
         ticketName: '工资单别称',
         reportName: '报表别称'
       },
       type: '薪资项类型',
       isModel: '是否绑定薪资项模板',
       group: '薪资组',
       dataType: '数据类型',
       defaultValue: '默认值',
       note: '备注',
       precision: '计算精度',
       decimal: '小数处理方式',
       useClx: '是否使用高級公式',
       formula: '公式',
       complxFormula: '复杂公式'
     }
  }
}
```
1.6.1 更新一个薪资项

> **PUT** /payItem/{id}

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
  modelId: '薪资项模板id',
  name: '薪资项名称',
  nickname: {
    baseName: '基本别称',
    batchName: '计算批次别称',
    ticketName: '工资单别称',
    reportName: '报表别称'
  },
  type: '薪资项类型',
  isModel: '是否绑定薪资项模板',
  group: '薪资组',
  dataType: '数据类型',
  defaultValue: '默认值',
  note: '备注',
  precision: '计算精度',
  decimal: '小数处理方式',
  useClx: '是否使用高級公式',
  formula: '公式',
  complxFormula: '复杂公式'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
     code: '薪资项编码'
  }
}
```
//默认值接口增加
### 2.薪资组

2.1.1 获取薪资组列表

> **GET** /prGroup

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    salaryGroupList: [
      {
        entityId: "entityID",
        parentId: "父薪资组ID",
        code: "薪资组编码",
        managementId: "管理方ID"
        name: "薪资组名称",
        version: "版本",
        remark: "备注",
        isTemplate: "是否模板",
        isActive: "是否启用",
        dataChangeCreateTime: "创建时间",
        dataChangeLastTime: "更新时间",
        createBy: "创建者",
        modifiedBy: "更新者"
      },
      ...
    ]
  }
}
```
2.1.2 查询薪资组列表

> **POST** /prGroupQuery

*Request payload*
```
{
  managementId: '管理方id',
  name: '薪资组名称',
  ...//自定义查询字段
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    salaryGroupList: [
      {
        entityId: "entityID",
        parentId: "父薪资组ID",
        code: "薪资组编码",
        managementId: "管理方ID"
        name: "薪资组名称",
        version: "版本",
        remark: "备注",
        isTemplate: "是否模板",
        isActive: "是否启用",
        dataChangeCreateTime: "创建时间",
        dataChangeLastTime: "更新时间",
        createBy: "创建者",
        modifiedBy: "更新者"
      },
      ...
    ]
  }
}
```

2.2.1 获取薪资组名称列表

> **GET** /prGroupName

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    salaryGroupNameList: [
      '薪资组名称',
      ...
    ]
  }
}
```
2.3.1 新建一个薪资组

> **POST** /prGroup

*Request payload*
```
{
  parentId: "继承薪资组ID",
  managementId: "管理方ID",
  name: "测试薪资组2",
  remark: "备注",
  isTemplate: "是否模板",
  isActive: "是否启用",
  prItemEntityList: [
    {...},
    ...
    //薪资项列表
  ]
  ,
  createBy: "创建者",
  modifiedBy: "更新者"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
     新建结果
  }
}
```
2.4.1 更新一个薪资组

> **PUT** /prGroup/{ID}

*Request payload*
```
{
  entityId: "entityID",
  parentId: "父薪资组ID",
  code: "薪资组编码",
  managementId: "管理方ID"
  name: "薪资组名称",
  version: "版本",
  remark: "备注",
  isTemplate: "是否模板",
  isActive: "是否启用",
  dataChangeCreateTime: "创建时间",
  dataChangeLastTime: "更新时间",
  createBy: "创建者",
  modifiedBy: "更新者"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
     更新结果
  }
}
```
2.5.1 薪资组导入

> **post** /importSalaryGroup

*Request payload*
```
{
  managementId: '管理方id',
  id: '薪资组编码'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    code: '薪资组编码',
  }
}
```
2.6.1 获取一个薪资组

> **GET** /prGroup/{ID}

*Request payload*
```
{
  managementId: '管理方id',
  id: '薪资组编码'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    entityId: "EntityId",
    parentId: "继承父薪资组ID",
    code: "薪资组编码",
    managementId: "管理方ID",
    name: "薪资组名",
    version: "版本",
    remark: "备注",
    isTemplate: "是否模板",
    isActive: "收否启用",
    prItemEntityList: {
      薪资项列表
    },
    dataChangeCreateTime: "创建时间",
    dataChangeLastTime: "更新时间",
    createdBy: "创建者",
    modifiedBy: "更新者"
  }
}
```
//复制接口
### 3.雇员组

3.1.1 获取雇员组列表

> **GET** /prEmployeeGroup

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    empGroupList: [
      {
        entityId: "entityId",
        managementId: "管理方ID",
        companyId: "客户ID",
        code: "雇员组code",
        name: "雇员组名称",
        remark: "备注",
        isActive: "是否启用",
        createdTime: "创建时间",
        createdBy: "创建者",
        dataChangeLastTime: "更新时间",
        modifiedBy: "更新者"
      },
      ...
    ]
  }
}
```
3.1.2 查询雇员组列表

> **POST** /prEmployeeGroupQuery

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
  ...//自定义查询参数
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    empGroupList: [
      {
        "entityId": "雇员组ID",
        "managementId": "管理方ID",
        "companyId": "客户ID",
        "code": "雇员组编码",
        "name": "雇员组名称",
        "remark": "备注",
        "employeeList": null
        "isActive": "是否启用",
        "dataChangeCreateTime": "创建时间",
        "createdBy": "创建者",
        "dataChangeLastTime": "更新时间",
        "modifiedBy": "更新者"
      },
      ...
    ]
  }
}
```
3.2.1 新建雇员组
> **POST** /prEmployeeGroup

*Request payload*
```
{
  "managementId": "管理方ID",
  "companyId": "客户ID",
  "name": "雇员组名称",
  "remark": "备注",
  "employeeList": [
    {
    	"employeeId": "雇员ID",
    	"departName": "部门名称",
    	"position": "职位名称",
    	"onBoardDate": "入职时间"
    },
    ...//雇员列表
  ]
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    插入条数
  }
}
```
3.3.1 查询雇员列表
> **GET** /prEmployee

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
  department: '部门',
  position: '岗位'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
      empList: [
        {
          code: '雇员编码',
          name: '雇员名称',
          department: '部门',
          position: '岗位',
          entryDate: '入职日期'
        },
        ...
    ],
  }
}
```
3.4.1 更新一个雇员组
> **PUT** /prEmployeeGroup/{ID}

*Request payload*
```
{
  "entityId": "entityId",
  "code": "雇员组Code",
  "managementId": "管理方id",
  "companyId": "客户ID",
  "name": "雇员组名称",
  "remark": "备注",
  "employeeList": [
  	{
  	  "employeeId": "雇员ID",
  	  "departName": "部门",
  	  "position": "职位",
  	  "onBoardDate": "入职日期"
  	},
    ...//雇员列表
  ]
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    更新数量,
  }
}
```
3.5.1 获取一个雇员组
> **GET** /prEmployeeGroup/{ID}

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    empGroup: {
      "entityId": "entityId",
      "managementId": "管理方id",
      "companyId": "客户id",
      "code": "雇员组code",
      "name": "雇员组名称",
      "remark": "备注",
      "employeeList": [
        {
          "employeeId": "雇员id",
          "employeeName": "雇员名",
          "employeeNameEn": "雇员英文名",
          "formerName": "曾用名",
          "gender": "性别",
          "birthday": "生日",
          "certificateType": "证件类型",
          "certificateNumber": "证件号",
          "countryCode": "国家代码",
          "provinceCode": "省市代码",
          "cityCode": "城市代码",
          "isHkmt": "是否港澳台",
          "personalProperty": "户口性质",
          "archiveLocation": "档案所在地",
          "marriage": "婚姻状况",
          "marriageDate": "结婚日期",
          "childrenNumber": "子女数量",
          "departName": "部门名称",
          "position": "职位",
          "onBoardDate": "入职日期"
        },
        ....//雇员列表
      ],
      "isActive": "是否启用",
      "dataChangeCreateTime": "创建时间",
      "createdBy": "创建者",
      "dataChangeLastTime": "更新时间",
      "modifiedBy": "更新者"
    }
  }
}
```
### 4.薪资账套

4.1.1 获取薪资账套列表

> **GET** /prAccountSet

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    salaryAccountList: [
      {
        "entityId": "entityId",
        "managementId": "管理方",
        "code": "薪资账套编码",
        "name": "薪资账套名称",
        "prGroupId": "薪资组ID",
        "prGroupEntity": "雇员组",
        "prEmployeeGroupId": "雇员组ID",
        "prEmployeeGroupEntity": "雇员组",
        "startSalaryDate": "薪资期间开始日期",
        "endSalaryDate": "薪资期间结束日期",
        "taxPeriod": "个税期间",
        "remark": "备注",
        "isActive": "是否启用",
        "dataChangeCreateTime": "创建时间",
        "createdBy": "创建者",
        "dataChangeLastTime": "更新时间",
        "modifiedBy": "更新者"
      },
      ...
    ]
  }
}
```
4.1.2 查询薪资账套列表

> **POST** /prAccountSetQuery

*Request payload*
```
{
  managementId: '管理方id',
  clientId: '客户id' ,
  name: '薪资账套名称',
  salaryGroupId: '薪资组ID',
  ...//自定义字段
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    salaryAccountList: [
      {
        "entityId": "entityId",
        "managementId": "管理方",
        "code": "薪资账套编码",
        "name": "薪资账套名称",
        "prGroupId": "薪资组ID",
        "prGroupEntity": "雇员组",
        "prEmployeeGroupId": "雇员组ID",
        "prEmployeeGroupEntity": "雇员组",
        "startSalaryDate": "薪资期间开始日期",
        "endSalaryDate": "薪资期间结束日期",
        "taxPeriod": "个税期间",
        "remark": "备注",
        "isActive": "是否启用",
        "dataChangeCreateTime": "创建时间",
        "createdBy": "创建者",
        "dataChangeLastTime": "更新时间",
        "modifiedBy": "更新者"
      },
      ...
    ]
  }
}
```
4.2.1 获取一个薪资账套

> **GET** /prAccountSet/{ID}

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    salaryAccount:
      {
        "entityId": "entityId",
        "managementId": "管理方",
        "code": "薪资账套编码",
        "name": "薪资账套名称",
        "prGroupId": "薪资组ID",
        "prGroupEntity": "雇员组",
        "prEmployeeGroupId": "雇员组ID",
        "prEmployeeGroupEntity": "雇员组",
        "startSalaryDate": "薪资期间开始日期",
        "endSalaryDate": "薪资期间结束日期",
        "taxPeriod": "个税期间",
        "remark": "备注",
        "isActive": "是否启用",
        "dataChangeCreateTime": "创建时间",
        "createdBy": "创建者",
        "dataChangeLastTime": "更新时间",
        "modifiedBy": "更新者"
      }
  }
}
```
4.3.1 新建一个薪资账套

> **POST** /prAccountSet

*Request payload*
```
{
  "managementId": "管理方",
  "name": "薪资账套名称",
  "prGroupId": "薪资组ID",
  "prEmployeeGroupId": "雇员组ID",
  "startSalaryDate": "薪资期间开始日期",
  "endSalaryDate": "薪资期间结束日期",
  "taxPeriod": "个税期间",
  "remark": "备注"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    更新结果
  }
}
```
4.4.1 更新一个薪资账套

> **PUT** /prAccountSet/{ID}

*Request payload*
```
{
  "entityId": "entityId",
  "managementId": "管理方",
  "name": "薪资账套名称",
  "prGroupId": "薪资组ID",
  "prEmployeeGroupId": "雇员组ID",
  "startSalaryDate": "薪资期间开始日期",
  "endSalaryDate": "薪资期间结束日期",
  "taxPeriod": "个税期间",
  "remark": "备注",
  "isActive": "是否启用"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    更新结果
  }
}
```
4.5.1 获取薪资账套名称列表

> **GET** /prAccountSetName

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    [
      '薪资账套名称'
      ...
    ]
  }
}
```
### 5.薪资项模板

5.1.1 获取薪资项模板列表

> **GET** /payItemModel

*Request payload*
```
{
  managementId: '管理方id',
  pageNum: '分页页码'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItemModelList: [
      {
        entityId: "EntityID",
        managementId: "管理方ID",
        code: "薪资项模板编码",
        name: "薪资项模板名称",
        type: "薪资项模板类型",
        dataType: "数据类型",
        ifComplex: "是否使用高级公式",
        condition: "条件",
        resolvedCondition: "解析后的公式",
        formula: "公式",
        remark: "备注",
        isActive: "是否启用",
        createdTime: "创建时间",
        createdBy: "创建者",
        dataChangeLastTime: "更新时间",
        modifiedBy: "更新者"
      },
      ...
    ]
  }
}
```
5.1.2 查询薪资项模板列表

> **POST** /prItemTemplateQuery

*Request payload*
```
{
  managementId: '管理方id',
  name: '薪资项模板名称',
  dataType: '数据类型',
  ...//自定义字段
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    payItemModelList: [
      {
        entityId: "EntityID",
        managementId: "管理方ID"
        code: "薪资项模板编码",
        name: "薪资项模板名称",
        type: "薪资项模板类型",
        dataType: "数据类型",
        ifComplex: "是否使用高级公式",
        condition: "条件",
        resolvedCondition: "解析后的公式",
        formula: "公式",
        remark: "备注",
        isActive: "是否启用",
        createdTime: "创建时间",
        createdBy: "创建者",
        dataChangeLastTime: "更新时间",
        modifiedBy: "更新者"
      },
      ...
    ]
  }
}
```
5.2.1 获取一个薪资项模板

> **GET** /payItemModel/{ID}

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
     entityId: "EntityID",
     managementId: "管理方ID",
     code: "薪资项模板编码",
     name: "薪资项模板名称",
     type: "薪资项模板类型",
     dataType: "数据类型",
     ifComplex: "是否使用高级公式",
     condition: "条件",
     resolvedCondition: "解析后的公式",
     formula: "公式",
     remark: "备注",
     isActive: "是否启用",
     createdTime: "创建时间",
     createdBy: "创建者",
     dataChangeLastTime: "更新时间",
     modifiedBy: "更新者"
  }
}
```
5.3.1 新建一个薪资项模板

> **POST** /payItemModel

*Request payload*
```
{
  managementId: '管理方id',
  name: '薪资项模板名称',
  type: '薪资项模板类型',
  dataType: '数据类型',
  ifComplex: '是否使用复杂公式',
  condition: '条件',
  formula: '公式',
  remark: '备注',
  isActive: '是否启用'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    code: '薪资项模板编码',
  }
}
```
5.4.1 更新一个薪资项模板

> **PUT** /payItemModel/{ID}

*Request payload*
```
{
  managementId: "管理方id",
  code: "薪资项模板编码",
  name: "薪资项模板名称",
  type: "薪资项类型",
  dataType: "数据类型",
  ifComplex: "是否使用复杂公式",
  condition: "条件",
  formula: "公式",
  remark: "备注",
  isActive: "是否启用"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    code: '薪资项模板编码',
  }
}
```
5.5.1 获取薪资项模板名称列表

> **GET** /payItemModelName

*Request payload*
```
{
  managementId: '管理方id'
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    name: '薪资项模板名称'
    ...
  }
}
```

### 6.薪资组模板

6.1.1 获取薪资组模板列表

> **GET** /prGroupTemplate

*Request payload*
```
{
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    {
      "entityId": "entityId",
      "code": "薪资组模板编码",
      "name": "薪资组模板名称",
      "version": "版本",
      "remark": "备注",
      "isActive": "是否有效",
      "dataChangeCreateTime": "创建时间",
      "createdBy": "创建者",
      "dataChangeLastTime": "更新时间",
      "modifiedBy": "更新者"
    },
    ...
  }
}
```

6.1.2 查询薪资组模板列表

> **POST** /prGroupTemplateQuery

*Request payload*
```
{
  "code": "薪资组模板编码",
  "name": "薪资组模板名称",
  "version": "版本",
  "remark": "备注",
  ...//自定义查询条件
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    {
      "entityId": "entityId",
      "code": "薪资组模板编码",
      "name": "薪资组模板名称",
      "version": "版本",
      "remark": "备注",
      "isActive": "是否有效",
      "dataChangeCreateTime": "创建时间",
      "createdBy": "创建者",
      "dataChangeLastTime": "更新时间",
      "modifiedBy": "更新者"
    },
    ...
  }
}
```

6.2.1 获取一个薪资组模板

> **POST** /prGroupTemplate/{ID}

*Request payload*
```
{
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    "entityId": "entityId",
    "code": "薪资组模板编码",
    "name": "薪资组模板名称",
    "version": "版本",
    "remark": "备注",
    "prItemEntityList": [
      {...},
      ...//薪资项列表
    ],
    "isActive": "是否有效",
    "dataChangeCreateTime": "创建时间",
    "createdBy": "创建者",
    "dataChangeLastTime": "更新时间",
    "modifiedBy": "更新者"
  }
}
```

6.3.1 更新一个薪资组模板

> **POST** /prGroupTemplate/{ID}

*Request payload*
```
{
    "name": "薪资组模板名称",
    "version": "版本",
    "remark": "备注",
    "prItemEntityList": [
      {...},
      ...//薪资项列表
    ],
    "isActive": "是否有效"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    更新结果
  }
}
```

6.4.1 新建一个薪资组模板

> **POST** /prGroupTemplate

*Request payload*
```
{
  "name": "薪资组模板名称",
  "remark": "备注"
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    新建结果
  }
}
```

6.5.1 获取薪资组模板名称列表

> **GET** /prGroupTemplateName

*Request payload*
```
{
}
```
*Response payload*
```
{
  response_at: Time.new,
  responseStatus: 200,
  responseErrorMessage: null,
  data: {
    "薪资组名称",
    ...
  }
}
```