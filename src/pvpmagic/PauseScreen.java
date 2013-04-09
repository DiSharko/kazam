package pvpmagic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import screen.Screen;
import screen.ScreenHolder;

public class PauseScreen extends Screen {

	public PauseScreen(ScreenHolder holder) {
		super(holder, "pause");
		_allowShowBehind = true;
		_allowUpdateBehind = true;
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	int _w = 300;
	int _h = 600;
	
	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRoundRect(_holder._w/2-_w/2, _holder._h/2-_h/2, _w, _h, 10, 10);
		g.setColor(Color.black);
		g.drawRoundRect(_holder._w/2-_w/2, _holder._h/2-_h/2, _w, _h, 10, 10);

	}

	@Override
	protected void onResize() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onKeyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE){
			_holder.hideBorder();
			_holder.putScreenAway();
		}
	}

}
