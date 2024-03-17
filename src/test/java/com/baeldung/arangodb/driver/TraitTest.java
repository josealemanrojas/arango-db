package com.baeldung.arangodb.driver;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.model.DocumentCreateOptions;
import com.arangodb.springframework.core.template.ArangoTemplate;
import com.baeldung.arangodb.model.Article;
import com.baeldung.arangodb.model.Identifiable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class TraitTest {

    @Autowired
    ArangoTemplate arangoTemplate;

    ArangoDatabase db;

    ArangoCollection collection;

    Set<String> keys = new LinkedHashSet<>();

    @BeforeEach
    void setup(){
        db = arangoTemplate.driver().db("baeldung-database");
        collection = db.collection("article");
    }

    @AfterEach
    void tearDown(){
        collection.deleteDocuments(keys);
    }


    @Test
    void testTraits(){

        ArangoCollection collection = db.collection("article");

        Article article = Article.builder()
                .name("name")
                .author("Jose Aleman")
                .build();

        DocumentCreateEntity<Identifiable> identifiableDocumentEntity =
                collection.insertDocument(article, new DocumentCreateOptions().returnNew(true), Identifiable.class);

        keys.add(identifiableDocumentEntity.getKey());

        log.info(identifiableDocumentEntity.getNew().toString());

        Article article1 = collection.getDocument(identifiableDocumentEntity.getKey(), Article.class);

        assertThat(article1.getName()).isEqualTo(article.getName());

    }

}
