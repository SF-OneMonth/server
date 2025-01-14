package org.hae.server.domain.sensor.exception;

public class WebSocketTransmissionException extends RuntimeException {
    public WebSocketTransmissionException(Throwable cause) {
        super("WebSocket 메시지 전송 중 오류가 발생했습니다.", cause);
    }
}