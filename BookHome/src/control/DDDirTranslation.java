package control;

import java.io.File;

import models.DDEPUB;
import models.EPUB;

public class DDDirTranslation extends DirTranslation {

	public DDDirTranslation(String fileDir) {
		super(fileDir);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EPUB getEPUB(File file) {
		// TODO Auto-generated method stub
		return new DDEPUB(file.getAbsolutePath(), fileDir + File.separator
				+ "temp");
	}
}
