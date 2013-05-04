package pvpmagic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import screen.Bar;
import screen.Button;
import screen.Screen;
import screen.ScreenHolder;
import screen.InterfaceElement;

public class GameScreen extends Screen {

	boolean DEBUG = false;

	GameData _data;
	Player _focus;
	View _view;

	Bar _healthBar;
	Bar _manaBar;

	public GameScreen(ScreenHolder holder) {
		super(holder, "game");

	}

	@Override
	public void switchInto(){
		_holder.setFPS(40);
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();
		
		_healthBar = new Bar("health", new Vector(400, 15), 1);
		_healthBar.name = "Health";
		_interfaceElements.add(_healthBar);

		_manaBar = new Bar("mana", new Vector(400, 15), 1);
		_manaBar.setColorRange(new Color(0.5f, 0.5f, 1f), null, null, null);
		_manaBar.name = "Mana";
		_interfaceElements.add(_manaBar);
		
		Button menu = new Button(this, "menu", Resource.get("menu"));
		menu.w = menu.h = 60;
		_interfaceElements.add(menu);

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
		if (_data._players.size() > 0) _focus = _data._players.get(0);
		else System.out.println("No players in game!");

		onResize();
	}

	private class VisionComparator implements Comparator<Unit> {
		@Override
		public int compare(Unit u1, Unit u2) {
			if (u1._pos.y == u2._pos.y) return (int) (u1._pos.x - u2._pos.x);
			return (int) (u1._pos.y - u2._pos.y);
		}
	}

	@Override
	protected void draw(Graphics2D g) {
		_view.setGraphics(g);

		// Draw things in a logical order of vision, put here instead of GameData because it should be done by the client
		PriorityQueue<Unit> vision = new PriorityQueue<Unit>(_data._units.size(), new VisionComparator());
		vision.addAll(_data._units);
		_data._units = new ArrayList<Unit>();
		while (!vision.isEmpty()){
			_data._units.add(vision.poll());
		}

		g.setColor(new Color(.4f, .6f, 0));
		g.fillRect(0, 0, _holder._w, _holder._h);

		for (int i = 0; i < _data._units.size(); i++){
			Unit u = _data._units.get(i);
			u.draw(_view);

			if (DEBUG){
				if (u._shape != null) {
					g.setColor(Shape._debugColor);
					u._shape.draw(_view, true);
					g.setColor(Color.blue);
					u._shape.draw(_view, false);
				}
			}
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

		Vector target = _view.screenToGamePoint(new Vector(_xMouse, _yMouse));
		if (key == KeyEvent.VK_Q){
			_data.startCastingSpell(_focus, _focus._spells[0], target);
		} else if (key == KeyEvent.VK_W){
			_data.startCastingSpell(_focus, _focus._spells[1], target);
		} else if (key == KeyEvent.VK_E){
			_data.startCastingSpell(_focus, _focus._spells[2], target);
		} else if (key == KeyEvent.VK_R){
			_data.startCastingSpell(_focus, _focus._spells[3], target);
		} else if (key == KeyEvent.VK_A){
			_data.startCastingSpell(_focus, _focus._spells[4], target);
		} else if (key == KeyEvent.VK_S){
			_data.startCastingSpell(_focus, _focus._spells[5], target);
		} else if (key == KeyEvent.VK_D){
			_data.startCastingSpell(_focus, _focus._spells[6], target);			
		} else if (key == KeyEvent.VK_F){
			_data.startCastingSpell(_focus, _focus._spells[7], target);
		}

		if (key == 192) DEBUG = !DEBUG;

	}

	@Override
	public boolean onMousePressed(MouseEvent e){
		if (!super.onMousePressed(e)){
			Vector point = _view.screenToGamePoint(new Vector(e.getX(), e.getY()));
			if (!_focus._isRooted) {
				_focus._destination = point;
			}
		}
		return true;
	}




	@Override
	protected void onResize() {
		if (_view != null) _view._size = new Vector(_holder._w, _holder._h);

		if (_interfaceElements != null){
			for (InterfaceElement e : _interfaceElements){
				if (e.id.equals("health")){
					e.x = _holder._w/2-e.w/2;
					e.y = _holder._h-e.h*2-10;
				} else if (e.id.equals("mana")){
					e.x = _holder._w/2-e.w/2;
					e.y = _holder._h-e.h-5;
				} else if (e.id.equals("menu")){
					e.x = _holder._w - e.w - 5;
					e.y = _holder._h - e.h - 5;
				}
			}
		}
	}
	
	@Override
	protected void handleElementReleased(InterfaceElement e){
		super.handleElementReleased(e);
		if (e.id.equals("menu")){
			_holder.showBorder();
			_holder.switchToScreen("pause");
		}
	}
}
