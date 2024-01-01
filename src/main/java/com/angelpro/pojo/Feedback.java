package com.angelpro.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gzx
 * @date 2023/11/11
 * @Description 用于封装反馈信息
 */
@TableName("feedback")
@Data
@Accessors(chain = true)
public class Feedback {

    @TableId(type = IdType.AUTO)
    @TableField("feedback_id")
    private int feedbackId;

    @TableField("user_id")
    private String userId;

    private String title;

    private String content;
}
