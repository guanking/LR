package views;

import java.awt.Font;

import javax.swing.JTextField;

public class MyTextFiled extends JTextField {
	private static Font font = new Font("Î¢ÈíÑÅºÚ", Font.ITALIC, 18);

	public MyTextFiled() {
		// TODO Auto-generated constructor stub
		super(20);
		this.setEditable(false);
		this.setFont(font);
	}

	public MyTextFiled(int cols) {
		super(cols);
		this.setEditable(false);
		this.setFont(font);
	}
}
