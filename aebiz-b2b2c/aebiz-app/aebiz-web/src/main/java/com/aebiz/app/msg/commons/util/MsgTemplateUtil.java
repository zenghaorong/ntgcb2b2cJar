package com.aebiz.app.msg.commons.util;

import org.nutz.lang.util.NutMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ThinkPad on 2017/7/13.
 */
public class MsgTemplateUtil {

    private final static String GLOBAL_VAR_PATTERN_ALL = "\\$\\{[a-zA-Z0-9_\\-\\u2E80-\\uFE4F]+(\\.[a-zA-Z0-9_\\-\\u2E80-\\uFE4F]+)*\\}";

    private final static String GLOBAL_VAR_PATTERN = "(?<=\\$\\{)[a-zA-Z0-9_\\-\\u2E80-\\uFE4F]+(\\.[a-zA-Z0-9_\\-\\u2E80-\\uFE4F]+)*(?=\\})";


    /**
     * 获得内容中所引用的不重复的key集合
     *
     * @param fileContent     - 内容
     * @param withPlaceholder - 是否包含占位符 true:匹配{key} false:匹配key
     * @return 不重复的key集合
     */
    public static List<String> getGlobalVarKeys(String fileContent, boolean withPlaceholder) {
        if (fileContent == null) {
            return Collections.emptyList();
        }
        String textContent = withOutComment(fileContent);
        Matcher m = Pattern.compile(withPlaceholder ? GLOBAL_VAR_PATTERN_ALL : GLOBAL_VAR_PATTERN).matcher(textContent);
        Set<String> globalVarSet = new HashSet<>();
        while (m.find()) {
            globalVarSet.add(m.group());
        }
        return new ArrayList<>(globalVarSet);
    }

    /**
     * 替换变量值
     * @param fileContent
     * @param map
     * @return
     */
    public static String replaceContent(String fileContent,NutMap map){
        List<String> keyList = getGlobalVarKeys(fileContent,false);
        for(String key:keyList){
            fileContent = fileContent.replace("${"+key+"}",map.getString(key));
        }
        return fileContent;
    }

    /**
     * 过滤注释行
     *
     * @param fileContent
     * @return
     */
    private static String withOutComment(String fileContent) {
        BufferedReader contentBr = new BufferedReader(new StringReader(fileContent));
        String line = null;
        StringBuilder psb = new StringBuilder();
        try {
            while ((line = contentBr.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#")) {//过滤注释行
                    psb.append("\r\n").append(line);
                }
            }
            return psb.delete(0, 2).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void main(String[] args) {
        int i=1;
        for (String a: getGlobalVarKeys("{中文.abc_08}sdfdasfdasfasdfasdfasdfsadf{global.config.path}dsgfasdfasdfasdfasdfasdfasdf" +
                "{广深行数据库账号和密码}{db.write-ip.port_写库IP和端口}{cnmbdhehehehehhsdfasdfhas._kdjhfakjsdhfkjas.dhfkjasdhfkjashdfasdfasdfasdf}",false)) {
            System.out.println((i++)+" "+a);
        }

    }

}
