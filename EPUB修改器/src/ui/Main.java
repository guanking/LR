package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class Main extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String dirPath = null;
	private JTextField choiceFileText;
	private Font labelFont = new Font("宋体", Font.LAYOUT_NO_START_CONTEXT|Font.BOLD, 14);
	private Font buttonFont = new Font("微软雅黑", Font.PLAIN, 14);
	private Color labelColor = new Color(240, 240, 240);

	public Main() {
		super("EPUB修改器");
		setSize(800, 500);
		ImageIcon background = new ImageIcon("images\\background.png");
		JLabel label = new JLabel(background);
		label.setBounds(0, 0, 800, 500);
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		((JPanel) this.getContentPane()).setOpaque(false);
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridLayout(8, 1, 0, 0));
		panel.setOpaque(false);
		Font font = new Font("微软雅黑", Font.BOLD, 18);
		Font innerFont=new Font("微软雅黑",Font.CENTER_BASELINE,14);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images//icon.png"));
		this.setFont(font);
		/**
		 * choice file field begin
		 */
		JPanel choiceFilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,
				5));
		choiceFilePanel.setOpaque(false);
		label = new JLabel("选取文件：");
		label.setFont(font);
		label.setOpaque(true);
		label.setBackground(labelColor);
		choiceFileText = new JTextField(30);
		JButton button = new JButton("浏览文件");
		button.setFont(new Font("微软雅黑",Font.PLAIN|Font.BOLD,16));
		button.setForeground(Color.RED);
		button.addActionListener(this);
		choiceFilePanel.add(label);
		choiceFilePanel.add(choiceFileText);
		choiceFilePanel.add(button);

		/**
		 * choice file filed end
		 */
		panel.add(choiceFilePanel);
		/**
		 * paragraph deal filed begin
		 */
		JPanel paragraphPanel = new JPanel(new FlowLayout(50, 20, 0));
		paragraphPanel.setOpaque(false);
		label = new MyLabel("一、断行处理 ");
		JButton paragraphButton = new JButton("处理");
		initButton(paragraphButton);
		paragraphPanel.add(label);
		paragraphPanel.add(paragraphButton);
		/**
		 * paragraph file filed end
		 */
		panel.add(paragraphPanel);
		/**
		 * Chinese and English deal filed begin
		 */
		JPanel chineseAndEnglishDealPanel = new JPanel(
				new FlowLayout(10, 20, 0));
		chineseAndEnglishDealPanel.setOpaque(false);
		label = new MyLabel("二、中英文处理 ");
		JButton chineseButton = new JButton("中文");
		JButton englishButton = new JButton("英文");
		initButton(chineseButton);
		initButton(englishButton);
		chineseAndEnglishDealPanel.add(label);
		chineseAndEnglishDealPanel.add(chineseButton);
		chineseAndEnglishDealPanel.add(englishButton);
		/**
		 * Chinese and English deal filed end
		 */
		panel.add(chineseAndEnglishDealPanel);
		/**
		 * quotation deal filed begin
		 */
		JPanel quotationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20,
				0));
		quotationPanel.setOpaque(false);
		label = new MyLabel("三、引号处理");
		JButton matchButton = new JButton("匹配");
		JButton dealButton = new JButton("处理");
		initButton(matchButton);
		initButton(dealButton);
		quotationPanel.add(label);
		quotationPanel.add(matchButton);
		quotationPanel.add(dealButton);
		/**
		 * quotation deal filed end
		 */
		panel.add(quotationPanel);
		/**
		 * English past deal filed begin
		 */
		JPanel englishPastPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,
				20, 0));
		englishPastPanel.setOpaque(false);
		label = new MyLabel("▲四、有规律粘连处理（正常书慎用）");
		label.setForeground(Color.RED);
		englishPastPanel.add(label);
		JButton globalDealButton = new JButton("全局处理");
		JButton localDealButton = new JButton("局部处理");
		initButton(globalDealButton);
		initButton(localDealButton);
		globalDealButton.addActionListener(this);
		localDealButton.addActionListener(this);
		englishPastPanel.add(globalDealButton);
		englishPastPanel.add(localDealButton);
		label = new JLabel("选择需处理界面");
		label.setFont(innerFont);
		label.setOpaque(true);
		label.setBackground(labelColor);
		JTextField dealText = new JTextField(10);
		englishPastPanel.add(globalDealButton);
		englishPastPanel.add(localDealButton);
		englishPastPanel.add(label);
		englishPastPanel.add(dealText);
		/**
		 * English past deal filed end
		 */
		panel.add(englishPastPanel);
		/**
		 * note change filed begin
		 */
		JPanel noteChangePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20,
				0));
		noteChangePanel.setOpaque(false);
		label = new MyLabel("五、注释修改 ");
		JButton matchNoteButton = new JButton("匹配");
		JButton TXButton = new JButton("腾讯");
		JButton DDButton = new JButton("当当");
		initButton(matchNoteButton);
		initButton(TXButton);
		initButton(DDButton);
		noteChangePanel.add(label);
		noteChangePanel.add(matchNoteButton);
		noteChangePanel.add(TXButton);
		noteChangePanel.add(DDButton);
		/**
		 * note change filed end
		 */
		panel.add(noteChangePanel);
		/**
		 * package filed begin
		 */
		JPanel packagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		packagePanel.setOpaque(false);
		JButton packageAgain = new JButton("重新打包EPUB(ops)");
		initButton(packageAgain);
		label = new JLabel("版本号");
		label.setFont(innerFont);
		label.setOpaque(true);
		label.setBackground(labelColor);
		JTextField versionNumber = new JTextField(10);
		packagePanel.add(packageAgain);
		packagePanel.add(label);
		packagePanel.add(versionNumber);
		/**
		 * package filed end
		 */
		panel.add(packagePanel);
		/**
		 * statistic filed begin
		 */
		JPanel statisticPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statisticPanel.setOpaque(false);
		label = new JLabel("批量统计: ");
		label.setOpaque(true);
		label.setFont(font);
		label.setBackground(labelColor);
		JTextField statisticText = new JTextField(50);
		statisticPanel.add(label);
		statisticPanel.add(statisticText);
		/**
		 * statistic filed end
		 */
		panel.add(statisticPanel);
		label = new JLabel();
		label.setPreferredSize(new Dimension(30, 50));
		this.add(label, BorderLayout.WEST);
		label = new JLabel();
		label.setPreferredSize(new Dimension(10, 18));
		this.add(label, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] Args) {
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = ((JButton) e.getSource()).getText();
		switch (text) {
		case "浏览文件":
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.setFont(labelFont);
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
					return f.isDirectory() ? true : f.getName().endsWith(
							".epub");
				}
			});
			fileChoose.showOpenDialog(null);
			File file = fileChoose.getSelectedFile();
			if (file != null) {
				dirPath = file.getParent();
				choiceFileText.setText(file.getName());
			} else {
				JOptionPane.showConfirmDialog(null, "您未选择任何文件！");
			}
			break;
		default:
			JOptionPane.showConfirmDialog(null, text);
			break;
		}
	}

	private class MyLabel extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyLabel(String text) {
			super(text);
			this.setFont(labelFont);
			this.setOpaque(true);
			this.setBackground(labelColor);
		}
	}

	private void initButton(JButton button) {
		button.setFont(buttonFont);
		button.addActionListener(this);
	}
}
