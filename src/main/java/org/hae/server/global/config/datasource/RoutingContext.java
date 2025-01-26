package org.hae.server.global.config.datasource;

/**
 * 어느 DB로 연결할지 결정하기 위한 ThreadLocal 관리 클래스
 */
public class RoutingContext {

	private static final ThreadLocal<Boolean> IS_READ_ONLY = new ThreadLocal<>();

	/**
	 * 현재 스레드에서 "읽기" 쿼리를 실행하는지 여부를 세팅
	 * @param readOnly true면 SELECT(salve), false면 쓰기(master)
	 */
	public static void setReadOnly(boolean readOnly) {
		IS_READ_ONLY.set(readOnly);
	}

	/**
	 * @return true면 읽기(slave), false면(master)
	 */
	public static boolean isReadOnly() {
		Boolean readOnly = IS_READ_ONLY.get();
		return readOnly != null && readOnly;
	}

	/**
	 * 스레드 로컬 초기화
	 */
	public static void clear() {
		IS_READ_ONLY.remove();
	}
}
