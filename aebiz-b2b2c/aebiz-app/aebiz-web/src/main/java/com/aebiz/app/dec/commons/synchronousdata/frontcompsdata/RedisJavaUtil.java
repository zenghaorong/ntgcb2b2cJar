package com.aebiz.app.dec.commons.synchronousdata.frontcompsdata;

import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.commons.utils.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.nutz.json.Json;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by 金辉 on 2017/2/28.
 */
public class RedisJavaUtil {
    //private static RedisService redisService = SpringUtil.getBean("redisService", RedisService.class);
    private static String directPath = "D:\\frontcompsdata\\redisFiles\\";
    //private static String localhost = "172.16.96.112";//"172.16.96.112";
    private static String localhost = "127.0.0.1";//"172.16.96.112";
    private static int port = 6379;
    public static void main(String[] args) throws Exception {
        Jedis jedis = new Jedis(localhost, port);
        inputRedisFiles(jedis); //从本地导入到指定redis
        //outputRedisFiles(jedis); //从指定的reids导入到本地
    }

    public static List getResourceKeys(){
        List resourceKeys = new ArrayList();

        String sql = "SELECT RESOURCEKEY FROM DEC_TEMPLATES_RESOURCE ";//SQL语句
        DBHelper db1 = new DBHelper(sql);//创建DBHelper对象
        try {
            ResultSet ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String resourceKey = ret.getString("RESOURCEKEY");
                resourceKeys.add(resourceKey);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resourceKeys;
    }

    /**
     * 输出到文件
     * @param jedis
     * @throws IOException
     */
    public static void outputRedisFiles(Jedis jedis) throws IOException {
        //组件基础文件
        output(jedis, DecorateCacheConstant.COMPONENTS_HTML_REDIS_KEY, directPath+"htmlFile");
        output(jedis, DecorateCacheConstant.COMPONENTS_JSP_REDIS_KEY, directPath+"jspFile");
        output(jedis,DecorateCacheConstant.COMPONENTS_JS_REDIS_KEY, directPath+"jsFile");
        /*如果不需要搭建的页面的话 只需要导入组件的基础文件即可*/
        //页面保存信息
        output(jedis,DecorateCacheConstant.DESIGNER_PAGEMODEL, directPath+"pageModelFile");
        output(jedis,DecorateCacheConstant.DESIGNER_PAGEMODELJSON, directPath+"pageModelJsonFile");
        output(jedis,DecorateCacheConstant.DESIGNER_PAGEVEIWHTML, directPath+"pageViewHtmlFile");
        output(jedis,DecorateCacheConstant.DESIGNER_PAGEJS, directPath+"pageJsFile");
        //使用样式
        output(jedis,DecorateCacheConstant.DECORATEPLATFORM_TEMPLATE_CSS, directPath+"useCssFile");
        output(jedis,getResourceKeys(), directPath+"resourceFiles");
    }

    /**
     * 导入文件数据
     * @param jedis
     * @throws Exception
     */
    public static void inputRedisFiles(Jedis jedis) throws Exception {


        input(jedis,directPath+"htmlFile");
        input(jedis,directPath+"jspFile");
        input(jedis,directPath+"jsFile");

        input(jedis,directPath+"pageModelFile");
        input(jedis,directPath+"pageModelJsonFile");
        input(jedis,directPath+"pageViewHtmlFile");
        input(jedis,directPath+"pageJsFile");

        input(jedis,directPath+"useCssFile");
        input(jedis,directPath+"resourceFiles");
    }
    /**
     * 根据key前缀导出redis中的数据
     * @param jedis
     * @param prefix
     * @param filePath
     * @throws IOException
     */
    public static void output(Jedis jedis,String prefix, String filePath) throws IOException {
        Set keys = jedis.keys(prefix + "*");
        Iterator it = keys.iterator();
        Map map = new HashMap<String, String>();
        while (it.hasNext()) {
            String key = it.next().toString();
            map.put(key, jedis.get(key));
        }
        Object obj = JSONObject.toJSON(map);
        File f = new File(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    /**
     * 根据keyList导出redis数据
     * @param jedis
     * @param keys
     * @param filePath
     * @throws IOException
     */
    public static void output(Jedis jedis,List<String> keys,String filePath) throws IOException {
        Map map = new HashMap<String, String>();
        for(String key :keys){
            map.put(key, jedis.get(key));
        }
        Object obj = JSONObject.toJSON(map);
        File f = new File(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    /**
     * 导入导出的redis数据
     * @param jedis
     * @param filePath
     * @throws Exception
     */
    public static void input(Jedis jedis,String filePath) throws Exception {
        File f = new File(filePath);
        ObjectInputStream io = new ObjectInputStream(new FileInputStream(f));
        Object obj = io.readObject();
        Map<String,String> map = (Map) JSONObject.toJSON(obj);
        Set keys = map.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            jedis.set(key,map.get(key));
        }
    }
}
