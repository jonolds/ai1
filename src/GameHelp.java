import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class GameHelp {

	Vector<Point> arr2PtVec(int[] arrayInt) {
		var pntVec = new Vector<Point>();
		for(var i = 0; i < arrayInt.length; i+=2)
			pntVec.add(new Point(arrayInt[i], arrayInt[i+1]));
		return pntVec;
	}
	int[] vecPt2Arr(Vector<Point> pts) {
		var intArray = new int[pts.size()*2];
		for(var i = 0; i < pts.size(); i++) {
			intArray[i*2] = pts.get(i).x;
			intArray[i*2+1] = pts.get(i).y;
		}
		return intArray;
	}

	ArrayList<int[]> scanIntsFromFile(int boardNum) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("initialValues.txt"));
		var line = "";
		var intList = new ArrayList<int[]>();
		while(!(sc.nextLine().contains("***" + Integer.toString(boardNum)))) {}
		while(!((line = sc.nextLine()).contains("---"))) {
			var s = line.trim().split(" ");
			var intArray = new int[s.length];
			for(var i = 0; i < s.length; i++)
				intArray[i] = Integer.parseInt(s[i]);
			intList.add(intArray);
		}
		sc.close();
		return intList;
	}
	
	Board loadBoard(int boardNum) throws IOException {
		var board = new Board();
		var intList = scanIntsFromFile(boardNum);
		board.size = new Point(intList.get(0)[0], intList.get(0)[1]);
		board.destOffset = new Point(intList.get(1)[0], intList.get(1)[1]);
		board.numPieces = intList.get(2)[0];
		
		//Get Pieces
		for(var i = 0; i < board.numPieces; i++) {
			var pts = Arrays.copyOfRange(intList.get(i+3), 3, intList.get(i+3).length);
			board.pieces.add(new Piece(i, intList.get(i+3)[0], intList.get(i+3)[1], intList.get(i+3)[2], arr2PtVec(pts)));
		}
		
		//Get Black Spaces
		for(var i = 0; i < intList.get(board.numPieces + 3).length; i+=2)
			board.blacks.add(new Point(intList.get(board.numPieces+3)[i], intList.get(board.numPieces+3)[i+1]));
		
		//Create origLayout
		board.frame = new boolean[board.size.x][board.size.y];
		for(var pt: board.blacks)
			board.frame[pt.x][pt.y] = true;
		return board;
	}
	
	public boolean[][] copy2d(boolean[][] orig) { 
		var copy = new boolean[orig.length][orig[0].length];
		for (var i = 0; i < orig.length; i++)
		    copy[i] = orig[i].clone();
		return copy;
	}
	
	void print(boolean[][] board) {
		for(var i = 0; i < board.length; i++) {
			for(var k = 0; k < board[0].length; k++)
				System.out.print(((board[k][i]) ? 1 : 0) + " ");
			System.out.println();
		}
		System.out.println("\n");
	}
}