package control;

import java.io.File;

import models.DDEPUB;
import models.EPUB;

public class DDFileTranslation extends FileTranslation {

	public DDFileTranslation(String filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EPUB getEPUB(File file) {
		// TODO Auto-generated method stub
		return new DDEPUB(this.path, this.path.substring(0,
				this.path.lastIndexOf('\\'))
				+ "\\temp");
	}
}
