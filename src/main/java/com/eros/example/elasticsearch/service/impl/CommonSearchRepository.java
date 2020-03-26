package com.eros.example.elasticsearch.service.impl;

import com.eros.example.elasticsearch.service.ICommonSearchRepository;
import com.eros.example.elasticsearch.service.ISnapshotsSearchRepository;
import com.eros.example.elasticsearch.service.vo.CommonEsCondition;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotsStatusRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/17 15:53
 * @Version: 1.0
 * @Modified By:
 */
@Service
public class CommonSearchRepository implements ICommonSearchRepository {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ISnapshotsSearchRepository snapshotsSearchRepository;

    /**
     * @Description: 判断index 是否存在
     * @Author: eros
     * @Date: 2020/3/18 12:15
     * @param condition
     * @Return: boolean
     * @Exception:
     */
    private boolean conditionExists(CommonEsCondition condition){
        return elasticsearchTemplate.indexExists(condition.getIndex());
    }

    /**
     * @Description: 判断 index 是否存在，不存在则创建
     * @Author: eros
     * @Date: 2020/3/18 12:16
     * @param condition
     * @Return: boolean
     * @Exception:
     */
    private boolean indexExists(CommonEsCondition condition){
        if(!this.conditionExists(condition)){
            /*
            1. 索引刷新间隔调整，默认为1s，数据写之后1s就可以被搜索到，每次refresh操作后，
            都会生成一个新的lucene段，这回导致频繁的segment merge行为
            2. 事务日志操作间隔调整
            3. segment merge
             */
            Settings settings = Settings.builder()
                    .put("index.refresh_interval", "30s")
                    .put("index.translog.durability", "async") //异步刷新
                    .put("index.translog.sync_interval", "60s") //间隔60s异步刷新（设置后无法更改）
                    .put("index.translog.flush_threshold_size", "1gb") //内容容量到达1gb异步刷新
                    .put("index.merge.scheduler.max_thread_count", Math.max(1,Math.min(4,Runtime.getRuntime().availableProcessors()/2)))
                    .build();
            return elasticsearchTemplate.createIndex(condition.getIndex(), settings);
        }
        return false;
    }

    /**
     * @Description: 封装参数 - 最简单的term 识别
     * @Author: eros
     * @Date: 2020/3/18 11:21
     * @param params
     * @Return: org.elasticsearch.index.query.BoolQueryBuilder
     * @Exception:
     */
    private BoolQueryBuilder prepareParam(Map<String, String> params){
        BoolQueryBuilder queryBuilder = null;
        if(!params.isEmpty()){
            queryBuilder = QueryBuilders.boolQuery();
            for(Map.Entry<String, String> entry : params.entrySet()){
                queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
        }
        return queryBuilder;
    }

    @Override
    public <S> String save(S entity, CommonEsCondition condition) {
        indexExists(condition);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withIndexName(condition.getIndex())
                .withType(condition.getType())
                .withObject(entity)
                .build();
        return elasticsearchTemplate.index(indexQuery);
    }

    @Override
    public <T> AggregatedPage<T> list(Class<T> entity, CommonEsCondition condition) {
        AggregatedPage<T> result = new AggregatedPageImpl<T>(new ArrayList(16));
        if(this.conditionExists(condition)){
            // 查询条件封装
            BoolQueryBuilder queryBuilder = prepareParam(condition.getParams());
            // 搜索条件封装
            NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                .withIndices(condition.getIndex()) // 索引名称
                .withTypes(condition.getType()) // 类型名
                .withPageable(PageRequest.of(condition.getPage(),condition.getSize())); //从哪页开始查（从0开始），每页几个结果
            if(Objects.nonNull(condition.getSort())){ //排序
                searchQuery.withSort(SortBuilders.fieldSort(condition.getSort()));
            }
            if(Objects.nonNull(queryBuilder)){ //查询条件
                searchQuery.withQuery(queryBuilder);
            }
            result = elasticsearchTemplate.queryForPage(searchQuery.build(),entity);
        }
        return result;
    }


    @Override
    public String delete(String id, CommonEsCondition condition) {
        return elasticsearchTemplate.delete(condition.getIndex(), condition.getType(), id);
    }

    @Override
    public void deleteBatch(String[] ids, CommonEsCondition condition) {
        DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setIndex(condition.getIndex());
        deleteQuery.setType(condition.getType());
        // todo 这里只写了根据id 集合来进行删除
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds(ids);
        deleteQuery.setQuery(queryBuilder);
        elasticsearchTemplate.delete(deleteQuery);
    }

    @Override
    public void backup(List<CommonEsCondition> conditions) {
//        String[] snapshotName = new String[]{};
        SnapshotsStatusRequest snapshotsStatusRequest = new SnapshotsStatusRequest();
        snapshotsStatusRequest.repository("my_backup");
//        snapshotsStatusRequest.
        elasticsearchTemplate.getClient().admin().cluster().snapshotsStatus(snapshotsStatusRequest);
        snapshotsSearchRepository.snapshotsStatus();
    }

    @Override
    public boolean deleteIndex(CommonEsCondition condition) {
        return elasticsearchTemplate.deleteIndex(condition.getIndex());
    }

    @Override
    public boolean closeIndex(CommonEsCondition condition) {
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(condition.getIndex());
        closeIndexRequest.timeout(TimeValue.timeValueMinutes(2));
        closeIndexRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        closeIndexRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        AcknowledgedResponse acknowledgedResponse = elasticsearchTemplate.getClient().admin().indices().close(closeIndexRequest).actionGet();
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public boolean openIndex(CommonEsCondition condition) {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(condition.getIndex());
        openIndexRequest.timeout(TimeValue.timeValueMinutes(2));
        openIndexRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        openIndexRequest.indicesOptions(IndicesOptions.strictExpandOpen());
        AcknowledgedResponse acknowledgedResponse = elasticsearchTemplate.getClient().admin().indices().open(openIndexRequest).actionGet();
        return acknowledgedResponse.isAcknowledged();
    }

}
