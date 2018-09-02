import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class GameHelp {
	
	int[] blacksTmp;
	Piece[] piecesTmp;
	int[] sizeTmp, destOffsetTmp;
	boolean[][] frameOrigTmp;
	
	int[] blacks;
	Piece[] pieces;
	final int[] size;
	final int[] destOffset;
	boolean[][] frameOrig;
	
	int blacks(int i) {
		return blacks[i];
	}
	int[] getBlacks() {
		return blacks.clone();
	}
	
	int size(int i) {
		return size[i];
	}
	int destOff(int i) {
		return destOffset[i];
	}
	boolean[][] frame() {
		boolean[][] f = new boolean[frameOrig.length][frameOrig[0].length];
		for(int i = 0; i < frameOrig.length; i++)
			f = frameOrig.clone();
		return f;
	}
	Piece piece(int i) {
		return new Piece(pieces[i]);
	}
	Piece[] getPieces() {
		Piece[] p = new Piece[pieces.length];
		p = Arrays.copyOf(pieces, pieces.length);
		return p;
	}

	GameHelp() throws IOException {
		loadBoard(1);
		this.pieces = this.piecesTmp.clone();
		this.size = this.sizeTmp.clone();
		this.destOffset = this.destOffsetTmp.clone();
		this.frameOrig = this.frameOrigTmp;
		this.blacks = this.blacksTmp.clone();

	}
	
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
	
	void loadBoard(int boardNum) throws IOException {
		var intList = scanIntsFromFile(boardNum);
		sizeTmp = new int[] {intList.get(0)[0], intList.get(0)[1]};
		int[] dest = new int[] {intList.get(1)[0], intList.get(1)[1]};
		int numPieces = intList.get(2)[0];
		
		//Get Pieces
		
		piecesTmp = new Piece[numPieces];
		for(var i = 0; i < numPieces; i++) {
			int[] pieceLine = intList.get(i+3);
			int[] piecePts = new int[pieceLine.length - 3];
			System.arraycopy(pieceLine, 3, piecePts, 0, pieceLine.length - 3);
			piecesTmp[i] = new Piece(i, pieceLine[0], pieceLine[1], pieceLine[2], piecePts);
		}
		
		//Calculate and set destOffset from piece0(pts0)
		destOffsetTmp = new int[] {dest[0] - piecesTmp[0].pts[0], dest[1] - piecesTmp[0].pts[1]};
		destOffsetTmp[0] = dest[0] - piecesTmp[0].pts[0];
		destOffsetTmp[1] = dest[1] - piecesTmp[0].pts[1];
		
		//Get Black Spaces
		blacksTmp = intList.get(numPieces + 3);
//		for(var i = 0; i < intList.get(numPieces + 3).length; i+=2) {
//			int[] blackLine = intList.get(numPieces+3);
//			blacksTmp = new int[] {intList.get(numPieces + 3)[i], intList.get(numPieces+3)[i+1]};
//		}
		
		//Create origLayout
		frameOrigTmp = new boolean[sizeTmp[0]][sizeTmp[1]];
		for(int i = 0; i < blacksTmp.length; i+=2)
			frameOrigTmp[blacksTmp[i]][blacksTmp[i+1]] = true;
	}
	
	public boolean[][] copy2d(boolean[][] orig) {
		var copy = new boolean[orig.length][orig[0].length];
		for (var i = 0; i < orig.length; i++)
		    copy[i] = orig[i].clone();
		return copy;
	}
	
	public int[] copy(int[] orig) {
		var copy = new int[orig.length];
		for (var i = 0; i < orig.length; i++)
			copy[i] = orig[i];
		return copy;
	}
	public byte[] copy(byte[] orig) {
		var copy = new byte[orig.length];
		for (var i = 0; i < orig.length; i++)
			copy[i] = orig[i];
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
		var s = new StringBuilder();
		for(int i = 0; i < bArray.length; i+=2)
			s.append("(" + bArray[i] + "," + bArray[i+1] + ") ");
		println(s);
	}
	public void print(int[] integ) {
		for(int i : integ)
			print(i + " ");
		println(" ");
	}
	public void print(byte[] b) {
		println(b);
	}
}