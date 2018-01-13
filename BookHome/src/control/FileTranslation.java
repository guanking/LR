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
				callback.setState("��ȡ�ļ�......");
			}
			RAR rar = new RAR(path.substring(0, path.lastIndexOf('\\') + 1)
					+ "translate", epub);
			Thread.sleep(10);
			if (callback != null) {
				rar.unCompress();
				callback.setValue(30);
				callback.setState("�ɹ���ȡ�ļ�" + path);
			} else {
				rar.unCompress();
			}
			Thread.sleep(10);
			if (callback != null) {
				callback.setState("ת���ļ�.......");
				rar.getEpub().translate();
				callback.setState("ת���ļ����");
				callback.setValue(60);
			} else {
				rar.getEpub().translate();
			}
			Thread.sleep(10);
			if (callback != null) {
				callback.setState("ѹ���ļ�.......");
				rar.compress();
				callback.setState("ѹ���ļ����");
				callback.setValue(90);
			} else {
				rar.compress();
			}
			Thread.sleep(10);
			if (callback != null) {
				callback.setState("ɾ�������ļ�.......");
				rar.deleteTempFile();
				callback.setState("ת�����");
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
