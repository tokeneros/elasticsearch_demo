package com.eros.example.elasticsearch;

import com.eros.example.elasticsearch.model.Blog;
import com.eros.example.elasticsearch.model.Book;
import com.eros.example.elasticsearch.model.Event;
import com.eros.example.elasticsearch.model.LogStash;
import com.eros.example.elasticsearch.service.BookSearchRepository;
import com.eros.example.elasticsearch.service.EventSearchRepository;
import com.eros.example.elasticsearch.service.ICommonSearchRepository;
import com.eros.example.elasticsearch.service.LogStashSearchRepository;
import com.eros.example.elasticsearch.service.vo.CommonEsCondition;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/13 11:39
 * @Version: 1.0
 * @Modified By:
 */
@Component
public class Executer  implements CommandLineRunner {

    @Autowired
    private BookSearchRepository bookSearchRepository;

    @Autowired
    private LogStashSearchRepository logStashSearchRepository;

    @Autowired
    private EventSearchRepository eventSearchRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ICommonSearchRepository commonSearchRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("执行命令......");
        // 备份
        List<CommonEsCondition> conditions = new ArrayList<CommonEsCondition>();
        CommonEsCondition commonEsCondition = new CommonEsCondition("logstash-2020.03.24","fluentd");
        conditions.add(commonEsCondition);
        commonEsCondition = new CommonEsCondition("logstash-2020.03.24","fluentd");
        conditions.add(commonEsCondition);
        commonSearchRepository.backup(conditions);

        // 批量删除
//        CommonEsCondition commonEsCondition = new CommonEsCondition("blog_test","blog_test");
//        String[] ids = {"gh8y63ABImHkdb8yWl1a", "hB8z63ABImHkdb8y810n"};
//        commonSearchRepository.deleteBatch(ids, commonEsCondition);

        // 单个删除 gB_553ABImHkdb8yIl06，gh8y63ABImHkdb8yWl1a，hB8z63ABImHkdb8y810n，gx8z63ABImHkdb8y810C
//        CommonEsCondition commonEsCondition = new CommonEsCondition("blog_test","blog_test");
//        System.out.println(commonSearchRepository.delete("gR8i6HABImHkdb8y_10H", commonEsCondition));

        // 列表查询
//        CommonEsCondition commonEsCondition = new CommonEsCondition("blog_test","blog_test");
//        commonEsCondition.setPage(0);
//        commonEsCondition.setSize(20);
//        AggregatedPage<Blog> list = commonSearchRepository.list(Blog.class, commonEsCondition);
//        list.forEach(item -> {
//            System.out.println(item);
//        });

        // 保存数据
//        Blog blog = new Blog();
//        blog.setContent("sssssssssssssssssssssssssssssssssssssssssss");
//        for(int i = 0 ; i < 5 ; i++){
//            Blog blog2 = new Blog();
//            BeanUtils.copyProperties(blog,blog2);
//            commonSearchRepository.save(blog2,new CommonEsCondition("blog_test","blog_test"));
//        }
//        Book book = new Book();
//        book.setName("我的现在");
//        book.setSize(20);
//        bookSearchRepository.save(book);
//        List<Book> list = new ArrayList<>();
//        for(int i = 0 ; i < 10000 ; i++ ){
//            list.add(book);
//        }
//        bookSearchRepository.saveAll(list);
//        bookSearchRepository.findBooksByName("我的现在", PageRequest.of(1001,10));
//        List<Book> books = bookSearchRepository.findBooksByName("我的现在");
//        books.forEach(item -> {
//            System.out.println(item);
//        });
//        LogStash logStash = new LogStash("instance");
//        logStashSearchRepository.save(logStash);
//        Page<LogStash> logStashes = logStashSearchRepository.findAll(PageRequest.of(1,10));
//        logStashes.forEach(item -> {
//            System.out.println(item);
//        });
//        Page<Event> events = eventSearchRepository.findAll(PageRequest.of(1,10));
//        events.forEach(item -> {
//            System.out.println(item);
//        });

        // search after 未实现
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withIndices("logstash-2020.03.16") // 索引名称
//                .withTypes("fluentd") // 类型名
////                .withQuery(QueryBuilders.termQuery("priority", "err")) // 查询条件
//                .withPageable(PageRequest.of(1000,10)) //从0页开始查，每页10个结果
//                .withFields("hostname","message") // 指定返回字段
//                .withSort(SortBuilders.fieldSort("_uid"))
//                .build();
//        Page<LogStash> logStashes = elasticsearchTemplate.queryForPage(searchQuery, LogStash.class);
//        logStashes.forEach(item -> {
//            System.out.println(item);
//        });

        // 轮训
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withIndices("logstash-2020.03.16") // 索引名称
//                .withTypes("fluentd") // 类型名
////                .withQuery(QueryBuilders.termQuery("priority", "err")) // 查询条件
//                .withPageable(PageRequest.of(0,10)) //从0页开始查，每页10个结果
//                .withFields("hostname","message") // 指定返回字段
//                .build();
//        Page<LogStash> logStashes = elasticsearchTemplate.queryForPage(searchQuery, LogStash.class);
//        logStashes.forEach(item -> {
//            System.out.println(item);
//        });
//        SearchResultMapper searchResultMapper = new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
//                List<LogStash> result = new ArrayList<>();
//                for(SearchHit hit : searchResponse.getHits()){
//                    if (searchResponse.getHits().getHits().length <= 0) {
//                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, pageable, searchResponse.getHits().getTotalHits(), searchResponse.getScrollId());
//                    }
//                    //可以做更复杂的映射逻辑
//                    Object userIdObj = hit.getSourceAsMap().get("hostname");
//                }if (result.isEmpty()) {
//                    return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, pageable, searchResponse.getHits().getTotalHits(), searchResponse.getScrollId());
//                }
//                return new AggregatedPageImpl<T>((List<T>) result, pageable, searchResponse.getHits().getTotalHits(), searchResponse.getScrollId());
//            }
//
//            @Override
//            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
//                return null;
//            }
//        };
//        ScrolledPage<LogStash> logStashes = elasticsearchTemplate.startScroll(10000, searchQuery, LogStash.class);
//        System.out.println("查询总数：" + logStashes.getTotalElements());
//        while (logStashes.hasNext()){
//            for(LogStash logStash : logStashes.getContent()){
//                System.out.println(logStash);
//            }
//            // 取下一页
//            logStashes = elasticsearchTemplate.continueScroll(logStashes.getScrollId(), 10000, LogStash.class);
//        }

        System.out.println("执行结束......");
    }

}
