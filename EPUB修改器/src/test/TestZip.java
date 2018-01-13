package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import tools.ParagraphMerger;
import compress.Compress;
import compress.Compress.ProcessListener;
import compress.Decompress;

public class TestZip {
	static String path = "F:\\play\\≤‚ ‘\\untitled.epub";
	static String outPath = "F:\\play\\≤‚ ‘\\my.epub";

	public static void main1(String[] args) throws IOException {
		FileInputStream in = new FileInputStream(path);
		ZipInputStream zipIn = new ZipInputStream(in);
		ZipEntry entry;
		while ((entry = zipIn.getNextEntry()) != null) {
			String name = entry.getName();
			System.out.println(name + " Content:");
			byte[] buf = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while (zipIn.read(buf, 0, 1024) != -1) {
				sb.append(new String(buf, "utf-8"));
			}
			System.out.println(sb.toString() + "\n\n");
		}
		zipIn.close();
	}

	public static Decompress deCompress(String path) {
		Decompress t = null;
		try {
			t = new Decompress(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Set<String> entrys = t.getEntrys();
		for (String entry : entrys) {
			System.out.println(entry);
			System.out.println(t.getContent(entry));
		}
		return t;
	}

	static void compress(Decompress t) {
		ProcessListener listener = new ProcessListener() {

			@Override
			public void onCompressing(String entryName) {
				System.out.println(entryName);
			}
		};
		try {
			Compress compress = new Compress(outPath, t.getContent(), listener);
			new Thread(compress).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] Args) throws FileNotFoundException {
		//compress(deCompress(path));
		//deCompress(outPath);
		Decompress t=deCompress(path);
		Iterator ptr=t.getEntrys().iterator();
		String content=t.getContent(ptr.next().toString());
		System.out.println(content+"\n\n\n\n\n\n");
		ParagraphMerger p=new ParagraphMerger(content);
		System.out.println(p.getContent());
		System.out.println("finish");
	}
}
