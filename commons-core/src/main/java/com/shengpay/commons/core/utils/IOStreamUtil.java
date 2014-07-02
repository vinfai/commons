package com.shengpay.commons.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.exception.SystemException;


/**
 * 输入输出流工具类
 * @description
 * @usage
 */
public class IOStreamUtil {

	/**
	 * 将文件保存到指定目录下
	 * 
	 * @param airDir 目标文件夹（必填）
	 * @param subAirDir 子文件夹
	 * @param fileName 原文件名（必填）
	 * @param inStream 文件输入流(操作完成后，会自动关闭输入流)（必填）
	 * @param userId 用户ID(非必填）
	 * @return 文件存放的绝对路径
	 * @throws CheckException 
	 * @throws ErrorMessageException
	 */
	public static String saveFileToDir(String airDir, String subAirDir, String fileName, InputStream inStream, Long userId) throws CheckException {
		if (airDir == null || userId == null || fileName == null) {
			throw new SystemException("参数不合法【saveFileToDir(String " + airDir + ",String " + fileName + ",InputStream " + inStream + ", Long " + userId + ")】");
		}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
		//创建文件夹
		String newFilePath = getNewFilePath(airDir, subAirDir, fileName, userId);
		File file = new File(newFilePath);
		File parentFile = file.getParentFile();
		if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
			throw new SystemException("无法创建文件夹[" + parentFile.getPath() + "]");
		}

		//建立输出流
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new SystemException("创建文件输出流时发生异常：", e);
		}

		//执行转存
		output(inStream, outStream, true);

		//返回新文件路径
		return newFilePath;
	}

	/**
	 * 将指定输入流的内容输出到指定输出流中去
	 * 
	 * @param input 原始输入流
	 * @param output 目标输出流
	 * @param closeRes 是否需要关闭输入输出流
	 * @throws CheckException 
	 * @throws ErrorMessageException
	 */
	public static void output(InputStream input, OutputStream output, boolean closeRes) throws CheckException {
		//验证参数合法性
		if (input == null || output == null) {
			throw new SystemException("参数不合法：【output(InputStream " + input + ",OutputStream " + output + ",boolean " + closeRes + ")】");
		}

		//封装流为缓存流
		BufferedInputStream bufInput = new BufferedInputStream(input);
		BufferedOutputStream bufOuput = new BufferedOutputStream(output);

		//执行输出操作
		byte[] buf = new byte[1024];
		int readLength = -1;
		try {
			while ((readLength = bufInput.read(buf)) != -1) {
				bufOuput.write(buf, 0, readLength);
			}
			// 刷新此输出流并强制写出所有缓冲的输出字节
			output.flush();
		} catch (IOException e) {
			throw new SystemException("执行流操作过程中发生异常：", e);
		} finally {
			if (closeRes) {
				Exception ex = null;
				try {
					bufInput.close();
				} catch (IOException e) {
					ex = e;
				}
				try {
					bufOuput.close();
				} catch (IOException e) {
					ex = e;
				}

				if (ex != null) {
					throw new CheckException("关闭输入输出流时发生异常：", ex);
				}
			}
		}
	}

	/**
	 * 取得（服务器上）新文件路径
	 * 
	 * @param airDir 服务器上存放新文件的地址（以“/”或“\\”结尾）（必填）
	 * @param subAirDir 子文件夹名称(可选)
	 * @param fileNameOrPath 原文件名或文件路径（用于取得文件类型名） （必填）
	 * @param userId 提交文件的用户ID（可选）
	 * @return
	 * @throws ErrorMessageException
	 */
	public static String getNewFilePath(String airDir, String subAirDir, String fileNameOrPath, Long userId) {
		//验证参数合法性
		if (airDir == null || fileNameOrPath == null) {
			throw new SystemException("参数不合法[getNewFilePath(String " + airDir + ", String " + subAirDir + ", String " + fileNameOrPath + ", Long " + userId + ")]");
		}

		//取得新文件名
		String newFileNamePrefix = String.valueOf(System.currentTimeMillis());
		if (userId != null) {
			newFileNamePrefix = userId + "_" + newFileNamePrefix;
		}
		String fileType = getFileType(fileNameOrPath);
		String newFileName = newFileNamePrefix + fileType;

		//取得新文件路径
		String newFilePath = null;
		if (subAirDir != null) {
			newFilePath = airDir + File.separatorChar + subAirDir + File.separatorChar + newFileName;
		} else {
			newFilePath = airDir + File.separatorChar + newFileName;
		}
		return newFilePath;
	}

	/**
	 * 取得文件后缀（例如：“.txt”）
	 * 
	 * @param fileNameOrPath 文件名或文件路径
	 * @return
	 * @throws ErrorMessageException
	 */
	public static String getFileType(String fileNameOrPath) {
		if (fileNameOrPath == null) {
			return "";
		}

		int postfixIndexOf = fileNameOrPath.indexOf(".");
		if (postfixIndexOf == -1) {
			return "";
		}

		return fileNameOrPath.substring(postfixIndexOf);
	}

	/**
	 * 从输入流读取字符串
	 * 
	 * @param in 指定输入流
	 * @return 读到的字符串
	 * @throws CheckException 流操作异常
	 */
	public static String readString(InputStream in) throws CheckException {
		byte[] buf = new byte[1024];
		int length = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
		} catch (IOException e) {
			throw new CheckException(e.getMessage(), e);
		}

		return new String(out.toByteArray());
	}

	public static void main(String[] args) {
		makeFile("c://lin//xx//a.txt");
	}

	/**
	 * 将字符串信息通过输出流输出
	 * @param outputStream
	 * @param str
	 * @throws CheckException 
	 */
	public static void writeString(OutputStream outputStream, String str) throws CheckException {
		byte[] bytes = str.getBytes();
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			throw new CheckException("通过输出流输出字符串信息【"+str+"】时发生异常",e);
		}
	}

	/**
	 * 将指定输入流内容输出到指定文件中
	 * @param input 输入流
	 * @param filePath 目标文件
	 * @param isAppend 是否为追加输出
	 * @throws CheckException 
	 */
	public static void writeInputToFile(InputStream input, String filePath, boolean isAppend) throws CheckException {
		OutputStream output = makeFileOutputStream(filePath);
		output(input, output, true);
	}

	/**
	 * 创建文件
	 * @param filePath 文件路径
	 * @return
	 */
	public static File makeFile(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new SystemException("无法创建文件【"+filePath+"】");
			}
		}
		
		return file;
	}
	/**
	 * 构造文件输出流
	 * @param filePath
	 * @return
	 * @throws CheckException 
	 */
	public static OutputStream makeFileOutputStream(String filePath) throws CheckException {
		try {
			return new FileOutputStream(makeFile(filePath));
		} catch (FileNotFoundException e) {
			throw new CheckException("创建文件【文件:"+filePath+"】的输出流时发生异常:",e);
		}
		
	}
	
    public static void closeOutputStream(OutputStream os) {
        try {
            if (os != null)
                os.close();
            os = null;
        } catch (Exception exx) {
            throw new SystemException(exx);
        }
    }
    
    public static String getLocalName() {
    	try {
			return InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			return "无法取得本地名称("+e.getMessage()+")";
		}
    }
}
