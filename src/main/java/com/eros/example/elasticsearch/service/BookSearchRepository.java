package com.eros.example.elasticsearch.service;

import com.eros.example.elasticsearch.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/13 11:06
 * @Version: 1.0
 * @Modified By:
 */
public interface BookSearchRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findBooksByName(String name);

    Page<Book> findBooksByName(String name, Pageable pageable);

    Book findBooksById(String Id);
}
