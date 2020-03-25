package com.eros.example.elasticsearch.service;

import com.eros.example.elasticsearch.service.vo.CommonEsCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.util.Collection;
import java.util.List;

/**
 * @Author: eros
 * @Description: es 通用的 服务层
 * @Date: Created in 2020/3/17 15:53
 * @Version: 1.0
 * @Modified By:
 */
public interface ICommonSearchRepository {

    /**
     * @Description: 保存数据
     * @Author: eros
     * @Date: 2020/3/17 17:29
     * @param entity 数据实体
     * @param condition index、type等封装
     * @Return: S
     * @Exception:
     */
    <S> String save(S entity, CommonEsCondition condition);

    /**
     * @Description: 列表查询 返回指定类型
     * @Author: eros
     * @Date: 2020/3/18 9:17
     * @param entity 指定返回的类型
     * @param condition 查询条件 index、type等封装
     * @Return: java.util.List<S>
     * @Exception:
     */
    <T> AggregatedPage<T> list(Class<T> entity, CommonEsCondition condition);

    /**
     * @Description: 删除数据
     * @Author: eros
     * @Date: 2020/3/17 18:49
     * @param id es数据id
     * @param condition index、type等封装
     * @Return: java.lang.String
     * @Exception:
     */
    String delete(String id, CommonEsCondition condition);

    /**
     * @Description: 批量删除数据
     * @Author: eros
     * @Date: 2020/3/17 18:49
     * @param ids es 数据id集合
     * @param condition
     * @Return: void
     * @Exception:
     */
    void deleteBatch(String[] ids, CommonEsCondition condition);

    /**
     * @Description: 备份数据
     * @Author: eros
     * @Date: 2020/3/25 14:43
     * @param conditions 条件集合
     * @Return: void
     * @Exception:
     */
    void backup(List<CommonEsCondition> conditions);
}
