package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class State extends JDialog implements Callback {
	private JProgressBar process;
	private JLabel state;
	private JLabel error;
	public static Font font = new Font("宋体", Font.TYPE1_FONT, 16);

	public State(JFrame par) {
		// TODO Auto-generated constructor stub
		super(par, "转化......", true);
		this.setLayout(new GridLayout(5, 1));
		this.setBounds(200, 100, 400, 200);
		this.add(new JLabel());
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		process = new JProgressBar(0, 100);
		process.setPreferredSize(new Dimension(330, 30));
		process.setForeground(Color.BLUE);
		panel.add(process);
		this.add(panel);
		this.add(new JLabel());
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		state = new JLabel();
		state.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(state);
		state.setFont(font);
		this.add(panel);
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		error = new JLabel();
		error.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(error);
		error.setFont(font);
		this.add(panel);
		this.getFocusableWindowState();
		this.requestFocus();
		this.setLocationRelativeTo(par);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// selfRun();
	}

	@Override
	public void setValue(int value) {
		// TODO Auto-generated method stub
		process.setValue(value);
		if (process.getValue() == process.getMaximum()) {
			this.setState("转化完毕");
		}
	}

	public void addValue() {
		setValue(process.getValue() + 1);
	}

	@Override
	public void setMaxValue(int value) {
		// TODO Auto-generated method stub
		process.setMaximum(value);
	}

	@Override
	public void setState(String msg) {
		// TODO Auto-generated method stub
		this.state.setText(msg);
	}

	@Override
	public void setError(String error) {
		// TODO Auto-generated method stub
		this.error.setText(error);
	}

	public void selfRun() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int i = 0;
				while (process.getValue() != process.getMaximum()) {
					setValue(++i);
					setState(i + "");
					setError("no errot " + i);
					System.out.println(i);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
