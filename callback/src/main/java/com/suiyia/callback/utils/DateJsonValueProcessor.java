package com.suiyia.callback.utils;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;

/**
 * @Author: ChenLi  2018/12/16 14:54
 * @Description:
 */

public class DateJsonValueProcessor implements JsonValueProcessor
{

    private String format;
    public DateJsonValueProcessor(String format){
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig)
    {
        return null;
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig)
    {
        if(value == null)
        {
            return "";
        }
        if(value instanceof java.sql.Timestamp)
        {
            String str = new SimpleDateFormat(format).format((java.sql.Timestamp)value);
            return str;
        }
        if (value instanceof java.util.Date)
        {
            String str = new SimpleDateFormat(format).format((java.util.Date) value);
            return str;
        }

        return value.toString();
    }
}