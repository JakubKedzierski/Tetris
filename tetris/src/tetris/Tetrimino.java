package tetris;

import java.awt.Point;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

enum ColorType{
	color1,color2,color3,color4,color5,color6,color7,empty;
}

@AllArgsConstructor(access=AccessLevel.PUBLIC)
@ToString
public class Tetrimino {
	
	@Getter
	@Setter
	private Point activeBrickskPivot = null;
	
	@Getter
	@Setter
	Point[] activeBricks = { new Point(5, 0), new Point(5, 1), new Point(6, 0), new Point(6, 1) };
	
	@Getter	
	@Setter
	ColorType brickColor = ColorType.empty;
	
	public Tetrimino() {}

	public void makeTetrimino(ColorType board[][],int tetriminoChoice[]) {

		switch (tetriminoChoice[0]) {

		// TETRIMINO "I"
		case 0:
			activeBricks[0].x = activeBricks[0].x;		activeBricks[0].y = 0;				
			activeBricks[1].x = activeBricks[0].x;		activeBricks[1].y = 1;	
			activeBricks[2].x = activeBricks[0].x;		activeBricks[2].y = 2;	
			activeBricks[3].x = activeBricks[0].x;		activeBricks[3].y = 3;
			
			activeBrickskPivot = activeBricks[1];
			break;

		// TETRIMINO "T"
		case 1:
			if (activeBricks[0].x + 2 > board.length - 1) {
				activeBricks[0].x -= 2;
			}
			activeBricks[0].x = activeBricks[0].x;		activeBricks[0].y = 0;
			activeBricks[1].x = activeBricks[0].x + 1;  activeBricks[1].y = 1;				
			activeBricks[2].x = activeBricks[0].x + 2;	activeBricks[2].y = 0;
			activeBricks[3].x = activeBricks[0].x + 1;	activeBricks[3].y = 0;
			
			activeBrickskPivot = activeBricks[1];
			break;

		// TETRIMINO "O"
		case 2:
			if (activeBricks[0].x + 1 > board.length - 1) {
				activeBricks[0].x -= 1;
			}
			activeBricks[0].x=activeBricks[0].x;		activeBricks[0].y = 0;
			activeBricks[1].x = activeBricks[0].x;		activeBricks[1].y = 1;
			activeBricks[2].x = activeBricks[0].x + 1;	activeBricks[2].y = 0;
			activeBricks[3].x = activeBricks[0].x + 1;	activeBricks[3].y = 1;
			
			activeBrickskPivot = null;
			break;

		// TETRIMINO "L"
		case 3:
			if (activeBricks[0].x + 1 > board.length - 1) {
				activeBricks[0].x -= 1;
			}
			activeBricks[0].y = 0;
			activeBricks[1].x = activeBricks[0].x;
			activeBricks[1].y = 1;
			activeBricks[2].x = activeBricks[0].x;
			activeBricks[2].y = 2;
			activeBricks[3].x = activeBricks[0].x + 1;
			activeBricks[3].y = 2;
			activeBrickskPivot = activeBricks[1];
			break;

		// TETRIMINO "J"
		case 4:
			if (activeBricks[0].x + 1 > board.length - 1) {
				activeBricks[0].x -= 1;
			}
			activeBricks[0].y = 2;
			activeBricks[1].x = activeBricks[0].x + 1;
			activeBricks[1].y = 0;
			activeBricks[2].x = activeBricks[0].x + 1;
			activeBricks[2].y = 1;
			activeBricks[3].x = activeBricks[0].x + 1;
			activeBricks[3].y = 2;
			activeBrickskPivot = activeBricks[1];
			break;

		// TETRIMINO "S"
		case 5:
			if (activeBricks[0].x + 2 > board.length - 1) {
				activeBricks[0].x -= 2;
			}
			activeBricks[0].x = activeBricks[0].x;
			activeBricks[0].y = 1;
			activeBricks[1].x = activeBricks[0].x + 1;
			activeBricks[1].y = 1;
			activeBricks[2].x = activeBricks[0].x + 1;
			activeBricks[2].y = 0;
			activeBricks[3].x = activeBricks[0].x + 2;
			activeBricks[3].y = 0;
			activeBrickskPivot = activeBricks[2];
			break;

		// TETRIMINO "Z"
		case 6:
			if (activeBricks[0].x + 2 > board.length - 1) {
				activeBricks[0].x -= 2;
			}
			activeBricks[0].y = 0;
			activeBricks[1].x = activeBricks[0].x + 1;
			activeBricks[1].y = 0;
			activeBricks[2].x = activeBricks[0].x + 1;
			activeBricks[2].y = 1;
			activeBricks[3].x = activeBricks[0].x + 2;
			activeBricks[3].y = 1;
			activeBrickskPivot = activeBricks[1];
			break;
		}

		

		for (Point act : activeBricks) {
			switch (tetriminoChoice[1]) {
			case 0:
				board[act.x][act.y] = ColorType.color1;
				break;
			case 1:
				board[act.x][act.y] = ColorType.color2;
				break;
			case 2:
				board[act.x][act.y] = ColorType.color3;
				break;
			case 3:
				board[act.x][act.y] = ColorType.color4;
				break;
			case 4:
				board[act.x][act.y] = ColorType.color5;
				break;
			case 5:
				board[act.x][act.y] = ColorType.color6;
				break;
			case 6:
				board[act.x][act.y] = ColorType.color7;
				break;
			}
		}

		brickColor=board[activeBricks[0].x][activeBricks[0].y];
	}
	
	public boolean checkPossibilityOfRotation() {
		int x = 0, y = 0;
		for (Point act : activeBricks) {
			x = activeBrickskPivot.x + activeBrickskPivot.y - act.y;
			y = activeBrickskPivot.y - activeBrickskPivot.x + act.x;
			if (x > Mechanics.BOARD_MAX_ROW - 1)
				return false;
			if (x < 0)
				return false;
			if (y < 0)
				return false;
			if (y > Mechanics.BOARD_MAX_COLUMN - 1)
				return false;
		}
		return true;
	}
	
	public void rotate(ColorType board[][]) {

		if (activeBrickskPivot != null) {
			if (!checkPossibilityOfRotation())
				return;

			ColorType tempColor = board[activeBricks[0].x][activeBricks[0].y];
			for (Point act : activeBricks) {
				board[act.x][act.y] = ColorType.empty;
			}

			for (Point act : activeBricks) {
				Point temp = (Point) (act.clone());
				act.x = activeBrickskPivot.x + activeBrickskPivot.y - temp.y;
				act.y = activeBrickskPivot.y - activeBrickskPivot.x + temp.x;
			}

			for (Point act : activeBricks) {
				board[act.x][act.y] = tempColor;
			}
		}
	}
	
	public void move(Direction direct) {

		switch (direct) {
		case DOWN:
			for (Point act : activeBricks) {
				act.y = act.y + 1;
			}
			break;

		case LEFT:
			for (Point act : activeBricks) {
				act.x = act.x - 1;
			}
			break;

		case RIGHT:
			for (Point act : activeBricks) {
				act.x = act.x + 1;
			}
			break;
		}
	}
	

}
