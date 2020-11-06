package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

class MouseClick implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

class InfoBox extends JPanel implements Observer,ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel time = new JLabel("Time:");
	private JLabel points = new JLabel("Points:");
	private JLabel nextBrick = new JLabel("   next brick will be:");
	private JToggleButton pause = new JToggleButton("Pause");
	private Color panelColor=new Color(237, 244, 248);
	private static int brickSize = 20;
	private Mechanics mechanics;

	InfoBox(Mechanics mechanics) {
		this.mechanics=mechanics;
		mechanics.attach(this);
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
		
		pause.setEnabled(false);
		add(pause,BorderLayout.SOUTH);
		
		add(timePanel, BorderLayout.NORTH);
		add(nextBrick, BorderLayout.CENTER);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		Color[][] board = new Color[4][4];
		Point active[]= {new Point(1,0),new Point(2,0),new Point(2,0),new Point(2,0)};
		Tetrimino.makeTetrimino(active,new Point(2,2),board,mechanics.randNextTetrimino());
		
		for(int i=0;i<6;i++) {
			g.setColor(new Color(99, 33, 222));
			g.fillRect(55 - brickSize, 350 + (i-1) * brickSize, brickSize - 2, brickSize - 2);
		}
		for(int i=0;i<6;i++) {
			g.setColor(new Color(99, 33, 222));
			g.fillRect(55 +4* brickSize, 350 + (i-1) * brickSize, brickSize - 2, brickSize - 2);
		}
		
		for(int i=0;i<4;i++) {
			g.setColor(new Color(99, 33, 222));
			g.fillRect(55 +i* brickSize, 350 + 4* brickSize, brickSize - 2, brickSize - 2);
		}
		for(int i=0;i<4;i++) {
			g.setColor(new Color(99, 33, 222));
			g.fillRect(55 +i* brickSize, 350 -  brickSize, brickSize - 2, brickSize - 2);
		}
		
		
				
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				g2.setColor(Color.WHITE);
				g2.drawRect(55 + i * brickSize, 350 + j * brickSize, brickSize, brickSize);
				if (board[i][j] != Color.WHITE) {
					g.setColor(board[i][j]);
					g.fillRect(55 + i * brickSize, 350 + j * brickSize, brickSize - 2, brickSize - 2);
					g.setColor(Color.WHITE);
				}

			}
		}
	}

	@Override
	public void update() {
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
