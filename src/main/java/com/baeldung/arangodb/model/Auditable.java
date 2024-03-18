package com.baeldung.arangodb.model;

public interface Auditable {

    String getRev();

    String getKey();

    String getId();

    String toString();
}
