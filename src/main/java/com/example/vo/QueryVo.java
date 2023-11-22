package com.example.vo;

import com.example.pojo.Message;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 前端与后端交互用到的实体
 */
@Data
public class QueryVo {
    private String text;

    private ArrayList<Message> messages;
}
