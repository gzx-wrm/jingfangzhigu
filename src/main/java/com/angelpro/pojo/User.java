package com.angelpro.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description
 */
@TableName("user")
@Data
@Accessors(chain = true)
public class User {

    // 用户id，使用uuid唯一生成
    @TableField("user_id")
    private String userId;

    // 用户名
    private String username;

    // 账号(手机号)
    private String phone;

    // 密码
    private String password;

    // 邮箱
    private String email;

}
