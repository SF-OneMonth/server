package org.hae.server.domain.sensor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hae.server.domain.sensor.dto.request.SensorDataRequest;
import org.hae.server.domain.sensor.model.SensorData;
import org.hae.server.domain.sensor.service.SensorDataProducer;
import org.hae.server.global.common.response.SFaaSResponse;
import org.hae.server.global.common.response.SuccessType;
import org.hae.server.global.common.response.ErrorType;
import org.hae.server.global.common.exception.SFaaSException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataProducer sensorDataProducer;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public SFaaSResponse<?> saveSensorData(@Valid @RequestBody SensorDataRequest request) {
        try {
            String message = objectMapper.writeValueAsString(request.toEntity());
            sensorDataProducer.sendSensorData(request.getEquipmentCode(), message);
            return SFaaSResponse.success(SuccessType.CREATED);
        } catch (JsonProcessingException e) {
            throw new SFaaSException(ErrorType.INTERNAL_SERVER);
        }
    }

    @GetMapping("/{equipmentCode}")
    public SFaaSResponse<List<SensorData>> getSensorData(
            @PathVariable String equipmentCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Query query = new Query();
        query.addCriteria(Criteria.where("equipmentCode").is(equipmentCode)
                .and("timestamp").gte(start).lte(end));
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));

        return SFaaSResponse.success(SuccessType.OK, mongoTemplate.find(query, SensorData.class));
    }

    @GetMapping("/{equipmentCode}/latest")
    public SFaaSResponse<List<SensorData>> getLatestSensorData(@PathVariable String equipmentCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("equipmentCode").is(equipmentCode));
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));
        query.limit(10);

        return SFaaSResponse.success(SuccessType.OK, mongoTemplate.find(query, SensorData.class));
    }
}