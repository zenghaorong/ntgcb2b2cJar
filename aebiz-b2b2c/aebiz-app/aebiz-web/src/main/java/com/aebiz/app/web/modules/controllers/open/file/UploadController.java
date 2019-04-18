package com.aebiz.app.web.modules.controllers.open.file;

import com.aebiz.app.shop.modules.models.Shop_image;
import com.aebiz.app.shop.modules.services.ShopImageService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.utils.FtpTool;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.fastdfs.NameValuePair;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.fastdfs.*;
import org.nutz.filepool.NutFilePool;
import org.nutz.img.Images;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.upload.TempFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/5.
 */
@Controller
@RequestMapping("/open/file/upload")
public class UploadController {
    private static final Log log = Logs.get();
    @Autowired
    private PropertiesProxy config;
    @Autowired
    private ShopImageService shopImageService;
    @Autowired
    private NutFilePool nutFilePool;

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @SJson
    public Object image(@RequestParam("Filedata") MultipartFile tf, @RequestParam(value = "type",required = false) String type) {
        if (!tf.isEmpty()) {
            List<String> imageList = config.getList("upload.suffix.image", ",");
            if (imageList.contains(tf.getOriginalFilename().substring(tf.getOriginalFilename().lastIndexOf(".") + 1))) {
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
                        if ("pc".equalsIgnoreCase(type)) {
                            File t = nutFilePool.createFile("." + fileExtName);
                            File l = nutFilePool.createFile("." + fileExtName);
                            File a = nutFilePool.createFile("." + fileExtName);
                            Shop_image pcImage = shopImageService.getImage("pc");
                            Images.zoomScale(file, t, pcImage.getImgAlbum_width(), pcImage.getImgAlbum_height(), Color.WHITE);
                            Images.zoomScale(file, l, pcImage.getImgList_width(), pcImage.getImgList_height(), Color.WHITE);
                            Images.zoomScale(file, a, pcImage.getImgAlbum_width(), pcImage.getImgAlbum_height(), Color.WHITE);
                            uploadSlaveFile(client, fileId, "_pc_thumb", t.getAbsolutePath());
                            uploadSlaveFile(client, fileId, "_pc_list", l.getAbsolutePath());
                            uploadSlaveFile(client, fileId, "_pc_album", a.getAbsolutePath());
                        }
                        if ("wap".equalsIgnoreCase(type)) {
                            File t = nutFilePool.createFile(fileExtName);
                            File l = nutFilePool.createFile(fileExtName);
                            File a = nutFilePool.createFile(fileExtName);
                            Shop_image wapImage = shopImageService.getImage("wap");
                            Images.zoomScale(file, t, wapImage.getImgAlbum_width(), wapImage.getImgAlbum_height(), Color.WHITE);
                            Images.zoomScale(file, l, wapImage.getImgList_width(), wapImage.getImgList_height(), Color.WHITE);
                            Images.zoomScale(file, a, wapImage.getImgAlbum_width(), wapImage.getImgAlbum_height(), Color.WHITE);
                            uploadSlaveFile(client, fileId, "_wap_thumb", t.getAbsolutePath());
                            uploadSlaveFile(client, fileId, "_wap_list", l.getAbsolutePath());
                            uploadSlaveFile(client, fileId, "_wap_album", a.getAbsolutePath());

                        }
                        if ("tv".equalsIgnoreCase(type)) {
                            File t = nutFilePool.createFile(fileExtName);
                            File l = nutFilePool.createFile(fileExtName);
                            File a = nutFilePool.createFile(fileExtName);
                            Shop_image tvImage = shopImageService.getImage("tv");
                            Images.zoomScale(file, t, tvImage.getImgAlbum_width(), tvImage.getImgAlbum_height(), Color.WHITE);
                            Images.zoomScale(file, l, tvImage.getImgList_width(), tvImage.getImgList_height(), Color.WHITE);
                            Images.zoomScale(file, a, tvImage.getImgAlbum_width(), tvImage.getImgAlbum_height(), Color.WHITE);
                            uploadSlaveFile(client, fileId, "_wap_thumb", t.getAbsolutePath());
                            uploadSlaveFile(client, fileId, "_wap_list", l.getAbsolutePath());
                            uploadSlaveFile(client, fileId, "_wap_album", a.getAbsolutePath());
                        }
                        return Result.success("globals.upload.success", config.get("fdfs.domain", "") + "/" + fileId);
                    } else if("ftp".equalsIgnoreCase(uploadMode)){
                        String fileName = tf.getOriginalFilename();
//                        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        String ftpHost = config.get("ftp.Host");
                        String ftpUserName = config.get("ftp.UserName");
                        String ftpPassword = config.get("ftp.Password");
                        int ftpPort = Integer.getInteger(config.get("ftp.Port","22"));
                        String ftpPath = config.get("ftp.Path");
                        InputStream in=  tf.getInputStream();
                        boolean test = FtpTool.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName,in);
                        if(test){
                            log.info(".....上传成功....:"+fileName);
                        }
                    }else {
                        String p = Globals.APP_ROOT;
                        String f = Globals.APP_UPLOAD_PATH + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getOriginalFilename().substring(tf.getOriginalFilename().lastIndexOf("."));
                        tf.transferTo(Files.createFileIfNoExists(p + f));
                        return Result.success("globals.upload.success", "http://" + Globals.APP_DOMAIN + Globals.APP_BASE + f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.error("globals.upload.error");
                } finally {
                    try {
                        if (trackerServer != null)
                            trackerServer.close();
                    } catch (Exception e) {

                    }
                }
            } else {
                return Result.error("globals.upload.suffix");
            }
        }
        return Result.error("globals.upload.fail");
    }

    public static String uploadSlaveFile(StorageClient1 client, String masterFileId, String prefixName, String slaveFilePath) throws Exception {
        String slaveFileId = "";
        String slaveFileExtName = "";
        if (slaveFilePath.contains(".")) {
            slaveFileExtName = slaveFilePath.substring(slaveFilePath.lastIndexOf(".") + 1);
        } else {
            log.warn("Fail to upload file, because the format of filename is illegal.");
            return slaveFileId;
        }
        slaveFileId = client.upload_file1(masterFileId, prefixName, slaveFilePath, slaveFileExtName, null);
        return slaveFileId;
    }

}
