import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

class GameState {
	GameState prev;
	byte[] state;
	
	GameState(GameState _prev) {
		prev = _prev;
		state = new byte[22];
	}
}

class Piece {
	byte[] color;
	int id;
	Vector<Point> pts;
	
	Piece(int id, byte[] color, Vector<Point> pts) {
		this.id = id;
		this.color = color;
		this.pts = pts;
	}
}

class StateComparator implements Comparator<GameState> {
	public int compare(GameState a, GameState b) {
		for(int i = 0; i < 22; i++) {
			if(a.state[i] < b.state[i])
				return -1;
			if(a.state[i] > b.state[i])
				return 1;
		}
			return 0;
	}
}

public class Game extends JFrame {
	StateComparator comp = new StateComparator();
	TreeSet<GameState> state = new TreeSet<GameState>(comp);
	Vector<Piece> pieces = new Vector<Piece>();
	Vector<Point> blackPts = new Vector<Point>();
	View view;
	boolean[][] boardOrig = new boolean[10][10];
	ArrayList<Point> blacks = new ArrayList<Point>();
	ArrayList<int[]> intsInit = new ArrayList<int[]>();
	ArrayList<Point[]> ptsInit = new ArrayList<Point[]>();

	public void init() throws FileNotFoundException {
		//BLACK
		Scanner sc = new Scanner(new File("initialValues.txt"));
		ArrayList<int[]> intAL = new ArrayList<int[]>();
	
		while(sc.hasNextLine()) {
			String[] s = sc.nextLine().trim().split(" ");
			int[] intArray = new int[s.length];
			for(int i = 0; i < s.length; i++)
				intArray[i] = Integer.parseInt(s[i]);
			intAL.add(intArray);
		}
		sc.close();

		for(int i = 0; i < intAL.size(); i++) {
			for(int k: intAL.get(i))
				System.out.print(k + " ");
			System.out.println();
		}
		
		/*
		for(int i = 0; i < 10; i++)		//top and bottom rows
			Collections.addAll(blacks, new Point(i,0), new Point(i,9));
		for(int i = 1; i < 9; i++)		//sides
			Collections.addAll(blacks, new Point(0,i), new Point(9,i));
		Collections.addAll(blacks, 
				new Point(1,1), new Point(1,2), new Point(2,1),
				new Point(7,1), new Point(8,1), new Point(8,2),
				new Point(1,7), new Point(1,8), new Point(2,8),
				new Point(8,7), new Point(7,8), new Point(8,8),
				new Point(3,4), new Point(4,4), new Point(4,3)); 
		for(Point pt: blacks) {
			boardOrig[pt.x][pt.y] = true;
			System.out.print(pt.x + " " + pt.y + " ");
		}
		System.out.println();
		*/
		int[] a0 = {1,3, 2,3, 1,4, 2,4};
		int[] a1 = {1,5, 1,6, 2,6};
		int[] a2 = {2,5, 3,5, 3,6};
		int[] a3 = {3,7, 3,8, 4,8};
		int[] a4 = {4,7, 5,7, 5,8};
		int[] a5 = {6,7, 7,7, 6,8};
		int[] a6 = {5,4, 5,5, 5,6, 4,5};
		int[] a7 = {6,4, 6,5, 6,6, 7,5};
		int[] a8 = {8,5, 8,6, 7,6};
		int[] a9 = {6,2, 6,3, 5,3};
		int[] a10 = {5,1, 6,1, 5,2};
		Collections.addAll(intsInit, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
		for(int[] a : intsInit) {
			for(int i : a)
				System.out.print(i + " ");
			System.out.println("");
			
			Point[] p = new Point[a.length/2];
			for(int i = 0; i < a.length; i+=2)
				p[i/2] = new Point(a[i], a[i+1]);
			ptsInit.add(p);	
		}
		
		for(boolean[] elem: boardOrig) {
			for(boolean elem2: elem)
				System.out.print((elem2 ? 1 : 0) + " ");
			System.out.println(" ");
		}
	}
	
	public Game() throws Exception {
		init();
		view = new View(this);
		view.addMouseListener(view);
		this.setTitle("Puzzle");
		this.setSize(482, 505);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		new Game();
	}
	
	public void run() {
		
	}
}

class View extends JPanel implements MouseListener {
	Game game;
	Random rand;
	byte[] state;
	Graphics graphics;
	int size;

	View(Game g) throws IOException {
		game = g;
		rand = new Random(0);
		state = new byte[22];
		size = 48;
	}

	public void mousePressed(MouseEvent e) {
		state[rand.nextInt(22)] += (rand.nextInt(2) == 0 ? -1 : 1);

		for(int i = 0; i < 11; i++)
		System.out.print("(" + state[2 * i] + "," +
			state[2 * i + 1] + ") ");
		System.out.println();
		game.repaint();
	}

	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	// Draw a block
	public void b(int x, int y) {
		graphics.fillRect(size * x, size * y, size, size);
	}

	// Draw a 3-block piece
	public void shape(int id, int red, int green, int blue,
		int x1, int y1, int x2, int y2, int x3, int y3) {
		graphics.setColor(new Color(red, green, blue));
		b(state[2 * id] + x1, state[2 * id + 1] + y1);
		b(state[2 * id] + x2, state[2 * id + 1] + y2);
		b(state[2 * id] + x3, state[2 * id + 1] + y3);
	}

	// Draw a 4-block piece
	public void shape(int id, int red, int green, int blue,
		int x1, int y1, int x2, int y2,
		int x3, int y3, int x4, int y4) {
		shape(id, red, green, blue, x1, y1, x2, y2, x3, y3);
		b(state[2 * id] + x4, state[2 * id + 1] + y4);
	}

	public void paintComponent(Graphics gfx) {
		// Draw the black squares
		graphics = gfx;
		gfx.setColor(new Color(0, 0, 0));
		for(int i = 0; i < 10; i++) { 
			b(i, 0); 
			b(i, 9); 
		}
		for(int i = 1; i < 9; i++) { 
			b(0, i); 
			b(9, i); 
		}
		b(1, 1); b(1, 2); b(2, 1);
		b(7, 1); b(8, 1); b(8, 2);
		b(1, 7); b(1, 8); b(2, 8);
		b(8, 7); b(7, 8); b(8, 8);
		b(3, 4); b(4, 4); b(4, 3);

		// Draw the pieces
		shape(0, 255, 0, 0, 1, 3, 2, 3, 1, 4, 2, 4);
		shape(1, 0, 255, 0, 1, 5, 1, 6, 2, 6);
		shape(2, 128, 128, 255, 2, 5, 3, 5, 3, 6);
		shape(3, 255, 128, 128, 3, 7, 3, 8, 4, 8);
		shape(4, 255, 255, 128, 4, 7, 5, 7, 5, 8);
		shape(5, 128, 128, 0, 6, 7, 7, 7, 6, 8);
		shape(6, 0, 128, 128, 5, 4, 5, 5, 5, 6, 4, 5);
		shape(7, 0, 128, 0, 6, 4, 6, 5, 6, 6, 7, 5);
		shape(8, 0, 255, 255, 8, 5, 8, 6, 7, 6);
		shape(9, 0, 0, 255, 6, 2, 6, 3, 5, 3);
		shape(10, 255, 128, 0, 5, 1, 6, 1, 5, 2);
	}
}