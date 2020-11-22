package tetris;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSpinner;
import java.awt.Color;

public class WelcomeWindow extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JTextField usernameField;
	JButton readSavedGameBtn = new JButton("Read saved game");
	JButton startNewGameBtn = new JButton("New game");
	JButton manualBtn = new JButton("Controls");
	static final String manualMessage = "left key - move left \n" +
	"right key - move right\n" + "down key - accelerate tetrimino move and move to end \n" +
	"d - rotate your active Tetrimino \n" + "space - pause your game";
	
	WelcomeWindow(){
		setForeground(SystemColor.textHighlight);
		setResizable(false);
		setBackground(SystemColor.desktop);
		getContentPane().setBackground(SystemColor.control);
		setSize(391,313);
		setTitle("Tetris");
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		

		startNewGameBtn.setBackground(SystemColor.activeCaption);
		startNewGameBtn.setBounds(114, 137, 146, 34);
		startNewGameBtn.addActionListener(this);
		getContentPane().add(startNewGameBtn);
		

		readSavedGameBtn.setBackground(SystemColor.activeCaption);
		readSavedGameBtn.setBounds(114, 182, 146, 34);
		readSavedGameBtn.addActionListener(this);
		getContentPane().add(readSavedGameBtn);
		
		JLabel lblNewLabel = new JLabel("Welcome to Tetris Game !");
		lblNewLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblNewLabel.setBounds(96, 11, 182, 34);
		getContentPane().add(lblNewLabel);
		
		JLabel usrLabel = new JLabel("Your username:");
		usrLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		usrLabel.setBounds(71, 83, 100, 34);
		getContentPane().add(usrLabel);
		
		usernameField = new JTextField();
		usernameField.setForeground(new Color(0, 0, 205));
		usernameField.setBackground(SystemColor.inactiveCaptionBorder);
		usernameField.setBounds(167, 91, 111, 20);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);
		manualBtn.addActionListener(this);

		manualBtn.setBackground(SystemColor.activeCaption);
		manualBtn.setBounds(114, 227, 146, 29);
		getContentPane().add(manualBtn);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object source=e.getSource();
		if(source == startNewGameBtn) {
			String username=usernameField.getText();
			new Tetris(username);
			dispose();
		}
		if(source == readSavedGameBtn) {
			Tetris game=new Tetris(null);
			game.read();
			dispose();
		}
		if(source == manualBtn) {
			JOptionPane.showMessageDialog(this, manualMessage,"Controls",JOptionPane.CANCEL_OPTION );
		}
		
	}
	
}
