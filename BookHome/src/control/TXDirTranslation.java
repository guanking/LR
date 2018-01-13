package control;

import java.io.File;

import models.EPUB;
import models.TXEPUB;

public class TXDirTranslation extends DirTranslation {

	public TXDirTranslation(String fileDir) {
		super(fileDir);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected EPUB getEPUB(File file) {
		// TODO Auto-generated method stub
		return new TXEPUB(file.getAbsolutePath(), fileDir + File.separator
				+ "temp");
	}
}
