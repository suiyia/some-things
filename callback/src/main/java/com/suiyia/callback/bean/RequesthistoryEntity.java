package com.suiyia.callback.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.suiyia.callback.utils.JsonDateSerializer;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Author: ChenLi  2018/12/11 20:27
 * @Description:
 */
@Entity
@Table(name = "requesthistory", schema = "test", catalog = "")
public class RequesthistoryEntity {
    private int id;
    private String queryid;
    private Date requesttime;
    private String request;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "queryid")
    public String getQueryid() {
        return queryid;
    }

    public void setQueryid(String queryid) {
        this.queryid = queryid;
    }

    @Basic
    @Column(name = "requesttime")
    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(Date requesttime) {
        this.requesttime = requesttime;
    }

    @Basic
    @Column(name = "request")
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequesthistoryEntity that = (RequesthistoryEntity) o;
        return id == that.id &&
                queryid == that.queryid &&
                Objects.equals(requesttime, that.requesttime) &&
                Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, queryid, requesttime, request);
    }
}
