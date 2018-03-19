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
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author kyou on 2018/2/4 15:58
 */
@Configuration
@MapperScan(basePackages = "cn.ok.mybatisdemo.mapper.mydb", sqlSessionTemplateRef = "MydbSqlSessionTemplate")
public class MydbDataSource {

    /**
     * 指定的 Bean name 跟方法名不能一样
     *
     * @return 数据源对象
     */
    @Bean(name = "MydbDataSource")
    @Autowired
    public DataSource systemDataSource(Environment env) {
        AtomikosDataSourceBean atomikosDataSource = new AtomikosDataSourceBean();
        Properties prop = SmpDataSource.getDataSourceProperties(env, "datasource.mysql.mydb.");
        atomikosDataSource.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        atomikosDataSource.setUniqueResourceName("MydbDB");
        atomikosDataSource.setMinPoolSize(5);
        atomikosDataSource.setMaxPoolSize(20);
        atomikosDataSource.setXaProperties(prop);
        return atomikosDataSource;
    }

    @Bean(name = "MydbSqlSessionFactory")
    public SqlSessionFactory mydbSqlSessionFactory(@Qualifier("MydbDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 下划线(数据库字段) 转 驼峰(业务对象)
        bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean(name = "MydbSqlSessionTemplate")
    public SqlSessionTemplate mydbSqlSessionTemplate(
            @Qualifier("MydbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
