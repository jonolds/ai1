import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;


class StateComparator implements Comparator<State> {
	public int compare(State a, State b) {
		for(var i = 0; i < 22; i++) 
			if(a.state[i] < b.state[i]) return -1;
			else if(a.state[i] > b.state[i]) return 1;
		return 0;
	}
}

class Piece {
	Color color;
	int id;
	Vector<Point> pts = new Vector<Point>();
	Piece(int id, int r, int g, int b, Vector<Point> pts) {
		this.id = id;
		this.color = new Color(r, g, b);
		this.pts = pts;
	}
}

class State {
	State prev;
	byte[] state;
	State(State _prev) {
		prev = _prev;
		state = new byte[22];
	}
	
	static String stateToString(byte[] b) {
		var sb = new StringBuilder();
		sb.append(Byte.toString(b[0]));
		for(var i = 1; i < b.length; i++) {
			sb.append(",");
			sb.append(Byte.toString(b[i]));
		}
		return sb.toString();
	}
}

class Board {
	Point size, destOffset;
	int numPieces;
	boolean[][] frame;
	Vector<Point> blacks = new Vector<Point>();
	Vector<Piece> pieces = new Vector<Piece>();

	public boolean[][] getFrame() { 
		var tmp = new boolean[frame.length][frame[0].length];
		for (var i = 0; i < size.x; i++)
		    tmp[i] = frame[i].clone();
		return tmp;
	}
}

public class Game extends GameHelp {

	boolean isBoardValid() {
		var frame = boardInit.getFrame();
		var frame2 = copy2d(frame);
		
		print(frame);
		print(frame2);
		frame[0][0] = false;
		print(frame);
		print(frame2);

		for(Piece p : boardInit.pieces)
			for(Point pt: p.pts) {
				if(frame[pt.x][pt.y]) {
					System.out.println("INVALID:  (" + pt.x + "," + pt.y + ")");
					return false;
				}
				frame[pt.x][pt.y] = true;
			}
		//display(boardInit.blacks);
		
		return true;
	}
	
	//==============MAIN BEGIN===============
	TreeSet<State> state = new TreeSet<State>(new StateComparator());
	GameWin win;
	Board boardInit;
	int numPieces, boardNum = 2;
	Point destPos, boardSize;
	
	public Game() throws IOException {
		boardInit = loadBoard(boardNum);
		isBoardValid();
		run();
	//	win = new GameWin(this);
	}
	public static void main(String[] args) throws Exception { new Game(); }
	
	public void run() {
		//var state = new State(null);
//		for(var s: state.state)
//			System.out.print(s + " ");
	}
	//==============MAIN END=================
	
	void display(Vector<Point> pos) {
		char[][] display = new char[boardSize.x][boardSize.y];
		boolean[][] frame = boardInit.getFrame();
		for (var i = 0; i < boardSize.x; i++)
			for(var k = 0; k < boardSize.y; k++)
				display[i][k] = (frame[i][k]) ? 'x' : ' ';
	}
	
	boolean isSolved(Point curPos) {
		return curPos.equals(destPos);
	}
}