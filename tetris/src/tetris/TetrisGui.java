package tetris;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TetrisGui {
	TetrisFrame frame;

	public TetrisGui(Mechanics game) {
		frame = new TetrisFrame(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void endGame() {
		
	}

	public void update() {
		frame.update();
	}

}

class KeyActions implements KeyListener {
	Mechanics game;
	TetrisFrame frame;
	int keyID;

	public KeyActions(Mechanics movementInTetris, TetrisFrame f) {
		game = movementInTetris;
		frame = f;
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
			if (game.isPaused())
				game.unPause();
			else
				game.pause();
		}
		frame.update();
	}
}

class TetrisFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int DEF_WIDTH = 800;
	private static final int DEF_HEIGHT = 600;
	JPanel WindowPanel;
	JPanel tetrisBoard;
	KeyActions k;

	Mechanics mech;

	public TetrisFrame(Mechanics m) {
		setSize(DEF_WIDTH, DEF_HEIGHT);
		setTitle("Tetris vol Jacob");
		setLocationRelativeTo(null);
		setResizable(false);

		tetrisBoard = new BoardBox(m.getBoard());
		WindowPanel = new InfoBox();
		k = new KeyActions(m, this);
		addKeyListener(k);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);

		JSplitPane splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneH.setEnabled(false);
		topPanel.add(splitPaneH, BorderLayout.CENTER);
		splitPaneH.setLeftComponent(tetrisBoard);
		splitPaneH.setRightComponent(WindowPanel);

		mech = m;
	}

	public void update() {
		tetrisBoard.repaint();
	}

}

class InfoBox extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel time = new JLabel("Time:");
	private JLabel points = new JLabel("Points:");
	private JLabel nextBrick = new JLabel("next brick will be:");
	private JButton pause = new JButton("Pause");
	private Color panelColor=new Color(237, 244, 248);

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
		// add(pause,BorderLayout.SOUTH);
	}

}

class BoardBox extends JPanel {

	private static final long serialVersionUID = 1L;
	private static Point startPos = new Point(55, 60);
	private static int brickSize = 20;

	final Color board[][];

	public BoardBox(final Color b[][]) {
		board = b;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(320, 550));
		setBackground(new Color(222, 236, 242));
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
				if (board[i][j] != Color.WHITE) {
					g.setColor(board[i][j]);
					g.fillRect(startPos.x + i * brickSize, startPos.y + j * brickSize, brickSize - 2, brickSize - 2);
					g.setColor(Color.BLACK);
				}

			}
		}

		String Message = "Tetris Game";
		g2.setFont(new Font("Verdana", Font.PLAIN, 20));
		g2.drawString(Message, 93, 30);
	}

}
