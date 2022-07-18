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
    public static final String DOMAIN_PREFIX = "https://sh***du.cn";

    //阿里云API的外网域名
//    public static final String ENDPOINT = "http://xxpt.oss-cn-***rocloud.10010sh.cn";
    public static final String ENDPOINT = "http://weixiao.oss-cn-s***a.procloud.10010sh.cn";
//    public static final String ENDPOINT = "http://oss-cn-sh***-a.procloud.cn/weixiao";
    //阿里云API的密钥Access Key ID8j
//    public static final String ACCESS_KEY_ID = "0aI5HO***wZW";
//    //阿里云API的密钥Access Key Secret
//    public static final String ACCESS_KEY_SECRET = "Vf8WS7j0nwn***C0f7mvZ";
    //阿里云API的密钥Access Key ID
    public static final String ACCESS_KEY_ID = "XxhTK***sa";
    //阿里云API的密钥Access Key Secret
    public static final String ACCESS_KEY_SECRET = "Vz6v3PD6ias***5ZWQTmsY0";
    //阿里云API的bucket名称
//    public static final String BACKET_NAME = "xxpt";
    public static final String BACKET_NAME = "weixiao";
//    阿里云API的文件夹名称
    public static final String FOLDER_PREFIX="smil***urce/";
    public static final String FOLDER="images/**dd/";
    public static final String FOLDER_VIDEO="images/u***oad/";
    public static final String FORMAT = new SimpleDateFormat("yyyyMMdd").format(new Date());
    public static final String FORMATS = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
}
