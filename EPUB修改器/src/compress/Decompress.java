package compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {
	private String epubPath;
	private Hashtable<String, String> content = new Hashtable<String, String>();

	/**
	 * @param epubPath
	 * @throws FileNotFoundException
	 */
	public Decompress(String epubPath) throws FileNotFoundException {
		super();
		this.epubPath = epubPath;
		compress();
	}

	public String getEpubPath() {
		return epubPath;
	}

	private void compress() throws FileNotFoundException {
		File file = new File(epubPath);
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw e;
		}
		ZipInputStream zipIn = new ZipInputStream(in);
		ZipEntry entry = null;
		try {
			String name = null;
			StringBuffer sb = new StringBuffer();
			while ((entry = zipIn.getNextEntry()) != null) {
				name = entry.getName();
				if (!name.endsWith(".xhtml"))
					continue;
				int count = 0;
				byte[] buf = new byte[1024];
				while ((count = zipIn.read(buf)) != -1) {
					sb.append(new String(buf, 0, count, "utf-8"));
				}
				content.put(name, sb.toString());
			}
			zipIn.closeEntry();
			zipIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<String> getEntrys() {
		return content.keySet();
	}

	public String getContent(String entryName) {
		return content.keySet().contains(entryName) ? content.get(entryName)
				: null;
	}

	public void setContent(String entryName, String content) throws Exception {
		if (!this.content.keySet().contains(entryName))
			throw new Exception("No such entryName : " + entryName);
		this.content.put(entryName, content);
	}

	public Hashtable<String, String> getContent() {
		return content;
	}
}
