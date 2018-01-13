package control;

import java.io.File;

import org.htmlparser.util.ParserException;

import models.EPUB;
import models.TXEPUB;
import tools.RAR;
import views.Callback;
import views.State;

public class DirTranslation extends AbstractTranslation{
	protected String fileDir;
	protected Callback callback;
	@Override
	public void setState( Callback callback) {
		this.callback = callback;
	}

	public DirTranslation(String fileDir) {
		// TODO Auto-generated constructor stub
		this.fileDir = fileDir;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			File dirFile = new File(fileDir);
			File[] files = dirFile.listFiles(fileFilter);
			RAR rar = new RAR();
			callback.setMaxValue(files.length * 3 + 1);
			callback.setValue(0);
			rar.setTranslatedDir(fileDir + File.separator + "translate");
			EPUB epub = null;
			for (File file : files) {
				epub = this.getEPUB(file);
				rar.setEpub(epub);
				callback.addValue();
				callback.setState("��ѹ��" + file.getName() + "��...");
				Thread.sleep(10);
				rar.unCompress();
				callback.setState("��ѹ��" + file.getName() + "�����");
				Thread.sleep(10);
				callback.addValue();
				callback.setState("ת����" + file.getName() + "��...");
				Thread.sleep(10);
				rar.getEpub().translate();
				callback.setState("ת����" + file.getName() + "�����");
				Thread.sleep(10);
				callback.addValue();
				callback.setState("ѹ����" + file.getName() + "��...");
				Thread.sleep(10);
				rar.compress();
				callback.setState("ѹ����" + file.getName() + "�����");
				Thread.sleep(10);
			}
			callback.setState("ɾ���м��ļ�...");
			callback.addValue();
			rar.deleteTempFile();
			callback.setState("ת�����");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			callback.setError(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			callback.setError(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected EPUB getEPUB(File file) throws ParserException {
		return null;
	}
}
