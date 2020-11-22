package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;



class InfoBox extends JPanel implements Observer,ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel points = new JLabel("Points:",SwingConstants.CENTER);
	private JLabel nextBrick = new JLabel("   next brick will be:");
	private JLabel usernameLab = new JLabel("Username",SwingConstants.CENTER);
	private JLabel username = new JLabel("Anonymous",SwingConstants.CENTER);
	private JButton pause = new JButton("     Pause     ");
	private JButton controls = new JButton("    Controls    ");
	private Color panelColor=new Color(237, 244, 248);
	private static int brickSize = 20;
	private Mechanics mechanics;
	

	InfoBox(Tetris game) {
		this.mechanics=game.getMechanics();
		mechanics.attach(this);
		if(game.getUsername() != null && !game.getUsername().equals("")) {
			username.setText(game.getUsername());
		}
		setBackground(panelColor);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200, 300));
		JLabel labels[] = { points, nextBrick, usernameLab };
		for (JLabel label : labels) {
			label.setFont(new Font("Verdana", Font.PLAIN, 18));
		}
		username.setFont(new Font("Yu Gothic UI Semibold",Font.PLAIN, 18));
		username.setForeground(new Color(0, 0, 205));
		
		points.setText("Points: " + mechanics.getPoints());

		controls.addActionListener(this);
		JPanel generalPanel = new JPanel();
		GridLayout layout = new GridLayout(3,1);
		layout.setVgap(10);
		generalPanel.setLayout(layout);
		generalPanel.add(points);
		generalPanel.add(usernameLab);
		generalPanel.add(username);
		generalPanel.setBackground(panelColor);
		pause.addActionListener(this);
		setFocusable(true);
		pause.setBackground(SystemColor.activeCaption);
		add(generalPanel, BorderLayout.NORTH);
		add(nextBrick, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(pause);
		GridLayout layout2 = new GridLayout(2,1);
		buttonPanel.setLayout(layout2);
		controls.setBackground(SystemColor.activeCaption);
		buttonPanel.add(controls);
		buttonPanel.setBackground(panelColor);
		add(buttonPanel,BorderLayout.SOUTH);
		
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
		points.setText("Points: " + mechanics.getPoints());

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
		nextBricks.makeTetrimino(board,mechanics.randNextTetrimino());
		
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
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == pause) {
			if (mechanics.isStop())
				mechanics.setStop(false);
			else
				mechanics.setStop(true);
		}
		if(e.getSource() == controls) {
			mechanics.setStop(true);
			JOptionPane.showMessageDialog(this, WelcomeWindow.manualMessage,"Controls",JOptionPane.CANCEL_OPTION );
			mechanics.setStop(false);
		}
	}

}

