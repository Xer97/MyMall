package com.xer97.mymall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author xer97
 * @date 2019/4/14 21:57
 */
@Slf4j
public class FtpUtil {

//    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.password");

    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FtpUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil = new FtpUtil(ftpIp, 21, ftpUser, ftpPassword);
        log.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        log.info("结束上传ftp服务器,上传结果{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = false;
        FileInputStream fis = null;
        // 连接FTP服务器
        if (connectServer(ip, port, user, password)) {
            try {
                // 切换FTP工作目录为remotePath
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                // 设置文件类型为二进制文件
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 设置被动模式
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
                uploaded = true;
            } catch (IOException e) {
                log.error("上传文件异常", e);
            } finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }else{
            log.error("连接ftp失败");
        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String password) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, password);
        } catch (IOException e) {
            log.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
