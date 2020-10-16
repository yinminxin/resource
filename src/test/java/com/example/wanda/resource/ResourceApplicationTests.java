package com.example.wanda.resource;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.HeadObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.example.wanda.resource.entity.*;
import com.example.wanda.resource.mapper.*;
import com.example.wanda.resource.utils.OSSUpload.AliyunOSSClientUtil;
import com.example.wanda.resource.utils.OSSUpload.OSSClientConstants;
import com.example.wanda.resource.vo.ResourceLocalLinkVO;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.print.DocFlavor;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class ResourceApplicationTests {

    @Autowired
    private LinkMapper linkMapper;
    @Autowired
    private OnLinkMapper onLinkMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private ResourceInfoMapper resourceInfoMapper;
    @Autowired
    private ResourceNamePuDongXinQuMapper resourceNamePuDongXinQuMapper;
    @Autowired
    private LinkPuDongXinQuMapper linkPuDongXinQuMapper;

    @Test
    void contextLoads() {

//        String onLinkSize = getOnLinkSize("https://shstudy.shec.edu.cn/smile-resource/images/upload/高中数学学案/20200927/1601258546179863.zip");
//        System.out.println(onLinkSize);

// ****************************************开始***********读取excel，根据资源名获取本地链接***********************************************
//        String fileUrl = "D:\\yinminxin\\项目需求\\2020\\资源上传\\resourceLocalTest3.xlsx";
//        try (InputStream inputStream = new FileInputStream(new File(fileUrl))) {
//            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer,String>>(){
//                int count = 0;
//                int total = 0;
//                private List<Map<Integer,String>> data = new ArrayList<>();
//                @Override
//                public void invoke(Map<Integer,String> resourceLocalLinkVO, AnalysisContext analysisContext) {
//                    total++;
//                    data.add(resourceLocalLinkVO);
//                }
//
//                @Override
//                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//                    List<Resource> resources = resourceMapper.selectByLocalLinkIsNull();
////                    System.out.println(data);
//                    for (Map<Integer, String> maps : data) {
//                        String resourceName = maps.get(0);
//                        String loacllink = maps.get(1);
//                        List<Resource> collect = resources.stream().filter(v -> resourceName.equals(v.getResource_name())).filter(Objects::nonNull).collect(Collectors.toList());
//                        if (collect != null && collect.size() ==1) {
//                            Resource resource = collect.get(0);
////                            loacllink = "F:\\万达信息\\微校资源复制\\各区汇总等多个文件" + loacllink;
//                            resource.setLocal_link(loacllink);
//                            resourceMapper.updateByPrimaryKey(resource);
//                            count++;
//                        }else if (collect != null && collect.size() > 1){
//                            for (Resource resource : collect) {
//                                resource.setLocal_link("2");
//                                resourceMapper.updateByPrimaryKey(resource);
//                            }
//                        }else {
//                            System.out.println(maps);
//                        }
//
//                    }
//                    System.out.println("更新成功条数" + count);
//                    System.out.println("总条数" + total);
//                }
//            }).doReadAll();
//            System.out.println("*****************************************************************结束*******************************************************************");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
// ****************************************结束***********读取excel，根据资源名获取本地链接***********************************************

// ****************************************开始***********将已上传资源入库***********************************************
//        List<Resource> resourcesMore = resourceMapper.selectByOnLineLinkIsNotNull();
//        //区数组
//        Map areaMap = new HashMap<String,String>();
//        areaMap.put("黄浦区","1");
//        areaMap.put("徐汇区","2");
//        areaMap.put("长宁区","3");
//        areaMap.put("静安区","4");
//        areaMap.put("普陀区","5");
//        areaMap.put("虹口区","6");
//        areaMap.put("杨浦区","7");
//        areaMap.put("闵行区","8");
//        areaMap.put("宝山区","9");
//        areaMap.put("嘉定区","10");
//        areaMap.put("浦东新区","11");
//        areaMap.put("金山区","12");
//        areaMap.put("松江区","13");
//        areaMap.put("青浦区","14");
//        areaMap.put("奉贤区","15");
//        areaMap.put("崇明区","16");
//        Map sectionMap = new HashMap<String,String>();
//        sectionMap.put("小学","1");
//        sectionMap.put("初中","2");
//        sectionMap.put("高中","3");
//        Map subjectMap = new HashMap<String,String>();
//        subjectMap.put("语文","1");
//        subjectMap.put("数学","2");
//        subjectMap.put("英语","3");
//        subjectMap.put("体育与健身","4");
//        subjectMap.put("道德与法治","5");
//        subjectMap.put("唱游","6");
//        subjectMap.put("美术","7");
//        subjectMap.put("信息科技","8");
//        subjectMap.put("劳动技术","9");
//        subjectMap.put("科学与技术","10");
//        subjectMap.put("科学","11");
//        subjectMap.put("物理","12");
//        subjectMap.put("化学","13");
//        subjectMap.put("生命科学","14");
//        subjectMap.put("地理","15");
//        subjectMap.put("历史","16");
//        subjectMap.put("社会","17");
//        subjectMap.put("音乐","18");
//        subjectMap.put("艺术","19");
//        subjectMap.put("思想政治","20");
//        subjectMap.put("公开课","25");
//        subjectMap.put("自然","26");
//        subjectMap.put("心理","27");
//        subjectMap.put("综合(研拓)类","28");
//        Map typeMap = new HashMap<String,String>();
//        typeMap.put("mp4","1");
//        typeMap.put("jpeg","2");
//        typeMap.put("png","3");
//        typeMap.put("word","4");
//        typeMap.put("ppt","5");
//        typeMap.put("xsl","6");
//        typeMap.put("pdf","7");
//        typeMap.put("flash","8");
//        typeMap.put("mp3","9");
//        typeMap.put("avi","10");
//        typeMap.put("jpg","11");
//        typeMap.put("MOV","13");
//        typeMap.put("zip","14");
//        typeMap.put("rar","15");
//
//        LocalDateTime now = LocalDateTime.now();
//
//        ArrayList<ResourceInfo> resourceInfos = new ArrayList<>();
//
//        for (Resource resource : resourcesMore) {
//            try {
//                String[] split = resource.getOnline_link().split(";");
////                if (split.length >= 8) {
////                    resource.setStatus("0");
////                    resource.setLocal_link(null);
////                    resource.setOnline_link("1");
////                    resourceMapper.updateByPrimaryKey(resource);
////                    continue;
////                }
//                List<String> ids = schoolMapper.selectIdByName(resource.getSchool());
//                for (String url : split) {
//                    if ("1".equals(url)) {
//                        continue;
//                    }
//                    ResourceInfo resourceInfo = new ResourceInfo();
//                    String area = (String) areaMap.get(resource.getArea());
//                    resourceInfo.setArea(StringUtils.isEmpty(area) ? "17" : area);
//                    String[] split1 = url.split("\\/");
//                    resourceInfo.setFilename(split1[split1.length-1]);
//                    resourceInfo.setFilesize(getOnLinkSize(url));
//                    resourceInfo.setImg("https://shstudy.shec.edu.cn/smile-resource/images/upload/cmsAdmin01/20200916/1600418896466422.jpg");
//                    resourceInfo.setName(resource.getResource_name());
//                    resourceInfo.setProvider(resource.getSource());
//                    resourceInfo.setRemoveflag("0");
//                    resourceInfo.setReview("2");
//                    if (ids != null && ids.size() == 1) {
//                        resourceInfo.setSchool(ids.get(0));
//                    }else {
//                        resourceInfo.setSchool("6775");
//                    }
//                    resourceInfo.setSection((String) sectionMap.get(resource.getSection()));
//                    String sub = (String) subjectMap.get(resource.getSubject());
//                    resourceInfo.setSubject(StringUtils.isEmpty(sub) ? "29" : sub);
//                    String[] split2 = split1[split1.length - 1].split("\\.");
//                    String type = (String) typeMap.get(split2[split2.length-1]);
//                    resourceInfo.setType(StringUtils.isEmpty(type) ? "12" : type);
//                    resourceInfo.setCreator(1L);
//                    resourceInfo.setCreatedTime(now);
//                    resourceInfo.setUpdator(1L);
//                    resourceInfo.setUpdatedTime(now);
//                    resourceInfo.setUrl(url);
//                    resourceInfos.add(resourceInfo);
////                    resourceInfoMapper.insert(resourceInfo);
//                }
//                resource.setStatus("1");
//                resourceMapper.updateByPrimaryKey(resource);
//            } catch (Exception e) {
//                System.out.println(resource);
//            }
//        }
//        resourceInfoMapper.insertList(resourceInfos);
// ****************************************结束***********将已上传资源入库***********************************************


//        ***********************************上传文件到联通云***开始**************************************
        //上传
        List<Resource> resources = resourceMapper.selectByOnLineLinkIsNullAndLocalLink();
          //上传文件
        for (Resource resource : resources) {
            StringBuilder onlineLink = new StringBuilder();
            if (StringUtils.isEmpty(resource.getLocal_link())) {
                continue;
            }
            String[] split = resource.getLocal_link().split(";");
//            if (split.length >= 8) {
//                System.out.println("kk");
//                resource.setLocal_link(null);
//                resourceMapper.updateByPrimaryKey(resource);
//                continue;
//            }
            for (String link : split) {
                try {
                    String s = upLoadFileByLinkName(link);
                    onlineLink.append(s).append(";");
                } catch (FileNotFoundException e) {
                    onlineLink = new StringBuilder("1");
                    break;
                }
            }
            resource.setOnline_link(onlineLink.toString());
            resourceMapper.updateByPrimaryKey(resource);
        }
//        ***********************************上传文件到联通云***结束**************************************
//
//        ****************************开始*******读取浦东新区的本地链接，上传**************************************
//        String area = "浦东新区";
//        //获取浦东新区所有没有本地链接的资源
//        List<Resource> resourceList = resourceMapper.selectByArea(area);
//        //获取浦东新区资源对应的编号
//        List<ResourceNamePuDongXinQu> resourceNamePuDongXinQus = resourceNamePuDongXinQuMapper.selectAll();
//        //获取浦东新区本地链接
//        List<LinkPuDongXinQu> linkPuDongXinQus = linkPuDongXinQuMapper.selectAll();
//        for (Resource resource : resourceList) {
//            String url = "";
//            int falg = 0;
//            for (ResourceNamePuDongXinQu namePuDongXinQus : resourceNamePuDongXinQus) {
//                if (resource.getResource_name().equals(namePuDongXinQus.getReourceName())) {
//                    falg++;
//                    url = "资源文件-" + namePuDongXinQus.getId() + ".";
//                    if (falg == 2) {
//                        break;
//                    }
//                }
//            }
//            if (StringUtils.isEmpty(url) || falg > 1) {
//                continue;
//            }
//            for (LinkPuDongXinQu puDongXinQus : linkPuDongXinQus) {
//                if (puDongXinQus.getFileName().contains(url)) {
//                    resource.setLocal_link(puDongXinQus.getLocalLink());
//                    resourceMapper.updateByPrimaryKey(resource);
//                    break;
//                }
//            }
//        }
//        ****************************结束*******读取浦东新区的本地链接，上传**************************************

//        ****************************结束*******获取浦东新区的本地链接存到数据库的匹配表LinkPudongxinqu**************************************
        //先写文件，将文件手动放到数据库表中
//        readFileName("F:\\万达信息\\微校资源复制\\各区汇总等多个文件\\各区汇总\\浦东新区\\浦东教育教学资源II\\浦东资源资源文件20200220");
//
//        //读表
//        List<LinkPuDongXinQu> links = linkPuDongXinQuMapper.selectAll();
//        for (LinkPuDongXinQu link : links) {
//            String[] split = link.getLocalLink().split("\\\\");
//            link.setFileName(split[split.length-1]);
//            linkPuDongXinQuMapper.updateByPrimaryKey(link);
//        }
//        ****************************结束*******获取浦东新区的本地链接存到数据库的匹配表LinkPudongxinqu**************************************
    }

    @Test
    void puDongXinQuSetLocalLink(){
        //读取本地excel
        String fileUrl = "D:\\yinminxin\\项目需求\\2020\\资源上传\\pudongxinqu.xlsx";
        try (InputStream inputStream = new FileInputStream(new File(fileUrl))) {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer,String>>(){
                int total = 0;
                private List<Map<Integer,String>> data = new ArrayList<>();
                @Override
                public void invoke(Map<Integer,String> resourceLocalLinkVO, AnalysisContext analysisContext) {
//                    total++;
                    data.add(resourceLocalLinkVO);
                }
                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    List<String> ids = new ArrayList<>();
                    for (Map<Integer, String> datum : data) {
//                        String id = datum.get(0);
                        ids.add(datum.get(0));
                    }
                    List<Resource> resources = resourceMapper.selectByIdsAndLocalLinkIsNull(ids);
                    List<LinkPuDongXinQu> linkPuDongXinQus = linkPuDongXinQuMapper.selectAll();
                    System.out.println(resources);
                    for (Resource resource : resources) {
                        String idStr = resource.getId().toString();
                        List<Map<Integer, String>> collect = data.stream().filter(v -> idStr.equals(v.get(0))).collect(Collectors.toList());
                        Map<Integer, String> integerStringMap = collect.get(0);
                        String[] splitNum = integerStringMap.get(1).split(";");
                        if (splitNum.length == 0) {
                            System.out.println("laji");
                            continue;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < splitNum.length; i++) {
                            String url = "资源文件-" + splitNum[i] + ".";
                            for (LinkPuDongXinQu puDongXinQus : linkPuDongXinQus) {
                                if (puDongXinQus.getFileName().contains(url)) {
                                    sb.append(puDongXinQus.getLocalLink()).append(";");
                                    break;
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(sb.toString())) {
                            resource.setLocal_link(sb.toString());
                            resourceMapper.updateByPrimaryKey(resource);
                            total++;
                        }
                    }
                    System.out.println("******************************total更新成功条数："+ total +"****************************");
                }
            }).doReadAll();
            System.out.println("*****************************************************************结束*******************************************************************");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void moreToOne(){
        //获取resource表中locallink不为空且onlink不为1的数据
        List<Resource> resourceList = resourceMapper.selectByOnLineLinkIsNotNullAndLocalLink();
        //所有可能重复的线上文件名
        List<String> objects = new ArrayList<>();
        List<OnLink> onLinks = new ArrayList<>();
        for (Resource resource : resourceList) {
            //获取locallink和onlink
            String local_link = resource.getLocal_link();
            String online_link = resource.getOnline_link();
            String school = resource.getSchool();
            //根据；切割，获取到两个数组
            String[] local_link_split = local_link.split(";");
            String[] online_link_split = online_link.split(";");
            if (local_link_split.length == online_link_split.length) {
                //匹配两个数组长度相同的，根据locallink查模糊查找数据库是否有多条数据
                for (int i = 0; i < local_link_split.length; i++) {
                    String[] local_link_split1 = local_link_split[i].split("\\\\");
                    int count = resourceMapper.selectByLocalLinkLike(local_link_split1[local_link_split1.length-1]);
                    if (count > 1) {
                        //如果有，拿到对应的onlink
                        String[] split = online_link_split[i].split("/");
                        objects.add(split[split.length-1]);
                        OnLink onLink = new OnLink();
                        onLink.setFileName(split[split.length-1]);
                        onLink.setLocalLink(local_link_split[i]);
                        onLink.setSchool(school);
                        onLink.setArea(resource.getArea());
                        onLink.setSection(resource.getSection());
                        onLink.setSubject(resource.getSubject());
                        onLinks.add(onLink);
//                        String fileNames = "D:\\yinminxin\\项目需求\\2020\\fileNamesOnLink.txt";
//                        try {
//                            writeFile(split[split.length-1],fileNames);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }else {
                System.out.println("11111");
            }
        }
        onLinkMapper.insertList(onLinks);
        System.out.println(objects);
    }

    /**
     * 根据线上链接获取文件大小
     * @param onlink
     * @return
     */
    private static String getOnLinkSize(String onlink){
        try {
            URL url = new URL(onlink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> strings = headerFields.get("Content-Length");
            String s = strings.get(0);
            String fileSize = getFileSize(s);
            System.out.println(fileSize);
            return fileSize;
        } catch (IOException e) {
            e.printStackTrace();
            return "未知";
        }
    }



    /**
     * 上传文件
     * @param linkName 文件本地路径
     * @return 文件线上链接
     * @throws FileNotFoundException
     */
    private static String upLoadFileByLinkName(String linkName) throws FileNotFoundException {
        File file = new File(linkName);

        if (!file.exists()) {
            linkName = linkName.replace("F:\\万达信息\\微校资源复制\\各区汇总等多个文件\\各区汇总\\嘉定区","F:\\万达信息\\微校资源复制\\嘉定压缩包\\嘉定区");
            file = new File(linkName);
            if (!file.exists()) {
                return "1";
            }
        }

        //上传到联通云
        String fileName = file.getName();
        boolean flag = checkFileSize(file.length(), 20, "M");
        String[] split = fileName.split("\\.");
        String fileType = split[1];


        InputStream inputStream;
        String onlineLink = "";
        try {
            inputStream = new FileInputStream(file);
            if (flag) {
                onlineLink = AliyunOSSClientUtil.uploadObject2OSS(AliyunOSSClientUtil.getOSSClient(), inputStream, fileName, AliyunOSSClientUtil.BACKET_NAME, fileType);
            } else {
                onlineLink = AliyunOSSClientUtil.fileUpload(AliyunOSSClientUtil.getOSSClient(),file, AliyunOSSClientUtil.BACKET_NAME, fileType);
            }
            if (StringUtils.isBlank(onlineLink)) {
                return "1";
            }else {
                return onlineLink;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "1";
        }
    }

    /**
     * 获取文件夹下所有文件的全路径
     * @param dirName
     */
    private static void readFileName(String dirName){
        File file = new File(dirName);
        File[] files = file.listFiles();
        if (files == null || files.length <=0) {
            return;
        }

        for (File file1 : files) {
            if (file1.isDirectory()) {
                readFileName(file1.getAbsolutePath());
            }else if (file1.isFile()){
                System.out.println(file1.getAbsolutePath());
                try {
                    //将全路径写到本地的文件中
                    String fileNames = "D:\\yinminxin\\项目需求\\2020\\fileNamesPudongxinqu.txt";
                    writeFile(file1.getAbsolutePath(),fileNames);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                sb.append(file1.getAbsolutePath());
            }
        }
//        String[] list = file.list();
//        for (String s : list) {
//            System.out.println(s);
//        }
    }

    private static void writeFile(String text,String fileNames) throws IOException {
        File file = new File(fileNames);

        FileWriter writer = null;
        BufferedWriter out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file,true);
            out = new BufferedWriter(writer);
            out.write(text + "\r\n");
            out.flush(); // 把缓存区内容压入文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            writer.close();
        }

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

    /**
     * 判断文件大小
     * @param len 文件大小
     * @return
     */
    public static String getFileSize(String len) {
        String result;
        Long size = Long.valueOf(len);
        if (size > 1073741824) {

            BigDecimal b = new BigDecimal((double) size / 1073741824);
            double v = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return v + "G";
        } else if (size > 1048576) {
            BigDecimal b = new BigDecimal((double) size / 1048576);
            double v = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return v + "M";
        } else if (size > 1024) {
            BigDecimal b = new BigDecimal((double) size / 1024);
            double v = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return v + "K";
        }else {
            return len + "B";
        }
    }

    /**
     * 匹配数据
     */
    public static void match(){

        //        List<Link> links = linkMapper.selectAll();

//        for (Link link : links) {
//            String[] split = link.getLocalLink().split("\\\\");
//            link.setFileName(split[split.length-1]);
//            linkMapper.updateByPrimaryKey(link);
//        }


        //匹配
//        List<Resource> resources = resourceMapper.selectByOnLineLinkIsNull();
        ////        匹配成功条数
//        int num = 0;
//
//        //匹配数据
//        for (Resource resource : resources) {
//            int count = 0;
//            StringBuilder sb = new StringBuilder();
//            String resourceNnameFull = resource.getResource_name().trim();
//            String resource_name = resourceNnameFull.replaceAll("\\s*", "").replaceAll("》", "").replaceAll("《", "");
//
//            StringBuilder resourceNameTemp = new StringBuilder();
//            for (int i = 0; i < resource_name.length(); i++) {
//                if (resource_name.charAt(i) >= 0x4E00 && resource_name.charAt(i) <= 0x29FA5) {
//                    //中文
//                    resourceNameTemp.append(resource_name.charAt(i));
//                }
//            }
//
//            if (resourceNnameFull.length() <= 2 || resource_name.length() <= 2 || resourceNameTemp.length() <= 2) {
//                resource.setLocal_link(null);
//                resourceMapper.updateByPrimaryKey(resource);
//                continue;
//            }
//
//            //先根据教师模糊匹配
//            for (Link link : links) {
//                if (link.getFileName().contains(resourceNnameFull) || resourceNnameFull.trim().contains(link.getFileName())) {
//                    //如果本地链接中包含教师名
//                    //判断教师是否符合
//                    if (link.getLocalLink().contains(resource.getSource())) {
//                        count++;
//                        sb.append(link.getLocalLink()).append(";");
//                    }
//                }
//            }
//
//            //先根据区模糊匹配
//            if (count == 0) {
//                for (Link link : links) {
//                    if (link.getFileName().contains(resourceNnameFull) || resourceNnameFull.contains(link.getFileName())) {
//                        //如果本地链接中包含区
//                        if (link.getLocalLink().contains("嘉定区") || link.getLocalLink().contains("青浦区")
//                                || link.getLocalLink().contains("浦东新区") || link.getLocalLink().contains("长宁区")
//                                || link.getLocalLink().contains("杨浦区") || link.getLocalLink().contains("个人")) {
//                            //判断区是否符合
//                            if (link.getLocalLink().contains(resource.getArea())) {
//                                count++;
//                                sb.append(link.getLocalLink()).append(";");
//                            }
//                        }
//                    }
//                }
//            }
//
//            //再直接模糊匹配
//            if (count == 0) {
//                for (Link link : links) {
//                    if (link.getFileName().contains(resourceNnameFull) || resourceNnameFull.contains(link.getFileName())) {
//                        count++;
//                        sb.append(link.getLocalLink()).append(";");
//                    }
//                }
//            }
//
//            if (count == 0) {
//                //没有匹配上
////                String resource_name = resource.getResource_name().replaceAll("\\s*", "");
//                for (int i = 0; i < resource_name.length(); i++) {
//                    //是否有中文
//                    if (resource_name.charAt(i) >= 0x4E00 && resource_name.charAt(i) <= 0x29FA5) {
//                        //有中文
//                        count++;
//                    }
//                }
//                if (count == 0) {
//                    //英文，什么也不做
//                } else {
//                    //有中文
//                    for (Link link : links) {
//                        boolean falg = true;
//                        for (int i = 0; i < resource_name.length(); i++) {
//                            if (resource_name.charAt(i) >= 0x4E00 && resource_name.charAt(i) <= 0x29FA5) {
//                                count++;
//                                //中文
//                                if (!link.getFileName().contains(String.valueOf(resource_name.charAt(i)))) {
//                                    falg = false;
//                                    break;
//                                }
//                            }
//                        }
//                        if (falg && count > 0) {
//                            //true,匹配上了
//                            sb.append(link.getLocalLink()).append(";");
//                        }
//                    }
//                }
//
//            }
//
//
//            if (StringUtils.isNotEmpty(sb)) {
//                num++;
//                resource.setLocal_link(sb.toString());
//            } else {
//                resource.setLocal_link(null);
//            }
//            resourceMapper.updateByPrimaryKey(resource);
//        }
//        System.out.println(num);
    }





}
