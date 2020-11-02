package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


class InfoBox extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel time = new JLabel("Time:");
	private JLabel points = new JLabel("Points:");
	private JLabel nextBrick = new JLabel("   next brick will be:");
	private JButton pause = new JButton("Pause");
	private Color panelColor=new Color(237, 244, 248);
	private static int brickSize = 20;

	InfoBox() {
		setBackground(panelColor);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200, 300));
		JLabel labels[] = { time, points, nextBrick };

		for (JLabel label : labels) {
			label.setBounds(50, 50, 100, 30);
			label.setFont(new Font("Verdana", Font.PLAIN, 18));
		}
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout());
		timePanel.add(time, BorderLayout.NORTH);
		timePanel.add(points, BorderLayout.CENTER);
		timePanel.setBackground(panelColor);
		pause.addActionListener(null);
		
		add(timePanel, BorderLayout.NORTH);
		add(nextBrick, BorderLayout.CENTER);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		Color[][] board = new Color[4][4];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				g2.drawRect(55 + i * brickSize, 350 + j * brickSize, brickSize, brickSize);
				if (board[i][j] != Color.WHITE) {
					g.setColor(board[i][j]);
					g.fillRect(55 + i * brickSize, 350 + j * brickSize, brickSize - 2, brickSize - 2);
					g.setColor(Color.BLACK);
				}

			}
		}
	}
}
