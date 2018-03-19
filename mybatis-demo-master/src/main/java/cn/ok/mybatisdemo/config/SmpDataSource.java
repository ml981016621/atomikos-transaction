package cn.ok.mybatisdemo.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 通过 basePackages 属性不同的数据源扫描不同包下的 Mapper.
 *
 * @author kyou on 2018/2/4 15:58
 */
@Configuration
@MapperScan(basePackages = "cn.ok.mybatisdemo.mapper.smp", sqlSessionTemplateRef = "SmpSqlSessionTemplate")
public class SmpDataSource {

    public static Properties getDataSourceProperties(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driverClassName", ""));
        prop.put("initialSize", env.getProperty(prefix + "initialSize", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "maxActive", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "minIdle", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "maxWait", Integer.class));
        prop.put("timeBetweenEvictionRunsMillis",
                env.getProperty(prefix + "timeBetweenEvictionRunsMillis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "minEvictableIdleTimeMillis", Integer.class));

        prop.put("validationQuery", env.getProperty(prefix + "validationQuery"));
        prop.put("validationQueryTimeout", env.getProperty(prefix + "validationQueryTimeout", Integer.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle", Boolean.class));
        prop.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "testOnReturn", Boolean.class));

        prop.put("poolPreparedStatements", env.getProperty(prefix + "poolPreparedStatements", Boolean.class));
        prop.put("maxPoolPreparedStatementPerConnectionSize",
                env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));
        prop.put("connectionProperties", env.getProperty(prefix + "connectionProperties"));

        return prop;
    }

    /**
     * 指定的 Bean name 跟方法名不能一样
     *
     * @return 数据源对象
     */
    @Bean(name = "SmpDataSource")
    @Primary
    @Autowired
    public DataSource systemDataSource(Environment env) {
        AtomikosDataSourceBean atomikosDataSource = new AtomikosDataSourceBean();
        Properties prop = getDataSourceProperties(env, "datasource.mysql.smp.");
        atomikosDataSource.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        atomikosDataSource.setUniqueResourceName("SmpDB");
        atomikosDataSource.setMinPoolSize(5);
        atomikosDataSource.setMaxPoolSize(20);
        atomikosDataSource.setXaProperties(prop);
        return atomikosDataSource;
    }

    @Bean(name = "SmpSqlSessionFactory")
    @Primary
    public SqlSessionFactory smpSqlSessionFactory(@Qualifier("SmpDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 下划线(数据库字段) 转 驼峰(业务对象)
        bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean(name = "SmpSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate smpSqlSessionTemplate(
            @Qualifier("SmpSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
