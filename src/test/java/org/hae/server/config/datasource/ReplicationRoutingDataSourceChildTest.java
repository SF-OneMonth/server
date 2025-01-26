package org.hae.server.config.datasource;

import org.hae.server.global.config.datasource.ReplicationRoutingDataSource;

public class ReplicationRoutingDataSourceChildTest extends ReplicationRoutingDataSource {

	public Object testLookupKey() {
		return this.determineCurrentLookupKey();
	}
}
