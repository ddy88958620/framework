package config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Preconditions;
import com.handu.apollo.config.service.easyzk.ConfigNode;
import com.handu.apollo.data.PaginationInterceptor;
import com.handu.apollo.utils.StringUtil;
import com.handu.apollo.utils.componet.ComponentContext;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by markerking on 14-4-22.
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig {

    @Autowired
    Environment env;
    @Autowired(required = false)
    @Qualifier("applicationConfigNode")
    private ConfigNode applicationConfigNode;
    @Autowired
    private ComponentContext _context;

    private static final String DEFAULT_MAX_ACTIVE = "250";

    @Bean
    public DruidDataSource dataSource() {
        final String url = Preconditions.checkNotNull(StringUtil.getProperty(applicationConfigNode, env, "db.url"), "db.url is null");
        final String username = Preconditions.checkNotNull(StringUtil.getProperty(applicationConfigNode, env, "db.username"), "db.username is null");
        final String password = Preconditions.checkNotNull(StringUtil.getProperty(applicationConfigNode, env, "db.password"), "db.password is null");
        final String maxActive = StringUtil.getProperty(applicationConfigNode, env, "db.maxActive", DEFAULT_MAX_ACTIVE);

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(Integer.parseInt(maxActive));

        return dataSource;
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        Resource[] resources = _context.getApplicationContext().getResources("classpath*:mappers/**/*.xml");
        factoryBean.setMapperLocations(resources);
        factoryBean.setPlugins(new Interceptor[] {paginationInterceptor()});
        return factoryBean.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

}
