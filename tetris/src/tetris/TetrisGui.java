package tetris;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class TetrisGui {
	TetrisFrame frame;
	KeyActions k;

	public TetrisGui(Mechanics game) {
		frame = new TetrisFrame(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		k = new KeyActions(game,frame);
		frame.addKeyListener(k);
	}

	public void update() {
		frame.update();
	}

}

class KeyActions implements KeyListener {
	Mechanics game;
	TetrisFrame frame;
	int keyID;

	public KeyActions(Mechanics movementInTetris,TetrisFrame f) {
		game = movementInTetris;
		frame=f;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyID = e.getKeyCode();
		update();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void update() {
		if (keyID == KeyEvent.VK_LEFT) {
			game.move(Direction.LEFT);
		}
		
		if (keyID == KeyEvent.VK_RIGHT) {
			game.move(Direction.RIGHT);
		}
		
		if (keyID == KeyEvent.VK_DOWN) {
			game.moveToEndPosition();
		}
		
		if (keyID == KeyEvent.VK_D) {
			game.rotate();
		}
		
		if (keyID == KeyEvent.VK_SPACE) {
			if(game.isPaused())
				game.unPause();
			else
				game.pause();
		}
		frame.update();
	}
}

class TetrisFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int DEF_WIDTH = 800;
	private static final int DEF_HEIGHT = 600;
	JPanel tetrisBoard;
	Mechanics mech;
	
	public TetrisFrame(Mechanics m) {
		setSize(DEF_WIDTH, DEF_HEIGHT);
		setTitle("Tetris vol Jacob");
		setLocationRelativeTo(null);
		tetrisBoard = new BoardBox(m.getBoard());
		add(tetrisBoard);
		mech=m;
	}

	public void update() {
		tetrisBoard.repaint();
	}
	


}

class BoardBox extends JPanel {

	private static final long serialVersionUID = 1L;

	final Color board[][];

	public BoardBox(final Color b[][]) {
		board = b;
		
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				g2.drawRect(40 + i * 20, 50 + j * 20, 20, 20);
				if (board[i][j] != Color.WHITE) {
					g.setColor(board[i][j]);
					g.fillRect(40 + i * 20, 50 + j * 20, 20, 20);
					g.setColor(Color.BLACK);
				}

			}
		}

		String Message = "Tetris";
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g2.drawString(Message, 40, 40);
	}

}
