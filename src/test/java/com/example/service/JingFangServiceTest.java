package com.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 测试经方智谷诊断服务
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JingFangServiceTest {

    @Resource
    JingFangService jingFangService;

}