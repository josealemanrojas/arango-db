package com.baeldung.arangodb.model;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Rev;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Document("follower")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowerEntity implements Auditable{
    @Id // db document field: _key
    private String key;

    @ArangoId // db document field: _id
    private String id;

    @Rev
    private String rev;

    String fullname;
}
