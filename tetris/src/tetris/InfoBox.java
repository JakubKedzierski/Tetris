package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;



class InfoBox extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private JLabel time = new JLabel("Time:");
	private JLabel points = new JLabel("Points:");
	private JLabel nextBrick = new JLabel("   next brick will be:");
	private JLabel pressButton = new JLabel("   Press SPACE to stop for a while");
	private Color panelColor=new Color(237, 244, 248);
	private static int brickSize = 20;
	private Mechanics mechanics;
	private Timer timer;
	private int timeCount=0;
	private final int PERIOD_INTERVAL = 1000;

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
		
		add(timePanel, BorderLayout.NORTH);
		add(nextBrick, BorderLayout.CENTER);
		add(pressButton, BorderLayout.SOUTH);
		
		timer=new Timer();
		timer.scheduleAtFixedRate(new SecCounter(),0,PERIOD_INTERVAL);
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
		points.setText("          Points: " + mechanics.getPoints());
		

		g.setColor(Color.BLACK);
		ColorType[][] board = new ColorType[4][4];
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				board[i][j]=ColorType.empty;
			}
		}
		
		Point active[]= {new Point(3,0),new Point(2,0),new Point(2,0),new Point(2,0)};
		Tetrimino nextBricks = new Tetrimino();
		nextBricks.setActiveBricks(active);
		nextBricks.makeTetrimino(board,mechanics.getNextBrickChoice());
		
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
				if (board[i][j] != ColorType.empty) {
					g.setColor(convertColor(board[i][j]));
					g.fillRect(55 + i * brickSize, 350 + j * brickSize, brickSize - 2, brickSize - 2);
					g.setColor(Color.WHITE);
				}

			}
		}
	}

	
	@Override
	public void update() {
		if(!mechanics.isEndGame())
			repaint();
		else {
			timer.cancel();
			timer.purge();
		}
	}


	
	private class SecCounter extends TimerTask {

		@Override
		public void run() {
			if(!mechanics.isEndGame()) {
				timeCount++;
				time.setText("          Time: " + timeCount);
				repaint();
			}else {
				timer.cancel();
				timer.purge();
			}
		}
	}
}
