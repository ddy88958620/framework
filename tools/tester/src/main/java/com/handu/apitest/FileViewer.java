package com.handu.apitest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfei  on 2014/6/3.
 */
public class FileViewer {

    /**
     * @param path
     *            文件路径
     * @param suffix
     *            后缀名, 为空则表示所有文件
     * @return list
     */
    public List<String> getListFiles(String path, String suffix) {
        List<String> lstFileNames = new ArrayList<String>();
        File file = new File(path);

        File[] t = file.listFiles();
        for (int i = 0; i < t.length; i++) {
            if (t[i].isFile()) {
                String filePath = t[i].getAbsolutePath();
                int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
                String tempsuffix;

                if (begIndex != -1) {
                    tempsuffix = filePath.substring(begIndex + 1, filePath.length());
                    if (tempsuffix.equals(suffix)) {
                        lstFileNames.add(filePath);
                    }
                }
            }
        }
        return lstFileNames;
    }

}
