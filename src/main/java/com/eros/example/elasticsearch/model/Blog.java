package com.eros.example.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/17 18:06
 * @Version: 1.0
 * @Modified By:
 */
@Document(indexName = "blog", type = "blog", shards = 1, replicas = 0)
public class Blog {

    @Id
    @Field(index = false, store = false)
    private String id;

    @Field(index = true, store = true, analyzer = "ik", searchAnalyzer = "ik", type = FieldType.Text)
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
