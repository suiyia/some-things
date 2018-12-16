package com.suiyia.callback;

import com.suiyia.callback.bean.RequesthistoryEntity;
import com.suiyia.callback.utils.DateJsonValueProcessor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: ChenLi  2018/12/15 15:41
 * @Description:
 */
public class Main {

    public static void main(String[] args) {
        List<RequesthistoryEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RequesthistoryEntity entity = new RequesthistoryEntity();
            entity.setId(i);
            entity.setQueryid(i+"");
            entity.setRequesttime(new Date());
            list.add(entity);
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray jsonArray = JSONArray.fromObject(list);
        System.out.println(jsonArray);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1",jsonArray);
        System.out.println(jsonObject);
        //Timestamp timestamp = new Timestamp();
        //JSONObject jsonObject = JSONObject.fromObject(timestamp);
        //System.out.println(jsonObject);

    }

}
