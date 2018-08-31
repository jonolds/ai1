import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;

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
	byte[] value;
	State(int pieces) {
		value = new byte[pieces*2];
		prev = null;
	}
	State(State _prev) {
		prev = _prev;
		value = new byte[_prev.value.length];
	}
	State(State _prev, byte[] _value) {
		prev = _prev;
		this.value = new byte[_value.length];
		for(int i = 0; i < _value.length; i++)
			this.value[i] = _value[i];
	}
}

class VisitedComp implements Comparator<State> {
	public int compare(State a, State b) {
		for(var i = 0; i < a.value.length; i++) 
			if(a.value[i] < b.value[i]) 
				return -1;
			else if(a.value[i] > b.value[i]) 
				return 1;
		return 0;
	}
}

class SolComp implements Comparator<State> {
	public int compare(State a, State b) {
		if(a.prev == b)
			return 0;
		return 1;
	}
}

public class Game extends GameHelp {
	GameWin win;
	
	public void start() throws IOException {
		loadBoard(2, this);
		println(findSolution() ? "Found Solution!" : "Can't Solve");
	}
	
	boolean findSolution() {
		var visited = new TreeSet<>(new VisitedComp());
		var solTree = new TreeSet<>(new SolComp());
		boolean finished = false;
		State curSt = new State(pieces.size());
		
		//Both trees empty at this point. Add inital state to both
		println(visited.add(curSt));
		println(solTree.add(curSt));
		
		
		while(!finished) {
			for(int i = 0; i < curSt.value.length; i+=2) {			
				byte[] potSt = Arrays.copyOf(curSt.value, curSt.value.length);
				int[] dir = new int[] {-1, 0, 2, 0, -1, -1, 0, 2};
				
				for(int k = 0; k < 8; k+=2) {
					potSt[i] += dir[k];
					println("k: " + k);
					potSt[i+1] += dir[k+1];
					if(isValidMove(potSt)) {
						if(visited.add(new State(curSt, potSt)))
							solTree.add(new State(curSt, potSt));
					}
				}
			}
			finished = true;
			
			for(State s: visited)
				println(s.value);
			println("");
			for(State s: solTree)
				println(s.value);
			
		}
		return false; //*(&(*&(&(*(&)%$#@#$%^
	}
	
	boolean isValidMove(byte[] map) {
		println(map);
		//1. Get bool map with all black spaces marked true.
		var frame = copy2d(frameOrig);
		//2. Loop through all original piece positions
		for(Piece p : pieces) {
			int id = p.id;
			println("id: " + id);
			//3. Add offset from state to each original piece position.
			for(Point pt: p.pts) {
				//4. Return false if space is already True. If space False, mark True and continue
				if(frame[pt.x + map[id]][pt.y + map[id+1]])
					return false;
				frame[pt.x + map[id]][pt.y + map[id+1]] = true;
			}
		}
		return true;
	}
	
	void display(Vector<Point> pos) {
		char[][] display = new char[size[0]][size[1]];
		boolean[][] frame = copy2d(frameOrig);
		for (var i = 0; i < size[0]; i++)
			for(var k = 0; k < size[1]; k++)
				display[i][k] = (frame[i][k]) ? 'x' : ' ';
	}
	
	boolean isSolved(int[] curPos) { return curPos.equals(destOffset); }
	
	
	
	public Game() throws IOException {
		start();
	}
	public static void main(String[] args) throws Exception { new Game(); }
}