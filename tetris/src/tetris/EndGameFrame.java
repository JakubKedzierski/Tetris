package tetris;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class EndGameFrame extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private Font font=new Font("Calibri", Font.PLAIN, 14);
	JLabel endMessage=new JLabel("<html>Game Over <br/> <br/> Now you can play another game or say Goodbye.</html>",SwingConstants.CENTER);
	JButton endButton=new JButton("Goodbye");
	JButton newGameButton=new JButton("New game");
	
	
	EndGameFrame(Window parent){
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		setTitle("Tetris");
		setSize(300, 160);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel endPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		endMessage.setFont(font);
		endPanel.add(endMessage);
		
		endButton.addActionListener(this);
		newGameButton.addActionListener(this);
		endPanel.add(endButton);
		endPanel.add(newGameButton);
		setContentPane(endPanel);
		
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object button=e.getSource();
		
		if(button==endButton) {
			System.exit(0);
		}
		if(button==newGameButton) {
			dispose();
			new Tetris();
		}
		
	}

}
