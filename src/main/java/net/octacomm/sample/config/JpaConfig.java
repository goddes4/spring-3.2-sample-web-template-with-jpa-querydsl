package net.octacomm.sample.config;

import javax.sql.DataSource;

import net.octacomm.sample.dao.UserRepository;
import net.octacomm.sample.domain.User;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("springDataJpa")
@EnableTransactionManagement
@EnableJpaRepositories(transactionManagerRef = "txManager", basePackageClasses = { UserRepository.class })
@ImportResource("file:resources/hsqlJdbcScriptContext.xml")
public class JpaConfig {

	@Autowired
	private Environment env;
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClass"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
//		dataSource.setValidationQuery("select 1");

		return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.HSQL);
		jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		factoryBean.setDataSource(dataSource());
		factoryBean.setPersistenceUnitName("testPersistenceUnit");
		factoryBean.setPackagesToScan(User.class.getPackage().toString().substring(8));
//		factoryBean.setPersistenceXmlLocation("file:resources/META-INF/spring-persistence.xml"); // persistence.xml 을 등록하게 되면 setPackagesToScan 으로 설정한 값이 초기화 된다.  
		factoryBean.setPersistenceProvider(new HibernatePersistence());
		factoryBean.setJpaDialect(new HibernateJpaDialect());

		return factoryBean;
	}
	
	@Bean
	public PlatformTransactionManager txManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return txManager;
	}

}
