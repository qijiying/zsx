package com.zsx.fwmp.web.configuration;


import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.plugins.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.toolkit.PluginUtils;


/**
  * 
  * @ClassName: MybatisPlusConfig 
  * @Description: MybatisPlusConfig
  * @author xiayy 
  * @date 2018年1月11日 上午11:57:49 
  *
 */
@Configuration
@MapperScan("com.zsx.dao.*")
public class MybatisPlusConfig {

	private static Logger logger =LoggerFactory.getLogger(MybatisPlusConfig.class);
	
    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
    	logger.info("==================================");
		logger.info("=                                =");
		logger.info("=   mybaits plus生效：                               =");
		logger.info("=                                =");
		logger.info("==================================");
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement ms = PluginUtils.getMappedStatement(metaObject);
                // 过滤自定义查询此时无租户信息约束【 麻花藤 】出现
                if ("com.baomidou.springboot.mapper.UserMapper.selectListBySQL".equals(ms.getId())) {
                    return true;
                }
                return false;
            }
        });
        return paginationInterceptor;
    }
}
