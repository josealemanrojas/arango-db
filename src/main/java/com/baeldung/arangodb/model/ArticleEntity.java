package com.baeldung.arangodb.model;

import com.arangodb.springframework.annotation.Relations;
import com.arangodb.springframework.annotation.Rev;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

import java.util.Collection;

@Document("article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleEntity implements Auditable {

    @Id // db document field: _key
    private String key;

    @ArangoId // db document field: _id
    private String id;

    @Rev
    private String rev;

    private String name;
    private String author;

    @Relations(edges = {RelationCL.class, RelationF.class})
    private Collection<Auditable> relations;
}
