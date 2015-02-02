package com.handu.apollo.schedule.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.handu.apollo.schedule.AutowiringSpringBeanJobFactory;
import com.handu.apollo.utils.componet.ComponentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;

/**
 * Created by markerking on 14-7-21.
 */
@Configuration
public class JobConfig {

    @Autowired
    private DruidDataSource dataSource;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private ComponentContext _context;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new AutowiringSpringBeanJobFactory();
    }

    @Bean
    public SchedulerFactoryBean quartzScheduler() throws IOException {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        Resource resource = _context.getApplicationContext().getResource("classpath:quartz.properties");
        factoryBean.setConfigLocation(resource);
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(transactionManager);
        factoryBean.setOverwriteExistingJobs(true);
        factoryBean.setAutoStartup(false);
        factoryBean.setApplicationContext(_context.getApplicationContext());
        factoryBean.setJobFactory(springBeanJobFactory());

        return factoryBean;
    }
}
