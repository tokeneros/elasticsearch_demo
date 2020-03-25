package com.eros.example.elasticsearch.model;

import com.eros.example.elasticsearch.utils.DateUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: eros
 * @Description:
 *  例子
 *           "message" : "1753032: Mar 16 00:14:25: %SCHED-3-THRASHING: Process thrashing on watched message event.",
 *           "priority" : "err",
 *           "facility" : "local7",
 *           "source" : "10.138.109.18",
 *           "hostname" : "b84b00a2b923",
 *           "time" : 1584288474,
 *           "time2" : 1.5842884743795462E9,
 *           "deploymentid" : "2",
 *           "tag" : "network.local7.err",
 *           "@timestamp" : "2020-03-16T00:07:54.379546208+08:00"
 * @Date: Created in 2020/3/16 15:49
 * @Version: 1.0
 * @Modified By:
 */
@Document(indexName = "logstash-" + "#{ T(com.eros.example.elasticsearch.model.LogStash).dateStr() }", type = "fluentd", shards = 1, replicas = 0)
public class LogStash {

    @Id
    private String id;

    @Field(index = true, store = true, type = FieldType.Text)
    private String priority;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String facility;

    @Field(index = true, store = true, analyzer = "ik", searchAnalyzer = "ik", type = FieldType.Text)
    private String message;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String hostname;

    @Field(index = true, store = true, type = FieldType.Long)
    private Long time;

    @Field(index = true, store = true, type = FieldType.Float)
    private Float time2;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String source;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String deploymentid;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String tag;

    public LogStash() {
    }

    public LogStash(String instance) {
        this.message = "1754820: Mar 16 15:54:02: %SCHED-3-THRASHING: Process thrashing on watched message event.";
        this.priority = "err";
        this.facility = "local7";
        this.source = "10.138.109.18";
        this.time = 1584344887L;
        this.time2 = 1584344887.570637F;
        this.deploymentid = "2";
        this.tag = "network.local7.err";
    }

    public static String dateStr() {
        String date = DateUtils.now().replaceAll("-",".");
        return date.substring(0, 10);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Float getTime2() {
        return time2;
    }

    public void setTime2(Float time2) {
        this.time2 = time2;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeploymentid() {
        return deploymentid;
    }

    public void setDeploymentid(String deploymentid) {
        this.deploymentid = deploymentid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "LogStash{" +
                "id='" + id + '\'' +
                ", priority='" + priority + '\'' +
                ", facility='" + facility + '\'' +
                ", message='" + message + '\'' +
                ", hostname='" + hostname + '\'' +
                ", time=" + time +
                ", time2=" + time2 +
                ", source='" + source + '\'' +
                ", deploymentid='" + deploymentid + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
