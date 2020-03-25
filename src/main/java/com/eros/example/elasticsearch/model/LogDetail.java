package com.eros.example.elasticsearch.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/16 17:13
 * @Version: 1.0
 * @Modified By:
 */
public class LogDetail {

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String source;

    @Field(index = true, store = true, type = FieldType.Long)
    private String time;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String tag;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String facility;

    @Field(index = true, store = true, analyzer = "ik", searchAnalyzer = "ik", type = FieldType.Text)
    private String message;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String priority;

    @Field(index = true, store = true, type = FieldType.Keyword)
    private String deploymentid;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDeploymentid() {
        return deploymentid;
    }

    public void setDeploymentid(String deploymentid) {
        this.deploymentid = deploymentid;
    }

    @Override
    public String toString() {
        return "LogDetail{" +
                "source='" + source + '\'' +
                ", time='" + time + '\'' +
                ", tag='" + tag + '\'' +
                ", facility='" + facility + '\'' +
                ", message='" + message + '\'' +
                ", priority='" + priority + '\'' +
                ", deploymentid='" + deploymentid + '\'' +
                '}';
    }
}
