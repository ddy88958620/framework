package config;

import com.alibaba.druid.pool.DruidDataSource;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.componet.ComponentContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

/**
 * Created by markerking on 14-4-22.
 */
@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"com.handu.apollo.data", "com.handu.apollo.utils.componet"})
@EnableTransactionManagement
public class TestMybatisConfig {

    private static final Log LOG = Log.getLog(TestMybatisConfig.class);

    @Autowired
    Environment env;
    @Autowired
    private ComponentContext _context;

    @Bean
    public DruidDataSource dataSource() {
        final String url = env.getProperty("db.url");
        final String username = env.getProperty("db.username");
        final String password = env.getProperty("db.password");
        final int maxActive = Integer.parseInt(env.getProperty("db.maxActive"));

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        try {
            Resource[] resources = _context.getApplicationContext().getResources("classpath*:mappers/**/*.xml");
            factoryBean.setMapperLocations(resources);
        } catch (IOException e) {
            LOG.error("加载mybatis的xml出错", e);
        }
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            LOG.error("获取SqlSessionFactory出错", e);
            return null;
        }
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

}
