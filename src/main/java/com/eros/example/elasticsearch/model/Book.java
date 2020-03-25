package com.eros.example.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/13 11:01
 * @Version: 1.0
 * @Modified By:
 */
@Document(indexName = "library", type = "bookshelf-1", shards = 1, replicas = 0)
public class Book {

    @Id
    private String id;

    private String name;

    private Integer size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
