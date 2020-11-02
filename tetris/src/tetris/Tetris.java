package tetris;

import java.util.Timer;
import java.util.TimerTask;

public class Tetris {
	private Mechanics mechanics;
	private TetrisGui gui;
	private Timer timer;
	private final int INITIAL_DELAY = 1300;
	private final int PERIOD_INTERVAL = 400;

	public void mainLoop() {
		while (true) {
			
		}

	}

	public Tetris() {
		mechanics = new Mechanics();
		gui = new TetrisGui(mechanics); // calego tetrisa przesylac i np okno
		mechanics.makeTetrimino();

		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), INITIAL_DELAY, PERIOD_INTERVAL);
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.mainLoop();

	}

	private class ScheduleTask extends TimerTask {

		@Override
		public void run() {
			if(!mechanics.playGame()) {
				timer.cancel();
				gui.endGame();
				timer.purge();
			}
			gui.update();
		}
	}

}
