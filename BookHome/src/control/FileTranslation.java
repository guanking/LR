package control;

import java.io.File;

import org.htmlparser.util.ParserException;

import tools.RAR;
import views.Callback;
import views.State;
import models.EPUB;
import models.TXEPUB;

public class FileTranslation extends AbstractTranslation {
	protected String path;
	protected Callback callback;
	@Override
	public void setState(Callback callback) {
		this.callback = callback;
	}

	public FileTranslation(String filePath) {
		// TODO Auto-generated constructor stub
		this.path = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			EPUB epub = getEPUB(null);
			if (callback != null) {
				callback.setValue(5);
				callback.setState("提取文件......");
			}
			RAR rar = new RAR(path.substring(0, path.lastIndexOf('\\') + 1)
					+ "translate", epub);
			Thread.sleep(10);
			if (callback != null) {
				rar.unCompress();
				callback.setValue(30);
				callback.setState("成功提取文件" + path);
			} else {
				rar.unCompress();
			}
			Thread.sleep(10);
			if (callback != null) {
				callback.setState("转化文件.......");
				rar.getEpub().translate();
				callback.setState("转化文件完成");
				callback.setValue(60);
			} else {
				rar.getEpub().translate();
			}
			Thread.sleep(10);
			if (callback != null) {
				callback.setState("压缩文件.......");
				rar.compress();
				callback.setState("压缩文件完成");
				callback.setValue(90);
			} else {
				rar.compress();
			}
			Thread.sleep(10);
			if (callback != null) {
				callback.setState("删除缓存文件.......");
				rar.deleteTempFile();
				callback.setState("转化完成");
				callback.setValue(100);
			} else {
				rar.deleteTempFile();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (callback != null) {
				callback.setError(e.getMessage());
			}
			e.printStackTrace();
		}
	}

	@Override
	protected EPUB getEPUB(File file) throws ParserException {
		// TODO Auto-generated method stub
		return null;
	}
}
