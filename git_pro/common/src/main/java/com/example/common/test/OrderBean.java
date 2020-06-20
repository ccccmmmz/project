package com.example.common.test;

import com.example.common.DbUtils.DbField;
import com.example.common.DbUtils.DbFile;
import com.example.common.DbUtils.DbPrimary;

@DbFile("gradle")
public class OrderBean {
    @DbField("_id")
    private int id;
    @DbField("_time")
    private String time;

    public OrderBean(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public OrderBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "id=" + id +
                ", time='" + time + '\'' +
                '}';
    }
}
