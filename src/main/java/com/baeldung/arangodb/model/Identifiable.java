package com.baeldung.arangodb.model;

public interface Identifiable {

    String getName();

    String getKey();

    String getId();

    String getRev();

    String toString();
}
