package com.shengpay.commons.web.webtools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 验证码生成器
 * @description	
 * @usage		
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * @company		5173 Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ValidateCodeBuilder.java,v 1.0 2010-11-30 下午05:04:25 lindongcheng Exp $
 * @create		2010-11-30 下午05:04:25
 */
public class ValidateCodeBuilder {
	/**
	 * 验证码长度
	 */
	private int codeCount;

	/**
	 * 图片宽度
	 */
	private int width;

	/**
	 * 图片高度
	 */
	private int height;
	
	/**
	 * 图片背景色
	 */
	private Color bgColor;

	/**
	 * 文字颜色
	 */
	private Color fontColor;

	/**
	 * 图片格式
	 */
	private String GRAPHIC_JPEG = "JPEG";

	/**
	 * 可选的字符
	 */
	private String base = "abcdefghknrtuvxy23456789";

	/**
	 * 随机码生成器
	 */
	private Random random = new Random();

	/**
	 * 构造指定样式的验证码生成器
	 * @param bgColor
	 * @param fontColor
	 * @param width
	 * @param height
	 * @param codeCount
	 */
	public ValidateCodeBuilder(Color bgColor, Color fontColor, int width, int height, int codeCount) {
		super();
		this.codeCount = codeCount;
		this.width = width;
		this.height = height;
		this.bgColor = bgColor;
		this.fontColor = fontColor;
	}

	/**
	 * 构造默认样式的验证码生成器
	 */
	public ValidateCodeBuilder() {
		codeCount = 6;
		width = 90;
		height = 30;
		bgColor= new Color(238,238,238);
		fontColor= new Color(0,79,250);
	}


	public void output2Response(HttpServletResponse response, HttpSession session, String attributeName) throws IOException {
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
//		response.setHeader("Cache-Control", "no-cache");放开后莫名显示黑色方块区域
		response.setDateHeader("Expires", 0);
		
		String sRand=output2Stream(response.getOutputStream());
		
		// 将"验证码"保持到会话中
		session.setAttribute(attributeName, sRand);
	}
	
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String output2File(String filePath) throws IOException{
		//创建文件输出流
		OutputStream output= new FileOutputStream(filePath);
		
		try {
			return output2Stream(output);
		} catch (IOException e) {
			throw e;
		}finally{
			output.close();
		}
	}
	
	/**
	 * 打印验证码图片到指定指定输出流里
	 * 
	 * @param response
	 * @return 验证码值
	 * @throws IOException
	 */
	public String output2Stream(OutputStream output) throws IOException {
		// 构造背景图片
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		graphics.setColor(bgColor);
		graphics.fillRect(0, 0, width, height);

		// 随机取得一个字符,并绘制到背景图片中
		StringBuffer sRand = new StringBuffer();
		for (int charIndex = 0; charIndex < codeCount; charIndex++) {
			String randomChar = getRandomChar();// 取得随机字符
			drawChar2Graphics(graphics, randomChar, charIndex);// 将随机字符绘制到背景图片中
			sRand.append(randomChar);// 追加到验证码字符串中
		}

		// 将图片输出
		graphics.dispose();
		ImageIO.write(bi, GRAPHIC_JPEG, output);

		return sRand.toString();
	}

	/**
	 * 绘制指定支付到图像中
	 * 
	 * @param graphics
	 *            绘制到的目标图像
	 * @param randomChar
	 *            被绘制的文字
	 * @param charIndex
	 *            字符在图像中的位置索引
	 */
	private void drawChar2Graphics(Graphics2D graphics, String randomChar, int charIndex) {
		//初始数据
		int charSpaceBetween = getCharSpaceBetween();//文字间距

		//构造文字图像
		BufferedImage fontBfImg = new BufferedImage(charSpaceBetween, height, BufferedImage.TRANSLUCENT);
		Graphics2D fontGra2D = fontBfImg.createGraphics();
		fontGra2D.setColor(bgColor);
		fontGra2D.fillRect(0, 0, charSpaceBetween, height);
		fontGra2D.setFont(new Font("Times New Roman", Font.BOLD, getFontHight()));// 设置字体
		fontGra2D.setColor(fontColor);// 设置字体颜色
		fontGra2D.drawString(randomChar, 3, 23);
		fontGra2D.dispose();

		//排除文字颜色和背景颜色相同的情况
		excludeSameColor(bgColor, fontBfImg, fontGra2D);

		//旋转文字
		fontBfImg=rotateFont(fontBfImg, bgColor);

		//绘制文字到指定图像中
		AffineTransform transform = new AffineTransform();
		BufferedImageOp biop = new AffineTransformOp(transform, null);
		int addImgX = charIndex * charSpaceBetween;
		int addImgY = 0;
		graphics.drawImage(fontBfImg, biop, addImgX, addImgY);
	}
	
	private int getFontHight(){
		return 20;
	}

	/**
	 * 排除文字颜色和背景颜色相同的情况
	 * 
	 * @param bgColor
	 * @param fontBfImg
	 * @param fontGra2D
	 */
	private void excludeSameColor(Color bgColor, BufferedImage fontBfImg, Graphics2D fontGra2D) {
		for (int k = 0; k < fontBfImg.getHeight(); k++) {
			for (int j = 0; j < fontBfImg.getWidth(); j++) {

				if (fontBfImg.getRGB(j, k) == bgColor.getRGB()) {
					fontBfImg.setRGB(j, k, 0x8F1C1C);

					fontGra2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.1f));
					fontGra2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
				}
			}
		}
	}

	/**
	 * 取得一个随机字符
	 * 
	 * @return
	 */
	private String getRandomChar() {
		return String.valueOf(base.charAt(random.nextInt(base.length())));
	}

	/**
	 * 随机取得一个颜色
	 * 
	 * @param fc
	 *            颜色开始区间
	 * @param bc
	 *            颜色结束区间
	 * @return
	 */
	public Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 旋转文字
	 * 
	 * @param bufImage
	 * @param bgColor
	 * @param zhuanAng
	 */
	private BufferedImage rotateFont(BufferedImage bufImage, Color bgColor) {
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
		BufferedImage filteredBufImage = new BufferedImage(width, height + 5, bufImage.getType()); // 过滤后的图像

		Graphics2D fontGra2D = filteredBufImage.createGraphics();
		fontGra2D.setColor(bgColor);
		fontGra2D.fillRect(0, 0, width, height);
		fontGra2D.dispose();

		AffineTransform transform = new AffineTransform(); // 仿射变换对象
		double randomAngle = getRandomAngle();
		transform.rotate(randomAngle, width / 2, height / 2); // 旋转图像

		AffineTransformOp imageOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);// 创建仿射变换操作对象
		imageOp.filter(bufImage, filteredBufImage);// 过滤图像，目标图像在filteredBufImage
		return filteredBufImage;
	}

	/**
	 * 取得随机旋转角度
	 * @return
	 */
	private double getRandomAngle() {
		int nextInt = random.nextInt(4);
		int mod = nextInt % 2;
		double angle = 0.05 * nextInt + 0.3;
		if (mod == 0) {
			angle = -angle;
		}
		return angle;
	}

	/**
	 * 取得字符间距
	 * 
	 * @return
	 */
	private int getCharSpaceBetween() {
		return width / codeCount;
	}

	public static void main(String[] args) {
		
		try {
			System.out.println(new ValidateCodeBuilder().output2File("e:/a/validateCode.jpeg"));
		} catch (IOException e) {
		}
	}
}
