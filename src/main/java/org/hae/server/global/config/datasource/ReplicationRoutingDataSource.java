package org.hae.server.global.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return RoutingContext.isReadOnly() ? "slave" : "master";
	}
}
