package com.eros.example.elasticsearch.service;

import com.eros.example.elasticsearch.model.LogStash;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/16 17:57
 * @Version: 1.0
 * @Modified By:
 */
public interface LogStashSearchRepository extends ElasticsearchRepository<LogStash, String> {
}
