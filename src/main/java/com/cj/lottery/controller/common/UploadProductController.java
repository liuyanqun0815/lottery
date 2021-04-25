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
import com.cj.lottery.enums.ImgTypeEnum;
import com.cj.lottery.util.EasyPoiUtils;
import com.cj.lottery.util.UuidUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("product/upload")
    public Boolean upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setStartSheetIndex(1);
        List<ProductUploadParam> result = EasyPoiUtils.importExcel(multipartFile.getInputStream(),
                ProductUploadParam.class, params);
        List<CjProductInfo> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(result)){
            return true;
        }
        List<CjProductInfo> finalList = list;
        List<CjLotteryActivity> activityList = cjLotteryActivityDao.getAllActivities();
        Map<String, CjLotteryActivity> activityNameMap = activityList.stream().collect(Collectors.toMap(CjLotteryActivity::getActivityName, Function.identity()));
        result.stream().forEach(s->{
            CjProductInfo productInfo = new CjProductInfo();
            BeanUtils.copyProperties(s,productInfo);
            productInfo.setCallbackRate(80);
            productInfo.setProductCode(UuidUtils.getTraceUUid());
            finalList.add(productInfo);
        });



        Map<String, CjProductInfo> collect = list.stream().collect(Collectors.toMap(CjProductInfo::getProductName, Function.identity()));
        List<CjProductInfo> all = cjProductInfoDao.selectAll();
        List<String> nameList = all.stream().map(CjProductInfo::getProductName).collect(Collectors.toList());
        //新增的批量保存
        if (true){
            Map<String, CjProductInfo> ProductMap = all.stream().collect(Collectors.toMap(CjProductInfo::getProductName, Function.identity()));
            for(CjProductInfo info : list){
                CjProductInfo productInfo = ProductMap.get(info.getProductName());
                if (productInfo != null){
                    info.setId(productInfo.getId());
                    cjProductInfoDao.updateByPrimaryKeySelective(info);
                }
            }
            return true;
        }
        list = list.stream().filter(s->!nameList.contains(s.getProductName())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            for(CjProductInfo info : list){
                cjProductInfoDao.insertSelective(info);
                CjLotteryActivity activity = activityNameMap.get(info.getActivityName());
                if (activity != null){
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
                    pool.setProductLatestNum(info.getGetFlag() == 1?productNum:0);
                    pool.setProductNum(info.getGetFlag() == 1?productNum:0);
                    prizePoolDao.insertSelective(pool);
                }

            }
        }
        this.activityUpload(multipartFile);
        return true;
    }


    @PostMapping("activity/upload")
    public Boolean activityUpload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setStartSheetIndex(0);
        List<ActivityUploadParam> result = EasyPoiUtils.importExcel(multipartFile.getInputStream(),
                ActivityUploadParam.class, params);
        if (CollectionUtils.isEmpty(result)){
            return true;
        }
        List<CjLotteryActivity> activities = Lists.newArrayList();
        List<CjLotteryActivity> finalActivities = activities;
        result.stream().forEach(s->{
            CjLotteryActivity activity = new CjLotteryActivity();
            BeanUtils.copyProperties(s,activity);
            activity.setActivityCode(UuidUtils.getUUid());
            finalActivities.add(activity);
        });
        List<CjLotteryActivity> newPeopleActivities = cjLotteryActivityDao.getAllActivities();
        List<String> nameList = newPeopleActivities.stream().map(CjLotteryActivity::getActivityName).collect(Collectors.toList());
        activities = activities.stream().filter(s->!nameList.contains(s.getActivityName())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(activities)){
            for(CjLotteryActivity activity : activities) {
                int i = cjLotteryActivityDao.insertSelective(activity);
            }
        }
        return true;
    }


}
