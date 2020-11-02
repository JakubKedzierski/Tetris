package tetris;

import java.awt.Color;
import java.awt.Point;

public class Tetrimino {

	public static boolean makeTetrimino(Point[] active,Point activePivot,Color board[][]) {
		int randomNumb = (int) (Math.random() * 7);
		
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
			if (active[0].x + 2 > board.length) {
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
			if (active[0].x + 1 >  board.length) {
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
			if (active[0].x + 1 > board.length) {
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
			if (active[0].x + 1 >  board.length) {
				active[0].x -= 1;
			}
			active[0].y = 2;
			active[1].x = active[0].x+1;
			active[1].y = 0;
			active[2].x = active[0].x+1;
			active[2].y = 1;
			active[3].x = active[0].x+ 1;
			active[3].y = 2;
			activePivot = active[1];
			break;

		// TETRIMINO "S"
		case 5:
			if (active[0].x + 2 >  board.length) {
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
			if (active[0].x + 2 >  board.length) {
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
