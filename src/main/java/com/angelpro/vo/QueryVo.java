package com.angelpro.vo;

import com.angelpro.pojo.Message;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 前端与后端交互用到的实体
 */
@Data
public class QueryVo {

    private String messageId;
    private String text;

    private ArrayList<Message> messages;
}
