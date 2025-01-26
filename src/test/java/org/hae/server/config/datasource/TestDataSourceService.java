package org.hae.server.config.datasource;

import org.hae.server.global.config.datasource.RoutingContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataSourceService {

	@Transactional(readOnly = true)
	public boolean readOnlyMethod() {
		return RoutingContext.isReadOnly();
	}

	@Transactional
	public boolean writeMethod() {
		return RoutingContext.isReadOnly();
	}
}
