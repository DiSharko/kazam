package pvpmagic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import screen.Bar;
import screen.Screen;
import screen.ScreenHolder;
import screen.InterfaceElement;

public class GameScreen extends Screen {

	GameData _data;
	Player _focus;
	View _view;
	
	Bar _healthBar;
	Bar _manaBar;
	
	public GameScreen(ScreenHolder holder) {
		super(holder, "game");
		_interfaceElements = new ArrayList<InterfaceElement>();
	}
	
	@Override
	public void switchInto(){
		_holder.setFPS(40);
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
		_data.setup(s);
		_focus = _data._players.get(0);
		_healthBar = new Bar("health", new Vector(400, 15), _focus._maxHealth);
		_interfaceElements.add(_healthBar);
		
		_manaBar = new Bar("mana", new Vector(400, 15), _focus._maxMana);
		_manaBar.setColorRange(new Color(0.5f, 0.5f, 1f), null, null, null);
		_interfaceElements.add(_manaBar);
		
		onResize();
	}
	
	@Override
	protected void draw(Graphics2D g) {
		_view.setGraphics(g);

		g.setColor(Color.white);
		g.fillRect(0, 0, _holder._w, _holder._h);
		
		for (int i = 0; i < _data._units.size(); i++){
			 Unit u = _data._units.get(i);
			 u.draw(_view);
			 
			 if (u._shape != null) {
				g.setColor(Shape._debugColor);
			 	u._shape.draw(_view, true);
			 }
		}

		if (Resource._gameImagesAlpha.containsKey("viewField")){
			int size = (int) Math.min(_holder._w, _holder._h);
			g.drawImage(Resource._gameImagesAlpha.get("viewField"), (_holder._w-size)/2, (_holder._h-size)/2, size, size, null);
		}
		
	}

	
	
	
	
	
	
	@Override
	public void update(){
		super.update();
		
		_data.update();
		
		_view._camera = _focus._pos;
		_view._scale = (Math.min(_holder._h, _holder._w))/800.0;
		
		if (_focus != null){
			_healthBar.current = _focus._health;
			_healthBar.total = _focus._maxHealth;

			_manaBar.current = _focus._mana;
			_manaBar.total = _focus._maxMana;
		}
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
		
		Vector dir = new Vector(_xMouse, _yMouse).minus(_view.gameToScreenPoint(_focus._pos));
		if (key == KeyEvent.VK_Q){
			_data.startCastingSpell(_focus, 0, dir);
		} else if (key == KeyEvent.VK_W){
			_data.startCastingSpell(_focus, 1, dir);
		} else if (key == KeyEvent.VK_E){
			_data.startCastingSpell(_focus, 2, dir);
		} else if (key == KeyEvent.VK_R){
			_data.startCastingSpell(_focus, 3, dir);
		} else if (key == KeyEvent.VK_A){
			_data.startCastingSpell(_focus, 4, dir);
		} else if (key == KeyEvent.VK_S){
			_data.startCastingSpell(_focus, 5, dir);
		} else if (key == KeyEvent.VK_D){
			_data.startCastingSpell(_focus, 6, dir);			
		} else if (key == KeyEvent.VK_F){
			_data.startCastingSpell(_focus, 7, dir);
		}
		
	}

	@Override
	public void onMousePressed(MouseEvent e){
		Vector point = _view.screenToGamePoint(new Vector(e.getX(), e.getY()));
		if (!_focus._isRooted) {
			_focus._destination = point;
		}
	}

	
	
	
	@Override
	protected void onResize() {
		if (_view != null) _view._size = new Vector(_holder._w, _holder._h);
		
		for (InterfaceElement e : _interfaceElements){
			if (e.id.equals("health")){
				e.x = _holder._w/2-e.w/2;
				e.y = _holder._h-e.h*2-10;
			} else if (e.id.equals("mana")){
				e.x = _holder._w/2-e.w/2;
				e.y = _holder._h-e.h-5;
			}
		}
		
	}
}
