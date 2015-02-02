package com.handu.apollo.api;


import com.google.common.collect.Maps;
import com.handu.apollo.core.ApiErrorCode;
import com.handu.apollo.utils.exception.ApiException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by hhc on 2014/8/11.
 */
public class UploadFile {

    public static Map<String, String> uploadFile(HttpServletRequest request, String filePath) throws ApiException {

        // 保存文件的目录
        String PATH_FOLDER = filePath + "upload";
        // 存放临时文件的目录,存放xxx.tmp文件的目录
        String TEMP_FOLDER = filePath + "uploadTemp";

        Map<String, String> paramMap = Maps.newHashMap();

        try {
            request.setCharacterEncoding("utf-8"); // 设置编码
            // 获得磁盘文件条目工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
            // 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
            /**
             * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem
             * 格式的 然后再将其真正写到 对应目录的硬盘上
             */
            factory.setRepository(new File(TEMP_FOLDER));
            // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
            factory.setSizeThreshold(1024 * 1024);
            // 高水平的API文件上传处理
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                // 提交上来的信息都在这个list里面
                // 这意味着可以上传多个文件
                // 请自行组织代码
                List<FileItem> list = upload.parseRequest(request);
                // 获取上传的文件
                FileItem item = getUploadFileItem(list);
                if (item != null) {
                    // 获取文件名
                    String filename = getUploadFileName(item);
                    // 真正写到磁盘上
                    item.write(new File(PATH_FOLDER, filename));

                    filename = PATH_FOLDER + "/" + filename;

                    paramMap.put(item.getFieldName(),filename);
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            throw new ApiException(ApiErrorCode.PARAM_ERROR, ex.getMessage(), ex);
        }

        return paramMap;
    }

    private static FileItem getUploadFileItem(List<FileItem> list) {
        for (FileItem fileItem : list) {
            if (!fileItem.isFormField()) {
                return fileItem;
            }
        }
        return null;
    }

    private static String getUploadFileName(FileItem item) {
        // 获取路径名
        String value = item.getName();
        // 索引到最后一个反斜杠
        int start = value.lastIndexOf("/");
        // 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
        String filename = value.substring(start + 1);

        return filename;
    }

}
