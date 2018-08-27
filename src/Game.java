import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;

class State {
	State prev;
	byte[] state;
	State(State _prev) {
		prev = _prev;
		state = new byte[22];
	}
	static String stateToString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		sb.append(Byte.toString(b[0]));
		for(int i = 1; i < b.length; i++) {
			sb.append(",");
			sb.append(Byte.toString(b[i]));
		}
		return sb.toString();
	}
}

class StateComparator implements Comparator<State> {
	public int compare(State a, State b) {
		for(int i = 0; i < 22; i++) 
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

class Board {
	Point size, destOffset;
	int numPieces;
	boolean[][] origLayout;
	Vector<Point> blacks = new Vector<Point>();
	Vector<Piece> pieces = new Vector<Piece>();
	
	public Board() {}
	Board(Board board){
		this.size = board.size;
		this.destOffset = board.destOffset;
		this.blacks = board.blacks;
		this.origLayout = new boolean[board.size.x][board.size.y];
		for (int i = 0; i < board.size.x; i++)
		     this.origLayout[i] = Arrays.copyOf(board.origLayout[i], board.origLayout[i].length);
	}
	
	void print() {
		for(int i = 0; i < size.x; i++) {
			for(int k = 0; k < size.y; k++)
				System.out.print(((origLayout[k][i]) ? 1 : 0) + " ");
			System.out.println();
		}
		System.out.println("\n");
	}
}

public class Game {
	TreeSet<State> state = new TreeSet<State>(new StateComparator());
	GameWin win;
	Board boardInit;
	int numPieces, boardNum = 2;
	Point dest, boardSize;
	Help help = new Help(this);

	public Game() throws IOException {
		boardInit = help.loadBoard(boardNum);
		isBoardValid();
		win = new GameWin(this);
	}
	public static void main(String[] args) throws Exception { new Game(); }
	
	boolean isBoardValid() {
		Board board = new Board(boardInit);
		for(Piece p : boardInit.pieces)
			for(Point pt: p.pts) {
				if(board.origLayout[pt.x][pt.y]) {
					System.out.println("CONFLICT:  (" + pt.x + "," + pt.y + ")");
					return false;
				}
				board.origLayout[pt.x][pt.y] = true;
			}
		return true;
	}
}