package pvpmagic;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import screen.Screen;
import screen.ScreenHolder;

public class GameScreen extends Screen {

	GameData _data;
	Player _focus;
	View _view;
	
	public GameScreen(ScreenHolder holder) {
		super(holder, "game");
	}

	@Override
	public void setup() {
		startGame();
	}
	
	public void startGame(){
		_holder.hideBorder();
		_holder._window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		_data = new GameData();
		_view = new View(_data);
	}
	
	public void configureGame(SetupScreen s){
		_data.addPlayer(s);
	}

	@Override
	protected void draw(Graphics2D g) {
		_view.setGraphics(g);
		for (int i = 0; i < _data._units.size(); i++){
			 Unit u = _data._units.get(i);
			 u.draw(_view);
		}
	}
	
	
	
	
	
	
	
	@Override
	public void update(){
		_data.update();
	}
	
	

	
	@Override
	public void onKeyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_ESCAPE){
			_holder.showBorder();
			_holder.switchToScreen("pause");
		}
		
		if (key == KeyEvent.VK_LEFT){
			_view._camera = _view._camera.minus(10, 0);
		}
		if (key == KeyEvent.VK_RIGHT){
			_view._camera = _view._camera.plus(10, 0);
		}
		if (key == KeyEvent.VK_UP){
			_view._camera = _view._camera.minus(0, 10);
		}
		if (key == KeyEvent.VK_DOWN){
			_view._camera = _view._camera.plus(0, 10);
		}
		
	}


	
	
	
	@Override
	protected void onResize() {}
}
