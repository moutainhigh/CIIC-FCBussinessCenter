package com.ciicsh.gto.fcsupportcenter.tax.util.mybatis;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
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
 * @author
 * created date 2017/12/05
 */
public class MybatisPlusGeneratorRun {

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        AutoGenerator mpg = handleGenerator();

        // 全局配置
        mpg.setGlobalConfig(handleGlobalConfig());
        // 数据源配置
        mpg.setDataSource(handleDataSource());
        // 策略配置
        mpg.setStrategy(handleStrategy());
        // 包配置
        mpg.setPackageInfo(handlePackageInfo());
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        // mpg.setCfg(handleCfg());
        // 关闭默认 xml 生成，调整生成 至 根目录
        // mpg.setTemplate(handleTemplate());

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        InjectionConfig cfg = mpg.getCfg();
        if (cfg != null && cfg.getMap() != null) {
            System.err.println(cfg.getMap().get("abc"));
        }
    }

    static AutoGenerator handleGenerator() {
        AutoGenerator mpg = new AutoGenerator() {
            @Override
            protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
                List<TableInfo> list = new ArrayList<>();

                super.getAllTableInfoList(config).forEach(tableInfo -> {
                    // 添加指定前缀的表
                    if (tableInfo.getName().toLowerCase().startsWith("ss_")) {
                        // 移除 entity 前缀
                        // tableInfo.setEntityName(this.getStrategy(), tableInfo.getEntityName().substring(2));
                        list.add(tableInfo);
                    }
                });
                return list;
            }
        };
        return mpg;
    }

    static GlobalConfig handleGlobalConfig() {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D://mp");

        // 不需要 ActiveRecord 特性的请改为 false
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(false);
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor("HuangXing");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//         gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        return gc;
    }

    static DataSourceConfig handleDataSource() {
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                String t = fieldType.toLowerCase();
                // jdk8 日期
                switch (t) {
                    case "time":
                        return DbColumnType.LOCAL_TIME;
                    case "year":
                    case "date":
                        return DbColumnType.LOCAL_DATE;
                    case "datetime":
                    case "timestamp":
                        return DbColumnType.LOCAL_DATE_TIME;
                    default:
                        return super.processTypeConvert(fieldType);
                }
            }
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("a111111");
        dsc.setUrl("jdbc:mysql://172.16.9.15:3306/gtobusinessdb?useUnicode=true&amp;characterEncoding=UTF-8");
        return dsc;
    }

    static StrategyConfig handleStrategy() {
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 实现 AutoGenerator 的 getAllTableInfoList 方法更灵活
        // strategy.setTablePrefix(new String[]{"ss_"});// 此处可以修改为您的表前缀
        // strategy.setInclude(new String[] { "user" }); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表

        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        return strategy;
    }

    static PackageConfig handlePackageInfo() {
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.ciicsh.gto.afsupportcenter.socialsecurity");
        pc.setModuleName("soccommandservice");
        pc.setMapper("dao");
        pc.setXml("mapping");
        pc.setController("host.controller");
        pc.setEntity("entity");
        pc.setService("business");
        pc.setServiceImpl("business.impl");
        return pc;
    }

    static InjectionConfig handleCfg() {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>(1);
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        return cfg;
    }

    static TemplateConfig handleTemplate() {
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        return tc;
    }

}
