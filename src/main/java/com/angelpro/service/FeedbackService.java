package com.angelpro.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.angelpro.exception.FeedbackException;
import com.angelpro.mapper.FeedbackMapper;
import com.angelpro.mapper.UserMapper;
import com.angelpro.pojo.User;
import com.angelpro.pojo.Feedback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author gzx
 * @date 2023/11/11
 * @Description 用于上传反馈
 */
@Service
public class FeedbackService {

    // 把所有图片都放到这个目录下
    private final String root = System.getProperty("user.dir");

    @Resource
    FeedbackMapper feedbackMapper;

    @Resource
    UserMapper userMapper;

    /**
     * 上传反馈信息
     * @param feedback 反馈对象
     * @return 该对象的id
     * @throws FeedbackException 上传时发生的异常
     */
    @Transactional(propagation = Propagation.SUPPORTS,  rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int commitFeedbackText(Feedback feedback) throws FeedbackException {
        User user = userMapper.selectOne(Wrappers.<User>query().eq("user_id", feedback.getUserId()));
        if (user == null)
            throw new FeedbackException("账号不存在！");

        // 新增一条记录
        feedbackMapper.insert(feedback);

        return feedback.getFeedbackId();
    }

    /**
     * 存储反馈的图片
     * @param file 图片对象
     * @param feedbackId 反馈的id
     * @throws FeedbackException
     * @throws IOException
     */
    public void store(MultipartFile file, int feedbackId) throws FeedbackException, IOException {
        if (file == null || file.isEmpty()) {
            throw new FeedbackException("上传的文件为空!");
        }

        File filePath = new File(root + "/" + feedbackId);
        if (!filePath.exists())
            filePath.mkdirs();
        File target = new File(filePath, file.getOriginalFilename());
        file.transferTo(target);
    }

}
