package com.angelpro.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 发送短信验证码的工具
 */
@PropertySource("classpath:application.yml")
@Component
public class SmsUtil {

    @Value("${tencent.secretId}")
    private String secretId;

    @Value("${tencent.secretKey}")
    private String secretKey;

    @Value("${tencent.sms.appId}")
    private String appId;

    @Value("${tencent.sms.signName}")
    private String signName;

    @Value("${tencent.sms.templateId}")
    private String templateId;

    public boolean sendSms(String phoneNumber, String message) {
        try {
            System.out.println(secretId);
            Credential cred = new Credential(secretId, secretKey);
            SmsClient client = new SmsClient(cred, "ap-guangzhou");

            SendSmsRequest req = new SendSmsRequest();
            // 设置appId
            req.setSmsSdkAppId(appId);
            // 设置签名
            req.setSignName(signName);
            // 设置模板id
            req.setTemplateId(templateId);
            // 填充模板参数
            req.setTemplateParamSet(new String[]{message, "5"});
            // 设置手机号
            req.setPhoneNumberSet(new String[]{phoneNumber});

            SendSmsResponse res = client.SendSms(req);

            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));
            // 根据需要处理响应结果
        } catch (TencentCloudSDKException e) {
            // 处理API调用过程中出现的异常
            e.printStackTrace();
        }
        return true;
    }
}
