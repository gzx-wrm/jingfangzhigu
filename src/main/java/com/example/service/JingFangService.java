package com.example.service;

import com.example.vo.JingFangQueryVo;
import com.example.vo.QueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;;
import java.util.Map;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 调用经方智谷诊断接口服务
 */
@Service
@Slf4j
public class JingFangService {

    private final String SERVICE_URL = "http://jingfangzhigu.vip.cpolar.cn";

    @Resource
    RestTemplate restTemplate;

    public ResponseEntity<Map> getDiagnosis(QueryVo queryVo){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JingFangQueryVo jingFangQueryVo = new JingFangQueryVo(queryVo);
//        log.debug(jingFangQueryVo.toString());
        HttpEntity<Object> requestEntity = new HttpEntity<>(jingFangQueryVo, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(SERVICE_URL, HttpMethod.POST, requestEntity, Map.class);

        return responseEntity;
    }

}
