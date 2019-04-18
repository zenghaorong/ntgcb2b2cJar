package com.aebiz.app.web.commons.utils;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by Administrator on 2018/5/22.
 */
public class FtpUtil {

    public static FtpClient connectFTP(String url, int port, String username,

                                       String password) { // 创建ftp

        FtpClient ftp = null;

        try {

            // 创建地址

            SocketAddress addr = new InetSocketAddress(url, port);

            // 连接

            ftp = FtpClient.create();

            ftp.connect(addr);

            // 登陆

            ftp.login(username, password.toCharArray());

            ftp.setBinaryType();

            System.out.println(ftp.getWelcomeMsg());

        } catch (FtpProtocolException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return ftp;

    }



    /**

     * 切换目录

     *

     * @param ftp

     * @param path

     */

    public static void changeDirectory(FtpClient ftp, String path) {

        try {

            ftp.changeDirectory(path);

            System.out.println(ftp.getWorkingDirectory());

        } catch (FtpProtocolException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }



    /**

     * 关闭ftp

     *

     * @param ftp

     */

    public static void disconnectFTP(FtpClient ftp) {

        try {

            ftp.close();

            System.out.println("disconnect success!!");

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }



    /**

     * 上传文件

     *

     * @param localFile

     * @param ftpFile

     * @param ftp

     */

    public static void upload(String localFile, String ftpFile, FtpClient ftp) {

        OutputStream os = null;

        FileInputStream fis = null;

        try {

            // 将ftp文件加入输出流中。输出到ftp上

            os = ftp.putFileStream(ftpFile);

            File file = new File(localFile);

            // 创建一个缓冲区

            fis = new FileInputStream(file);

            byte[] bytes = new byte[1024];

            int c;

            while ((c = fis.read(bytes)) != -1) {

                os.write(bytes, 0, c);

            }

            System.out.println("upload success!!");

        } catch (FtpProtocolException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } finally {

            try {

                if (os != null)

                    os.close();

                if (fis != null)

                    fis.close();

            } catch (IOException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }

        }

    }



    /**

     * 文件下载

     *

     * @param localFile

     * @param ftpFile

     * @param ftp

     */

    public static void download(String localFile, String ftpFile, FtpClient ftp) {

        InputStream is = null;

        FileOutputStream fos = null;

        try {

            // 获取ftp上的文件

            is = ftp.getFileStream(ftpFile);

            File file = new File(localFile);

            byte[] bytes = new byte[1024];

            int i;

            fos = new FileOutputStream(file);

            while ((i = is.read(bytes)) != -1) {

                fos.write(bytes, 0, i);

            }

            System.out.println("download success!!");

        } catch (FtpProtocolException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } finally {

            try {

                if (fos != null)

                    fos.close();

                if (is != null) {

                    is.close();

                }

            } catch (IOException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }

        }

    }


    public static void main(String[] args) {

        try {

            InetAddress addr = InetAddress.getLocalHost();

            System.out.println(addr.getHostAddress());

        } catch (Exception e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        String ip = "139.129.149.81";

        int port = 21;

        String username = "qxu1590100010";

        String password = "z1642749502";

        String path = "/htdocs";

        // 连接ftp

        FtpClient ftp = FtpUtil.connectFTP(ip, port, username, password);

        System.out.println(ftp.getWelcomeMsg());

        // 切换目录

        FtpUtil.changeDirectory(ftp, path);

        System.out.println("-----上传----");

        FtpUtil.upload("D:\\aaa.txt", "/aaa.txt", ftp);

        System.out.println("-----下载----");

//        FtpUtil.download("D:aaa.txt", "/home/aaa.txt", ftp);

    }

}






