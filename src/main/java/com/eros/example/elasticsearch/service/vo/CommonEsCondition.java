package com.eros.example.elasticsearch.service.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/17 16:48
 * @Version: 1.0
 * @Modified By:
 */
public class CommonEsCondition {

    private String index;

    private String type;
    // 页数
    private Integer page;
    // 每页的 大小
    private Integer size;
    // 排序字段
    private String sort;
    // 查询条件
    private Map<String, String> params = new HashMap<>(16);

    public CommonEsCondition(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
