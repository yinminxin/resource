package com.example.wanda.resource.utils.OSSUpload;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @ClassName: OSSClientConstants
 * @Description: OSS阿里云常用变量
 * @author wy
 * @date 2017年5月5日 上午11:56:25
 *
 */
public class OSSClientConstants {
    //阿里云API的上传完成后响应域名
    public static final String DOMAIN_PREFIX = "https://shstudy.shec.edu.cn";

    //阿里云API的外网域名
//    public static final String ENDPOINT = "http://xxpt.oss-cn-shanghai-shltjyy-d01-a.procloud.10010sh.cn";
    public static final String ENDPOINT = "http://weixiao.oss-cn-shanghai-shltjyy-d01-a.procloud.10010sh.cn";
//    public static final String ENDPOINT = "http://oss-cn-shanghai-shltjyy-d01-a.procloud.cn/weixiao";
    //阿里云API的密钥Access Key ID
//    public static final String ACCESS_KEY_ID = "0aI5HO2SxgO8jwZW";
//    //阿里云API的密钥Access Key Secret
//    public static final String ACCESS_KEY_SECRET = "Vf8WS7j0nwnaeqEQ8jFjGsSC0f7mvZ";
    //阿里云API的密钥Access Key ID
    public static final String ACCESS_KEY_ID = "XxhTKc77Ie4N0Ssa";
    //阿里云API的密钥Access Key Secret
    public static final String ACCESS_KEY_SECRET = "Vz6v3PD6iaskanPoDuMPg5ZWQTmsY0";
    //阿里云API的bucket名称
//    public static final String BACKET_NAME = "xxpt";
    public static final String BACKET_NAME = "weixiao";
//    阿里云API的文件夹名称
    public static final String FOLDER_PREFIX="smile-resource/";
    public static final String FOLDER="images/upload/";
    public static final String FOLDER_VIDEO="images/upload/";
    public static final String FORMAT = new SimpleDateFormat("yyyyMMdd").format(new Date());
    public static final String FORMATS = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
}
