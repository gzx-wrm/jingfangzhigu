package com.example.vo;

import com.example.pojo.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 调用接口传递的参数对象
 */
@Data
public class JingFangQueryVo {

    // 从QueryVo中构造JingFangQueryVo
    public JingFangQueryVo(QueryVo queryVo) {
        this.prompt = queryVo.getText();

        this.history = new ArrayList<>();
        for (Message message: queryVo.getMessages()) {
            HashMap<Object, Object> m = new HashMap<>();
            String role = message.getRole();
            m.put("role", role);
            m.put("content", message.getContent());
            if ("assistant".equals(role))
                m.put("metadata", "");
            history.add(m);
        }
    }

    private String prompt;

    private ArrayList<HashMap<Object, Object>> history;
}
