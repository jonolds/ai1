import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class GameHelp {
	
	Vector<Point> blacks = new Vector<>();
	Vector<Piece> pieces = new Vector<>();
	byte[] size, destOffset = new byte[2];
	boolean[][] frameOrig;

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
	
	void loadBoard(int boardNum, Game game) throws IOException {
		var intList = scanIntsFromFile(boardNum);
		size = new byte[] {(byte)intList.get(0)[0], (byte)intList.get(0)[1]};
		byte[] dest = new byte[] {(byte)intList.get(1)[0], (byte)intList.get(1)[1]};
		int numPieces = intList.get(2)[0];
		
		//Get Pieces
		for(var i = 0; i < numPieces; i++) {
			var pts = Arrays.copyOfRange(intList.get(i+3), 3, intList.get(i+3).length);
			pieces.add(new Piece(i, intList.get(i+3)[0], intList.get(i+3)[1], intList.get(i+3)[2], arr2PtVec(pts)));
		}
		
		//Calculate and set destOffset from piece0(pts0)
		destOffset[0] = (byte) (dest[0] - pieces.get(0).pts.get(0).x);
		destOffset[1] = (byte) (dest[1] - pieces.get(0).pts.get(0).y);
		
		//Get Black Spaces
		for(var i = 0; i < intList.get(numPieces + 3).length; i+=2)
			blacks.add(new Point(intList.get(numPieces+3)[i], intList.get(numPieces+3)[i+1]));
		
		//Create origLayout
		frameOrig = new boolean[size[0]][size[1]];
		for(var pt: blacks)
			frameOrig[pt.x][pt.y] = true;
	}
	
	public boolean[][] copy2d(boolean[][] orig) {
		var copy = new boolean[orig.length][orig[0].length];
		for (var i = 0; i < orig.length; i++)
		    copy[i] = orig[i].clone();
		return copy;
	}
	
	static String arr2Str(byte[] b) {
		var sb = new StringBuilder(Byte.toString(b[0]));
		for(var i = 1; i < b.length; i++)
			sb.append("," + Byte.toString(b[i]));
		return sb.toString();
	}
	void print(boolean[][] board) {
		for(var i = 0; i < board.length; i++) {
			for(var k = 0; k < board[0].length; k++)
				System.out.print(((board[k][i]) ? 1 : 0) + " ");
			System.out.println();
		}
		System.out.println("\n");
	}
	void print(State st) {
		var s = new StringBuilder();
		for(int i = 0; i < st.val.length; i+=2)
			s.append("(" + st.val[i] + "," + st.val[i+1] + ") ");
		println(s);
	}
	public <T>void print(T obj) {
		System.out.print(obj);
	}
	public <T>void println(T obj) {
		System.out.println(obj);
	}
	public void println(byte[] bArray) {
		if(bArray.length > 0) {
			for(int i = 0; i < bArray.length; i++)
				System.out.print(bArray[i] + " ");
		}
		System.out.print("\n");
	}
}