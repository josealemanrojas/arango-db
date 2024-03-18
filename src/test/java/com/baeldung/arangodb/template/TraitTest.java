package com.baeldung.arangodb.template;

import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.entity.StreamTransactionEntity;
import com.arangodb.model.DocumentCreateOptions;
import com.arangodb.model.DocumentReadOptions;
import com.arangodb.model.OverwriteMode;
import com.arangodb.model.StreamTransactionOptions;
import com.arangodb.springframework.core.template.ArangoTemplate;
import com.baeldung.arangodb.model.ArticleEntity;
import com.baeldung.arangodb.model.Auditable;
import com.baeldung.arangodb.model.ChangeLogEntity;
import com.baeldung.arangodb.model.FollowerEntity;
import com.baeldung.arangodb.model.Relation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class TraitTest {

    @Autowired
    ArangoTemplate arangoTemplate;

    @BeforeEach
    void setup() {
       arangoTemplate.collection("article").truncate();
       arangoTemplate.collection("change_log").truncate();
       arangoTemplate.collection("follower").truncate();
    }

    @Test
    void testEdgesNoTransactions() {
        try{
            ArticleEntity article = ArticleEntity.builder()
                    .name("name")
                    .author("Jose Aleman")
                    .build();

            ChangeLogEntity changeLogEntity = ChangeLogEntity.builder()
                    .createdDate(LocalDateTime.now())
                    .note("First note")
                    .build();

            FollowerEntity followerEntity = FollowerEntity.builder()
                    .fullname("Johan Aleman")
                    .build();


            DocumentCreateEntity<ArticleEntity> auditableDocumentEntity =
                    arangoTemplate.insert(article, new DocumentCreateOptions().returnNew(true));

            DocumentCreateEntity<ChangeLogEntity> auditableDocumentEntity1 =
                    arangoTemplate.insert(changeLogEntity, new DocumentCreateOptions().returnNew(true));

            DocumentCreateEntity<FollowerEntity> auditableDocumentEntity2 =
                    arangoTemplate.insert(followerEntity, new DocumentCreateOptions().returnNew(true));

            Relation relation = Relation.builder()
                    .articleEntity(auditableDocumentEntity.getNew())
                    .auditable(auditableDocumentEntity1.getNew())
                    .build();

            Relation relation1 = Relation.builder()
                    .articleEntity(auditableDocumentEntity.getNew())
                    .auditable(auditableDocumentEntity2.getNew())
                    .build();

           // arangoTemplate.insert(relation1, new DocumentCreateOptions().overwriteMode(OverwriteMode.replace));

            arangoTemplate.insert(relation, new DocumentCreateOptions().overwriteMode(OverwriteMode.replace));


            ArticleEntity article1 = arangoTemplate.find(auditableDocumentEntity.getId(), ArticleEntity.class,
                    new DocumentReadOptions()).get();

            assertThat(article1.getName()).isEqualTo(article.getName());


        }
        catch (Exception e) {
           log.error(e.toString());
        }

    }
    @Test
    void testEdges() {
        ArangoDatabase db = arangoTemplate.driver().db("baeldung-database");

        StreamTransactionEntity transactionEntity = db
                .beginStreamTransaction(
                        new StreamTransactionOptions().readCollections("article", "change_log", "follower")
                                .writeCollections("article", "change_log", "relation", "follower")
                );

        try{
            ArticleEntity article = ArticleEntity.builder()
                    .name("name")
                    .author("Jose Aleman")
                    .build();

            ChangeLogEntity changeLogEntity = ChangeLogEntity.builder()
                    .createdDate(LocalDateTime.now())
                    .note("First note")
                    .build();

            FollowerEntity followerEntity = FollowerEntity.builder()
                    .fullname("Johan Aleman")
                    .build();


            DocumentCreateEntity<ArticleEntity> auditableDocumentEntity =
                    arangoTemplate.insert(article, new DocumentCreateOptions().returnNew(true));

            DocumentCreateEntity<ChangeLogEntity> auditableDocumentEntity1 =
                    arangoTemplate.insert(changeLogEntity, new DocumentCreateOptions().returnNew(true));

            DocumentCreateEntity<FollowerEntity> auditableDocumentEntity2 =
                    arangoTemplate.insert(followerEntity, new DocumentCreateOptions().returnNew(true));

            Relation relation = Relation.builder()
                    .articleEntity(auditableDocumentEntity.getNew())
                    .auditable(auditableDocumentEntity1.getNew())
                    .build();

            Relation relation1 = Relation.builder()
                    .articleEntity(auditableDocumentEntity.getNew())
                    .auditable(auditableDocumentEntity2.getNew())
                    .build();

            arangoTemplate.insert(relation1, new DocumentCreateOptions().overwriteMode(OverwriteMode.replace));

            arangoTemplate.insert(relation, new DocumentCreateOptions().overwriteMode(OverwriteMode.replace));
            

            ArticleEntity article1 = arangoTemplate.find(auditableDocumentEntity.getId(), ArticleEntity.class,
                    new DocumentReadOptions()).get();

            assertThat(article1.getName()).isEqualTo(article.getName());
            db.commitStreamTransaction(transactionEntity.getId());

        }
        catch (Exception e) {
            db.abortStreamTransaction(transactionEntity.getId());
        }

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

        DocumentCreateEntity<Auditable> auditableDocumentEntity =
                arangoTemplate.insert(article, new DocumentCreateOptions().returnNew(true));

        log.info(auditableDocumentEntity.getNew().toString());

        ArticleEntity article1 = arangoTemplate.find(auditableDocumentEntity.getId(), ArticleEntity.class,
                new DocumentReadOptions()).get();

        assertThat(article1.getName()).isEqualTo(article.getName());

        db.abortStreamTransaction(transactionEntity.getId());

    }

}
