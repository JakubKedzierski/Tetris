package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

class BoardBox extends JPanel {

	private static final long serialVersionUID = 1L;
	private static Point startPos = new Point(55, 60);
	private static int brickSize = 20;
	JLabel tetrisLabel = new JLabel("                 Tetris Game");


	final ColorType board[][];

	public BoardBox(final ColorType b[][]) {
		board = b;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(320, 550));
		setBackground(new Color(222, 236, 242));
		tetrisLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		add(tetrisLabel,BorderLayout.NORTH);
	}
	
	public Color convertColor(ColorType colorType) {
		switch(colorType) {
		case color1:
			return Color.BLUE;
		case color2:
			return Color.PINK;
		case color3:
			return Color.YELLOW;
		case color4:
			return Color.CYAN;
		case color5:
			return Color.GRAY;
		case color6:
			return Color.RED;
		case color7:
			return Color.GREEN;
		case empty:
			return Color.WHITE;
		default:
			return Color.WHITE;
		
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);

		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < board.length + 2; i++) {
				g.setColor(new Color(99, 33, 222));
				g2.fillRect(startPos.x - brickSize + i * brickSize, startPos.y- brickSize +j*21*brickSize, 18, 18);
				g.setColor(Color.LIGHT_GRAY);
				g2.drawRect(startPos.x - brickSize + i * brickSize, startPos.y- brickSize +j*21*brickSize, 20, 20);
			}
		}

		for (int j = 0; j < 2; j++) {
			for (int i = -1; i < 21; i++) {
				g.setColor(new Color(99, 33, 222));
				g2.fillRect(startPos.x - brickSize + j * 11 * brickSize, startPos.y + i * brickSize, 18, 18);
				g.setColor(Color.LIGHT_GRAY);
				g2.drawRect(startPos.x - brickSize + j * 11 * brickSize, startPos.y + i * brickSize, 20, 20);
			}
		}

		g.setColor(Color.BLACK);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				g2.drawRect(startPos.x + i * brickSize, startPos.y + j * brickSize, brickSize, brickSize);
				if (board[i][j] != ColorType.empty) {
					g.setColor(convertColor(board[i][j]));
					g.fillRect(startPos.x + i * brickSize, startPos.y + j * brickSize, brickSize - 2, brickSize - 2);
					g.setColor(Color.BLACK);
				}

			}
		}

	}

}