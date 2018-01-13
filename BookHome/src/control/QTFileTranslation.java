package control;

import java.io.File;
import java.util.LinkedList;

import org.htmlparser.util.ParserException;

import models.EPUB;
import models.QTEPUB;

public class QTFileTranslation extends FileTranslation {
public LinkedList<LinkedList<String>> rules;
	public QTFileTranslation(String filePath, LinkedList<LinkedList<String>> rules) {
		// TODO Auto-generated constructor stub
		super(filePath);
		this.rules=rules;
	}

	@Override
	protected EPUB getEPUB(File file) throws ParserException {
		// TODO Auto-generated method stub
		System.out.println(this.path);
		return new QTEPUB(this.path, this.path.substring(0,
				this.path.lastIndexOf('\\'))
				+ "\\temp",rules);
	}
}
