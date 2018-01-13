package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Content {

	public static void main(String[] args) throws IOException {
		File file=new File("F:\\play\\≤‚ ‘\\untitled.epub");
		FileReader reader=new FileReader(file);
		StringBuffer sb=new StringBuffer();
		int len=0;
		char[] buf=new char[1024];
		while((len=reader.read(buf,0,1024))!=-1){
			sb.append(buf,0,len);
		}
		System.out.println(sb.toString());
	}
}
