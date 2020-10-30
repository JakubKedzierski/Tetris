package tetris;

import java.awt.*;
import java.util.stream.Collectors;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Collections;
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

public class Mechanics {
	final private static int BOARD_MAX_ROW = 10;
	final private static int BOARD_MAX_COLUMN = 20;
	private boolean stop = false;
	private Color board[][] = new Color[BOARD_MAX_ROW][BOARD_MAX_COLUMN];
	private Point[] active = { new Point(5, 0), new Point(5, 1), new Point(6, 0), new Point(6, 1) };
	private Point activePivot = null;

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

	public void rotate() throws ArrayIndexOutOfBoundsException {

		if (activePivot != null) {
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

	public boolean checkMovePossibility(Direction direct) {
		boolean skip = false; // temporary variable to skip checking move possibility for inner cells

		for (Point act : active) {
			skip = false;

			switch (direct) {
			case DOWN:

				for (Point skipCheck : active) {
					if ((act.y + 1 == skipCheck.y) && (act.x == skipCheck.x))
						skip = true;
				}

				if (skip == false) {

					if (act.y + 1 > BOARD_MAX_COLUMN - 1)
						return makeTetrimino();
					if (board[act.x][act.y + 1] != Color.WHITE)
						return makeTetrimino();

				}

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

	public void deleteSequenceHorizontal(int row, int beg, int end) {
		for (int i = beg; i < end; i++) {
			board[i][row] = Color.WHITE;
		}
	}

	public void deleteSequenceVertical(int column, int beg, int end) {
		for (int i = beg; i < end; i++) {
			board[column][i] = Color.WHITE;
		}
	}

	/*
	 * public static void main(String[] args) { Mechanics m = new Mechanics();
	 * m.makeTetrimino(); m.getBoard()[0][6] = Color.BLUE; m.getBoard()[1][6] =
	 * Color.BLUE; m.getBoard()[2][6] = Color.BLUE; m.getBoard()[3][6] = Color.BLUE;
	 * m.getBoard()[4][6] = Color.BLUE; m.getBoard()[5][6] = Color.BLUE;
	 * m.getBoard()[6][6] = Color.BLUE;
	 * 
	 * m.getBoard()[0][1] = Color.BLUE; m.getBoard()[0][2] = Color.BLUE;
	 * m.getBoard()[0][3] = Color.BLUE; m.getBoard()[0][4] = Color.BLUE;
	 * m.getBoard()[0][5] = Color.BLUE;
	 * 
	 * m.move(Direction.DOWN); m.move(Direction.DOWN);
	 * 
	 * System.out.println(m.boardToString()); m.findSequence(); m.rotate();
	 * System.out.println(m.boardToString()); }
	 */

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
			if (checkMovePossibility(direct)) {
				changeActivePosition(direct);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Creating Tetrimino and giving them color
	 * 
	 * @return
	 */
	public boolean makeTetrimino() {
		int randomNumb = (int) (Math.random() * 7);

		findSequence();
		moveBricksToEndPosition();

		switch (randomNumb) {

		// TETRIMINO "I"
		case 0:
			active[0].y = 0;
			active[1].y = 1;
			active[1].x = active[0].x;
			active[2].y = 2;
			active[2].x = active[0].x;
			active[3].y = 3;
			active[3].x = active[0].x;
			activePivot = active[1];
			break;

		// TETRIMINO "T"
		case 1:
			if (active[0].x + 2 > BOARD_MAX_ROW - 1) {
				active[0].x -= 2;
			}
			active[0].y = 0;
			active[1].y = 1;
			active[1].x = active[0].x + 1;
			active[2].y = 0;
			active[2].x = active[0].x + 2;
			active[3].y = 0;
			active[3].x = active[0].x + 1;
			activePivot = active[1];
			break;

		// TETRIMINO "O"
		case 2:
			if (active[0].x + 1 > BOARD_MAX_ROW - 1) {
				active[0].x -= 1;
			}
			active[0].y = 0;
			active[1].x = active[0].x;
			active[1].y = 1;
			active[2].x = active[0].x + 1;
			active[2].y = 0;
			active[3].x = active[0].x + 1;
			active[3].y = 1;
			activePivot = null;
			break;

		// TETRIMINO "L"
		case 3:
			if (active[0].x + 1 > BOARD_MAX_ROW - 1) {
				active[0].x -= 1;
			}
			active[0].y = 0;
			active[1].x = active[0].x;
			active[1].y = 1;
			active[2].x = active[0].x;
			active[2].y = 2;
			active[3].x = active[0].x + 1;
			active[3].y = 2;
			activePivot = active[1];
			break;

		// TETRIMINO "J"
		case 4:
			if (active[0].x - 1 < 0) {
				active[0].x += 1;
			}
			active[0].x = active[0].x + 1;
			active[0].y = 0;
			active[1].x = active[0].x;
			active[1].y = 1;
			active[2].x = active[0].x;
			active[2].y = 2;
			active[3].x = active[0].x - 1;
			active[3].y = 2;
			activePivot = active[1];
			break;

		// TETRIMINO "S"
		case 5:
			if (active[0].x + 2 > BOARD_MAX_ROW - 1) {
				active[0].x -= 2;
			}
			active[0].x = active[0].x;
			active[0].y = 1;
			active[1].x = active[0].x + 1;
			active[1].y = 1;
			active[2].x = active[0].x + 1;
			active[2].y = 0;
			active[3].x = active[0].x + 2;
			active[3].y = 0;
			activePivot = active[2];
			break;

		// TETRIMINO "Z"
		case 6:
			if (active[0].x + 2 > BOARD_MAX_ROW - 1) {
				active[0].x -= 2;
			}
			active[0].y = 0;
			active[1].x = active[0].x + 1;
			active[1].y = 0;
			active[2].x = active[0].x + 1;
			active[2].y = 1;
			active[3].x = active[0].x + 2;
			active[3].y = 1;
			activePivot = active[1];
			break;
		}

		randomNumb = (int) (Math.random() * 7);
		for (Point act : active) {
			switch (randomNumb) {
			case 0:
				board[act.x][act.y] = Color.BLUE;
				break;
			case 1:
				board[act.x][act.y] = Color.PINK;
				break;
			case 2:
				board[act.x][act.y] = Color.YELLOW;
				break;
			case 3:
				board[act.x][act.y] = Color.CYAN;
				break;
			case 4:
				board[act.x][act.y] = Color.GRAY;
				break;
			case 5:
				board[act.x][act.y] = Color.RED;
				break;
			case 6:
				board[act.x][act.y] = Color.GREEN;
				break;
			}
		}
		return false;
	}

}
