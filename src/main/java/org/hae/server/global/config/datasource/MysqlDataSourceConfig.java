package org.hae.server.global.config.datasource;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "org.hae.server.domain")
public class MysqlDataSourceConfig {

	// TODO: AOP 먼저 작성 필요 (RoutingContext)
	// public static class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
	// 	@Override
	// 	protected Object determineCurrentLookupKey() {
	// 		return ;
	// 	}
	// }

	// Master DataSource 설정
	@Bean
	@ConfigurationProperties("spring.datasource.master")
	public DataSourceProperties masterDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "masterDataSource")
	public DataSource masterDataSource() {
		return masterDataSourceProperties().initializeDataSourceBuilder().build();
	}

	// Slave DataSource 설정
	@Bean
	@ConfigurationProperties("spring.datasource.slave")
	public DataSourceProperties slaveDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "slaveDataSource")
	public DataSource slaveDataSource() {
		return slaveDataSourceProperties().initializeDataSourceBuilder().build();
	}

	// 라우팅 DataSource
	// @Bean(name = "routingDataSource")
	// public DataSource routingDataSource(
	// 	@Qualifier("masterDataSource") DataSource masterDataSource,
	// 	@Qualifier("slaveDataSource") DataSource salveDataSource
	// ) {
	//
	// }

}
