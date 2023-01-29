package com.example.sensorsdataprocessor.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.sensorsdataprocessor.model.elastic.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchClient elasticsearchClient;

    public void save(Document document) {
        var indexName = document.indexName("001");
        log.debug("Saving new document: {}. For index: {}", document, indexName);
        try {
            elasticsearchClient.index(request -> request
                    .index(indexName)
                    .document(document));
        } catch (IOException e) {
            log.error("Failed to save document: {}. For index: {}. Error: {}", document, indexName, e.getMessage());
            throw new IllegalStateException("Failed to save document", e);
        }
    }

}
