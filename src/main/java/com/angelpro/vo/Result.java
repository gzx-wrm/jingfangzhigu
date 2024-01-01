package com.angelpro.vo;

import lombok.Data;

import java.util.HashMap;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 封装结果
 */
@Data
public class Result {

    // 状态码
    private String code;
    // 消息
    private String message;
    // 数据
    private Object data;

    public static Result ok(){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("成功");
        result.setData(new HashMap<>());
        return result;
    }

    public static Result ok(String msg){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage(msg);
        result.setData(new HashMap<>());
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setCode(ResultCode.FAIL);
        result.setMessage("失败");
        result.setData(new HashMap<>());
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.setCode(ResultCode.FAIL);
        result.setMessage(msg);
        result.setData(new HashMap<>());
        return result;
    }

    public Result data(Object key, Object value){
        if("java.util.HashMap".equals(data.getClass().getName())) {
            ((HashMap) data).put(key, value);
        }
        return this;
    }
}
