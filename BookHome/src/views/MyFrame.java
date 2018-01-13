package views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import org.htmlparser.util.ParserException;

import tools.FileOperator;

public class MyFrame extends JFrame implements ActionListener {
	protected LinkedList<JTextField> rules = new LinkedList<JTextField>();
	private JTextField text;
	private JPanel rulePanel;
	private JScrollBar scrollBar;
	private int count = 0;
	String path;
	public static final int TEXT = 0, TAG = 1;
	private Trans trans;

	public MyFrame(Trans trans, String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
		this.trans = trans;
		this.setSize(400, 500);
		this.setResizable(false);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton button = new JButton("选择规则");
		button.setName("rule");
		button.addActionListener(this);
		panel.add(button);
		text = new JTextField(25);
		panel.add(text);
		this.add(panel, BorderLayout.NORTH);
		rulePanel = new JPanel(new GridLayout(0, 1));
		rulePanel.setSize(350, 10);
		JScrollPane scroll = new JScrollPane(rulePanel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollBar = scroll.getVerticalScrollBar();
		this.add(scroll);
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button = new JButton("导出");
		button.setName("import");
		button.addActionListener(this);
		panel.add(button);
		button = new JButton("添加");
		button.setName("add");
		panel.add(button);
		button.addActionListener(this);
		button = new JButton("确认");
		button.setName("confirm");
		button.addActionListener(this);
		panel.add(button);
		this.add(panel, BorderLayout.SOUTH);
		this.setTitle("选择转化规则");
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	private void addRule() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new Label(getNextOrder()));
		JTextField text = new JTextField(11);
		text.setPreferredSize(new Dimension(20, 27));
		panel.add(text);
		this.rules.add(text);
		panel.add(new JLabel(" 转化为： "));
		text = new JTextField(11);
		text.setPreferredSize(new Dimension(20, 27));
		panel.add(text);
		this.rulePanel.add(panel);
		this.rules.add(text);
		this.validate();
		this.scrollBar.setValue(this.scrollBar.getMaximum());
	}

	private void addRule(String src, String des) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new Label(getNextOrder()));
		JTextField text = new JTextField(src, 11);
		text.setPreferredSize(new Dimension(20, 27));
		panel.add(text);
		this.rules.add(text);
		panel.add(new JLabel(" 转化为： "));
		text = new JTextField(des, 11);
		text.setPreferredSize(new Dimension(20, 27));
		panel.add(text);
		this.rulePanel.add(panel);
		this.rules.add(text);
		this.validate();
		this.scrollBar.setValue(this.scrollBar.getMaximum());
	}

	private String getNextOrder() {
		count++;
		if (count < 10)
			return "0" + count;
		else
			return count + "";
	}

	/**
	 * 从输入框中获取转化规则
	 * 
	 * @return LinkedList<LinkedList<String>> rules，分为两类：一类是tag，一类是text。
	 *         分别使用MyFrame.TAG，MyFrame.TEXT作为下标获取。这样获取到的每一类是一个数组，
	 *         偶数下标对应src，基数下标是与其前面偶数下标对应的des
	 */
	public LinkedList<LinkedList<String>> getRules() {
		LinkedList<LinkedList<String>> rules = new LinkedList<LinkedList<String>>();
		String src, des;
		rules.add(new LinkedList<String>());
		rules.add(new LinkedList<String>());
		for (int i = 0; i < this.rules.size(); i += 2) {
			src = this.rules.get(i).getText();
			des = this.rules.get(i + 1).getText();
			if (src.matches("^<.+>$") && des.matches("^<.+>$")) {
				rules.get(MyFrame.TAG).addLast(src);
				rules.get(MyFrame.TAG).addLast(des);
			} else {
				rules.get(MyFrame.TEXT).addLast(src);
				rules.get(MyFrame.TEXT).addLast(des);
			}
		}
		return rules;
	}

	private File getRuleFile() {
		JFileChooser chooser = new JFileChooser(
				this.path.endsWith(".epub") ? this.path.substring(0,
						this.path.lastIndexOf('\\')) : path);
		chooser.setDialogTitle("选择规则文件");
		chooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return ".txt";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".txt");
			}
		});
		chooser.showOpenDialog(MyFrame.this);
		return chooser.getSelectedFile();
	}

	// public static void main(String[] args) throws ParserException {
	// new MyFrame(
	// "F:\\LRtec\\callback\\测试1228\\测试1228\\货币信贷与人口周期影响下的中国经济转折点-洪迪华-1221原始.epub");
	// }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		File file;
		LinkedList<LinkedList<String>> tempRules = null;
		switch (button.getName()) {
		case "add":
			addRule();
			break;
		case "confirm":
			tempRules = getRules();
			System.out.println("tag:");
			for (String rule : tempRules.get(MyFrame.TAG)) {
				System.out.print(rule + " ");
			}
			System.out.println("text");
			for (String rule : tempRules.get(MyFrame.TEXT)) {
				System.out.print(rule + " ");
			}
			this.trans.transQTFile(tempRules, this.path.endsWith(".epub"));
			this.dispose();
			System.gc();
			break;
		case "rule":
			file = getRuleFile();
			if (file == null) {
				JOptionPane.showConfirmDialog(this, "您未选择任何文件", "错误提示",
						JOptionPane.CANCEL_OPTION);
				return;
			}
			try {
				this.text.setText(file.getAbsolutePath());
				String text = FileOperator.readTextFile(file);
				System.out.print(text);
				Matcher matcher = Pattern.compile("｜(.+?)｜.+?｜(.+?)｜").matcher(
						text);
				while (matcher.find()) {
					this.addRule(matcher.group(1), matcher.group(2));
					System.out.println(matcher.group(1) + " "
							+ matcher.group(2));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "import":
			String fileName = JOptionPane.showInputDialog(this, "文件名：", "导出",
					JOptionPane.INFORMATION_MESSAGE);
			file = new File(this.path);
			if (!fileName.endsWith(".txt")) {
				fileName = fileName + ".txt";
			}
			if (file.isDirectory()) {
				file = new File(file, fileName);
			} else {
				file = new File(file.getParent(), fileName);
			}
			tempRules = getRules();
			StringBuffer sb = new StringBuffer();
			for (LinkedList<String> ele : tempRules) {
				for (int i = 0; i < ele.size(); i += 2) {
					sb.append("替换字符：｜" + ele.get(i) + "｜替换为：｜" + ele.get(i + 1)
							+ "｜\n");
				}
			}
			try {
				FileOperator.writeToTxtFile(file, sb.toString());
				JOptionPane.showMessageDialog(this,
						"成功将文件保存在" + file.getAbsolutePath(), "导出成功",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"文件导出失败：\n\t" + e1.getMessage(), "导出失败",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			break;
		default:
			System.out.println(button.getText());
			break;
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		this.trans.setEnabled(true);
	}
}