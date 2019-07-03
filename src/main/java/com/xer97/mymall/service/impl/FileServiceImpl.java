package com.xer97.mymall.service.impl;

import com.google.common.collect.Lists;
import com.xer97.mymall.service.IFileService;
import com.xer97.mymall.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author xer97
 * @date 2019/4/14 21:30
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

//    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        // 获取上传文件名
        String fileName = file.getOriginalFilename();
        // 获取文件拓展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 赋予一个唯一的文件名,避免多个文件名相同导致覆盖
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        // 创建文件夹
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            // 若path包含多层文件夹,则全部创建
            fileDir.mkdirs();
        }
        // 文件上传的目标位置
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            // 将targetFile上传到FTP服务器
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            // 上传完，删除path下的文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常", e);
            return null;
        }
        return uploadFileName;
    }
}
