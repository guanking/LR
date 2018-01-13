package control;

import java.io.File;
import java.io.FileFilter;

import org.htmlparser.util.ParserException;

import views.Callback;
import views.State;
import models.EPUB;

public abstract class AbstractTranslation implements Runnable {
	FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(File file) {
			return file.getAbsolutePath().endsWith(".epub");
		}
	};
	protected abstract EPUB getEPUB(File file) throws ParserException;
	public abstract void setState(Callback callback);
}
