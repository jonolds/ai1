import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

class State {
	State prev;
	byte[] val;
	State(int pieces) {
		this.val = new byte[pieces*2];
		this.prev = null;
	}
	State(State _prev) throws CloneNotSupportedException {
		this.prev = (State) _prev.clone();
		this.val = new byte[_prev.val.length];
	}
	State(State _prev, byte[] _value) {
		this.prev = _prev;
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
class Piece {
	Color color;
	int id;
	int[] pts;
	
	Piece(int id, int r, int g, int b, int[] pts) {
		this.id = id;
		this.color = new Color(r, g, b);
		this.pts = new int[pts.length];
		for(int i = 0; i < pts.length; i++) 
			this.pts[i] = pts[i];
	}
	Piece(Piece p) {
		this.color = new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue());
		this.id = p.id;
		this.pts = p.pts;
	}
	int[] pts() {
		int[] p = new int[pts.length];
		for(int i = 0; i < pts.length; i++)
			p[i] = this.pts[i];
		return p;
	}
}

public class Game extends GameHelp {
	
	public void start() throws IOException, InterruptedException {
		println(findSolution() ? "Found Solution!" : "Can't Solve");
	}
	TreeSet<State> visited = new TreeSet<>(new VisitedComp());
	boolean findSolution() throws IOException, InterruptedException {
		byte[][] dir = new byte[][] {new byte[] {-1,0}, new byte[] {1, 0}, new byte[] {0, -1}, new byte[] {0, 1}};
		Queue<State> q = new LinkedList<State>();
		boolean found = false;
		State initSt = new State(pieces.length);
		
		if(isValidMove(initSt))
			q.add(new State(pieces.length));

		while(!q.isEmpty()) {
			State st = q.remove();
			if(visited.add(st)) {
				if((st.val[0] == destOffset[0]) && (st.val[1] == destOffset[1])) {
					printMoves(st);
					found = true;
					break;
				}
				else {
					//Add child candidates to queue
					for(int i = 0; i < st.val.length; i+=2) {
						for(int k = 0; k < 4; k++) {
							byte[] potSt = copy(st.val);
							potSt[i] += dir[k][0];
							potSt[i+1] += dir[k][1];
							
							State newSt = new State(st, potSt);
							if(isValidMove(newSt))
								q.add(new State(st, potSt));
						}
					}
				}
			}
		}
		return found;
	}
	
	boolean isValidMove(State st) {
		var frame = copy2d(frameOrig);
		var stVal = copy(st.val);
		for(int p = 0; p < pieces.length; p++) {
			byte[] row = new byte[pieces[p].pts.length];
			int dx = stVal[p*2];
			int dy = stVal[p*2+1];
			int[] newPts = copy(pieces[p].pts);
			
			for(int i = 0; i < pieces[p].pts.length; i+=2) {
				int newX = newPts[i] + dx;
				int newY = newPts[i+1] + dy;
				row[i] = (byte) newX;
				row[i+1] = (byte) newY;
				if(frame[newX][newY])
					return false;
				frame[newX][newY] = true;
			}
		}

		if(visited.contains(st))
				return false;

		return true;
	}
	void printMoves(State st) throws IOException, InterruptedException {
		State state = st;
		Deque<State> deque = new LinkedList<State>();
		
		File file = new File("results.txt");
		if(!file.exists())
			file.createNewFile();
		BufferedWriter buf = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
		
		while(!isOrigState(state)) {
			deque.push(state);
			state = state.prev;
		}
		deque.push(state);
		
		while(!deque.isEmpty()) {
			byte[] by = deque.pop().val;
			var s = new StringBuilder();
			for(int i = 0; i < by.length; i+=2)
				s.append("(" + by[i] + "," + by[i+1] + ") ");
			
			println(s.toString());
			buf.write(s.toString());
			buf.newLine();
		}
		buf.flush();
		buf.close();
	}
	
	boolean isOrigState(State s) {
		byte[] orig = new byte[s.val.length];
		
		for(int i = 0; i < orig.length; i++)
			if(orig[i] != s.val[i])
				return false;
		return true;
	}

	public Game() throws IOException, InterruptedException {
		super();
		start();
	}
	public static void main(String[] args) throws Exception { new Game(); }
}