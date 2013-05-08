package pvpmagic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import network.SyncedString;

import pvpmagic.spells.Spell;

import screen.Bar;
import screen.Button;
import screen.Screen;
import screen.ScreenHolder;
import screen.InterfaceElement;

public class GameScreen extends Screen {

	boolean DEBUG = false;
	
	// shared network vars - setup in setup TODO
	boolean _isClient;
	boolean _isHost;
	HashMap<Integer,Unit> _staticMap;
	HashMap<Integer,Unit> _dynamicMap;
	int lobbyVersion;
	
	// Server vars - set in setup TODO
	public PriorityBlockingQueue<String> _netInputs; // inputs used by server
	HashMap<Integer,Player> _playerMap;
	AtomicBoolean _running;
	AtomicBoolean _startedS;
	int _port;
	GameData _dataS;
	int _tick;
	
	// Client vars - set in setup TODO
	ConcurrentLinkedQueue<String> _netOutputs; // outputs added to by client
	AtomicBoolean _connected;
	AtomicBoolean _started;
	SyncedString _gameData;
	SyncedString _lobbyData;
	int _getPort;
	int _sendPort;
	AtomicInteger _focusID;
	PriorityQueue<Player> _players;
	int _lastTick;
	int _clientTick;
	

	GameData _data;
	Player _focus;
	View _view;

	Bar _healthBar;
	Bar _manaBar;

	int _spellButtonSize = 60;
	Button[] _focusSpellButtons;

	public GameScreen(ScreenHolder holder) {
		super(holder, "game");

	}

	@Override
	public void switchInto(){
		_holder.setFPS(40);
		_holder.hideBorder();
		_holder._window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
		
		_isHost = false;
		
		onResize();
	}

	public void initializeGame(SetupScreen s){
		_data = new GameData();
		_data.setup(s);
		
		if (_data._players.size() > 0) setFocus(_data._players.get(0));
		else System.out.println("No players in game!");

		_view = new View(_data);
		
		onResize();
	}
	
	private class VisionComparator implements Comparator<Unit> {
		@Override
		public int compare(Unit u1, Unit u2) {
			if(!(u1._drawUnder && u2._drawUnder)) {
				if (u1._drawUnder) return -1;
				else if (u2._drawUnder) return 1;
			}
			if (u1._pos.y == u2._pos.y) return (int) (u2._pos.x - u1._pos.x);
			return (int) (u1._pos.y - u2._pos.y);
		}
	}

	@Override
	protected void draw(Graphics2D g){
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
	protected void drawOnTop(Graphics2D g){
		if (_focusSpellButtons != null){
			for (int i = 0; i < _focusSpellButtons.length; i++){
				if (_focus._spells[i] == null || _focusSpellButtons[i] == null) continue;
				int x = (i%4)*(_spellButtonSize+5) + 10;
				int y = ((int)(i/4))*(_spellButtonSize+5)+_holder._h-_spellButtonSize*2-10;

				g.setColor(Color.black);
				g.setFont(new Font("Helvetica", Font.PLAIN, 16));
				g.drawString(""+Resource._spellKeys[i], x+6, y+17);

				Spell proto = Spell.newSpell(_data, _focus._spells[i], null, null);
				if (_focus._spellCastingTimes.containsKey(_focusSpellButtons[i].name)){
					double timeSinceCast = System.currentTimeMillis() - _focus._spellCastingTimes.get(_focusSpellButtons[i].name);
					double cooldown = proto._cooldown;

					if (timeSinceCast < cooldown){
						double fraction = (cooldown-timeSinceCast)/cooldown;
						g.setColor(new Color(0,0,1,0.4f));
						g.fillRoundRect(x, y, (int)(_spellButtonSize*fraction), _spellButtonSize, 15, 15);
					}
				}
				if (proto._manaCost > _focus._mana){
					g.setColor(new Color(1,0.7f,0.7f,0.7f));
					g.fillRoundRect(x, y, _spellButtonSize, _spellButtonSize, 15, 15);
				}
				if (_focus._isSilenced && !_focusSpellButtons[i].name.equals("Cleanse")){
					g.drawImage(Resource.get("silenceEffect"), x, y, 60, 45, null);
				}
			}

		}

	}



	public void setFocus(Player focus){
		if (_focusSpellButtons != null && _interfaceElements != null){
			for (int i = 0; i < _focusSpellButtons.length; i++){
				if (_focusSpellButtons[i] != null){
					_interfaceElements.remove(_focusSpellButtons[i]);
				}
			}
		}
		_focusSpellButtons = new Button[8];
		_focus = focus;
		for (int i = 0; i < _focus._spells.length; i++){
			String spell = _focus._spells[i];
			if (spell != null){
				Button b = new Button(this, spell, Resource.get(spell+"SpellIcon"));
				b.name = spell;
				b.namePositionFractionY = 0.8;
				_focusSpellButtons[i] = b;
				_interfaceElements.add(b);
			}
		}
		onResize();
	}



	@Override
	public void update(){
		super.update();

		_data.update();

		_view._camera = _focus._pos;
		_view._scale = (Math.min(_holder._h, _holder._w))/600.0;

		//_view._camera = new Vector(1000,-500);
		//_view._scale = .5;
		//System.out.println(_view._scale);

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

		String eventNetString = "";
		Vector target = _view.screenToGamePoint(new Vector(_xMouse, _yMouse));
		if (key == KeyEvent.VK_Q){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[0], target);
			} else {
				eventNetString = _lastTick + "Q\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_W){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[1], target);
			} else {
				eventNetString = _lastTick + "W\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_E){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[2], target);
			} else {
				eventNetString = _lastTick + "E\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_R){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[3], target);
			} else {
				eventNetString = _lastTick + "R\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_A){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[4], target);
			} else {
				eventNetString = _lastTick + "A\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_S){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[5], target);
			} else {
				eventNetString = _lastTick + "S\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_D){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[6], target);
			} else {
				eventNetString = _lastTick + "D\t" + target.toNet();
			}
		} else if (key == KeyEvent.VK_F){
			if (!_isClient) {
				_data.startCastingSpell(_focus, _focus._spells[7], target);
			} else {
				eventNetString = _lastTick + "F\t" + target.toNet();
			}
		}
		
		if (!_isClient) {
			if (key == 192) DEBUG = !DEBUG;
		}

		if (key == KeyEvent.VK_G){
			try {
				_data._units = new ArrayList<Unit>();
				_data.readInMap("Department of Secrets");
			} catch (Exception e1){};
		}
		if (key == KeyEvent.VK_P){
			System.out.println(_view.screenToGamePoint(new Vector(_xMouse, _yMouse)));
			
		_netOutputs.add(eventNetString);
		}

	}

	@Override
	public boolean onMousePressed(MouseEvent e){
		if (!super.onMousePressed(e)){
			Vector point = _view.screenToGamePoint(new Vector(e.getX(), e.getY()));
			if (_isClient) {
				String eventNetString = _lastTick + "\t";
				eventNetString = "CLICK\t" + point.toNet();
				_netOutputs.add(eventNetString);
			} else {
				if (!_focus._isRooted) {
					_focus._destination = point;
				}
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
		if (_focusSpellButtons != null){
			for (int i = 0; i < _focusSpellButtons.length; i++){
				if (_focusSpellButtons[i] != null){
					_focusSpellButtons[i].x = (i%4)*(_spellButtonSize+5) + 10;
					_focusSpellButtons[i].y = ((int)(i/4))*(_spellButtonSize+5)+_holder._h-_spellButtonSize*2-10;
					_focusSpellButtons[i].w = _focusSpellButtons[i].h = _spellButtonSize;
					_focusSpellButtons[i].recalculateText();
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
