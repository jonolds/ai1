import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
import java.util.Vector;

class State {
	State prev;
	byte[] val;
	State(int pieces) {
		val = new byte[pieces*2];
		prev = null;
	}
	State(State _prev) {
		prev = _prev;
		val = new byte[_prev.val.length];
	}
	State(State _prev, byte[] _value) {
		prev = _prev;
		this.val = new byte[_value.length];
		for(int i = 0; i < _value.length; i++)
			this.val[i] = _value[i];
	}
}

class VisitedComp implements Comparator<State> {
	public int compare(State a, State b) {
		for(var i = 0; i < a.val.length; i++) 
			if(a.val[i] < b.val[i]) 
				return -1;
			else if(a.val[i] > b.val[i]) 
				return 1;
		return 0;
	}
}

public class Game extends GameHelp {
	GameWin win;
	
	public void start() throws IOException {
		loadBoard(2, this);
		println(destOffset);
		println(findSolution() ? "Found Solution!" : "Can't Solve");
	}
	
	boolean findSolution() {
		Queue<State> q = new LinkedList<State>();
		var visited = new TreeSet<>(new VisitedComp());
		boolean found = false;
		State initSt = new State(pieces.size());
		if(isValidMove(initSt))
			q.add(new State(pieces.size()));

		while(!q.isEmpty()) {
			State st = q.remove();
			print(st);
			visited.add(st);
			if((st.val[0] == destOffset[0]) && (st.val[1] == destOffset[1])) {
				q.clear();
				found = true;
				printMoves(st);
			}
			else {
				//Add child candidates to queue
				for(int i = 0; i < st.val.length; i+=2) {
					byte[] potSt = Arrays.copyOf(st.val, st.val.length);
					byte[] dir = new byte[] {-1, 0, 2, 0, -1, -1, 0, 2};
					
					for(int k = 0; k < 8; k+=2) {
						potSt[i] += dir[k];
						potSt[i+1] += dir[k+1];
						State newSt = new State(st, potSt);
						if((isValidMove(newSt)) && !(visited.contains(newSt)))
							q.add(new State(st, potSt));
					}
				}
			}
			if(q.size() > 70000)
				break;
		}
		return found;
	}
	
	boolean isValidMove(State st) {
		//1. Get bool map with all black spaces marked true.
		var frame = copy2d(frameOrig);
		//2. Loop through all original piece positions
		for(Piece p : pieces) {
			int id = p.id;
			//3. Add offset from state to each original piece position.
			for(Point pt: p.pts) {
				//4. Return false if space is already True. If space False, mark True and continue
				if(frame[(pt.x + st.val[id])][(pt.y + st.val[id+1])])
					return false;
				frame[pt.x + st.val[id]][pt.y + st.val[id+1]] = true;
			}
		}
		
		
		return true;
	}
	void printMoves(State st) {
		State state = st;
		Deque<State> deque = new LinkedList<State>();
		while(st.prev != null) {
			deque.push(state);
			state = st.prev;
		}
		while(!deque.isEmpty()) {
			print(deque.pop());
		}
		
	}

	boolean isSolved(int[] curPos) { return curPos.equals(destOffset); }
	
	
	
	public Game() throws IOException {
		start();
	}
	public static void main(String[] args) throws Exception { new Game(); }
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