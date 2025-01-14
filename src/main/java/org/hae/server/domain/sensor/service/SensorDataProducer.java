package org.hae.server.domain.sensor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.sensor.model.SensorData;
import org.hae.server.global.common.exception.SFaaSException;
import org.hae.server.global.common.response.ErrorType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 센서 데이터를 Kafka 토픽에 발행하는 프로듀서 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "sensor-data";

    /**
     * 센서 데이터를 Kafka 토픽에 발행하는 메서드
     * 
     * @param equipmentCode 장비 코드 (메시지 키로 사용)
     * @param sensorData    발행할 센서 데이터
     * @throws SFaaSException JSON 직렬화 실패 시
     */
    public void sendSensorData(String equipmentCode, SensorData sensorData) {
        try {
            String message = objectMapper.writeValueAsString(sensorData);
            log.debug("센서 데이터 Kafka 전송 시작: equipmentCode={}, data={}", equipmentCode, message);

            kafkaTemplate.send(TOPIC, equipmentCode, message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("센서 데이터 Kafka 전송 완료: equipmentCode={}, topic={}, partition={}, offset={}",
                                    equipmentCode, result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("센서 데이터 Kafka 전송 실패: equipmentCode={}", equipmentCode, ex);
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("센서 데이터 직렬화 실패: {}", sensorData, e);
            throw new SFaaSException(ErrorType.INTERNAL_SERVER);
        }
    }
}