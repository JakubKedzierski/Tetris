package tetris;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class TetrisGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int DEF_WIDTH = 800;
	private static final int DEF_HEIGHT = 600;
	JPanel WindowPanel;
	JPanel tetrisBoard;
	KeyActions keyActions;

	public TetrisGui(Tetris game) {
		setSize(DEF_WIDTH, DEF_HEIGHT);
		setTitle("Tetris vol Jacob");
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		requestFocusInWindow();

		tetrisBoard = new BoardBox(game.getMechanics().getBoard());
		WindowPanel = new InfoBox(game);
		keyActions = new KeyActions(game, this);
		addKeyListener(keyActions);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);

		JSplitPane splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneH.setEnabled(false);
		topPanel.add(splitPaneH, BorderLayout.CENTER);
		splitPaneH.setLeftComponent(tetrisBoard);
		splitPaneH.setRightComponent(WindowPanel);
		splitPaneH.setBackground(SystemColor.inactiveCaptionBorder);
		splitPaneH.setDividerSize(1);
		pack();
		
		setVisible(true);

	}
	
	public void endGame() {
		new EndGameFrame(this);
		dispose();
	}

	public void update() {
		tetrisBoard.repaint();
		requestFocusInWindow();
	}
	
	
	public void sayMessage(Exception err) {
		JOptionPane.showMessageDialog(this, err.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
	}

}



class KeyActions implements KeyListener {
	Tetris game;
	TetrisGui frame;
	int keyID;

	public KeyActions(Tetris movementInTetris, TetrisGui f) {
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
			game.moveSide(Direction.LEFT);
		}

		if (keyID == KeyEvent.VK_RIGHT) {
			game.moveSide(Direction.RIGHT);
		}

		if (keyID == KeyEvent.VK_DOWN) {
			game.moveTetriminoToEnd();
		}

		if (keyID == KeyEvent.VK_D) {
			game.rotate(true);
		}
		
		if (keyID == KeyEvent.VK_X) {
			game.write();
		}
		
		
		if (keyID == KeyEvent.VK_A) {
			game.rotate(false);
		}

		if (keyID == KeyEvent.VK_SPACE) {
			if (game.getMechanics().isStop())
				game.getMechanics().setStop(false);
			else
				game.getMechanics().setStop(true);;
		}
		frame.update();
	}
}


