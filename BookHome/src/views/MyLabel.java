package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyLabel extends JLabel {
	public static Color labelColor = new Color(240, 240, 240);
	protected static Font font = new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 20);
	public State state = null;

	MyLabel(String text) {
		super(text);
		this.setOpaque(true);
		this.setBackground(labelColor);
		this.setFont(font);
		this.setMaximumSize(this.getSize());
		this.setMinimumSize(this.getSize());
		this.setHorizontalAlignment(JLabel.CENTER);
	}

	public JPanel getTitle() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(800, 50));
		this.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 30));
		panel.add(this);
		return panel;
	}

	public MyLabel getRegLabel() {
		this.setPreferredSize(new Dimension(130, 20));
		// System.out.println(this.getHeight());
		return this;
	}
}
