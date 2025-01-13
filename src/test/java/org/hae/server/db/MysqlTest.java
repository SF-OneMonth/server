package org.hae.server.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-secret.properties")
public class MysqlTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "test_table";

    @BeforeEach
    void setUp() {
        log.info("테스트 테이블 초기화 시작");
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
        jdbcTemplate.execute(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "name VARCHAR(255)," +
                        "age INT," + "email VARCHAR(255))");
        log.info("테스트 테이블 생성 완료: {}", TABLE_NAME);
    }

    @Test
    @DisplayName("MySQL 연결 테스트")
    void connectionTest() {
        log.info("MySQL 연결 테스트 시작");
        // when
        boolean isConnected = false;
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            isConnected = result != null && result == 1;
            log.info("MySQL 연결 성공: result = {}", result);
        } catch (Exception e) {
            log.error("MySQL 연결 실패: ", e);
        }

        // then
        assertThat(isConnected).isTrue();
        log.info("MySQL 연결 테스트 완료");
    }

    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        log.info("INSERT 테스트 시작");
        // given
        String name = "테스트";
        int age = 25;
        String email = "test@example.com";
        log.info("테스트 데이터 준비 - name: {}, age: {}, email: {}", name, age, email);

        // when
        int rowsAffected = jdbcTemplate.update(
                "INSERT INTO " + TABLE_NAME + " (name, age, email) VALUES (?, ?, ?)",
                name, age, email);
        log.info("INSERT 쿼리 실행 결과 - 영향받은 행: {}", rowsAffected);

        // then
        assertThat(rowsAffected).isEqualTo(1);

        var result = jdbcTemplate.queryForMap(
                "SELECT * FROM " + TABLE_NAME + " WHERE name = ?",
                name);
        log.info("INSERT 결과 조회 - 저장된 데이터: {}", result);

        assertThat(result.get("name")).isEqualTo(name);
        assertThat(result.get("age")).isEqualTo(age);
        assertThat(result.get("email")).isEqualTo(email);
        log.info("INSERT 테스트 완료");
    }

    @Test
    @DisplayName("select 테스트")
    void selectTest() {
        log.info("SELECT 테스트 시작");
        // given
        String name = "테스트";
        int age = 25;
        log.info("테스트 데이터 준비 - name: {}, age: {}", name, age);

        jdbcTemplate.update(
                "INSERT INTO " + TABLE_NAME + " (name, age) VALUES (?, ?)",
                name, age);

        // when
        var result = jdbcTemplate.queryForMap(
                "SELECT * FROM " + TABLE_NAME + " WHERE name = ?",
                name);
        log.info("SELECT 쿼리 실행 결과: {}", result);

        // then
        assertThat(result).isNotNull();
        assertThat(result.get("name")).isEqualTo(name);
        assertThat(result.get("age")).isEqualTo(age);
        log.info("SELECT 테스트 완료");
    }

    @Test
    @DisplayName("update 테스트")
    void updateTest() {
        log.info("UPDATE 테스트 시작");
        // given
        String name = "테스트";
        int age = 25;
        log.info("초기 테스트 데이터 준비 - name: {}, age: {}", name, age);

        jdbcTemplate.update(
                "INSERT INTO " + TABLE_NAME + " (name, age) VALUES (?, ?)",
                name, age);

        // when
        int newAge = 30;
        log.info("데이터 수정 - name: {}, newAge: {}", name, newAge);

        int rowsAffected = jdbcTemplate.update(
                "UPDATE " + TABLE_NAME + " SET age = ? WHERE name = ?",
                newAge, name);
        log.info("UPDATE 쿼리 실행 결과 - 영향받은 행: {}", rowsAffected);

        // then
        assertThat(rowsAffected).isEqualTo(1);

        var result = jdbcTemplate.queryForMap(
                "SELECT * FROM " + TABLE_NAME + " WHERE name = ?",
                name);
        log.info("UPDATE 결과 조회: {}", result);

        assertThat(result.get("age")).isEqualTo(newAge);
        log.info("UPDATE 테스트 완료");
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        log.info("DELETE 테스트 시작");
        // given
        String name = "테스트";
        int age = 25;
        log.info("테스트 데이터 준비 - name: {}, age: {}", name, age);

        jdbcTemplate.update(
                "INSERT INTO " + TABLE_NAME + " (name, age) VALUES (?, ?)",
                name, age);

        // when
        log.info("데이터 삭제 실행 - name: {}", name);
        int rowsAffected = jdbcTemplate.update(
                "DELETE FROM " + TABLE_NAME + " WHERE name = ?",
                name);
        log.info("DELETE 쿼리 실행 결과 - 영향받은 행: {}", rowsAffected);

        // then
        assertThat(rowsAffected).isEqualTo(1);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE name = ?",
                Integer.class,
                name);
        log.info("DELETE 후 데이터 확인 - 조회된 레코드 수: {}", count);

        assertThat(count).isZero();
        log.info("DELETE 테스트 완료");
    }
}