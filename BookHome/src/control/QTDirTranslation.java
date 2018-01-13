package control;

import java.io.File;
import java.util.LinkedList;

import org.htmlparser.util.ParserException;

import models.EPUB;
import models.QTEPUB;
import models.TXEPUB;

public class QTDirTranslation extends DirTranslation {
	private LinkedList<LinkedList<String>> rules;
	public QTDirTranslation(String fileDir,LinkedList<LinkedList<String>> rules) {
		super(fileDir);
		this.rules=rules;
		// TODO Auto-generated constructor stub
	}
	@Override
	protected EPUB getEPUB(File file) throws ParserException {
		// TODO Auto-generated method stub
		return new QTEPUB(file.getAbsolutePath(), fileDir + File.separator
				+ "temp",this.rules);
	}
}
