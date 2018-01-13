package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import control.AbstractTranslation;
import control.DDDirTranslation;
import control.DDFileTranslation;
import control.DirTranslation;
import control.FileTranslation;
import control.QTDirTranslation;
import control.QTFileTranslation;
import control.TXDirTranslation;
import control.TXFileTranslation;

public class Trans extends JFrame {
	public static final String TXFile = "TXFile", TXDir = "TXDir",
			DDFile = "DDFile", DDDir = "DDDir", QTFile = "QTFile",
			QTDir = "QTDir";
	private String dirPath;
	private HashMap<String, MyTextFiled> texts = new HashMap<String, MyTextFiled>();
	private HashMap<String, MyButton> buttons = new HashMap<String, MyButton>();
	private HashMap<String, MyButton> trans = new HashMap<String, MyButton>();

	public Trans() {
		// TODO Auto-generated constructor stub
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("书房代码转化工具");
		this.setLocationRelativeTo(null);
		this.texts.put(TXFile, new MyTextFiled());
		this.texts.put(TXDir, new MyTextFiled());
		this.texts.put(DDFile, new MyTextFiled());
		this.texts.put(DDDir, new MyTextFiled());
		this.texts.put(QTFile, new MyTextFiled());
		this.texts.put(QTDir, new MyTextFiled());
		this.buttons.put(TXFile, new MyButton("选择文件", buttonClick, TXFile));
		this.buttons.put(TXDir, new MyButton("选择文件夹", buttonClick, TXDir));
		this.buttons.put(DDFile, new MyButton("选择文件", buttonClick, DDFile));
		this.buttons.put(DDDir, new MyButton("选择文件夹", buttonClick, DDDir));
		this.buttons.put(QTFile, new MyButton("选择文件", buttonClick, QTFile));
		this.buttons.put(QTDir, new MyButton("选择文件夹", buttonClick, QTDir));
		this.trans.put(TXFile, new MyButton("转化", transClick, TXFile));
		this.trans.put(TXDir, new MyButton("批量转化", transClick, TXDir));
		this.trans.put(DDFile, new MyButton("转化", transClick, DDFile));
		this.trans.put(DDDir, new MyButton("批量转化", transClick, DDDir));
		this.trans.put(QTFile, new MyButton("转化", transClick, QTFile));
		this.trans.put(QTDir, new MyButton("批量转化", transClick, QTDir));
		try {
			Image image = ImageIO.read(new File("images/icon.png"));
			this.setIconImage(image);
			ImageIcon background = new ImageIcon("images\\background.png");
			JLabel label = new JLabel(background);
			label.setBounds(0, 0, 850, 650);
			this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
			((JPanel) this.getContentPane()).setOpaque(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.add(new MyLabel("书房代码转化工具").getTitle(), BorderLayout.NORTH);
		this.add(leftPanel(), BorderLayout.WEST);
		this.add(centerPanel(), BorderLayout.CENTER);
		this.setVisible(true);
	}

	private JPanel leftPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.setOpaque(false);
		JLabel label = new MyLabel("一、腾讯项目").getRegLabel();
		JPanel tempPanel = new JPanel(new FlowLayout());
		tempPanel.add(label);
		tempPanel.setOpaque(false);
		panel.add(tempPanel);
		label = new MyLabel("二、当当项目").getRegLabel();
		tempPanel = new JPanel(new FlowLayout());
		tempPanel.add(label);
		tempPanel.setOpaque(false);
		panel.add(tempPanel);
		label = new MyLabel("三、其他项目").getRegLabel();
		tempPanel = new JPanel(new FlowLayout());
		tempPanel.setOpaque(false);
		tempPanel.add(label);
		panel.add(tempPanel);
		return panel;
	}

	private JPanel centerPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.setOpaque(false);
		/* TX */
		JPanel subPanel = getCenterSubPanel();
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.setOpaque(false);
		temp.add(buttons.get(TXFile));
		temp.add(texts.get(TXFile));
		temp.add(trans.get(TXFile));
		subPanel.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.setOpaque(false);
		temp.add(buttons.get(TXDir));
		temp.add(texts.get(TXDir));
		temp.add(trans.get(TXDir));
		subPanel.add(temp);
		panel.add(subPanel);
		/* DD */
		subPanel = getCenterSubPanel();
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.setOpaque(false);
		temp.add(buttons.get(DDFile));
		temp.add(texts.get(DDFile));
		temp.add(trans.get(DDFile));
		subPanel.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.setOpaque(false);
		temp.add(buttons.get(DDDir));
		temp.add(texts.get(DDDir));
		temp.add(trans.get(DDDir));
		subPanel.add(temp);
		panel.add(subPanel);
		/* QT */
		subPanel = getCenterSubPanel();
		JLabel tempLabel = new JLabel();
		tempLabel.setPreferredSize(new Dimension(100, 10));
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.setOpaque(false);
		temp.add(buttons.get(QTFile));
		temp.add(texts.get(QTFile));
		temp.add(trans.get(QTFile));
		subPanel.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.setOpaque(false);
		temp.add(buttons.get(QTDir));
		temp.add(texts.get(QTDir));
		tempLabel = new JLabel();
		tempLabel.setPreferredSize(new Dimension(100, 10));
		temp.add(trans.get(QTDir));
		subPanel.add(temp);
		panel.add(subPanel);
		return panel;
	}

	private JPanel getCenterSubPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.setOpaque(false);
		return panel;
	}

	public static void main(String[] args) {
		new Trans();
	}

	private ActionListener buttonClick = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = ((JButton) e.getSource()).getName();
			String text;
			if (name.endsWith("File")) {
				text = getFileChoice();
			} else {
				text = getDirChoice();
			}
			Trans.this.texts.get(name).setText(text);
		}
	};
	ActionListener transClick = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String name = ((JButton) e.getSource()).getName();
			String text = Trans.this.texts.get(name).getText();
			if (text.equals("")) {
				JOptionPane.showConfirmDialog(null, "请选择文件");
				return;
			} else {
				switch (name) {
				case TXFile:
					(new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							State d = new State(Trans.this);
							String path = Trans.this.texts.get(TXFile)
									.getText();
							FileTranslation tr = new TXFileTranslation(path);
							tr.setState(d);
							new Thread(tr).start();
							d.show();
						}
					})).start();
					break;
				case DDFile:
					(new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							State d = new State(Trans.this);
							String path = Trans.this.texts.get(DDFile)
									.getText();
							FileTranslation tr = new DDFileTranslation(path);
							tr.setState(d);
							new Thread(tr).start();
							d.show();
						}
					})).start();
					break;
				case QTFile:
					new MyFrame(Trans.this, Trans.this.texts.get(QTFile)
							.getText());
					Trans.this.setEnabled(false);
					break;
				case TXDir:
					new Thread(new Runnable() {
						public void run() {
							State d = new State(Trans.this);
							String path = Trans.this.texts.get(TXDir).getText();
							TXDirTranslation tr = new TXDirTranslation(path);
							tr.setState(d);
							new Thread(tr).start();
							d.show();
						}
					}).start();
					break;
				case DDDir:
					new Thread(new Runnable() {
						public void run() {
							State d = new State(Trans.this);
							String path = Trans.this.texts.get(DDDir).getText();
							DDDirTranslation tr = new DDDirTranslation(path);
							tr.setState(d);
							new Thread(tr).start();
							d.show();
						}
					}).start();
					break;
				case QTDir:
					new MyFrame(Trans.this, Trans.this.texts.get(QTDir)
							.getText());
					Trans.this.setEnabled(false);
					break;
				default:
					JOptionPane.showConfirmDialog(null, "未匹配的标签名字 ：" + name);
				}
			}
		}
	};

	public void transQTFile(final LinkedList<LinkedList<String>> rules,
			final boolean isFile) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				State d = new State(Trans.this);
				String path = null;
				AbstractTranslation tr = null;
				if (isFile) {
					path = Trans.this.texts.get(QTFile).getText();
					tr = new QTFileTranslation(path, rules);
				} else {
					path = Trans.this.texts.get(QTDir).getText();
					tr = new QTDirTranslation(path, rules);
				}
				tr.setState(d);
				new Thread(tr).start();
				d.show();
			}
		}).start();
	}

	private String getFileChoice() {
		JFileChooser fileChoose = new JFileChooser();
		fileChoose.setDialogTitle("选择文件");
		fileChoose.setFont(MyLabel.font);
		if (dirPath != null) {
			fileChoose.setSelectedFile(new File(dirPath));
		}
		fileChoose.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "*.epub";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() ? true : f.getName().endsWith(".epub");
			}
		});
		fileChoose.showOpenDialog(null);
		File file = fileChoose.getSelectedFile();
		if (file != null) {
			return file.getAbsolutePath();
		} else {
			JOptionPane.showConfirmDialog(null, "您未选择任何文件！");
			return null;
		}
	}

	private String getDirChoice() {
		JFileChooser fileChoose = new JFileChooser();
		fileChoose.setDialogTitle("选择文件夹");
		fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChoose.setFont(MyLabel.font);
		if (dirPath != null) {
			fileChoose.setSelectedFile(new File(dirPath));
		}
		fileChoose.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "*.dir";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		fileChoose.showOpenDialog(null);
		File file = fileChoose.getSelectedFile();
		if (file != null) {
			return file.getAbsolutePath();
		} else {
			JOptionPane.showConfirmDialog(null, "您未选择任何文件！");
			return null;
		}
	}
}
