package com.eros.example.elasticsearch.service.impl;

import com.eros.example.elasticsearch.service.ISnapshotsSearchRepository;
import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsRequest;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotStatus;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotsStatusRequest;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotsStatusResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.repositories.fs.FsRepository;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.snapshots.RestoreInfo;
import org.elasticsearch.snapshots.SnapshotInfo;
import org.elasticsearch.snapshots.SnapshotShardFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/26 10:00
 * @Version: 1.0
 * @Modified By:
 */
@Service
public class SnapshotsSearchRepository implements ISnapshotsSearchRepository {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private static String repository = "my_backup";

    private static String snapshotName = "backup_snapshot";

    private static String index = "logstash-2020.03.24";

    private static String type = "fluentd";



    /**
     * @Description: 插入 es Repository
     * @Author: eros
     * @Date: 2020/3/26 14:23
     * @param
     * @Return: void
     * @Exception:
     * @return
     */
    private boolean putRepositories(String repository){
        PutRepositoryRequest putRepositoryRequest = new PutRepositoryRequest();
        // 相关配置 配置repository
        String locationKey = FsRepository.LOCATION_SETTING.getKey();
        String locationValue = ".";
        String compressKey = FsRepository.COMPRESS_SETTING.getKey();
        boolean compressValue = true;
        Settings settings = Settings.builder()
                .put(locationKey, locationValue)
                .put(compressKey, compressValue)
                .build();
        putRepositoryRequest.settings(settings);
        // 配置 repository 的 名称 和 type
        putRepositoryRequest.name(repository);
        putRepositoryRequest.type(FsRepository.TYPE);
        // 所有节点 对于 配置settings应答 的超时时间
        putRepositoryRequest.timeout(TimeValue.timeValueMinutes(1));
        // 主节点节点 对于 连接 的超时时间
        putRepositoryRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        // 创建之后对其进行核查
        putRepositoryRequest.verify(true);
        AcknowledgedResponse acknowledgedResponse = elasticsearchTemplate.getClient().admin().cluster().putRepository(putRepositoryRequest).actionGet();
        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * @Description: 删除 es repository
     * @Author: eros
     * @Date: 2020/3/26 14:49
     * @param repository
     * @Return: boolean
     * @Exception:
     */
    private boolean deleteRepository(String repository){
        DeleteRepositoryRequest deleteRepositoryRequest = new DeleteRepositoryRequest(repository);
        // 所有节点 对于 配置settings应答 的超时时间
        deleteRepositoryRequest.timeout(TimeValue.timeValueMinutes(1));
        // z主节点节点 对于 连接 的超时时间
        deleteRepositoryRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        AcknowledgedResponse acknowledgedResponse = elasticsearchTemplate.getClient().admin().cluster().deleteRepository(deleteRepositoryRequest).actionGet();
        return acknowledgedResponse.isAcknowledged();
    }

    // 验证式的，暂时用不上
    private void verifyRepository(String repository){
        VerifyRepositoryRequest verifyRepositoryRequest = new VerifyRepositoryRequest(repository);
        verifyRepositoryRequest.timeout(TimeValue.timeValueMinutes(1));
        verifyRepositoryRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        VerifyRepositoryResponse verifyRepositoryResponse = elasticsearchTemplate.getClient().admin().cluster().verifyRepository(verifyRepositoryRequest).actionGet();
        List<VerifyRepositoryResponse.NodeView> repositoryMetaDataResponse = verifyRepositoryResponse.getNodes();
        if( Objects.nonNull(repositoryMetaDataResponse) && !repositoryMetaDataResponse.isEmpty()){
            for (VerifyRepositoryResponse.NodeView nodeView : repositoryMetaDataResponse) {
//                nodeView.
            }
        }
        return ;
    }

    @Override
    public GetRepositoriesResponse repositoryStatus(){
        GetRepositoriesRequest getRepositoriesRequest = new GetRepositoriesRequest();
        getRepositoriesRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        GetRepositoriesResponse response = elasticsearchTemplate.getClient().admin().cluster()
                .getRepositories(getRepositoriesRequest).actionGet();
        // 当我们获取不到 repository 时，我们执行插入
        if(Objects.isNull(response.repositories()) || response.repositories().isEmpty()){
            if(putRepositories(repository)) {
                // 定义仓库名称
                String[] repositories = new String[]{repository};
                getRepositoriesRequest.repositories(repositories);
                response = elasticsearchTemplate.getClient().admin().cluster()
                        .getRepositories(getRepositoriesRequest).actionGet();
            }
        }
        return response;
    }

    @Override
    public boolean putSnapshot(){
        CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest();
        createSnapshotRequest.repository(repository);
        createSnapshotRequest.snapshot(snapshotName);
        // 设置索引 列表 和 索引参数
        createSnapshotRequest.indices(index);
        createSnapshotRequest.indicesOptions(IndicesOptions.fromOptions(false, false, true, true));
        // true : 允许没有所有索引主分片的可用性，来成功生成快照
        createSnapshotRequest.partial(false);
        // false : 防止将群集的全局状态写入快照
        createSnapshotRequest.includeGlobalState(true);
        createSnapshotRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        // 等待快照完成，然后返回响应
        createSnapshotRequest.waitForCompletion(true);
        CreateSnapshotResponse createSnapshotResponse = elasticsearchTemplate.getClient().admin().cluster().createSnapshot(createSnapshotRequest).actionGet();
        SnapshotInfo snapshotInfo = createSnapshotResponse.getSnapshotInfo();
        // 查看响应状态
        return snapshotInfo.status().getStatus() == RestStatus.OK.getStatus();
    }

    @Override
    public void getSnapshot(){
        GetSnapshotsRequest getSnapshotsRequest = new GetSnapshotsRequest();
        getSnapshotsRequest.repository(repository);
        String[] snapshots = { snapshotName };
        getSnapshotsRequest.snapshots(snapshots);
        getSnapshotsRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        // 指示响应是否应该冗长
        getSnapshotsRequest.verbose(true);
        // 是否忽略不可用的快照，否则如果任何快照都不可用，则请求将失败
        getSnapshotsRequest.ignoreUnavailable(false);
        GetSnapshotsResponse getSnapshotsResponse = elasticsearchTemplate.getClient().admin().cluster().getSnapshots(getSnapshotsRequest).actionGet();
        List<SnapshotInfo> snapshotInfos = getSnapshotsResponse.getSnapshots();
        for (SnapshotInfo snapshotInfo : snapshotInfos) {
            System.out.println(snapshotInfo.status().getStatus());
            System.out.println(snapshotInfo.snapshotId());
            List<SnapshotShardFailure> snapshotShardFailures = snapshotInfo.shardFailures();
            long startTime = snapshotInfo.startTime();
            long endTime = snapshotInfo.endTime();
            System.out.println(startTime);
            System.out.println(endTime);
        }
    }

    @Override
    public void snapshotsStatus() {
        SnapshotsStatusRequest snapshotsStatusRequest = new SnapshotsStatusRequest();
        snapshotsStatusRequest.repository(repository);
        SnapshotsStatusResponse response = elasticsearchTemplate.getClient().admin().cluster().snapshotsStatus(snapshotsStatusRequest).actionGet();
        for (SnapshotStatus snapshot : response.getSnapshots()) {
            System.out.println(snapshot.getIndices());
            System.out.println(snapshot.getShards());
            System.out.println(snapshot.getShardsStats());
            System.out.println(snapshot.getSnapshot().getRepository());
            System.out.println(snapshot.getSnapshot().getSnapshotId());
            System.out.println(snapshot.getState());
            System.out.println(snapshot.getStats());
        }
    }

    @Override
    public boolean deleteSnapshot(){
        DeleteSnapshotRequest deleteSnapshotRequest = new DeleteSnapshotRequest(repository);
        deleteSnapshotRequest.snapshot(snapshotName);
        deleteSnapshotRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        AcknowledgedResponse acknowledgedResponse = elasticsearchTemplate.getClient().admin().cluster().deleteSnapshot(deleteSnapshotRequest).actionGet();
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public boolean restoreSnapshot(){
        RestoreSnapshotRequest restoreSnapshotRequest = new RestoreSnapshotRequest(repository, snapshotName);
        restoreSnapshotRequest.indices(index);
        restoreSnapshotRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        restoreSnapshotRequest.waitForCompletion(true);
        restoreSnapshotRequest.partial(false);
        restoreSnapshotRequest.includeGlobalState(false);
        restoreSnapshotRequest.includeAliases(false);
        restoreSnapshotRequest.indexSettings(
                Settings.builder()
                        .put("index.number_of_replicas", 0)
                        .build());
        RestoreSnapshotResponse restoreSnapshotResponse = elasticsearchTemplate.getClient().admin().cluster().restoreSnapshot(restoreSnapshotRequest).actionGet();
        RestoreInfo restoreInfo = restoreSnapshotResponse.getRestoreInfo();
        // 查看响应状态
        return restoreInfo.status().getStatus() == RestStatus.OK.getStatus();
    }

}
