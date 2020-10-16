package com.example.wanda.resource.utils.OSSUpload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class AliyunOSSClientUtil {
    // log日志
    private static Logger logger = LoggerFactory.getLogger(AliyunOSSClientUtil.class);
    // 阿里云API的内或外网域名
    private static String ENDPOINT;
    // 阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    // 阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    // 阿里云API的bucket名称
    public static String BACKET_NAME;
    // 阿里云API的文件夹名称
    private static String FOLDER_PREFIX;
    private static String FOLDER;
    private static String FORMAT;
    private static String FORMATS;
    private static String FOLDER_VIDEO;

    // 初始化属性
    static {
        ENDPOINT = OSSClientConstants.ENDPOINT;
        ACCESS_KEY_ID = OSSClientConstants.ACCESS_KEY_ID;
        ACCESS_KEY_SECRET = OSSClientConstants.ACCESS_KEY_SECRET;
        BACKET_NAME = OSSClientConstants.BACKET_NAME;
        FOLDER_PREFIX = OSSClientConstants.FOLDER_PREFIX;
        FOLDER = OSSClientConstants.FOLDER;
        FORMAT = OSSClientConstants.FORMAT;
        FORMATS = OSSClientConstants.FORMATS;
        FOLDER_VIDEO = OSSClientConstants.FOLDER_VIDEO;
    }

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public static OSSClient getOSSClient() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        // 存储空间
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient, String bucketName, String folder) {
        // 文件夹名
        final String keySuffixWithSlash = folder;
        // 判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            // 创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            // 得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
        ossClient.deleteObject(bucketName, folder + key);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }

    /**
     * 上传图片至OSS 文件流  普通上传
     *
     * @param ossClient  oss连接
     *                   上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName 存储空间
     * @return String 返回的唯一MD5数字签名
     */
    public static String uploadObject2OSS(OSSClient ossClient, InputStream ins, String name, String bucketName, String user_id) {
        String resultStr = null;
//        String[] fo = new String[] { "", "" };
        try {
            // 以输入流的形式上传文件
            String folder = "";
            folder = FOLDER_PREFIX + FOLDER + user_id + "/" + FORMAT + "/";
//            String folderNoPrefix = FOLDER + user_id + "/" + FORMAT + "/";
//            InputStream is = new FileInputStream(file);
            InputStream is = ins;
            // 文件名
            FORMATS = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400);
            String timefile = FORMATS;
            //String fileName = file.getName();
            String fileName = name;
            fileName = timefile + fileName.substring(fileName.lastIndexOf("."));
            logger.info("上传到路径" + folder + fileName);
            // 文件大小
            //Long fileSize = file.length();
            Long fileSize = new Long(ins.available());
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度
            metadata.setContentLength(is.available());
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");


            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
            // 解析结果
//            resultStr = putResult.getETag();
            resultStr = OSSClientConstants.DOMAIN_PREFIX + "/" + folder + fileName;
            //fo[1] = ENDPOINT + "/" + folder + fileName;
//            fo[0] = resultStr;
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }


    /**
     * 断点续传方法
     *
     * @param ossClient
     * @param filePath
     * @param name,
     * @param bucketName
     * @return
     * @author sunxiaodong
     * @date 2020/2/14 14:17
     */
    public static String uploadPart2OSS(OSSClient ossClient, String filePath, String name, String bucketName, String user_id) {
        String resultStr = null;
        try {
            String folder = "";
            folder = FOLDER_PREFIX + FOLDER_VIDEO + user_id + "/" + FORMAT + "/";
//            String folderNoPrefix = FOLDER_VIDEO + user_id + "/" + FORMAT + "/";
            // 文件名
            FORMATS = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400);
            String timefile = FORMATS;
            //String fileName = file.getName();
            String fileName = name;
            fileName = timefile + fileName.substring(fileName.lastIndexOf("."));
            logger.info("上传到路径" + folder + fileName);
            // 通过UploadFileRequest设置多个参数。
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, folder + fileName);
            // 指定上传的本地文件。
            uploadFileRequest.setUploadFile(filePath);
            // 指定上传并发线程数，默认为1,设为10。
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小，范围为100KB~5GB，默认为文件大小/10000。
            uploadFileRequest.setPartSize(5 * 1024 * 1024);
            // 开启断点续传，默认关闭。
            uploadFileRequest.setEnableCheckpoint(true);

            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));

            uploadFileRequest.setObjectMetadata(metadata);

            // 记录本地分片上传结果的文件。开启断点续传功能时需要设置此参数，上传过程中的进度信息会保存在该文件中，
            // 如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。
            // 上传完成后，该文件会被删除。默认与待上传的本地文件同目录，为uploadFile.ucp。
            uploadFileRequest.setCheckpointFile(filePath+".ucp");

            // 设置上传成功回调，参数为Callback类型。我们不需要
//            if (callback != null) {
//                uploadFileRequest.setCallback(callback);
//            }

            // 断点续传上传。
            try {

                ossClient.uploadFile(uploadFileRequest);
                resultStr = OSSClientConstants.DOMAIN_PREFIX + "/" + folder + fileName;

            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message: " + oe.getErrorMessage());
                System.out.println("Error Code:       " + oe.getErrorCode());
                System.out.println("Request ID:      " + oe.getRequestId());
                System.out.println("Host ID:           " + oe.getHostId());
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message: " + ce.getMessage());
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                ossClient.shutdown();
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }

        return resultStr;
    }


    /**
     * 上传图片至OSS 文件流  分片上传的方法
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @return String 返回的唯一MD5数字签名
     */
    public static String multipartUploadObject2OSS(OSSClient ossClient, MultipartFile file, String name, String bucketName, String user_id) {
        String resultStr = null;
//        String[] fo = new String[] { "", "" };
        try {
            // 以输入流的形式上传文件
            String folder = "";
            folder = FOLDER_PREFIX + FOLDER + "video" + "/" + user_id + "/" + FORMAT + "/";
//            String folderNoPrefix = FOLDER + user_id + "/" + FORMAT + "/";
//            InputStream is = new FileInputStream(file);
            InputStream is = file.getInputStream();
            // 文件名
            FORMATS = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400);
            String timefile = FORMATS;
            String fileName = name;
            fileName = timefile + fileName.substring(fileName.lastIndexOf("."));

            String key = folder + fileName;
            logger.info("上传到路径:" + key);
            // 文件大小
            Long fileSize = file.getSize();
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度
            metadata.setContentLength(is.available());
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");

            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);

            // 如果需要在初始化分片时设置文件存储类型，请参考以下示例代码。
            request.setObjectMetadata(metadata);

            // 初始化分片。
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
            String uploadId = upresult.getUploadId();

            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> partETags = new ArrayList<>();
            // 计算文件有多少个分片。
            // 5MB
//            final long partSize = 1 * 1024 * 1024L;
            final long partSize = 10 * 1024 * 1024L;
            final File sampleFile = new File(name);
            inputStreamToFile(is, sampleFile);

            int partCount = (int) (fileSize / partSize);
            if (fileSize % partSize != 0) {
                partCount++;
            }

            // 遍历分片上传。
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileSize - startPos) : partSize;
                InputStream instream = new FileInputStream(sampleFile);
                // 跳过已经上传的分片。
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(uploadId);
//                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber(i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
                partETags.add(uploadPartResult.getPartETag());
            }


            // 创建CompleteMultipartUploadRequest对象。
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);

            // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
            // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

            // 完成上传。
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);

            resultStr = OSSClientConstants.DOMAIN_PREFIX + "/" + folder + fileName;
            sampleFile.delete();
            // 关闭OSSClient。
            ossClient.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }


    /**
     *   多线程分片上传
     *
     * @author sunxiaodong
     * @date 2020/2/17 18:58
     * @param  client
     * @param  file
     * @param  bucketName
     * @param  user_id
     * @return
     */
    public static String fileUpload(OSSClient client, File file, String bucketName, String user_id) {

        String resultStr = null;
        // 以输入流的形式上传文件
        String folder = "";
        folder = FOLDER_PREFIX + FOLDER + "video" + "/" + user_id + "/" + FORMAT + "/";

        // 文件名
        FORMATS = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400);
        String timefile = FORMATS;
//        String fileName = file.getOriginalFilename();
        String fileName = file.getName();
        fileName = timefile + fileName.substring(fileName.lastIndexOf("."));
        // 完整的文件名
        String key = folder + fileName;
        logger.info("上传到路径:" + key);

        // 创建一个可重用固定线程数的线程池。若同一时间线程数大于10，则多余线程会放入队列中依次执行
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        // 上传的文件的长度
//        metadata.setContentLength(file.getInputStream().available());
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(getContentType(fileName));
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
//        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
        try {
            String uploadId = AliyunOSSUpload.claimUploadId(client, bucketName, key, metadata);// key是文件名 By berton

            // 计算分块数目
            long fileLength = file.length();// 文件大小
            boolean flag = checkFileSize(fileLength, 200, "M");

            // 设置每块为 5M(除最后一个分块以外，其他的分块大小都要大于5MB)
            final long partSize = 10 * 1024 * 1024L;
//            if (flag) {
//                partSize = 5 * 1024 * 1024L;
//            }else {
//                partSize = 10 * 1024 * 1024L;
//            }

            logger.info("分片大小:" + partSize);

            int partCount = (int) (fileLength / partSize);// 文件大小%分块大小
            if (fileLength % partSize != 0) {
                partCount++;
            }

            // 分块 号码的范围是1~10000。如果超出这个范围，OSS将返回InvalidArgument的错误码。
            if (partCount > 10000) {
                throw new RuntimeException("文件过大(分块大小不能超过10000)");
            } else {
                logger.info("一共分了 " + partCount + " 块");
            }

            /**
             * 将分好的文件块加入到list集合中
             */
            for (int i = 0; i < partCount; i++) {
                // 起始point
                long startPos = i * partSize;
                // 判断当前partSize的长度 是否最后一块
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                // 线程执行。将分好的文件块加入到list集合中()
                executorService
                        .execute(new AliyunOSSUpload(file, startPos, curPartSize, i + 1, uploadId, key, bucketName));
            }


            /**
             * 等待所有分片完毕
             */
            // 关闭线程池（线程池不马上关闭），执行以前提交的任务，但不接受新任务。
            executorService.shutdown();
            // 如果关闭后所有任务都已完成，则返回 true。
            while (!executorService.isTerminated()) {
                try {
                    // 用于等待子线程结束，再继续执行下面的代码
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /**
             * partETags(上传块的ETag与块编号（PartNumber）的组合) 如果校验与之前计算的分块大小不同，则抛出异常
             */
            System.out.println(AliyunOSSUpload.partETags.size() + " -----   " + partCount);
//            if (AliyunOSSUpload.partETags.size() != partCount) {
//                throw new IllegalStateException("OSS分块大小与文件所计算的分块大小不一致");
//            } else {
                logger.info("将要上传的文件名  " + key + "\n");
//            }

            /*
             * 列出文件所有的分块清单并打印到日志中，该方法仅仅作为输出使用
             */
            AliyunOSSUpload.listAllParts(client, uploadId);

            /*
             * 完成分块上传
             */
            AliyunOSSUpload.completeMultipartUpload(client, uploadId);

            resultStr = OSSClientConstants.DOMAIN_PREFIX + "/" + folder + fileName;
//            logger.info("结束时间:" + DateUtil.getCurrentDate(DateUtil.CURRENT_DATETIME_FORMAT));

        } catch (Exception e) {
            logger.error("上传失败！", e);
        } finally {
            AliyunOSSUpload.partETags.clear();
            if (client != null) {
                client.shutdown();
            }
        }

        return resultStr;
    }




    //获取流文件
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //上传路径返回地址，图片视频都可以
    //需要改变上传那文件位置FOLDER
    public static String[] uploadObjectOSS(OSSClient ossClient, String file, String bucketName, String user_id) {
        String resultStr = null;
        String[] fo = new String[]{"", ""};
        try {
            // 以输入流的形式上传文件
            String folder = "";
            folder = FOLDER + user_id + "/" + FORMAT + "/";
//            InputStream is = new FileInputStream(file);
            // 文件名
            String timefile = FORMATS;
//            String fileName = file.getName();
            file = timefile + file.substring(file.lastIndexOf("."));
            logger.info("上传路径:" + folder + file);
            // 文件大小
            Integer fileSize = file.length();
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
//            // 上传的文件的长度
//            metadata.setContentLength(is.available());
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(file));
            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + file + "/" + fileSize + "Byte.");
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + file, new ByteArrayInputStream(file.getBytes("UTF-8")), metadata);
            // 解析结果
            resultStr = putResult.getETag();
            fo[1] = folder + file;
            fo[0] = resultStr;

            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return fo;
    }

    //上传视频
    public static String uploadByteVideoOSS(OSSClient ossClient, byte[] b, String bucketName, String user_id) {

        // byte[] content = "Hello OSS".getBytes();

        // 以输入流的形式上传文件
        String folder = "";
        folder = FOLDER_VIDEO + user_id + "/" + FORMAT + "/";
        // 文件名
        String timefile = FORMATS;// 文件名
        String fileName = ".MP4";// 后缀扩展名
        fileName = timefile + fileName;
        logger.info("上传到路径" + folder + fileName);

        Long fileSize = (long) b.length;

        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(getContentType(fileName));
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");

        PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, new ByteArrayInputStream(b),
                metadata);
        ossClient.shutdown();
        String filepath = folder + fileName;
        return filepath;
    }


    //上传图片
    public static String uploadByteOSS(OSSClient ossClient, byte[] b, String bucketName, String user_id) {

        // byte[] content = "Hello OSS".getBytes();

        // 以输入流的形式上传文件
        String folder = "";
        folder = FOLDER + user_id + "/" + FORMAT + "/";
        // 文件名
        String timefile = FORMATS;// 文件名
        String fileName = ".jpg";// 后缀扩展名
        fileName = timefile + fileName;
        logger.info("上传到路径" + folder + fileName);

        Long fileSize = (long) b.length;
//        String timefile = FORMATS;
////        String fileName = file.getName();
//        file = timefile + file.substring(file.lastIndexOf("."));
        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(getContentType(fileName));
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");

        PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, new ByteArrayInputStream(b),
                metadata);
        ossClient.shutdown();
        String filepath = folder + fileName;
        return filepath;
    }

    public static byte[] image2Bytes(String imgSrc) throws Exception {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        // 可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes = new byte[fin.available()];
        // 将文件内容写入字节数组，提供测试的case
        fin.read(bytes);

        fin.close();
        return bytes;
    }

    //图片转化为byte数组
    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xls".equalsIgnoreCase(fileExtension) || ".xlsx".equalsIgnoreCase(fileExtension)) {
            return "application/x-xls";
        }

        if (".ppt".equalsIgnoreCase(fileExtension) || ".pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }

        if (".zip".equalsIgnoreCase(fileExtension)) {
            return "application/zip";
        }

        if (".mp3".equalsIgnoreCase(fileExtension)) {
            return "audio/mp3";
        }

        if (".wma".equalsIgnoreCase(fileExtension)) {
            return "audio/x-ms-wma";
        }

        if (".mp4".equalsIgnoreCase(fileExtension)) {
            return "video/mp4";
        }
        if (".pdf".equalsIgnoreCase(fileExtension)) {
            return "application/pdf";
        }
        // 默认返回类型
        return "application/octet-stream";
    }

    /**
     * 获得url链接
     *
     * @return
     */
    public static String getUrl(OSSClient ossClient, String bucketName, String fileName) {
        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + (3600L * 1000 * 24 * 365 * 10));
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
        if (url != null) {
            return url.toString();
        }
        return "获网址路径出错";
    }

    /**
     * 判断文件大小
     * @param len 文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {

        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }


}
