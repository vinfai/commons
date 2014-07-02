/**
 * 
 */
package com.shengpay.commons.core.cipher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.CodingUtils;

/**
 * 
 * @description
 * @usage
 */

public class CipherHandlerImpl implements CipherHandler {

	/**
	 * 加密器
	 */
	private Cipher cipher;

	/**
	 * 密码KEY
	 */
	private final Key chipherKey;

	/**
	 * @param algorithm 加密算法(AES/DES)
	 * @param keyPath 密码key文件路径
	 */
	public CipherHandlerImpl(String algorithm, String keyPath) {
		this(algorithm, keyPath, true);
	}

	/**
	 * @param algorithm 加密算法(AES/DES)
	 * @param keyPath 当byKeyPath=true时,为密码key文件路径;否则为加密密码
	 * @param byKeyPath 是否根据密码文件路径取得key信息
	 */
	public CipherHandlerImpl(String algorithm, String keyPath, boolean byKeyPath) {
		//初始化加密器
		try {
			cipher = Cipher.getInstance(algorithm);
		} catch (Exception e) {
			throw new SystemException("创建加密器Cipher时发生异常[加密算法:" + algorithm + "]:", e);
		}

		//初始化密码key
		if (byKeyPath) {
			chipherKey = getKeyByPath(keyPath);
		} else {
			chipherKey = getKeyByPassword(algorithm, keyPath);
		}
	}

	/**
	 * 从指定文件中取得密码KEY信息
	 * 
	 * @param keyPath 密码KEY文件路径
	 * @return
	 */
	private Key getKeyByPath(String keyPath) {
		Key key;
		try {
			ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(keyPath));
			key = (Key) keyIn.readObject();
			keyIn.close();
		} catch (Exception e) {
			throw new SystemException("读入文件[" + keyPath + "]中的密码KEY信息时发生异常:", e);
		}
		return key;
	}

	/**
	 * 根据一个密码取得KEY
	 * 
	 * @param password 加密密码
	 * @return
	 */
	private Key getKeyByPassword(String algorithm, String password) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			return kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new SystemException("取得密码键生成器时发生异常:", e);
		}
	}

	/* (non-Javadoc)
	 */
	public String encode(String msg) {
		if (msg == null) {
			return null;
		}

		try {
			cipher.init(Cipher.ENCRYPT_MODE, chipherKey);
		} catch (InvalidKeyException e) {
			throw new SystemException("初始化加密器时发生异常:", e);
		}
		byte[] crypt = crypt(cipher, msg.getBytes());
		return CodingUtils.bin2hex(crypt);
	}

	/* (non-Javadoc)
	 */
	public String decode(String msg) {
		if (msg == null) {
			return null;
		}

		byte[] byteArr = CodingUtils.hex2bin(msg);
		try {
			cipher.init(Cipher.DECRYPT_MODE, chipherKey);
		} catch (InvalidKeyException e) {
			throw new SystemException("初始化加密器时发生异常:", e);
		}
		byte[] crypt = crypt(cipher, byteArr);
		return new String(crypt);
	}

	/**
	 * 使用加密器对信息进行加/解密
	 * 
	 * @param cipher 加密器
	 * @param msg 源信息
	 * @return
	 */
	private static byte[] crypt(Cipher cipher, byte[] byteArr) {
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(blockSize);
		byte[] inBytes = new byte[blockSize];
		byte[] outBytes = new byte[outputSize];

		int inLength = 0;
		boolean more = true;
		ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (more) {
			try {
				inLength = in.read(inBytes);
			} catch (IOException e) {
				throw new SystemException("从输入流读取加密源码信息时发生异常:", e);
			}
			if (inLength == blockSize) {
				int outLength;
				try {
					outLength = cipher.update(inBytes, 0, blockSize, outBytes);
				} catch (ShortBufferException e) {
					throw new SystemException("向加密器追加源信息时发生异常:" + e);
				}
				out.write(outBytes, 0, outLength);
			} else {
				more = false;
			}
		}

		if (inLength > 0) {
			try {
				outBytes = cipher.doFinal(inBytes, 0, inLength);
			} catch (Exception e) {
				throw new SystemException("结束加密器添加信息时发生异常:" + e);
			}
		} else {
			try {
				outBytes = cipher.doFinal();
			} catch (Exception e) {
				throw new SystemException("结束加密器添加信息时发生异常:" + e);
			}
		}

		try {
			out.write(outBytes);
		} catch (IOException e) {
			throw new SystemException("向解密结果输出流输出结果时发生异常:" + e);
		}

		try {
			in.close();
			out.close();
		} catch (Exception e) {
			throw new SystemException("关闭输入/输出流时发生异常:" + e);
		}
		return out.toByteArray();
	}

	/**
	 * 生成密码KEY文件
	 * 
	 * @param keyPath 密码key文件路径
	 */
	public static void genkey(String keyPath, String algorithm) {
		SecureRandom random = new SecureRandom();
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new SystemException("初始化密码KEY生成器时发生异常:", e);
		}
		keygen.init(random);
		SecretKey key = keygen.generateKey();
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyPath));
			out.writeObject(key);
			out.close();
		} catch (Exception e) {
			throw new SystemException("将密码KEY输出到文件过程中发生异常:", e);
		}
	}

	public static void main(String[] args) {
		String keyPath = "c:/cipher.txt";
		String algorithm = "AES";
		CipherHandler cipherHandler = new CipherHandlerImpl(algorithm, keyPath, false);

		String msg = "LincolnLincoln";
		System.out.println("msg:" + msg);
		String encode = cipherHandler.encode(msg);
		System.out.println("encode:" + encode);
		String decode = cipherHandler.decode(encode);
		System.out.println("decode:" + decode);
	}

	public static void main2(String[] args) {
		String keyPath = "c:/cipher.txt";
		String algorithm = "AES";
		genkey(keyPath, algorithm);
	}
}
