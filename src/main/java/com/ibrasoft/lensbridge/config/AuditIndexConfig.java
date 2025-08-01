package com.ibrasoft.lensbridge.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class AuditIndexConfig {

    private final MongoTemplate mongoTemplate;

    public AuditIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createAuditIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("audit_events");

        // Index on timestamp for chronological queries
        indexOps.createIndex(new Index().on("timestamp", Sort.Direction.DESC));

        // Index on adminId for admin-specific queries
        indexOps.createIndex(new Index().on("adminId", Sort.Direction.ASC));

        // Index on entityType and entityId for entity-specific queries
        indexOps.createIndex(new Index().on("entityType", Sort.Direction.ASC).on("entityId", Sort.Direction.ASC));

        // Index on action for action-specific queries
        indexOps.createIndex(new Index().on("action", Sort.Direction.ASC));

        // Index on result for failed operation queries
        indexOps.createIndex(new Index().on("result", Sort.Direction.ASC));

        // Compound index for date range queries with admin filter
        indexOps.createIndex(new Index().on("timestamp", Sort.Direction.DESC).on("adminId", Sort.Direction.ASC));
    }
}
