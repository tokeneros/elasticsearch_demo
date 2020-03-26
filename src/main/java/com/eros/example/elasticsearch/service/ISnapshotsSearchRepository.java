package com.eros.example.elasticsearch.service;

import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/26 9:59
 * @Version: 1.0
 * @Modified By:
 */
public interface ISnapshotsSearchRepository {

    /**
     * @Description: 获取当前 所有 仓库信息
     * @Author: eros
     * @Date: 2020/3/26 14:05
     * @param
     * @Return: org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse
     * @Exception:
     */
    GetRepositoriesResponse repositoryStatus();

    /**
     * @Description: 创建 snapshot
     * @Author: eros
     * @Date: 2020/3/26 15:08
     * @param
     * @Return: boolean
     * @Exception:
     */
    boolean putSnapshot();

    void getSnapshot();

    void snapshotsStatus();

    boolean deleteSnapshot();


    boolean restoreSnapshot();
}
