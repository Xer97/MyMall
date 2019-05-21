package com.xer97.mymall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author xer97
 * @date 2019/4/14 21:29
 */
public interface IFileService {

    /**
     * 上传文件
     *
     * @param file 需要上传的文件
     * @param path 上传的目标目录
     * @return 上传后在目标目录下的文件名
     */
    String upload(MultipartFile file, String path);
}
