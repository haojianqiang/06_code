package com.gpdi.web.web.config.farm.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.nav.SysNav;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.service.SysNavService;
import com.gpdi.web.service.config.farm.FarmService;
import com.gpdi.web.service.config.house.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);
    @Autowired
    private HouseService houseService;

    @Autowired
    private FarmService farmService;
    @Value("${filesPath}")
    private String filesPath;   //上传到服务器的真实目录

    @Value("${host}")
    private String host;        //虚拟映射全路径

    @Value("${head}")
    private String head;        //访问路径头部

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings){
        QueryResult result = farmService.query(queryData,settings);
        List<Map<String, Object>> list = result.getRows();
        for (Map<String, Object> map : list) {
            String introduce = (String) map.get("introduce");
            if(introduce != null && !introduce.equals("")){
                introduce = introduce.replaceAll("\\{host\\}",head);
                map.put("introduce",introduce);
            }
        }
        result.setRows(list);
        return result;
    }

    @RequestMapping
    public ActionResult getFarm(Integer farmId) {
        Farm farm= farmService.getFarm(farmId);
        //地址
        String address = farm.getAddress();
        address = address.replace(farm.getCounty(),"");
        address = address.replace(farm.getTown(),"");
        farm.setAddress(address);
        //营业执照
        String businessLicense = farm.getIntelligence();
        businessLicense = head + businessLicense;
        farm.setIntelligence(businessLicense);
        //简介图片地址
        String introduce = farm.getIntroduce();
        introduce = introduce.replaceAll("\\{host\\}",head);
        farm.setIntroduce(introduce);
        return ActionResult.okWithData(farm);
    }

    @RequestMapping
    public ActionResult save(Farm farm,HttpServletRequest request) {
        String imageHead = head; //访问路径头部
        //详细地址
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(farm.getCounty());
        addressBuilder.append(farm.getTown());
        addressBuilder.append(farm.getAddress());
        farm.setAddress(addressBuilder.toString());
        //营业执照图片地址
        String businessLicense = farm.getIntelligence();
        businessLicense = businessLicense.replace(head,"");
        farm.setIntelligence(businessLicense);
        //简介图片地址
        String introduce = farm.getIntroduce();
        introduce = introduce.replaceAll(imageHead,"{host}");
        farm.setIntroduce(introduce);
        if(farm.getId()!= null){
            HttpSession session = request.getSession();
            int userId = (Integer)session.getAttribute("UserId");
            farm.setModifierId(userId);
            farm.setModifyTime(new Date());
        }else {
            farm.setCreateTime(new Date());
        }
        try {
            farmService.save(farm);
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for(Integer id: ids) {
                //删除鸡场前判断此鸡场是否存在鸡棚
                List<House> houseList = houseService.getHouseByFarm_id(id);
                if (houseList.size() == 0) {
                    farmService.delete(id);
                } else {
                    return ActionResult.error("删除失败，此鸡场已分配鸡棚");
                }
            }
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    public String getNameDate() {
        Date date = new Date();//获取系统当前日期
        SimpleDateFormat nowdate = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        return nowdate.format(date);
    }

    @RequestMapping
    public ActionResult uploadImage(MultipartFile file) {
        String uploadDir = filesPath; //上传目录
        String fileUrl = host;//访问路径
        String imageHead = head; //访问路径头部
        String fileName = null;
        try {
            fileName = new String(file.getOriginalFilename().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;//错误
        }
        String suffix = fileName.substring(file.getOriginalFilename().lastIndexOf("."));//文件后缀名
        fileName = this.getNameDate() + suffix;  //上传文件名--用时间代替，加后缀

        File serverFile = new File(uploadDir + fileName);//服务器端保存的文件对象

        // 检测是否存在目录
        if (!serverFile.getParentFile().exists()) {
            serverFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(serverFile); //存入服务器
        } catch (IOException e) {
            e.printStackTrace();
            return null;//错误
        }
        String path = imageHead + fileUrl + serverFile.getName();//成功，返回全路径

        return ActionResult.okWithData(path);
    }

}
