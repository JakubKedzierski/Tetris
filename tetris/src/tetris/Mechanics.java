package tetris;

import java.awt.*;
import java.util.stream.Collectors;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

enum Direction {
	LEFT, RIGHT, DOWN;
}

class GameException extends Exception {
	static final long serialVersionUID = 1L;

	public GameException(String exceptionMessage) {
		super(exceptionMessage);
	}
}

public class Mechanics implements Observable {
	final static int BOARD_MAX_ROW = 10;
	final static int BOARD_MAX_COLUMN = 20;
	private boolean stop = false;
	private Color board[][] = new Color[BOARD_MAX_ROW][BOARD_MAX_COLUMN];
	private Point[] active = { new Point(5, 0), new Point(5, 1), new Point(6, 0), new Point(6, 1) };
	private Point activePivot = null;
	private boolean sequenceCheckFlag = false;
	private Set<Observer> observers = new HashSet<>();

	public Mechanics() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = Color.WHITE;
			}
		}
	}

	public void pause() {
		stop = true;
	}

	public void unPause() {
		stop = false;
	}

	public boolean isPaused() {
		return stop;
	}

	public Color[][] getBoard() {
		return board;
	}

	public String ToString() {
		StringBuilder str = new StringBuilder();
		for (Point act : active) {
			str.append("x: " + act.x + " y: " + act.y + "\n");
		}
		return str.toString();
	}

	public String boardToString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < BOARD_MAX_COLUMN; i++) {
			str.append("\n");
			for (int j = 0; j < BOARD_MAX_ROW; j++) {
				if (board[j][i] != Color.WHITE)
					str.append(1 + " ");
				else {
					str.append(0 + " ");
				}
			}
		}
		return str.toString();
	}

	public boolean checkPossibilityOfRotation() {
		int x = 0, y = 0;
		for (Point act : active) {
			x = activePivot.x + activePivot.y - act.y;
			y = activePivot.y - activePivot.x + act.x;
			if (x > BOARD_MAX_ROW - 1)
				return false;
			if (x < 0)
				return false;
			if (y < 0)
				return false;
			if (y > BOARD_MAX_COLUMN - 1)
				return false;
		}
		return true;
	}

	public void rotate() {

		if (activePivot != null) {
			if (!checkPossibilityOfRotation())
				return;

			Color tempColor = board[active[0].x][active[0].y];
			for (Point act : active) {
				board[act.x][act.y] = Color.WHITE;
			}

			for (Point act : active) {
				Point temp = (Point) (act.clone());
				act.x = activePivot.x + activePivot.y - temp.y;
				act.y = activePivot.y - activePivot.x + temp.x;
			}

			for (Point act : active) {
				board[act.x][act.y] = tempColor;
			}
		}
	}

	public void changeActivePosition(Direction direct) {
		Color color = board[active[0].x][active[0].y];

		for (Point act : active) {
			board[act.x][act.y] = Color.WHITE;
		}

		switch (direct) {
		case DOWN:
			for (Point act : active) {
				act.y = act.y + 1;
				board[act.x][act.y] = color;
			}
			break;

		case LEFT:
			for (Point act : active) {
				act.x = act.x - 1;
				board[act.x][act.y] = color;
			}
			break;

		case RIGHT:
			for (Point act : active) {
				act.x = act.x + 1;
				board[act.x][act.y] = color;
			}
			break;
		}
	}

	public void moveToEndPosition() {
		while (move(Direction.DOWN))
			;
	}

	public boolean move(Direction direct) {
		if (!stop) {
			if (direct == Direction.DOWN) {
				if (checkMoveDown()) {
					changeActivePosition(direct);
					return true;
				} else {
					makeTetrimino();
					return false;
				}
			}

			if (checkMovePossibility(direct)) {
				changeActivePosition(direct);
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean checkMoveDown() {
		boolean skip = false;
		for (Point act : active) {
			skip = false;

			for (Point skipCheck : active) {
				if ((act.y + 1 == skipCheck.y) && (act.x == skipCheck.x))
					skip = true;
			}

			if (skip == false) {

				if (act.y + 1 > BOARD_MAX_COLUMN - 1) {
					return false;
				}

				if (board[act.x][act.y + 1] != Color.WHITE) {
					return false;
				}

			}
		}
		return true;
	}

	// do przerobienia check move down jest
	public boolean checkMovePossibility(Direction direct) {
		boolean skip = false; // temporary variable to skip checking move possibility for inner cells

		for (Point act : active) {
			skip = false;

			switch (direct) {
			case DOWN:
				checkMoveDown();
				break;

			case RIGHT:
				for (Point skipCheck : active) {
					if ((act.x + 1 == skipCheck.x) && (act.y == skipCheck.y))
						skip = true;
				}

				if (skip == false) {

					if (act.x + 1 > BOARD_MAX_ROW - 1)
						return false;
					if (board[act.x + 1][act.y] != Color.WHITE)
						return false;

				}

				break;
			case LEFT:
				for (Point skipCheck : active) {
					if ((act.x - 1 == skipCheck.x) && (act.y == skipCheck.y))
						skip = true;
				}

				if (skip == false) {

					if (act.x - 1 < 0)
						return false;
					if (board[act.x - 1][act.y] != Color.WHITE)
						return false;

				}

				break;
			}
		}
		return true;
	}

	public void moveBricksToEndPosition() {
		boolean flag = true;
		while (flag) {
			flag = false;

			for (int i = BOARD_MAX_COLUMN - 2; i > 0; i--) {
				for (int j = 0; j < BOARD_MAX_ROW; j++) {
					if (board[j][i] != Color.WHITE) {
						if (board[j][i + 1] == Color.WHITE) {
							board[j][i + 1] = board[j][i];
							board[j][i] = Color.WHITE;
							flag = true;
						}
					}
				}
			}
		}
	}

	public void deleteSequenceHorizontal(int row, int beg, int end) {
		sequenceCheckFlag = true;
		for (int i = beg; i < end; i++) {
			board[i][row] = Color.WHITE;
		}
	}

	public void deleteSequenceVertical(int column, int beg, int end) {
		sequenceCheckFlag = true;
		for (int i = beg; i < end; i++) {
			board[column][i] = Color.WHITE;
		}
	}

	public void checkSequenceRow(Entry<Color, Long> entry, final ArrayList<Color> array, int deleteNum) {
		int counter = 0;

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).equals(entry.getKey())) {
				counter++;
				if (counter > 4) {

					if (array.size() > 10)
						deleteSequenceVertical(deleteNum, i - counter + 1, i + 1);
					else
						deleteSequenceHorizontal(deleteNum, i - counter + 1, i + 1);

				}
			} else {
				counter = 0;
			}

		}
	}

	public void findFiveInARow(final ArrayList<Color> array, int deleteNum) {
		Map<Color, Long> occurrences = array.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		Set<Entry<Color, Long>> entrySet = occurrences.entrySet();

		for (Entry<Color, Long> entry : entrySet) {
			if (entry.getKey() != Color.WHITE && entry.getValue() > 4) {
				checkSequenceRow(entry, array, deleteNum);
			}
		}

	}

	public void findSequence() {
		ArrayList<Color> array = new ArrayList<Color>(Collections.nCopies(10, Color.WHITE));

		for (int i = 0; i < BOARD_MAX_COLUMN; i++) {
			for (int j = 0; j < BOARD_MAX_ROW; j++) {
				array.set(j, board[j][i]);
			}
			findFiveInARow(array, i);
		}

		array = new ArrayList<Color>(Collections.nCopies(20, Color.WHITE));

		for (int i = 0; i < BOARD_MAX_ROW; i++) {
			for (int j = 0; j < BOARD_MAX_COLUMN; j++) {
				array.set(j, board[i][j]);
			}
			findFiveInARow(array, i);
		}

	}

	private void lookForSequences() {
		do {
			sequenceCheckFlag = false;
			moveBricksToEndPosition();
			findSequence();
		} while (sequenceCheckFlag);
	}

	public boolean makeTetrimino() {
		lookForSequences();
		activePivot=Tetrimino.makeTetrimino(active, activePivot, board);
		if(!checkMoveDown()) notifyObservers();
		return false;
	}

	void playGame() {
		move(Direction.DOWN);
	}

	@Override
	public void attach(Observer observer) {
		observers.add(observer);

	}

	@Override
	public void detach(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		observers.forEach(Observer::update);

	}

}
