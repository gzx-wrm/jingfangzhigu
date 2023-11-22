package com.example.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 用户的每一条对话
 */
@TableName("message")
@Data
@Accessors(chain = true)
public class Message {

    private String messageId;

    private String chatId;

    private String role;

    private Date createTime;

    private String content;

    private String metadata;

    private int isDelete;
}
