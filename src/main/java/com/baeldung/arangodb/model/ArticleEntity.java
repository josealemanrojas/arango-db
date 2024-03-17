package com.baeldung.arangodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

@Document("article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleEntity implements Identifiable{

    @Id // db document field: _key
    private String key;

    @ArangoId // db document field: _id
    private String id;

    private String name;
    private String author;
}
