package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.test;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * <p>
 * 代码生成器
 * </p>
 */
public class MapperGenerator {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(System.getProperty("user.dir"));
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setOpen(false);
        gc.setAuthor("gaoyang");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//        gc.setServiceName("%sCommandService");
//        gc.setServiceImplName("%sCommandServiceImpl");
//        gc.setMapperName("%sCommandMapper");
//        gc.setXmlName("%sCommandMapper");

        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("a111111");
        dsc.setUrl("jdbc:mysql://172.16.9.15:3306/gtobusinessdb?useUnicode=true&characterEncoding=utf8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[]{"sg_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        //strategy.setInclude(new String[]{"agt_service_product","agt_service_organization","agt_service_organization_agreement","agt_service_organization_operator_info","agt_service_organization_rule"}); // 需要生成的表
        strategy.setInclude(new String[]{"sg_offer_document","sg_offer_document_file"}); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.ciicsh.gto.fcbusinesscenter.salarygrant");
        //pc.setModuleName("agent.dictionary.command");
        //pc.setModuleName("agent.dictionary.query");

        //pc.setModuleName("entrust.query");
        //pc.setModuleName("entrust.command");

        pc.setModuleName("siteservice");
        //pc.setModuleName("entrust.command");

        pc.setService("business.salarygrant");
        pc.setServiceImpl("business.salarygrant.impl");
        //pc.setController("host.controller");
        pc.setEntity("entity.po");
        pc.setMapper("dao");
        pc.setXml("dao.mapper");
        mpg.setPackageInfo(pc);

        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        // tc.setEntity("...");
//         tc.setMapper(null);
        // tc.setXml("...");
//         tc.setService(null);
//         tc.setServiceImpl(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

    }

}
