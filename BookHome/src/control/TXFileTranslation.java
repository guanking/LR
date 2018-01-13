package control;

import java.io.File;

import models.EPUB;
import models.TXEPUB;

public class TXFileTranslation extends FileTranslation {

	public TXFileTranslation(String filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EPUB getEPUB(File file) {
		// TODO Auto-generated method stub
		return new TXEPUB(this.path, this.path.substring(0,
				this.path.lastIndexOf('\\'))
				+ "\\temp");
	}
}
