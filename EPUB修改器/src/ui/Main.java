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
	private Font labelFont = new Font("����", Font.LAYOUT_NO_START_CONTEXT|Font.BOLD, 14);
	private Font buttonFont = new Font("΢���ź�", Font.PLAIN, 14);
	private Color labelColor = new Color(240, 240, 240);

	public Main() {
		super("EPUB�޸���");
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
		Font font = new Font("΢���ź�", Font.BOLD, 18);
		Font innerFont=new Font("΢���ź�",Font.CENTER_BASELINE,14);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images//icon.png"));
		this.setFont(font);
		/**
		 * choice file field begin
		 */
		JPanel choiceFilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,
				5));
		choiceFilePanel.setOpaque(false);
		label = new JLabel("ѡȡ�ļ���");
		label.setFont(font);
		label.setOpaque(true);
		label.setBackground(labelColor);
		choiceFileText = new JTextField(30);
		JButton button = new JButton("����ļ�");
		button.setFont(new Font("΢���ź�",Font.PLAIN|Font.BOLD,16));
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
		label = new MyLabel("һ�����д��� ");
		JButton paragraphButton = new JButton("����");
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
		label = new MyLabel("������Ӣ�Ĵ��� ");
		JButton chineseButton = new JButton("����");
		JButton englishButton = new JButton("Ӣ��");
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
		label = new MyLabel("�������Ŵ���");
		JButton matchButton = new JButton("ƥ��");
		JButton dealButton = new JButton("����");
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
		label = new MyLabel("���ġ��й���ճ���������������ã�");
		label.setForeground(Color.RED);
		englishPastPanel.add(label);
		JButton globalDealButton = new JButton("ȫ�ִ���");
		JButton localDealButton = new JButton("�ֲ�����");
		initButton(globalDealButton);
		initButton(localDealButton);
		globalDealButton.addActionListener(this);
		localDealButton.addActionListener(this);
		englishPastPanel.add(globalDealButton);
		englishPastPanel.add(localDealButton);
		label = new JLabel("ѡ���账�����");
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
		label = new MyLabel("�塢ע���޸� ");
		JButton matchNoteButton = new JButton("ƥ��");
		JButton TXButton = new JButton("��Ѷ");
		JButton DDButton = new JButton("����");
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
		JButton packageAgain = new JButton("���´��EPUB(ops)");
		initButton(packageAgain);
		label = new JLabel("�汾��");
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
		label = new JLabel("����ͳ��: ");
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
		case "����ļ�":
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
				JOptionPane.showConfirmDialog(null, "��δѡ���κ��ļ���");
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
