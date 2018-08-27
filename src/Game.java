import java.awt.Color;
import java.awt.Point;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JFrame;

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

public class Game extends JFrame {
	TreeSet<State> state = new TreeSet<State>(new StateComparator());
	Vector<Piece> pieces = new Vector<Piece>();
	Vector<Point> blackPts = new Vector<Point>();
	View view;
	boolean[][] boardMaster = new boolean[10][10];
	
	int[] boardSize = new int[2];
	int numPieces, boardNum = 2;
	Help help = new Help(this);

	public Game() throws Exception {
		boardMaster = help.scanInitLayout(boardNum, pieces, blackPts);
		isBoardValid();
		view = new View(this);
		this.setTitle("Puzzle");
		this.setSize(482, 505);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String[] args) throws Exception { 
		new Game();
	}
	
	boolean isBoardValid() {
		boolean[][] board = help.deepCopyBoard(boardMaster);
		
		for(Piece p : pieces)
			for(Point pt: p.pts) {
				if(board[pt.x][pt.y]) {
					System.out.println("CONFLICT:  (" + pt.x + "," + pt.y + ")");
					return false;
				}
				board[pt.x][pt.y] = true;
			}
	
		return true;
	}
}