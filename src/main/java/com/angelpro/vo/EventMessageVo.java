package com.angelpro.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gzx
 * @date 2023/11/18
 * @Description
 */
@Data
@Accessors(chain = true)
public class EventMessageVo {

    private String event;
    private Object data;

    public String toJson() throws JsonProcessingException {
//        return  "{\"event\":\"" + event + "\",\"data\":" + data.toString() + "}";
        return new ObjectMapper().writeValueAsString(this);
    }
}
