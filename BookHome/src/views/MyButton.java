package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class MyButton extends JButton {
	private static Font font = new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 20);

	public MyButton(String text, ActionListener listener, String name) {
		// TODO Auto-generated constructor stub
		super(text);
		this.setOpaque(true);
		this.setBackground(MyLabel.labelColor);
		this.setFont(font);
		this.setPreferredSize(new Dimension(140, 32));
		this.setName(name);
		this.addMouseListener(mouseListen);
		this.addActionListener(listener);
	}

	private MouseListener mouseListen = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			MyButton.this.setBackground(MyLabel.labelColor);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			MyButton.this.setBackground(Color.cyan);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			MyButton.this.setBackground(MyLabel.labelColor);
			MyButton.this.setForeground(Color.BLACK);
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			MyButton.this.setBackground(MyLabel.labelColor);
			MyButton.this.setForeground(Color.RED);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			MyButton.this.setBackground(Color.BLUE);
		}
	};
}
