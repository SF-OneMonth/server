package org.hae.server.db;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-secret.properties")
public class MongoDbTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "test_collection";

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }

    @Test
    @DisplayName("MongoDB 연결 테스트")
    void connectionTest() {
        // when
        boolean isConnected = false;
        try {
            Document pingCommand = new Document("ping", 1);
            Document result = mongoTemplate.getDb().runCommand(pingCommand);
            isConnected = result.getDouble("ok") == 1.0;
        } catch (Exception e) {
            log.error("MongoDB 연결 실패: ", e);
        }

        // then
        assertThat(isConnected).isTrue();
    }

    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        // given
        Document document = new Document()
                .append("name", "테스트")
                .append("age", 25)
                .append("email", "test@example.com");

        // when
        Document savedDoc = mongoTemplate.insert(document, COLLECTION_NAME);

        // then
        assertThat(savedDoc).isNotNull();
        assertThat(savedDoc.get("_id")).isNotNull();
        assertThat(savedDoc.getString("name")).isEqualTo("테스트");
        assertThat(savedDoc.getInteger("age")).isEqualTo(25);
    }

    @Test
    @DisplayName("find 테스트")
    void findTest() {
        // given
        Document document = new Document()
                .append("name", "테스트")
                .append("age", 25);
        mongoTemplate.insert(document, COLLECTION_NAME);

        // when
        Query query = Query.query(Criteria.where("name").is("테스트"));
        List<Document> results = mongoTemplate.find(query, Document.class, COLLECTION_NAME);

        // then
        assertThat(results).isNotEmpty();
        Document foundDoc = results.get(0);
        assertThat(foundDoc).isNotNull();
        assertThat(foundDoc.getString("name")).isEqualTo("테스트");
        assertThat(foundDoc.getInteger("age")).isEqualTo(25);
    }

    @Test
    @DisplayName("update 테스트")
    void updateTest() {
        // given
        Document document = new Document()
                .append("name", "테스트")
                .append("age", 25);
        mongoTemplate.insert(document, COLLECTION_NAME);

        // when
        Query query = Query.query(Criteria.where("name").is("테스트"));
        Update update = Update.update("age", 30);
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);

        // then
        Document updatedDoc = mongoTemplate.findOne(query, Document.class, COLLECTION_NAME);
        assertThat(updatedDoc).as("Updated document should not be null").isNotNull();
        assertThat(updatedDoc.getInteger("age")).as("Age should be updated to 30").isEqualTo(30);
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        // given
        Document document = new Document()
                .append("name", "테스트")
                .append("age", 25);
        mongoTemplate.insert(document, COLLECTION_NAME);

        // when
        Query query = Query.query(Criteria.where("name").is("테스트"));
        mongoTemplate.remove(query, COLLECTION_NAME);

        // then
        Document deletedDoc = mongoTemplate.findOne(query, Document.class, COLLECTION_NAME);
        assertThat(deletedDoc).as("Document should be deleted").isNull();
    }

}