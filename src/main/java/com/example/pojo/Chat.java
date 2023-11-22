package com.example.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 用户chat对象，每个用户有若干个chat，每个chat有若干条message
 */
@TableName("chat")
@Data
@Accessors(chain = true)
public class Chat {

    @TableField("chat_id")
    private String chatId;

    @TableField("user_id")
    private String userId;

    @TableField("chat_name")
    private String chatName;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("is_delete")
    private int is_delete;

    @TableField(exist = false)
    private ArrayList<Message> message;
}
