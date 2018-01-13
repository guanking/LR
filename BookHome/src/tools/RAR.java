package tools;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import models.EPUB;
import models.QTEPUB;
import models.TXEPUB;

public class RAR {
	private String tempDir;
	private String translatedDir;
	private EPUB epub;

	/**
	 * @param tempDir
	 * @param translatedDir
	 * @param epub
	 */
	public RAR(String translatedDir, EPUB epub) {
		super();
		this.tempDir = epub.getTempDir();
		this.translatedDir = translatedDir;
		this.epub = epub;
	}

	public RAR() {
		// TODO Auto-generated constructor stub
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getTranslatedDir() {
		return translatedDir;
	}

	public void setTranslatedDir(String translatedDir) {
		this.translatedDir = translatedDir;
	}

	public EPUB getEpub() {
		return epub;
	}

	public void setEpub(EPUB epub) {
		this.epub = epub;
		this.tempDir = epub.getTempDir();
	}

	public void unCompress() throws Exception {
		ZipFile zip = new ZipFile(new File(epub.getPath()));
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		String name = null;
		ZipEntry entry;
		String expand;
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			name = entry.getName();
			try {
				expand = name.substring(name.lastIndexOf("."));
			} catch (Exception e) {
				epub.setMimetype(name);
				FileOperator.writeContent(new File(tempDir, name), FileOperator
						.readContent(zip.getInputStream(entry), "utf-8"));
				continue;
			}
			switch (expand) {
			case ".jpg":
				epub.getImages().put(name, name);
				FileOperator.writeImage(new File(tempDir, name),
						FileOperator.readImage(zip.getInputStream(entry)),
						"JPG");
				break;
			case ".png":
				epub.getImages().put(name, name);
				FileOperator.writeImage(new File(tempDir, name),
						FileOperator.readImage(zip.getInputStream(entry)),
						"PNG");
				break;
			case ".xhtml":
				epub.getTexts().add(name);
				FileOperator.writeContent(new File(tempDir, name), FileOperator
						.readContent(zip.getInputStream(entry), "utf-8"));
				break;
			case ".css":
				epub.setStylesheet(name);
				FileOperator.writeContent(new File(tempDir, name), FileOperator
						.readContent(zip.getInputStream(entry), "utf-8"));
				break;
			case ".opf":
				epub.setOpf(name);
				FileOperator.writeContent(new File(tempDir, name), FileOperator
						.readContent(zip.getInputStream(entry), "utf-8"));
				break;
			case ".xml":
				epub.setContainer(name);
				FileOperator.writeContent(new File(tempDir, name), FileOperator
						.readContent(zip.getInputStream(entry), "utf-8"));
				break;
			case ".ncx":
				epub.setNcx(name);
				FileOperator.writeContent(new File(tempDir, name), FileOperator
						.readContent(zip.getInputStream(entry), "utf-8"));
				break;
			default:
				throw new Exception("Can't match type : " + expand);
			}
		}
	}

	public void compress() throws Exception {
		File file = new File(translatedDir, new File(epub.getPath()).getName());
		if (!file.exists()) {
			FileOperator.createFile(file);
		}
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
				file), new CRC32());
		ZipOutputStream out = new ZipOutputStream(cos);
		/* compress container.xml */
		out.putNextEntry(new ZipEntry(epub.getContainer()));
		out.write(FileOperator.getBytesOfFile(new File(tempDir, epub
				.getContainer())));
		/* compress content.opf */
		out.putNextEntry(new ZipEntry(epub.getOpf()));
		out.write(FileOperator.getBytesOfFile(new File(tempDir, epub.getOpf())));
		/* compress mimetype */
		out.putNextEntry(new ZipEntry(epub.getMimetype()));
		out.write(FileOperator.getBytesOfFile(new File(tempDir, epub
				.getMimetype())));
		/* compress toc.ncx */
		out.putNextEntry(new ZipEntry(epub.getNcx()));
		out.write(FileOperator.getBytesOfFile(new File(tempDir, epub.getNcx())));
		/* compress stylesheet.css */
		out.putNextEntry(new ZipEntry(epub.getStylesheet()));
		out.write(FileOperator.getBytesOfFile(new File(tempDir, epub
				.getStylesheet())));
		/* compress *.xhtml */
		for (String path : epub.getTexts()) {
			out.putNextEntry(new ZipEntry(path));
			out.write(FileOperator.getBytesOfFile(new File(tempDir, path)));
		}
		/* compress images */
		Hashtable<String, String> tempImages = epub.getImages();
		Image image;
		String imageName;
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		for (String key : epub.getImages().keySet()) {
			out.putNextEntry(new ZipEntry(imageName=tempImages.get(key)));
			image = FileOperator.readImage(new File(this.tempDir, key));
			ImageIO.write((RenderedImage) image, imageName.endsWith("jpg")?"JPG":"PNG", out);
			out.write(buf.toByteArray());
			buf.flush();
		}
		out.close();
	}

	public void deleteTempFile() {
		FileOperator.deleteFile(new File(tempDir));
	}

	public static void main(String[] args) throws Exception {
		RAR r = new RAR("F:\\LRtec\\test\\translated", new TXEPUB(
				"F:\\LRtec\\test\\货币信贷与人口周期影响下的中国经济转折点-洪迪华-11.15.epub",
				"F:\\LRtec\\test\\temp"));
		r.unCompress();
		System.out.println("extra finish!");
		r.getEpub().translate();
		// r.compress();
	}
}
