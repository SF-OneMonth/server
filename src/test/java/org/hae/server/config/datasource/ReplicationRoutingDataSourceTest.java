package org.hae.server.config.datasource;

import static org.assertj.core.api.Assertions.*;

import org.hae.server.global.config.datasource.RoutingContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ReplicationRoutingDataSource 단위 테스트
 */
public class ReplicationRoutingDataSourceTest {

	@AfterEach
	void tearDown() {
		RoutingContext.clear();
	}

	@Test
	@DisplayName("readOnly=true, determineCurrentLookupKey() → 'slave' 반환")
	void testDetermineCurrentLookupKey_readOnlyIsTrue() {
		// given
		RoutingContext.setReadOnly(true);

		// when
		ReplicationRoutingDataSourceChildTest dataSource = new ReplicationRoutingDataSourceChildTest();
		Object lookupKey = dataSource.testLookupKey();

		// then
		assertThat(lookupKey).isEqualTo("slave");
	}

	@Test
	@DisplayName("readOnly=false인 경우 determineCurrentLookupKey() → 'master' 반환")
	void testDetermineCurrentLookupKey_readOnlyIsFalse() {
		// given
		RoutingContext.setReadOnly(false);

		// when
		ReplicationRoutingDataSourceChildTest dataSource = new ReplicationRoutingDataSourceChildTest();
		Object lookupKey = dataSource.testLookupKey();

		// then
		assertThat(lookupKey).isEqualTo("master");
	}
}
