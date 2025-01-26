package org.hae.server.global.config.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Transactional에서 readOnly = true인 경우 slave,
 * 아니면 master를 쓰도록 설정하는 AOP
 */
@Aspect
@Component
public class MyDataSourceAspect {

	@Before("@annotation(tx)")
	public void beforeTransaction(JoinPoint jp, Transactional tx) {
		boolean isReadOnly = tx.readOnly();
		RoutingContext.setReadOnly(isReadOnly);
	}

	@After("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void afterTransaction(JoinPoint jp) {
		RoutingContext.clear();
	}

}
