package pvpmagic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import screen.InterfaceElement;

import screen.Button;
import screen.Screen;
import screen.ScreenHolder;

public class PauseScreen extends Screen {
	
	GameScreen _game;

	public PauseScreen(ScreenHolder holder) {
		super(holder, "pause");
		_allowShowBehind = true;
		_allowUpdateBehind = true;
		
		setup();
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();
		Button game = new Button(this, "game", 200, 50, "Return to Game", KeyEvent.VK_R);
		_interfaceElements.add(game);
		
		Button main = new Button(this, "main", 200, 50, "Return to Main", KeyEvent.VK_M);
		_interfaceElements.add(main);
		
		onResize();
	}

	int _w = 300;
	int _h = 600;
	
	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRoundRect(_holder._w/2-_w/2, _holder._h/2-_h/2, _w, _h, 10, 10);
		g.setColor(Color.black);
		g.drawRoundRect(_holder._w/2-_w/2, _holder._h/2-_h/2, _w, _h, 10, 10);
		
		g.setFont(new Font("Times New Roman", Font.PLAIN, 36));
		g.drawString("Options", _holder._w/2-58, _holder._h/2-_h/2+50);
		
		g.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		g.drawString("(Press ESC to return to game)", _holder._w/2-144, _holder._h/2+_h/2-12);

	}

	@Override
	protected void onResize() {
		for (InterfaceElement e : _interfaceElements){
			if (e.id.equals("game")){
				e.x = _holder._w/2-e.w/2;
				e.y = _holder._h/2-_h/2+90;
			} else if (e.id.equals("main")){
				e.x = _holder._w/2-e.w/2;
				e.y = _holder._h/2-_h/2+250;
			}
		}

	}
	
	@Override
	protected void handleElementReleased(InterfaceElement e){
		if (e.id.equals("game")){
			_holder.hideBorder();
			_holder.putScreenAway();
		} else if (e.id.equals("main")){
			_game.end();
		}
	}

	public void setGame(GameScreen game) {
		_game = game;
	}
	
	public void onKeyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE){
			_holder.hideBorder();
			_holder.putScreenAway();
		}
	}

}
