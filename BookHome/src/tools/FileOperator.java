package tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ParagraphTag;

public class FileOperator {
	public static String readContent(File file, String charset)
			throws Exception {
		if (!file.exists()) {
			throw new Exception("File " + file.getAbsolutePath() + " not find!");
		}
		FileInputStream in = new FileInputStream(file);
		return readContent(in, charset);
	}

	public static String readContent(InputStream in, String charset)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(in, charset);
		BufferedReader r = new BufferedReader(reader);
		String temp;
		StringBuffer sb = new StringBuffer();
		while ((temp = r.readLine()) != null) {
			sb.append(temp);
		}
		r.close();
		return sb.toString();
	}

	public static byte[] getBytesOfFile(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf, 0, 1024)) != -1) {
			out.write(buf, 0, len);
		}
		in.close();
		return out.toByteArray();
	}

	public static String readContent(File file) throws Exception {
		return readContent(file, "utf-8");
	}

	public static String readTextFile(File file) throws IOException {
		FileReader reader = new FileReader(file);
		StringBuffer sb = new StringBuffer();
		int len = 0;
		char buf[] = new char[1024];
		while ((len = reader.read(buf, 0, 1024)) != -1) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}

	public static void writeContent(File file, String text) throws IOException {
		if (!file.exists())
			createFile(file);
		OutputStreamWriter w = new OutputStreamWriter(
				new FileOutputStream(file), "utf-8");
		BufferedWriter writer = new BufferedWriter(w);
		writer.write(text);
		writer.flush();
		writer.close();
	}

	public static void writeToTxtFile(File file, String text)
			throws IOException {
		FileWriter writer = null;
		if (!file.exists()) {
			createFile(file);
		}
		try {
			writer = new FileWriter(file);
			writer.write(text);
			writer.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public static void writeToTxtFile(String path, String text)
			throws IOException {
		writeToTxtFile(new File(path), text);
	}

	public static Image readImage(File imageFile) throws Exception {
		if (!imageFile.exists()) {
			throw new Exception("File " + imageFile.getAbsolutePath()
					+ " not find!");
		}
		return ImageIO.read(imageFile);
	}

	public static Image readImage(InputStream in) throws IOException {
		return ImageIO.read(in);
	}

	public static void writeImage(File imageFile, Image image, String mode)
			throws IOException {
		if (!imageFile.exists()) {
			createFile(imageFile);
		}
		ImageIO.write((RenderedImage) image, mode, imageFile);
	}

	public static ByteArrayOutputStream readImageFile(File file)
			throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] buf = new byte[1024];
		int len = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((len = in.read(buf)) > 0)
			out.write(buf, 0, len);
		in.close();
		return out;
	}

	public static void createDir(File file) {
		// System.out.println(file.getAbsolutePath());
		if (!file.mkdir()) {
			createDir(file.getParentFile());
			file.mkdir();
		}
	}

	public static void createFile(File file) throws IOException {
		try {
			file.createNewFile();
		} catch (Exception e) {
			createDir(file.getParentFile());
			file.createNewFile();
		}
	}

	public static Image imResize(BufferedImage image, int width, int height) {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.drawImage(image.getScaledInstance(width, height, Image.SCALE_FAST),
				0, 0, null);
		g.dispose();
		return img;
	}

	public static void copyFile(String src, String des) throws Exception {
		System.out.println("copy from " + src + " to " + des);
		File srcFile = new File(src);
		File desFile = new File(des);
		if (!srcFile.exists()) {
			throw new Exception("文件" + src + "不存在！");
		}
		if (!desFile.exists()) {
			FileOperator.createFile(desFile);
			// System.out.println("createFile " + desFile.getAbsolutePath() +
			// " "
			// + desFile.exists());
		}
		FileOperator.writeContent(desFile, FileOperator.readContent(srcFile));
	}

	public static void deleteFile(File file) {
		if (!file.exists()) {
			// System.out.println(file.getAbsolutePath()+" "+file.exists());
			return;
		}
		if (file.isDirectory()) {
			for (File ele : file.listFiles()) {
				deleteFile(ele);
			}
		}
		file.delete();
		// System.out.println("delete file "+file.getAbsolutePath());
	}

	public static void main(String[] args) throws Exception {
		// String path="F:\\LRtec\\test\\temp\\OEBPS\\Text\\chapter10.xhtml";
		// String content=FileOperator.readContent(new File(path));
		// System.out.println(content.substring(2120,2130));
		TagNode tempParagraph = new TagNode();
		tempParagraph.setTagName("/p");
		tempParagraph.setAttribute("class", "\"bodyPic\"");
		TagNode node = new TagNode();
		node.setTagName("p");
		System.out.println(tempParagraph.toHtml() + " " + node.toHtml());
	}
}
