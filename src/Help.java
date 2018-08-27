import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class Help {
	Game game;
	Help(Game game) {
		this.game = game;
	}
	
	Vector<Point> arr2PtVec(int[] arrayInt) {
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
		String line;
		ArrayList<int[]> intList = new ArrayList<>();
		while(!(sc.nextLine().contains("***" + Integer.toString(boardNum)))) {}
		while(!((line = sc.nextLine()).contains("---"))) {
			String[] s = line.trim().split(" ");
			int[] intArray = new int[s.length];
			for(int i = 0; i < s.length; i++)
				intArray[i] = Integer.parseInt(s[i]);
			intList.add(intArray);
		}
		sc.close();
		return intList;
	}
	
	Board loadBoard(int boardNum) throws IOException {
		Board board = new Board();
		ArrayList<int[]> intList = scanIntsFromFile(boardNum);
		board.size = new Point(intList.get(0)[0], intList.get(0)[1]);
		board.destOffset = new Point(intList.get(1)[0], intList.get(1)[1]);
		board.numPieces = intList.get(2)[0];
		
		//Get Pieces
		for(int i = 0; i < board.numPieces; i++) {
			int[] pts = Arrays.copyOfRange(intList.get(i+3), 3, intList.get(i+3).length);
			board.pieces.add(new Piece(i, intList.get(i+3)[0], intList.get(i+3)[1], intList.get(i+3)[2], arr2PtVec(pts)));
		}
		
		//Get Black Spaces
		for(int i = 0; i < intList.get(board.numPieces + 3).length; i+=2)
			board.blacks.add(new Point(intList.get(board.numPieces+3)[i], intList.get(board.numPieces+3)[i+1]));
		
		//Create origLayout
		board.origLayout = new boolean[board.size.x][board.size.y];
		for(Point pt: board.blacks) {
			if(board.origLayout[pt.x][pt.y]) {
				System.out.println("CONFLICT CREATING BOARD MASTER: black point:  (" + pt.x + "," + pt.y + ")");
				System.exit(1);
			}
			board.origLayout[pt.x][pt.y] = true;
		}
		return board;
	}
}