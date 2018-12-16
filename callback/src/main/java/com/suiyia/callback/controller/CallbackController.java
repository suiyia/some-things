package com.suiyia.callback.controller;

import cn.com.egova.cloudsystem.common.bean.ResultInfo;
import com.suiyia.callback.bean.RequesthistoryEntity;
import com.suiyia.callback.utils.DateJsonValueProcessor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenLi  2018/12/7 11:23
 * @Description:
 */
@Controller
public class CallbackController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(Model model){
        return "callback";
    }

    @RequestMapping(value = "/request",name = "接收根据 calid 得到的 HTTP 请求")
    @ResponseBody
    public ResultInfo requestInsert(HttpServletRequest request,
                              @RequestParam(required = true) String calid){
        ResultInfo resultInfo = new ResultInfo(true);
        try {
            JSONObject path = new JSONObject();
            path.put("RequestURL",request.getRequestURL());
            path.put("RequestURI",request.getRequestURI());
            path.put("ServletPath",request.getServletPath());
            path.put("method",request.getMethod());
            path.put("ContextPath",request.getContextPath());
            path.put("AuthType",request.getAuthType());
            path.put("PathInfo",request.getPathInfo());
            path.put("PathTranslated",request.getPathTranslated());
            path.put("ContextPath",request.getContextPath());
            path.put("QueryString",request.getQueryString());
            path.put("RemoteUser",request.getRemoteUser());

            JSONObject header = new JSONObject();
            Enumeration<String> e = request.getHeaderNames();
            while(e.hasMoreElements()){
                String param = (String) e.nextElement();
                header.put(param,request.getHeader(param));
            }

            JSONObject parameters = new JSONObject();
            Map<String, String[]> paramsMap = request.getParameterMap();
            for (String key : paramsMap.keySet()) {
                parameters.put(key,request.getParameter(key));
            }

            JSONObject session = new JSONObject();
            session.put("RemoteAddr",request.getRemoteAddr());
            session.put("RequestedSessionId",request.getRequestedSessionId());
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    session.put("cookie",cookie);
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("path",path);
            jsonObject.put("header",header);
            jsonObject.put("parameters",parameters);
            jsonObject.put("session",session);
            logger.info(jsonObject.toString());
            String requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            jdbcTemplate.update("insert into requesthistory(queryid,requesttime,request)values (?,?,?) on duplicate key update requesttime = ?,request=?",calid,requestTime,jsonObject.toString(),requestTime,jsonObject.toString());
            resultInfo.setMessage("请求成功");
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setMessage("请求异常"+e.getMessage());
        }
        return resultInfo;
    }


    @ResponseBody
    @RequestMapping(value = "/getallrequest")
    public String getALLRequest(HttpServletRequest request,
                                @RequestParam(required = false) String search,
                                @RequestParam(required = false) String order,
                                @RequestParam(required = false) int offset,
                                @RequestParam(required = false) int limit){
        System.out.println("offset:"+offset+"limit:"+limit);
        JSONObject jsonObject = new JSONObject();
        try{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select * from requesthistory");
            if (search != null && !search.isEmpty()){
                stringBuilder.append(" where queryid like '%"+search+"%'");
            }
            if (order != null && !order.isEmpty()){
                stringBuilder.append(" order by requesttime "+ order);
            }
            if (offset >=0 && limit != 0){
                stringBuilder.append(" limit "+offset+","+limit);
            }
            System.out.println("sql:"+stringBuilder.toString());
            List<RequesthistoryEntity> list = jdbcTemplate.query(stringBuilder.toString(), new RowMapper<RequesthistoryEntity>() {
                @Override
                public RequesthistoryEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                    RequesthistoryEntity requesthistoryEntity = new RequesthistoryEntity();
                    requesthistoryEntity.setId(resultSet.getInt("id"));
                    requesthistoryEntity.setQueryid(resultSet.getString("queryid"));
                    requesthistoryEntity.setRequesttime(resultSet.getDate("requesttime"));
                    requesthistoryEntity.setRequest(resultSet.getString("request"));
                    return requesthistoryEntity;
                }
            });
            jsonObject.put("total",jdbcTemplate.queryForObject("select count(1) from requesthistory",Integer.class));
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss:SSS"));
            JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
            jsonObject.put("rows",jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
