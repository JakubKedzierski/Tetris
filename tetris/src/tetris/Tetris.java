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
	private TetrisGui gui;
	private Timer timer;
	private final int INITIAL_DELAY = 600;
	private final int PERIOD_INTERVAL = 400;
	private boolean blockSideMoves=false;


	public Tetris() {
		mechanics = new Mechanics();
		gui = new TetrisGui(this); // calego tetrisa przesylac i np okno
		mechanics.makeTetrimino();
		mechanics.attach(this);

		timer = new Timer();
		timer.scheduleAtFixedRate(new mainLoop(), INITIAL_DELAY, PERIOD_INTERVAL);
	}

	public static void main(String[] args) {
		new Tetris();
	}
	
	public void moveTetriminoToEnd() {
		timer.cancel();
		timer = new Timer();
		timer.scheduleAtFixedRate(new mainLoop(), 0, PERIOD_INTERVAL/8);
		blockSideMoves=true;
	}
	
	public void moveSide(Direction direction) {
		if(!blockSideMoves) mechanics.move(direction);
	}

	private class mainLoop extends TimerTask {

		@Override
		public void run() {
			if(mechanics.playGame())
				gui.update();
			else {
				timer.cancel();
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
			gui.endGame();
			timer.purge();
		}
		
		gui.update();
	}

}
