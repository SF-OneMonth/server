package org.hae.server.domain.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.sensor.dto.request.SensorDataRequest;
import org.hae.server.domain.sensor.model.SensorData;
import org.hae.server.domain.sensor.service.SensorDataProducer;
import org.hae.server.global.common.response.SFaaSResponse;
import org.hae.server.global.common.response.SuccessType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 센서 데이터 관련 REST API를 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {
    private final SensorDataProducer sensorDataProducer;
    private final MongoTemplate mongoTemplate;

    /**
     * 센서 데이터를 저장하는 엔드포인트
     * 
     * @param request 센서 데이터 요청 객체
     * @return 생성 성공 응답
     */
    @PostMapping
    public SFaaSResponse<?> saveSensorData(@Valid @RequestBody SensorDataRequest request) {
        log.info("센서 데이터 저장 요청 수신: {}", request);
        try {
            SensorData sensorData = request.toEntity();
            sensorDataProducer.sendSensorData(request.getEquipmentCode(), sensorData);
            log.info("센서 데이터 저장 성공: equipmentCode={}", request.getEquipmentCode());
            return SFaaSResponse.success(SuccessType.CREATED);
        } catch (Exception e) {
            log.error("센서 데이터 저장 실패: {}", request, e);
            throw e;
        }
    }

    /**
     * 특정 장비의 센서 데이터를 조회하는 엔드포인트
     * 
     * @param equipmentCode 장비 코드
     * @param start         조회 시작 시간
     * @param end           조회 종료 시간
     * @return 센서 데이터 목록
     */
    @GetMapping("/{equipmentCode}")
    public SFaaSResponse<List<SensorData>> getSensorData(
            @PathVariable String equipmentCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        log.info("센서 데이터 조회 요청: equipmentCode={}, start={}, end={}", equipmentCode, start, end);
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("equipment_code").is(equipmentCode)
                    .and("timestamp").gte(start).lte(end));

            List<SensorData> sensorDataList = mongoTemplate.find(query, SensorData.class);
            log.info("센서 데이터 조회 성공: equipmentCode={}, 데이터 수={}", equipmentCode, sensorDataList.size());
            return SFaaSResponse.success(SuccessType.OK, sensorDataList);
        } catch (Exception e) {
            log.error("센서 데이터 조회 실패: equipmentCode={}, start={}, end={}", equipmentCode, start, end, e);
            throw e;
        }
    }
}