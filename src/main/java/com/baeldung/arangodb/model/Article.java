package com.baeldung.arangodb.model;

import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends DocumentEntity implements Identifiable {


    private String name;
    private String author;

}
