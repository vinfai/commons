package com.shengpay.commons.core.tools;

import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.utils.IOStreamUtil;

import java.io.*;
import java.net.URL;

/**
 * Created by lindongcheng on 14-5-7.
 */
public class HttpDownload {
    private final String downloadUrl;
    private final String saveDir;
    private final String fileName;
    private final String filePath;

    public HttpDownload(String downloadUrl, String saveDir) {
        this(downloadUrl, saveDir, downloadUrl.substring(downloadUrl.lastIndexOf("/")));
    }

    public HttpDownload(String downloadUrl, String saveDir, String fileName) {
        this.downloadUrl = downloadUrl;
        this.saveDir = convertDir(saveDir);
        this.fileName = fileName;
        this.filePath = saveDir + fileName;
    }

    private String convertDir(String saveDir) {
        String dir = saveDir.replace('\\', '/');
        if (dir.endsWith("/"))
            return dir;
        else
            return dir + "/";
    }

    public void start() {
        InputStream inputStream = openInputStream();
        OutputStream outputStream = openOutputStream();
        try {
            IOStreamUtil.output(inputStream, outputStream, true);
        } catch (CheckException e) {
            throw new RuntimeException("happened exceptoin when download [" + downloadUrl + "] to [" + saveDir + "]", e);
        }
    }

    private InputStream openInputStream() {
        try {
            return new URL(downloadUrl).openStream();
        } catch (IOException e) {
            throw new RuntimeException("happen exception when open input stream for [" + downloadUrl + "]", e);
        }
    }

    private OutputStream openOutputStream() {
        mkdir();

        try {
            return new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("happened exception when open output stream for [" + filePath + "]", e);
        }
    }

    private void mkdir() {
        File dirFile = new File(saveDir);
        if (dirFile.exists()) return;
        if (dirFile.mkdirs()) return;
        throw new RuntimeException("can't make dir for [" + dirFile + "]");
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public static void main(String[] args) {
        new HttpDownload("http://minishua.shengpay.com/mpos3/android/m.apk", "d:/download").start();
    }
}
