package com.cj.lottery.controller.common;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryActivityImgDao;
import com.cj.lottery.dao.CjPrizePoolDao;
import com.cj.lottery.dao.CjProductInfoDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.CjProductInfo;
import com.cj.lottery.domain.common.ActivityUploadParam;
import com.cj.lottery.domain.common.ProductUploadParam;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.ImgTypeEnum;
import com.cj.lottery.util.EasyPoiUtils;
import com.cj.lottery.util.UuidUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/file")
public class UploadProductController {

    @Autowired
    private CjProductInfoDao cjProductInfoDao;
    @Autowired
    private CjLotteryActivityDao cjLotteryActivityDao;
    @Autowired
    private CjLotteryActivityImgDao cjLotteryActivityImgDao;
    @Autowired
    private CjPrizePoolDao prizePoolDao;

    private int productNum = 200;

    @RequestMapping("product/upload")
    public CjResult upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setStartSheetIndex(1);
        List<ProductUploadParam> result = EasyPoiUtils.importExcel(multipartFile.getInputStream(),
                ProductUploadParam.class, params);
        List<CjProductInfo> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(result)) {
            return CjResult.success();
        }
        List<CjProductInfo> finalList = list;
        List<CjLotteryActivity> activityList = cjLotteryActivityDao.getAllActivities();
        Map<String, CjLotteryActivity> activityNameMap = activityList.stream().collect(Collectors.toMap(CjLotteryActivity::getActivityName, Function.identity()));
        result.stream().forEach(s -> {
            CjProductInfo productInfo = new CjProductInfo();
            BeanUtils.copyProperties(s, productInfo);
            productInfo.setCallbackRate(80);
            productInfo.setProductCode(UuidUtils.getTraceUUid());
            finalList.add(productInfo);
        });


//        Map<String, CjProductInfo> collect = list.stream().collect(Collectors.toMap(CjProductInfo::getProductName, Function.identity()));
        List<CjProductInfo> all = cjProductInfoDao.selectAll();
        List<String> nameList = all.stream().map(CjProductInfo::getProductName).collect(Collectors.toList());
        //新增的批量保存
//        if (true){
//            Map<String, CjProductInfo> ProductMap = all.stream().collect(Collectors.toMap(CjProductInfo::getProductName, Function.identity()));
//            for(CjProductInfo info : list){
//                CjProductInfo productInfo = ProductMap.get(info.getProductName());
//                if (productInfo != null){
//                    info.setId(productInfo.getId());
//                    cjProductInfoDao.updateByPrimaryKeySelective(info);
//                }
//            }
//            return CjResult.success();
//        }
        list = list.stream().filter(s -> !nameList.contains(s.getProductName())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            for (CjProductInfo info : list) {
                if (StringUtils.isEmpty(info.getProductName()) || StringUtils.isEmpty(info.getProductImgUrl())
                        || StringUtils.isEmpty(info.getPrice()) || StringUtils.isEmpty(info.getProductFlag())) {
                    continue;
                }
                cjProductInfoDao.insertSelective(info);
                CjLotteryActivity activity = activityNameMap.get(info.getActivityName());
                if (activity != null) {
                    CjLotteryActivityImg img = new CjLotteryActivityImg();
                    img.setImgUrl(info.getLunboUrl());
                    img.setActivityId(activity.getId());
                    img.setProductId(info.getId());
                    img.setType(ImgTypeEnum.LUN_BO.getCode());

                    cjLotteryActivityImgDao.insertSelective(img);
                    CjLotteryActivityImg imginfo = new CjLotteryActivityImg();
                    imginfo.setImgUrl(info.getActivityInfoUrl());
                    imginfo.setProductId(info.getId());
                    imginfo.setActivityId(activity.getId());
                    imginfo.setType(ImgTypeEnum.IMG_BODY.getCode());
                    cjLotteryActivityImgDao.insertSelective(imginfo);

                    CjPrizePool pool = new CjPrizePool();
                    pool.setProductId(info.getId());
                    pool.setActivityId(activity.getId());
                    pool.setProductLatestNum(info.getGetFlag() == 1 ? productNum : 0);
                    pool.setProductNum(info.getGetFlag() == 1 ? productNum : 0);
                    prizePoolDao.insertSelective(pool);
                }

            }
        }
        this.activityUpload(multipartFile);
        return CjResult.success();
    }


    @PostMapping("activity/upload")
    public CjResult activityUpload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setStartSheetIndex(0);
        List<ActivityUploadParam> result = EasyPoiUtils.importExcel(multipartFile.getInputStream(),
                ActivityUploadParam.class, params);
        if (CollectionUtils.isEmpty(result)) {
            return CjResult.success();
        }
        List<CjLotteryActivity> activities = Lists.newArrayList();
        List<CjLotteryActivity> finalActivities = activities;
        result.stream().forEach(s -> {
            CjLotteryActivity activity = new CjLotteryActivity();
            BeanUtils.copyProperties(s, activity);
            activity.setActivityCode(UuidUtils.getUUid());
            finalActivities.add(activity);
        });
        List<CjLotteryActivity> newPeopleActivities = cjLotteryActivityDao.getAllActivities();
        List<String> nameList = newPeopleActivities.stream().map(CjLotteryActivity::getActivityName).collect(Collectors.toList());
        activities = activities.stream().filter(s -> !nameList.contains(s.getActivityName())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(activities)) {
            for (CjLotteryActivity activity : activities) {
                if (StringUtils.isEmpty(activity.getActivityName()) || StringUtils.isEmpty(activity.getConsumerMoney())) {
                    continue;
                }
                int i = cjLotteryActivityDao.insertSelective(activity);
            }
        }
        return CjResult.success();
    }

    @RequestMapping("file-upload")
    public CjResult fileUpload(@RequestParam("file") MultipartFile[] files) throws Exception {
        // 1. 用数组MultipartFile[]来表示多文件,所以遍历数组,对其中的文件进行逐一操作
        for (MultipartFile file : files) {
            // 2. 通过一顿file.getXXX()的操作,获取文件信息。
            // 2.1 这里用文件名举个栗子
            String filename = file.getOriginalFilename();
            // 3. 接下来调用方法来保存文件到本地磁盘,返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            // 4. 保存文件信息到数据库
            // 4.1 搞个实体类，把你需要的文件信息保存到实体类中
            // 4.2 调用Service层或者Dao层，保存数据库即可。
        }

        return CjResult.success();
    }


    public static String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 你的项目路径/FileSpace  也就是和src同级
//        String fileSpace = System.getProperty("user.dir") + File.separator + "FileSpace";
        String fileSpace = "/usr/share/tomcat/webapps/cos/h5_img";
//        String fileSpace ="/Users/liuyanqun/Downloads";
        String[] split = fileName.split("-");
        String path = fileSpace;
        if (split.length>2){
            path = path+"/"+split[0]+"/"+split[1]+"/"+fileName;
        }else {
            path = path + fileName;
        }
        // 判断父文件夹是否存在
        File file = new File(path);
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


}
