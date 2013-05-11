package pvpmagic;

import java.awt.Font;
import java.awt.Graphics2D;

import screen.Screen;
import screen.ScreenHolder;

public class EndScreen extends Screen {

	boolean _winner = false;
	
	public EndScreen(ScreenHolder holder) {
		super(holder, "end");
		
		
	}

	@Override
	public void setup() {
		
	}

	@Override
	protected void draw(Graphics2D g) {
		if (_winner){
			g.setFont(new Font("Times New Roman", Font.PLAIN, 36));
			
			String s = "Victory!";
			int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, _holder._w/2-sWidth/2, 100);
		} else {
			
		}
	}

	@Override
	protected void onResize() {
		
	}

}
