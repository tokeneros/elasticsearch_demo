package com.eros.example.elasticsearch.model;

import com.eros.example.elasticsearch.utils.DateUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: eros
 * @Description:
 *           "source" : "10.138.109.18",
 *           "time" : 1584289078,
 *           "type" : "event",
 *           "eventid" : "d5fd121459ff4002aa7477fa04e9dc5e",
 *           "deviceid" : "c4ced669c57746d793444472f5a5e1d4",
 *           "tag" : "network.event",
 *           "ruleid" : "57843a137004431e9dd842adaff6a198",
 *           "deploymentid" : "2",
 *           "resourceGroup" : "48374dc9a0f64696bf954e2e629d5800",
 *           "log" : {
 *             "source" : "10.138.109.18",
 *             "time" : 1584289078,
 *             "tag" : "network.local7.err",
 *             "facility" : "local7",
 *             "message" : "1753051: -Process= \"TTY Background\", ipl= 6, pid= 26",
 *             "priority" : "err",
 *             "deploymentid" : "2"
 *           },
 *           "@timestamp" : "2020-03-16T00:17:59.823232496+08:00"
 * @Date: Created in 2020/3/16 15:50
 * @Version: 1.0
 * @Modified By:
 */
@Document(indexName = "event-" + "#{ T(com.eros.example.elasticsearch.model.Event).dateStr() }", type = "eventlog", shards = 1, replicas = 0)
public class Event {

    @Id
    private String id;

    @Field(index = true, store = true, type = FieldType.Long)
    private Long time;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String type;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String eventid;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String deviceid;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String tag;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String ruleid;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String deploymentid;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String resourceGroup;

    @Field(index = true, store = true, type = FieldType.Object)
    private LogDetail log;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid;
    }

    public String getDeploymentid() {
        return deploymentid;
    }

    public void setDeploymentid(String deploymentid) {
        this.deploymentid = deploymentid;
    }

    public String getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceGroup(String resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    public LogDetail getLog() {
        return log;
    }

    public void setLog(LogDetail log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                ", eventid='" + eventid + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", tag='" + tag + '\'' +
                ", ruleid='" + ruleid + '\'' +
                ", deploymentid='" + deploymentid + '\'' +
                ", resourceGroup='" + resourceGroup + '\'' +
                ", log=" + log +
                '}';
    }

    public static String dateStr() {
        String date = DateUtils.now().replaceAll("-",".");
        return date.substring(0, 10);
    }

}
