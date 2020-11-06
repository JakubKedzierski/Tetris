package tetris;

import java.util.Timer;
import java.util.TimerTask;

interface Observer {
	public void update();
}

interface Observable {
	public void attach(Observer observer);

	public void detach(Observer observer);

	public void notifyObservers();
}

public class Tetris implements Observer {
	private Mechanics mechanics;
	private TetrisGui gui;
	private Timer timer;
	private final int INITIAL_DELAY = 1300;
	private final int PERIOD_INTERVAL = 400;


	public Tetris() {
		mechanics = new Mechanics();
		gui = new TetrisGui(mechanics); // calego tetrisa przesylac i np okno
		mechanics.makeTetrimino();
		mechanics.attach(this);

		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), INITIAL_DELAY, PERIOD_INTERVAL);
	}

	public static void main(String[] args) {
		new Tetris();
	}

	private class ScheduleTask extends TimerTask {

		@Override
		public void run() {
			mechanics.playGame();
			gui.update();
		}
	}

	@Override
	public void update() {
		if (mechanics.getEndGame()) {
			timer.cancel();
			gui.endGame();
			timer.purge();
		}

	}

}
