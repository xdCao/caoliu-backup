package com.xdcao.caoliu.crawl; /**
 * @Author: buku.ch
 * @Date: 2018/10/21 9:20 PM
 */


import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 说明
 * 利用httpclient下载文件
 * maven依赖
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId>
 * <version>4.0.1</version>
 * </dependency>
 * 可下载http文件、图片、压缩文件
 * bug：获取response header中Content-Disposition中filename中文乱码问题
 *
 * @author tanjundong
 */
public class HttpDownload {

    private static final Logger logger = LoggerFactory.getLogger(HttpDownload.class);

    public static final int cache = 10 * 1024;
    public static final boolean isWindows;
    public static final String splash;
    public static final String root;


    static {
        if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
            splash = "\\";
            root = "D:";
        } else {
            isWindows = false;
            splash = "/";
            root = "/root/caoliu";
        }
    }

    /**
     * 根据url下载文件，文件名从response header头中获取
     *
     * @param url
     * @return
     */
    public static String download(String url) {
        return download(url, null);
    }

    /**
     * 根据url下载文件，保存到filepath中
     *
     * @param url
     * @param filepath
     * @return
     */
    public static String download(String url, String filepath) {

        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";
        httpGet.setHeader("User-Agent",userAgent);

        HttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            logger.error("Http Get response error !!!\n",e);
            return "fail";
        }

        if (response == null) {
            return "fail";
        }

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                System.out.println("404 not found");
                return "fail";
        }

        HttpEntity entity = response.getEntity();
        InputStream is = null;
        try {
            is = entity.getContent();
        } catch (IOException e) {
            logger.error("Get entity error!!!\n",e);
            return "fail";
        }

        if (is == null) {
            return "fail";
        }

        if (filepath == null)
            filepath = getFilePath(response);

        if (!filepath.contains(".mp4")) {
            return "fail";
        }

        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
            file = new File(filepath);
        }

        file.getParentFile().mkdirs();

        FileOutputStream fileout = null;
        try {
            fileout = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            logger.error(" ",e);
            return "fail";
        }
        /**
         * 根据实际运行效果 设置缓冲区大小
         */

        try {
            byte[] buffer = new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }
            is.close();
            fileout.flush();
            fileout.close();
            return filepath;
        }catch (Exception e){
            file.delete();
            logger.error("file write error!!!: \n",e);
            return "fail";
        }

    }

    /**
     * 获取response要下载的文件的默认路径
     *
     * @param response
     * @return
     */
    public static String getFilePath(HttpResponse response) {
        String filepath = root + splash;
        String filename = getFileName(response);

        if (filename != null) {
            filepath += filename;
        } else {
            filepath += getRandomFileName();
        }
        return filepath;
    }

    /**
     * 获取response header中Content-Disposition中的filename值
     *
     * @param response
     * @return
     */
    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
                        filename = param.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filename;
    }

    /**
     * 获取随机文件名
     *
     * @return
     */
    public static String getRandomFileName() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void outHeaders(HttpResponse response) {
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            System.out.println(headers[i]);
        }
    }

    public static void main(String[] args) {


        try {
            HttpClient client = HttpClients.createDefault();
//            HttpGet httpGet = new HttpGet("http://www.sejie10.com/3c96064535352323c0c5665170f431bae92cb29be8/0/53/53.mp4/?br=707&embed=true");
            HttpGet httpGet = new HttpGet("https://yaoshe24.com/get_file/1/5d0261dc8a5b2c06dff73a727ab5e5fb/17000/17775/17775.mp4/?br=707&embed=true");

            String userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
            httpGet.setHeader("User-Agent",userAgent);

            HttpResponse response = client.execute(httpGet);

            System.out.println(response.getStatusLine());

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            String filepath = getFilePath(response);
            String filename = getFileName(response);

            System.out.println("filename :   "+filename);

//            File file = new File(filepath);
//            file.getParentFile().mkdirs();
//            FileOutputStream fileout = new FileOutputStream(file);
//            /**
//             * 根据实际运行效果 设置缓冲区大小
//             */
//            byte[] buffer = new byte[cache];
//            int ch = 0;
//            while ((ch = is.read(buffer)) != -1) {
//                fileout.write(buffer, 0, ch);
//            }
//            is.close();
//            fileout.flush();
//            fileout.close();



        } catch (Exception e) {
            e.printStackTrace();

        }

    }


}