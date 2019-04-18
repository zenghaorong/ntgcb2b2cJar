package com.aebiz.app.web.modules.controllers.open.ueditor;

import com.aebiz.app.shop.modules.models.Shop_image;
import com.aebiz.app.shop.modules.services.ShopImageService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.utils.FtpTool;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.fastdfs.NameValuePair;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.fastdfs.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.filepool.NutFilePool;
import org.nutz.img.Images;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 百度编辑器的上传类
 * Created by Wizzer on 2016/7/9.
 */
@Controller
@RequestMapping("/open/ueditor/bd")
public class BdController {
    @Autowired
    private PropertiesProxy config;
    @Autowired
    private ShopImageService shopImageService;
    @Autowired
    private NutFilePool nutFilePool;

    @RequestMapping("/index")
    @SJson
    public Object index(@RequestParam(value = "action", required = false) String action, HttpServletRequest req) {
        return Json.fromJson(Files.read(Globals.APP_ROOT + "/assets/platform/vendor/ueditor/nutz/config.json").replace("$base", Globals.APP_BASE));
    }

    @RequiresAuthentication
    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
    @SJson
    public Object uploadimage(@RequestParam("Filedata") MultipartFile tf) {
        NutMap nutMap = new NutMap();
        if (!tf.isEmpty()) {
            List<String> imageList = config.getList("upload.suffix.image", ",");
            if (imageList.contains(tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1))) {
                TrackerServer trackerServer = null;
                TrackerClient tracker = null;
                StorageServer storageServer = null;
                try {
                    String uploadMode = config.get("upload.mode", "file");
                    if ("fdfs".equalsIgnoreCase(uploadMode)) {
                        //如果使用文件服务器
                        String fileName = tf.getOriginalFilename();
                        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        byte[] fileBuff = tf.getBytes();
                        ClientGlobal.init(config);
                        //建立连接
                        tracker = new TrackerClient();
                        trackerServer = tracker.getConnection();
                        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
                        //设置元信息
                        NameValuePair[] metaList = new NameValuePair[3];
                        metaList[0] = new NameValuePair("fileName", fileName);
                        metaList[1] = new NameValuePair("fileExtName", fileExtName);
                        metaList[2] = new NameValuePair("fileLength", String.valueOf(tf.getSize()));
                        //上传文件
                        String fileId = client.upload_file1(fileBuff, fileExtName, metaList);
                        File file = nutFilePool.createFile("." + fileExtName);
                        Files.write(file, fileBuff);
                        nutMap.addv("state", "SUCCESS");
                        nutMap.addv("url", config.get("fdfs.domain", "") + "/" + fileId);
                        nutMap.addv("original", tf.getOriginalFilename());
                        nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                        nutMap.addv("size", tf.getSize());
                        return nutMap;
                    }else if("ftp".equalsIgnoreCase(uploadMode)){
                        String fileName = tf.getOriginalFilename();
//                        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        String ftpHost = config.get("ftp.Host");
                        String ftpUserName = config.get("ftp.UserName");
                        String ftpPassword = config.get("ftp.Password");
                        int ftpPort = Integer.getInteger(config.get("ftp.Port","22"));
                        String ftpPath = config.get("ftp.Path");
                        InputStream in=  tf.getInputStream();
                        boolean test = FtpTool.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName,in);
                    } else {
                        String p = Globals.APP_ROOT;
                        String f = Globals.APP_UPLOAD_PATH + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf("."));
                        tf.transferTo(Files.createFileIfNoExists(p + f));
                        nutMap.addv("state", "SUCCESS");
                        nutMap.addv("url", Globals.APP_BASE + f);
                        nutMap.addv("original", tf.getOriginalFilename());
                        nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                        nutMap.addv("size", tf.getSize());
                        return nutMap;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return nutMap.addv("state", "FAIL");
                } finally {
                    try {
                        if (trackerServer != null)
                            trackerServer.close();
                    } catch (Exception e) {

                    }
                }
            } else {
                return nutMap.addv("state", "FAIL");
            }
        }
        return nutMap.addv("state", "FAIL");
    }

    @RequiresAuthentication
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @SJson
    public Object uploadfile(@RequestParam("Filedata") MultipartFile tf) {
        NutMap nutMap = new NutMap();
        if (!tf.isEmpty()) {
            List<String> imageList = config.getList("upload.suffix.file", ",");
            if (imageList.contains(tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1))) {
                TrackerServer trackerServer = null;
                TrackerClient tracker = null;
                StorageServer storageServer = null;
                try {
                    String uploadMode = config.get("upload.mode", "file");
                    if ("fdfs".equalsIgnoreCase(uploadMode)) {
                        //如果使用文件服务器
                        String fileName = tf.getOriginalFilename();
                        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        byte[] fileBuff = tf.getBytes();
                        ClientGlobal.init(config);
                        //建立连接
                        tracker = new TrackerClient();
                        trackerServer = tracker.getConnection();
                        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
                        //设置元信息
                        NameValuePair[] metaList = new NameValuePair[3];
                        metaList[0] = new NameValuePair("fileName", fileName);
                        metaList[1] = new NameValuePair("fileExtName", fileExtName);
                        metaList[2] = new NameValuePair("fileLength", String.valueOf(tf.getSize()));
                        //上传文件
                        String fileId = client.upload_file1(fileBuff, fileExtName, metaList);
                        File file = nutFilePool.createFile("." + fileExtName);
                        Files.write(file, fileBuff);
                        nutMap.addv("state", "SUCCESS");
                        nutMap.addv("url", config.get("fdfs.domain", "") + "/" + fileId);
                        nutMap.addv("original", tf.getOriginalFilename());
                        nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                        nutMap.addv("size", tf.getSize());
                        return nutMap;
                    } else {
                        String p = Globals.APP_ROOT;
                        String f = Globals.APP_UPLOAD_PATH + "/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf("."));
                        tf.transferTo(Files.createFileIfNoExists(p + f));
                        nutMap.addv("state", "SUCCESS");
                        nutMap.addv("url", Globals.APP_BASE + f);
                        nutMap.addv("original", tf.getOriginalFilename());
                        nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                        nutMap.addv("size", tf.getSize());
                        return nutMap;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return nutMap.addv("state", "FAIL");
                } finally {
                    try {
                        if (trackerServer != null)
                            trackerServer.close();
                    } catch (Exception e) {

                    }
                }
            } else {
                return nutMap.addv("state", "FAIL");
            }
        }
        return nutMap.addv("state", "FAIL");
    }

    @RequiresAuthentication
    @RequestMapping(value = "/uploadvideo", method = RequestMethod.POST)
    @SJson
    public Object uploadvideo(@RequestParam("Filedata") MultipartFile tf) {
        NutMap nutMap = new NutMap();
        if (!tf.isEmpty()) {
            List<String> imageList = config.getList("upload.suffix.video", ",");
            if (imageList.contains(tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1))) {
                TrackerServer trackerServer = null;
                TrackerClient tracker = null;
                StorageServer storageServer = null;
                try {
                    String uploadMode = config.get("upload.mode", "file");
                    if ("fdfs".equalsIgnoreCase(uploadMode)) {
                        //如果使用文件服务器
                        String fileName = tf.getOriginalFilename();
                        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        byte[] fileBuff = tf.getBytes();
                        ClientGlobal.init(config);
                        //建立连接
                        tracker = new TrackerClient();
                        trackerServer = tracker.getConnection();
                        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
                        //设置元信息
                        NameValuePair[] metaList = new NameValuePair[3];
                        metaList[0] = new NameValuePair("fileName", fileName);
                        metaList[1] = new NameValuePair("fileExtName", fileExtName);
                        metaList[2] = new NameValuePair("fileLength", String.valueOf(tf.getSize()));
                        //上传文件
                        String fileId = client.upload_file1(fileBuff, fileExtName, metaList);
                        File file = nutFilePool.createFile("." + fileExtName);
                        Files.write(file, fileBuff);
                        nutMap.addv("state", "SUCCESS");
                        nutMap.addv("url", config.get("fdfs.domain", "") + "/" + fileId);
                        nutMap.addv("original", tf.getOriginalFilename());
                        nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                        nutMap.addv("size", tf.getSize());
                        return nutMap;
                    } else {
                        String p = Globals.APP_ROOT;
                        String f = Globals.APP_UPLOAD_PATH + "/video/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf("."));
                        tf.transferTo(Files.createFileIfNoExists(p + f));
                        nutMap.addv("state", "SUCCESS");
                        nutMap.addv("url", Globals.APP_BASE + f);
                        nutMap.addv("original", tf.getOriginalFilename());
                        nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                        nutMap.addv("size", tf.getSize());
                        return nutMap;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return nutMap.addv("state", "FAIL");
                } finally {
                    try {
                        if (trackerServer != null)
                            trackerServer.close();
                    } catch (Exception e) {

                    }
                }
            } else {
                return nutMap.addv("state", "FAIL");
            }
        }
        return nutMap.addv("state", "FAIL");
    }

}
