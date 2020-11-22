package tetris;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Getter;

interface Observer {
	public void update();
}

interface Observable {
	public void attach(Observer observer);

	public void detach(Observer observer);

	public void notifyObservers();
}

public class Tetris implements Observer {
	
	@Getter
	private Mechanics mechanics;
	@Getter
	private TetrisGui gui;
	private Timer timer;
	private final int INITIAL_DELAY = 600;
	private final int PERIOD_INTERVAL = 400;
	private boolean blockSideMoves=false;
	
	@Getter
	private String username;


	public Tetris(String username) {
		this.username=username;
		mechanics = new Mechanics();
		timer = new Timer();
		timer.scheduleAtFixedRate(new mainLoop(), INITIAL_DELAY, PERIOD_INTERVAL);
		
		gui = new TetrisGui(this); // calego tetrisa przesylac i np okno
		mechanics.makeTetrimino();
		mechanics.attach(this);
	}
	
	
	public void write() {
		try {
			Mechanics.writeObject("save.bin", mechanics);
		} catch (GameException e) {
			gui.sayMessage(e);
		}
	}
	
	public void read() {
		try {
			mechanics=Mechanics.readObject("save.bin");
			mechanics.attach(this);
			gui.dispose();
			gui=new TetrisGui(this);
		} catch (GameException e) {
			gui.sayMessage(e);
		} catch (ClassNotFoundException e) {
			gui.sayMessage(e);
		}
	}

	public static void main(String[] args) {
		new WelcomeWindow();
	}
	
	public void moveTetriminoToEnd() {
		timer.cancel();
		timer.purge();
		timer = new Timer();
		timer.scheduleAtFixedRate(new mainLoop(), 0, PERIOD_INTERVAL/8);
		blockSideMoves=true;
	}
	
	public void moveSide(Direction direction) {
		if(!blockSideMoves) mechanics.move(direction);
	}
	
	public void rotate(boolean rotationDirection) {
		if(!blockSideMoves) mechanics.rotate(rotationDirection);
	}

	private class mainLoop extends TimerTask {

		@Override
		public void run() {
			if(mechanics.playGame())
				gui.update();
			else if (!mechanics.isEndGame())  {
				timer.cancel();
				timer.purge();
				timer = new Timer();
				timer.scheduleAtFixedRate(new mainLoop(), 0, PERIOD_INTERVAL);
				blockSideMoves=false;
			}
		}
	}

	@Override
	public void update() {
		if (mechanics.isEndGame()) {
			timer.cancel();
			timer.purge();
			gui.endGame();
			return;
		}
		
		gui.update();
	}

}
