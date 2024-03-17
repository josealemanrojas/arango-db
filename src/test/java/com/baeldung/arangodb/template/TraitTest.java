package com.baeldung.arangodb.template;

import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.entity.StreamTransactionEntity;
import com.arangodb.model.DocumentCreateOptions;
import com.arangodb.model.DocumentReadOptions;
import com.arangodb.model.StreamTransactionOptions;
import com.arangodb.springframework.core.template.ArangoTemplate;
import com.baeldung.arangodb.model.ArticleEntity;
import com.baeldung.arangodb.model.Identifiable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class TraitTest {

    @Autowired
    ArangoTemplate arangoTemplate;

    @BeforeEach
    void setup() {
        arangoTemplate.collection("article").truncate();
    }


    @Test
    void testTraits() {
        ArangoDatabase db = arangoTemplate.driver().db("baeldung-database");

        StreamTransactionEntity transactionEntity = db
                .beginStreamTransaction(
                        new StreamTransactionOptions().readCollections("article")
                                .writeCollections("article")
                );

        ArticleEntity article = ArticleEntity.builder()
                .name("name")
                .author("Jose Aleman")
                .build();

        DocumentCreateEntity<Identifiable> identifiableDocumentEntity =
                arangoTemplate.insert(article, new DocumentCreateOptions().returnNew(true).streamTransactionId(transactionEntity.getId()));

        log.info(identifiableDocumentEntity.getNew().toString());

        ArticleEntity article1 = arangoTemplate.find(identifiableDocumentEntity.getId(), ArticleEntity.class,
                new DocumentReadOptions().streamTransactionId(transactionEntity.getId())).get();

        assertThat(article1.getName()).isEqualTo(article.getName());

        db.abortStreamTransaction(transactionEntity.getId());

    }

}
