package com.baeldung.arangodb.model;

import com.arangodb.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends DocumentEntity implements Identifiable {

    private String rev;
    private String name;
    private String author;

}
