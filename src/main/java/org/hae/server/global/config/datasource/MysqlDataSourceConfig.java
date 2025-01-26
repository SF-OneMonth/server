package org.hae.server.global.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "org.hae.server.domain")
public class MysqlDataSourceConfig {

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
	@Bean(name = "routingDataSource")
	public DataSource routingDataSource(
		@Qualifier("masterDataSource") DataSource masterDataSource,
		@Qualifier("slaveDataSource") DataSource slaveDataSource
	) {
		ReplicationRoutingDataSource dataSource = new ReplicationRoutingDataSource();

		// 등록할 DataSource 매핑
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("master", masterDataSource);
		targetDataSources.put("slave", slaveDataSource);

		// 라우팅에 사용할 맵, 디폴트 DS 설정
		dataSource.setTargetDataSources(targetDataSources);
		dataSource.setDefaultTargetDataSource(masterDataSource);

		return dataSource;
	}

}
