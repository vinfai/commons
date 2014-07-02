package com.shengpay.commons.core.tools;

import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.utils.IOStreamUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by lindongcheng on 14-5-7.
 */
public class Unzip {

    private String filePath;
    private String dir;
    private File file;
    private ZipFile zipFile;

    public Unzip(String filePath) {
        this(filePath, new File(filePath).getParent());
    }

    public Unzip(String filePath, String dir) {
        this.filePath = filePath;
        this.dir = dir;
        file = new File(filePath);
        try {
            zipFile = new ZipFile(file);
        } catch (IOException e) {
            throw new RuntimeException("读取ZIP文件[" + file + "]时发生异常", e);
        }
    }

    public void start() {
        try {
            doStart();
        } catch (Exception e) {
            throw new RuntimeException("解压文件[" + filePath + "]到目录[" + dir + "]时发生异常", e);
        }
    }

    private void doStart() throws IOException, CheckException {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            output(zipEntry);
        }
        zipInputStream.close();

    }

    private void output(ZipEntry zipEntry) throws IOException, CheckException {
        String fileName = zipEntry.getName();
        File targetFile = new File(dir + File.separator + fileName);
        mkParentDir(targetFile);

        if (zipEntry.isDirectory()) {
            if (!targetFile.mkdir()) throw new RuntimeException("创建文件夹[" + targetFile + "]失败");
            return;
        }

        InputStream input = zipFile.getInputStream(zipEntry);
        OutputStream output = new FileOutputStream(targetFile);
        IOStreamUtil.output(input, output, true);
    }

    private void mkParentDir(File temp) {
        File parentFile = temp.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) throw new RuntimeException("创建文件夹[" + parentFile + "]失败");
    }

    public static void main2(String[] args) throws Exception {
        new Unzip("D:\\develop\\mpos-pc\\1.2.zip").start();
    }
}
