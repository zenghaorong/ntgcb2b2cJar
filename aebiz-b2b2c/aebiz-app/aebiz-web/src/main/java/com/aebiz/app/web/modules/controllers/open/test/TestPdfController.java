package com.aebiz.app.web.modules.controllers.open.test;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.view.annotation.SFile;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfStamper;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.util.Callback;
import org.nutz.lang.util.Context;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by wizzer on 2017/3/31.
 */
@Controller
@RequestMapping("/open/test/sfile")
public class TestPdfController {
    private static final Log log = Logs.get();
    @Autowired
    private RedisService redisService;

    /**
     * PNG文件下载
     *
     * @return
     */
    @RequestMapping("/png")
    @SFile("png")
    public Object png() {
        return new File(Globals.APP_ROOT + "/assets/platform/images/logo.png");
    }

    @RequestMapping("/css")
    @SFile("css")
    public Object css(HttpServletResponse response) {
        String filename="test.css";
        response.setContentType("text/css");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        try (Jedis jedis = redisService.jedis()) {
            byte[] keyb = "css".getBytes();
            return jedis.get(keyb);
        }
    }

    @RequestMapping("/css_save")
    @SJson
    public Object css_save() {
        try {
            File f = new File(Globals.APP_ROOT + "/assets/platform/styles/panel.css");
            try (Jedis jedis = redisService.jedis()) {
                byte[] keyb = "css".getBytes();
                byte[] valueb = Lang.toBytes(Files.readAllBytes(f.toPath()));
                jedis.set(keyb, valueb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    /**
     * PND文件字节流输出
     *
     * @return
     */
    @RequestMapping("/png2")
    @SFile("png")
    public Object png2() {
        try {
            File f = new File(Globals.APP_ROOT + "/assets/platform/images/logo.png");
            return Files.readAllBytes(f.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * PDF文件输出
     *
     * @param req
     * @return
     */
    @RequestMapping("/pdf")
    @SFile("pdf")
    public Object downloadPayPdf(HttpServletRequest req) {
        return doPdf("1", false, req);
    }

    /**
     * PDF文件预览
     *
     * @param req
     * @return
     */
    @RequestMapping("/pdfview")
    @SFile("pdf")
    public Object viewPayPdf(HttpServletRequest req) {
        return doPdf("2", true, req);
    }

    private Object doPdf(String out_trace_no, boolean viewOnly, HttpServletRequest req) {
        Context cnt = Lang.context();
        cnt.set("tpl", "pdftpl/beepay_one.pdf");//设置模版
        cnt.set("*viewOnly", viewOnly);
        cnt.set("payment_fromUser", "大鲨鱼");
        cnt.set("payment_toUser", "小虾米");
        cnt.set("payment_fee", String.format("%.1f", 100.2));
        cnt.set("payment_time", DateUtil.getDateTime());
        cnt.set("filename", "pdftest_tips_" + out_trace_no + ".pdf");
        cnt.set("*callback", new Callback<PdfStamper>() {
            public void invoke(PdfStamper ps) {
                try {
                    BarcodeQRCode qrcode = new BarcodeQRCode("https://wizzer.cn", 1, 1, null);
                    Image qrcodeImage = qrcode.getImage();
                    qrcodeImage.setAbsolutePosition(350, 0);
                    qrcodeImage.scalePercent(512);
                    ps.getUnderContent(1).addImage(qrcodeImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return cnt;
    }

}
