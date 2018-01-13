package compress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress implements Runnable {
	public static interface ProcessListener {
		void onCompressing(String entryName);
	}

	private ProcessListener listener = null;
	private ZipOutputStream out = null;
	private Hashtable<String, String> content = null;

	/**
	 * @param epubPath
	 * @throws IOException
	 */
	public Compress(String epubPath, Hashtable<String, String> content)
			throws IOException {
		super();
		this.content = content;
		File file = new File(epubPath);
		if (!file.exists())
			file.createNewFile();
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
				file), new CRC32());
		out = new ZipOutputStream(cos);
	}

	public Compress(String epubPath, Hashtable<String, String> content,
			ProcessListener listener) throws IOException {
		this.content = content;
		this.listener = listener;
		File file = new File(epubPath);
		if (!file.exists())
			file.createNewFile();
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
				file), new CRC32());
		out = new ZipOutputStream(cos);
	}

	@Override
	public void run() {
		Iterator<String> ptr = content.keySet().iterator();
		String entryName = null;
		while (ptr.hasNext()) {
			entryName = ptr.next();
			ZipEntry entry = new ZipEntry(entryName);
			try {
				out.putNextEntry(entry);
				out.write(content.get(entryName).getBytes("utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (listener != null) {
				listener.onCompressing(entryName);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
