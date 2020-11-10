package tetris;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


enum Direction {
	LEFT, RIGHT, DOWN;
}

class GameException extends Exception {
	static final long serialVersionUID = 1L;

	public GameException(String exceptionMessage) {
		super(exceptionMessage);
	}
}

@AllArgsConstructor(access=AccessLevel.PUBLIC)
public class Mechanics implements Observable,Serializable {
	private static final long serialVersionUID = 1L;
	
	final static int BOARD_MAX_ROW = 10;
	final static int BOARD_MAX_COLUMN = 20;
	
	@Getter
	@Setter
	private boolean stop = false;
	
	@Getter
	@Setter
	private ColorType board[][] = new ColorType[BOARD_MAX_ROW][BOARD_MAX_COLUMN];

	@Getter
	@Setter
	private Tetrimino activeTetrimino = null;
	
	private boolean sequenceCheckFlag = false;
	
	@Getter
	@Setter
	private boolean endGame=false;
	
	@Getter
	private int points=0; 
	
	transient
	public Set<Observer> observers = new HashSet<>();
	
	@Getter
	private int nextBrickChoice[] = { (int) (Math.random() * 7),(int) (Math.random() * 7) };
	
	public static void writeObject(String file_name, Mechanics mechancsToSave) throws GameException {
		try(ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file_name))){
			outputStream.writeObject(mechancsToSave);
		}
		catch (FileNotFoundException e) {
			throw new GameException("File not found");
		} catch (IOException e) {
			throw new GameException("IO Exception");
		}
	}
	
	public static Mechanics readObject(String file_name) throws GameException, ClassNotFoundException {
		Mechanics mechancsToRead= new Mechanics();
		try(ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream(file_name))) {
			mechancsToRead=(Mechanics) inputStream.readObject();
		} catch (FileNotFoundException e) {
			throw new GameException("File not found");
		} catch (IOException e) {
			throw new GameException("Some troubles with serialization");
		}
		return mechancsToRead;
	}

	public Mechanics() {
		randNextTetrimino() ;
		stop=false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = ColorType.empty;
			}
		}
	}
	

	public String ToString() {
		StringBuilder str = new StringBuilder();
		for (Point act : activeTetrimino.getActiveBricks()) {
			str.append("x: " + act.x + " y: " + act.y + "\n");
		}
		return str.toString();
	}

	public String boardToString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < BOARD_MAX_COLUMN; i++) {
			str.append("\n");
			for (int j = 0; j < BOARD_MAX_ROW; j++) {
				if (board[j][i] != ColorType.empty)
					str.append(1 + " ");
				else {
					str.append(0 + " ");
				}
			}
		}
		return str.toString();
	}
	
	public void rotate(boolean rotationDirection) {
		if(!stop)
		activeTetrimino.rotate(board,rotationDirection);
	}



	public void changeActivePosition(Direction direct) {
		ColorType color = activeTetrimino.getBrickColor();

		for (Point act : activeTetrimino.getActiveBricks()) {
			board[act.x][act.y] = ColorType.empty;
		}

		activeTetrimino.move(direct);
		
		/* Changing position on the board - tetrimino itself already moved */
		
		switch (direct) {
		case DOWN:
			for (Point act : activeTetrimino.getActiveBricks()) {
				board[act.x][act.y] = color;
			}
			break;

		case LEFT:
			for (Point act : activeTetrimino.getActiveBricks()) {
				board[act.x][act.y] = color;
			}
			break;

		case RIGHT:
			for (Point act : activeTetrimino.getActiveBricks()) {
				board[act.x][act.y] = color;
			}
			break;
		}
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
		for (Point act : activeTetrimino.getActiveBricks()) {
			skip = false;

			for (Point skipCheck : activeTetrimino.getActiveBricks()) {
				if ((act.y + 1 == skipCheck.y) && (act.x == skipCheck.x))
					skip = true;
			}

			if (skip == false) {

				if (act.y + 1 > BOARD_MAX_COLUMN - 1) {
					return false;
				}

				if (board[act.x][act.y + 1] != ColorType.empty) {
					return false;
				}

			}
		}
		return true;
	}


	public boolean checkMovePossibility(Direction direct) {
		boolean skip = false; // temporary variable to skip checking move possibility for inner cells

		for (Point act : activeTetrimino.getActiveBricks()) {
			skip = false;

			switch (direct) {
			case DOWN:
				checkMoveDown();
				break;

			case RIGHT:
				for (Point skipCheck : activeTetrimino.getActiveBricks()) {
					if ((act.x + 1 == skipCheck.x) && (act.y == skipCheck.y))
						skip = true;
				}

				if (skip == false) {

					if (act.x + 1 > BOARD_MAX_ROW - 1)
						return false;
					if (board[act.x + 1][act.y] != ColorType.empty)
						return false;

				}

				break;
			case LEFT:
				for (Point skipCheck : activeTetrimino.getActiveBricks()) {
					if ((act.x - 1 == skipCheck.x) && (act.y == skipCheck.y))
						skip = true;
				}

				if (skip == false) {

					if (act.x - 1 < 0)
						return false;
					if (board[act.x - 1][act.y] != ColorType.empty)
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
					if (board[j][i] != ColorType.empty) {
						if (board[j][i + 1] == ColorType.empty) {
							board[j][i + 1] = board[j][i];
							board[j][i] = ColorType.empty;
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
			board[i][row] = ColorType.empty;
		}
	}

	public void deleteSequenceVertical(int column, int beg, int end) {
		sequenceCheckFlag = true;
		for (int i = beg; i < end; i++) {
			board[column][i] = ColorType.empty;
		}
	}

	public void checkSequenceRow(Entry<ColorType, Long> entry, final ArrayList<ColorType> array, int deleteNum) {
		int counter = 0;

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).equals(entry.getKey())) {
				counter++;
				if (counter > 4) {

					if (counter == 5) {
						points+=5;    //////////////////////////// not working
					}
					
					if (counter > 5) {
						points+=1;    //////////////////////////// not working
					}
					
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

	public void findFiveInARow(final ArrayList<ColorType> array, int deleteNum) {
		Map<ColorType, Long> occurrences = array.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		Set<Entry<ColorType, Long>> entrySet = occurrences.entrySet();

		for (Entry<ColorType, Long> entry : entrySet) {
			if (entry.getKey() != ColorType.empty && entry.getValue() > 4) {
				checkSequenceRow(entry, array, deleteNum);
			}
		}

	}

	public void findSequence() {
		ArrayList<ColorType> array = new ArrayList<ColorType>(Collections.nCopies(BOARD_MAX_ROW, ColorType.empty));

		for (int i = 0; i < BOARD_MAX_COLUMN; i++) {
			for (int j = 0; j < BOARD_MAX_ROW; j++) {
				array.set(j, board[j][i]);
			}
			findFiveInARow(array, i);
		}

		array = new ArrayList<ColorType>(Collections.nCopies(BOARD_MAX_COLUMN, ColorType.empty));

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

	public int[] randNextTetrimino() {
		nextBrickChoice[0] =  (int) (Math.random() * 7);
		nextBrickChoice[1] =  (int) (Math.random() * 7);
		return nextBrickChoice;
	}
	
	public boolean makeTetrimino() {
		lookForSequences();
		activeTetrimino=new Tetrimino();
		activeTetrimino.makeTetrimino(board,nextBrickChoice);
		randNextTetrimino() ;
		notifyObservers();
		
		if(!checkMoveDown()) {
			setEndGame(true);
		}
		return false;
	}

	boolean playGame() {
		return move(Direction.DOWN);
	}

	@Override
	public void attach(Observer observer) {
		if(observers!=null && observer != null)
			observers.add(observer);
		else if(observer != null) {
			observers=new HashSet<>();
			observers.add(observer);
		}
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
