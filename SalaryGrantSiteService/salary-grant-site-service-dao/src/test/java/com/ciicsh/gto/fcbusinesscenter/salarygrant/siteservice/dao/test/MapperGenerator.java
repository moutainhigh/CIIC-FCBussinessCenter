package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.test;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String dirRootPath = System.getProperty("user.dir");//根目录，默认为中心的根目录如：薪资发放
        String srcRootPath = "/src/main/java";
        String resourcePah = "/src/main/resources";
        String modulePath = "/com/ciicsh/gto/fcbusinesscenter/salarygrant/siteservice/";
        gc.setOutputDir(dirRootPath + srcRootPath);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setOpen(false);
        gc.setAuthor("chenpb");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
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
        strategy.setTablePrefix(new String[]{""});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[]{"hm_af_pe_task"}); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        strategy.setLogicDeleteFieldName("is_active");
        strategy.setDbColumnUnderline(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.ciicsh.gto.fcbusinesscenter");
        pc.setModuleName("SalaryGrantSiteService");
        pc.setController("host.controller");
        pc.setService("business");
        pc.setServiceImpl("business.impl");
        pc.setEntity("entity.po");
        pc.setMapper("dao");
        pc.setXml("dao.mapper");
        mpg.setPackageInfo(pc);

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
//        // service
//        focList.add(new FileOutConfig("/templates/service.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return dirRootPath + "/ApiService/ApiService-Business/" + srcRootPath + modulePath + "/business/projectservice/" + tableInfo.getEntityName() + "Service.java";
//            }
//        });
//        // serviceImpl
//        focList.add(new FileOutConfig("/templates/serviceImpl.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return dirRootPath + "/ApiService/ApiService-Business/" + srcRootPath + modulePath + "/business/projectservice/impl/" + tableInfo.getEntityName() + "ServiceImpl.java";
//            }
//        });
//
//        //dao
//        focList.add(new FileOutConfig("/templates/mapper.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return dirRootPath + "/ApiService/ApiService-Dao/" + srcRootPath + modulePath + "/dao/" + tableInfo.getEntityName() + "Mapper.java";
//            }
//        });
        //model
//        focList.add(new FileOutConfig("/templates/entity.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return dirRootPath + "/ApiService/ApiService-Entity/" + srcRootPath + modulePath + "/entity/po/" + tableInfo.getEntityName() + ".java";
//            }
//        });
        //xml
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dirRootPath + "/ApiService/ApiService-Dao/" + resourcePah + "/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setEntity(null);
        tc.setMapper(null);
        tc.setXml(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

    }

}
