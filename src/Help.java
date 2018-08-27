import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class Help {
	Game game;
	Help(Game game) {
		this.game = game;
	}
	
	Vector<Point> cvt_ArrInt2VecPoint(int[] arrayInt) {
		Vector<Point> pntVec = new Vector<Point>();
		for(int i = 0; i < arrayInt.length; i+=2)
			pntVec.add(new Point(arrayInt[i], arrayInt[i+1]));
		return pntVec;
	}
	
	int[] cvt_VecPoint2ArrInt(Vector<Point> pts) {
		int[] intArray = new int[pts.size()*2];
		for(int i = 0; i < pts.size(); i++) {
			intArray[i*2] = pts.get(i).x;
			intArray[i*2+1] = pts.get(i).y;
		}
		return intArray;
	}
	
	ArrayList<int[]> scanIntsFromFile(int boardNum) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("initialValues.txt"));
		while(!(sc.nextLine().contains("***" + Integer.toString(boardNum)))) {}

		game.boardSize[0] = sc.nextInt(); 
		game.boardSize[1] = sc.nextInt();
		game.numPieces = sc.nextInt();
		sc.nextLine();				//NEEDED after getting ints
		
		ArrayList<int[]> intAL = new ArrayList<int[]>();
		
		for(int k = 0; k < game.numPieces+1; k++) {
			String[] s = sc.nextLine().trim().split(" ");
			int[] intArray = new int[s.length];
			for(int i = 0; i < s.length; i++)
				intArray[i] = Integer.parseInt(s[i]);
			intAL.add(intArray);
		}
		sc.close();
		return intAL;
	}
	
	boolean[][] scanInitLayout(int boardNum, Vector<Piece> pieces, Vector<Point> blackPts) throws FileNotFoundException {
		ArrayList<int[]> intAL = scanIntsFromFile(boardNum);
		//Get Pieces
		for(int i = 0; i < game.numPieces; i++) {
			int[] pts = Arrays.copyOfRange(intAL.get(i), 3, intAL.get(i).length);
			pieces.addElement(new Piece(i, intAL.get(i)[0], intAL.get(i)[1], intAL.get(i)[2], cvt_ArrInt2VecPoint(pts)));
		}
		//Get Black Points
		for(int i = 0; i < intAL.get(game.numPieces).length; i+=2)
			blackPts.add(new Point(intAL.get(game.numPieces)[i], intAL.get(game.numPieces)[i+1]));
		
		//Create Master Original Board
		boolean[][] boardMaster = new boolean[game.boardSize[0]][game.boardSize[1]];
		for(Point pt: blackPts) {
			if(boardMaster[pt.x][pt.y]) {
				System.out.println("CONFLICT CREATING BOARD MASTER: black point:  (" + pt.x + "," + pt.y + ")");
				System.exit(1);
			}
			boardMaster[pt.x][pt.y] = true;
		}
		
//		for(Piece p: pieces) {
//			for(Point pt: p.pts) {
//				if(boardMaster[pt.x][pt.y]) {
//					System.out.println("CONFLICT INITIALIZING MASTER. piece id: " + p.id +  "  Point: (" + pt.x + "," + pt.y + ")");
//					System.exit(1);
//				}
//				boardMaster[pt.x][pt.y] = true;
//			}
//		}
		return boardMaster;
	}
	
	void printBoolBoard(boolean[][] board) {
		for(int i = 0; i < game.boardSize[0]; i++) {
			for(int k = 0; k < game.boardSize[1]; k++)
				System.out.print(((board[k][i]) ? 1 : 0) + " ");
			System.out.println();
		}
		System.out.println("\n");
	}
	
	boolean[][] deepCopyBoard(boolean[][] board) {
		boolean[][]  boardNew = new boolean[board.length][board[0].length];
		for (int i = 0; i < game.boardSize[0]; i++)
		     boardNew[i] = Arrays.copyOf(board[i], board[i].length);
		return boardNew;
	}
}