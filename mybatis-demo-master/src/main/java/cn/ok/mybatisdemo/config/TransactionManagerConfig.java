package cn.ok.mybatisdemo.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Properties;

/**
 * @author kyou on 2018/2/4 15:58
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagerConfig {

    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("userTransaction") UserTransaction userTransaction,
            @Qualifier("atomikosTransactionManager") TransactionManager atomikosTransactionManager) {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }

    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        // 事物管理器
        transactionInterceptor.setTransactionManager(platformTransactionManager);
        Properties transactionAttributes = new Properties();

        // test
        transactionAttributes.setProperty("test*", "PROPAGATION_REQUIRED,-Throwable");

        // 新增
        transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Throwable");
        // 修改

        transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Throwable");
        // 删除
        transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Throwable");
        // 查询
        transactionAttributes.setProperty("select*", "PROPAGATION_REQUIRED,-Throwable,readOnly");

        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;

    }
}
