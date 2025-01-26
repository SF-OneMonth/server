package org.hae.server.config.datasource;

import static org.assertj.core.api.Assertions.*;

import org.hae.server.global.config.datasource.RoutingContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RoutingContext 단위 테스트
 * ThreadLocal 기반 읽기/쓰기 설정 로직 검증
 */
public class RoutingContextTest {

	@AfterEach
	void tearDown() {
		RoutingContext.clear();
	}

	@Test
	@DisplayName("readOnly=true를 세팅 후 isReadOnly() → true 반환")
	void testSetReadOnly_true() {
		// given
		RoutingContext.setReadOnly(true);

		// when
		boolean result = RoutingContext.isReadOnly();

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("readOnly=false를 세팅 후 isReadOnly → false 반환")
	void testSetReadOnly_false() {
		// given
		RoutingContext.setReadOnly(false);

		// when
		boolean result = RoutingContext.isReadOnly();

		// then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("ThreadLocal에 true 세팅 후 clear() → isReadOnly()는 false로 돌아옴")
	void testClear() {
		// given
		RoutingContext.setReadOnly(true);

		// when
		RoutingContext.clear();
		boolean result = RoutingContext.isReadOnly();

		// then
		assertThat(result).isFalse();
	}
}
