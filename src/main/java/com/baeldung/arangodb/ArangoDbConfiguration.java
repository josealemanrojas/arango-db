package com.baeldung.arangodb;

import com.arangodb.ArangoDB;
import com.arangodb.entity.LoadBalancingStrategy;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.arangodb.springframework.core.ArangoOperations;
import com.arangodb.springframework.core.template.ArangoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = {"com.baeldung"})
public class ArangoDbConfiguration implements ArangoConfiguration {

    @Override
    public ArangoDB.Builder arango() {

        ArangoDB.Builder arango = new ArangoDB.Builder()
                .host("localhost", 7001)
                .host("localhost", 7002)
                //.acquireHostList(true)
                .loadBalancingStrategy(LoadBalancingStrategy.ONE_RANDOM);
        return arango;
    }

    @Override
    public String database() {
        return "baeldung-database";
    }

    @Bean
    ArangoTemplate arangoTemplate(ArangoOperations arangoTemplate) {
        return new ArangoTemplate(arangoTemplate.driver(), "baeldung-database",
                arangoTemplate.getConverter(), arangoTemplate.getResolverFactory());
    }
}
