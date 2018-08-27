import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;

import javax.swing.JPanel;

class View extends JPanel {
	Game game;
	byte[] state;
	Graphics graphics;
	int size = 48;

	View(Game g) throws IOException {
		game = g;
		state = new byte[22];
	}
	
	public void drawBlock(int x, int y) { graphics.fillRect(size * x, size * y, size, size); }
	
	public void paintComponent(Graphics gfx) {
		graphics = gfx;
		graphics.setColor(new Color(0, 0, 0));
		for(Point pt : game.blackPts)
			drawBlock(pt.x, pt.y);
		for(Piece p : game.pieces) {
			graphics.setColor(p.color);
			for(Point pt: p.pts)
				drawBlock(state[p.id] + pt.x, state[p.id+1] + pt.y);
		}
	}
}