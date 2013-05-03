package pvpmagic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import screen.Button;
import screen.ChooserScreen;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;
import screen.TextInputLine;
import screen.TransitionScreen.Transition;

public class SetupScreen extends Screen {


	ArrayList<InterfaceElement> _tabs;
	ArrayList<InterfaceElement> _joinGameElements;
	ArrayList<InterfaceElement> _hostGameElements;
	ArrayList<InterfaceElement> _serverElements;

	Button _currentTab = null;

	Color _tabColor = Color.GRAY;
	Color _bgColor = Color.LIGHT_GRAY;

	int _tabStartX = 80;
	double _tabWidth = 200;
	int _tabHeight = 95;

	Button[] _spells;

	ChooserScreen _characterChooserScreen;
	ChooserScreen _spellChooserScreen;
	ChooserScreen _spellAllChooserScreen;

	ChooserScreen _gameTypeChooserScreen;
	ChooserScreen _mapChooserScreen;

	public SetupScreen(ScreenHolder holder) {
		super(holder, "setup");
		setup();
	}

	@Override
	public void switchInto(){
		_holder.setFPS(25);
	}

	@Override
	public void setup() {
		_tabHeight = Resource._borderHeight+55;

		_interfaceElements = new ArrayList<InterfaceElement>();
		_joinGameElements = new ArrayList<InterfaceElement>();
		_hostGameElements = new ArrayList<InterfaceElement>();
		_serverElements = new ArrayList<InterfaceElement>();
		_tabs = new ArrayList<InterfaceElement>();

		Button joinTab = new Button(this, "joinTab", 200, 50, "Join Game", -1).setRoundness(16);
		_tabs.add(joinTab);
		Button hostTab = new Button(this, "hostTab", 200, 50, "Host Game", -1).setRoundness(16);
		_tabs.add(hostTab);
		Button serverTab = new Button(this, "serverTab", 200, 50, "Dedicated Server", -1).setRoundness(16);
		_tabs.add(serverTab);
		Button home = new Button(this, "home", 50, 30, "Home", KeyEvent.VK_ESCAPE);
		_tabs.add(home);
		Button play = new Button(this, "play", 50, 30, "Play", -1);
		_tabs.add(play);


		// PLAYER NAME
		TextInputLine playerName = new TextInputLine(this, "playerName", 20);
		playerName.h = 20;
		_joinGameElements.add(playerName);
		_hostGameElements.add(playerName);

		// CHARACTER
		Button characterChooser = new Button(this, "characterChooser", 200, 50, "Choose New Character", -1);
		characterChooser.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_joinGameElements.add(characterChooser);
		_hostGameElements.add(characterChooser);

		Button selectedCharacter = new Button(this, "selectedCharacter", 165, 165);
		selectedCharacter.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_joinGameElements.add(selectedCharacter);
		_hostGameElements.add(selectedCharacter);

		_characterChooserScreen = new ChooserScreen(_holder, "characterChooser");
		_characterChooserScreen._caller = this;
		for (int i = 0; i < Resource._characters.size(); i++){
			String name = Resource._characters.get(i);
			_characterChooserScreen._choices.addButton(new Button(this, name, 100, 100, name, -1));
		}

		// SPELLS
		_spellChooserScreen = new ChooserScreen(_holder, "spellChooser");
		_spellChooserScreen._caller = this;
		for (int i = 0; i < Resource._spells.size(); i++){
			String name = Resource._spells.get(i);
			_spellChooserScreen._choices.addButton(new Button(null, name, 100, 100, name, -1));
		}

		_spellAllChooserScreen = new ChooserScreen(_holder, "spellChooser");
		_spellAllChooserScreen._caller = this;
		_spellAllChooserScreen._totalToChoose = 8;
		_spellAllChooserScreen._numberChoices = true;
		_spellAllChooserScreen._choiceNumberings = new String[]{"Q", "W", "E", "R", "A", "S", "D", "F"};
		for (int i = 0; i < Resource._spells.size(); i++){
			String name = Resource._spells.get(i);
			_spellAllChooserScreen._choices.addButton(new Button(null, name, 100, 100, name, -1));
		}

		_spells = new Button[8];
		for (int i = 0; i < _spells.length; i++){
			Button b = new Button(this, "spell_"+i, 80, 80);
			b.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
			_spells[i] = b;
			_joinGameElements.add(b);
			_hostGameElements.add(b);
		}

		Button spellChooser = new Button(this, "spellChooser", 200, 50, "Choose All Spells", -1);
		spellChooser.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_joinGameElements.add(spellChooser);
		_hostGameElements.add(spellChooser);

		// GAME TYPES
		Button gameTypeChooser = new Button(this, "gameTypeChooser", 200, 50, "Choose Game Type", -1);
		gameTypeChooser.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_hostGameElements.add(gameTypeChooser);
		_serverElements.add(gameTypeChooser);

		Button selectedGameType = new Button(this, "selectedGameType", 165, 165);
		selectedGameType.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_hostGameElements.add(selectedGameType);
		_serverElements.add(selectedGameType);

		_gameTypeChooserScreen = new ChooserScreen(_holder, "gameTypeChooser");
		_gameTypeChooserScreen._caller = this;
		for (int i = 0; i < Resource._gameTypes.size(); i++){
			String name = Resource._gameTypes.get(i);
			_gameTypeChooserScreen._choices.addButton(new Button(null, name, 100, 100, name, -1));
		}


		// MAPS
		Button mapChooser = new Button(this, "mapChooser", 200, 50, "Choose Map", -1);
		mapChooser.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_hostGameElements.add(mapChooser);
		_serverElements.add(mapChooser);

		Button selectedMap = new Button(this, "selectedMap", 165, 165);
		selectedMap.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		_hostGameElements.add(selectedMap);
		_serverElements.add(selectedMap);

		_mapChooserScreen = new ChooserScreen(_holder, "mapChooser");
		_mapChooserScreen._caller = this;
		for (int i = 0; i < Resource._maps.size(); i++){
			String name = Resource._maps.get(i);
			_mapChooserScreen._choices.addButton(new Button(null, name, 100, 100, name, -1));
		}



		displayTab(hostTab);
	}


	protected void displayTab(Button tab){
		for (InterfaceElement i : _interfaceElements) i.visible = false;
		_interfaceElements.clear();
		_interfaceElements.addAll(_tabs);
		for (InterfaceElement t : _tabs){
			t.setEnabled(true);
			t.setColors(_bgColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		}
		tab.setColors(_tabColor, new Color(0.5f,0.5f,0.5f,0.2f), new Color(0.3f,0.3f,0.3f,0.3f), null);
		tab.setEnabled(false);
		_currentTab = tab;

		if (_currentTab.id.equals("joinTab")){
			_interfaceElements.addAll(_joinGameElements);
		} else if (_currentTab.id.equals("hostTab")){
			_interfaceElements.addAll(_hostGameElements);
		} else if (_currentTab.id.equals("serverTab")){
			_interfaceElements.addAll(_serverElements);
		}

		for (InterfaceElement i : _interfaceElements) i.visible = true;

		onResize();
	}

	@Override
	protected void draw(Graphics2D g) {
		int w = _holder._w;
		int h = _holder._h;

		g.setColor(_tabColor);
		g.fillRect(0, 0, w, h);

		g.setColor(_bgColor);
		g.fillRect(0, 0, w, _tabHeight);

	}

	@Override
	protected void drawOnTop(Graphics2D g){
		int w = _holder._w;

		g.setColor(_tabColor);
		g.fillRect(0, _tabHeight, w, 20);

		char[] spellKeys = {'Q','W','E','R','A','S','D','F'};
		g.setColor(Color.black);
		for (int i = 0; i < _spells.length && _spells[i].visible; i++){
			g.drawString(""+spellKeys[i], (int)(_spells[i].x+_spells[i].w-20), (int)(_spells[i].y+_spells[i].h-5));
		}

		if (_currentTab.id.equals("joinTab") || _currentTab.id.equals("hostTab")){
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			g.drawString("Choose your name:", 195, 135);
		}
	}

	@Override
	public void handleElementReleased(InterfaceElement e){
		if (e.id.equals("joinTab") || e.id.equals("hostTab") || e.id.equals("serverTab")){
			displayTab((Button)e);
		} else if (e.id.equals("home")){
			_holder.transitionToScreen(Transition.FADE,"welcome");
		} else if (e.id.equals("characterChooser") || e.id.equals("selectedCharacter")){
			_holder.showChooser(_characterChooserScreen);
		} else if (e.id.startsWith("spell_")){
			_spellChooserScreen._callingElement = e;
			_holder.showChooser(_spellChooserScreen);
		} else if (e.id.equals("spellChooser")){
			_spellAllChooserScreen._callingElement = e;
			_holder.showChooser(_spellAllChooserScreen);
		} else if (e.id.equals("gameTypeChooser") || e.id.equals("selectedGameType")){
			_holder.showChooser(_gameTypeChooserScreen);
		} else if (e.id.equals("mapChooser") || e.id.equals("selectedMap")){
			_holder.showChooser(_mapChooserScreen);
		} else if (e.id.equals("play")){
			//			if (getElement("selectedMap").name == null) {
			//				JOptionPane.showMessageDialog(null, "Please choose a map!", "PvP Magic", JOptionPane.ERROR_MESSAGE);
			//			} else {
			_holder.transitionToScreen(Transition.FADE, "game");
			_holder.getScreen("game").setup();
			((GameScreen)(_holder.getScreen("game"))).configureGame(this);
			//			}
		}
	}

	@Override
	public void returnChoice(ChooserScreen c){
		if (c._name.equals("characterChooser")){
			if (c._chosen != null) getElement("selectedCharacter").name = c._chosen.name;
		} else if (c._name.equals("spellChooser")){
			if (c._callingElement != null && c._callingElement.id.startsWith("spell_")){ // for choosing 1 spell
				if (c._chosen != null){
					if (c._callingElement.name != null && !c._callingElement.equals("")){
						for (Button b : c._choices.buttons){
							if (b.name.equals(c._callingElement.name)){
								b.enabled = true;
							}
						}
					}
					c._callingElement.name = c._chosen.name;
					c._chosen.enabled = false;
				}
				_spellChooserScreen._callingElement = null;
			} else if (c._callingElement.id.equals("spellChooser")){ // for choosing all spells
				for (Button b : _spellChooserScreen._choices.buttons) b.enabled = true;
				for (int i = 0; i < c._chosens.size(); i++){
					_spells[i].name = c._chosens.get(i).name;
					for (Button b : _spellChooserScreen._choices.buttons){
						if (_spells[i].name.equals(b.name)){
							b.enabled = false;
						}
					}
				}
			}
		} else if (c._name.equals("gameTypeChooser")){
			if (c._chosen != null) getElement("selectedGameType").name = c._chosen.name;
		} else if (c._name.equals("mapChooser")){
			if (c._chosen != null) getElement("selectedMap").name = c._chosen.name;
		}
	}

	@Override
	public void update(){
		onResize();
	}

	@Override
	protected void onResize() {
		for (InterfaceElement e : _interfaceElements){
			_tabWidth = Math.min(200, Math.floor(_holder._w/3.85));
			if (e.id.equals("joinTab")){
				e.w = _tabWidth;
				e.x = _tabStartX;
				e.y = _tabHeight-e.h+5;

			} else if (e.id.equals("hostTab")){
				e.w = _tabWidth;
				e.x = _tabStartX+e.w;
				e.y = _tabHeight-e.h+5;

			} else if (e.id.equals("serverTab")){
				e.w = _tabWidth;
				e.x = _tabStartX+e.w*2;
				e.y = _tabHeight-e.h+5;

			} else if (e.id.equals("home")){
				e.x = 10;
				e.y = _tabHeight-40;
			} else if (e.id.equals("play")){
				e.x = _holder._w - e.w - 15;
				e.y = _tabHeight-40;
			}

			// Setup for each different tab layout:
			if (_currentTab.id.equals("joinTab") || _currentTab.id.equals("hostTab")){
				if (e.id.equals("selectedCharacter")){
					e.x = 55;
					e.y = _tabHeight + 75;
				} else if (e.id.equals("characterChooser")){
					e.x = 37;
					e.y = _tabHeight + 250;
				} else if (e.id.equals("spellChooser")){
					e.x = 420;
					e.y = _tabHeight + 250;
				}
				if (e.id.equals("playerName")){
					e.x = 350;
					e.y = _tabHeight + 25;
					e.h = 20;
				}
			}

			if (_currentTab.id.equals("hostTab")){
				if (e.id.equals("selectedGameType")){
					e.x = 55;
					e.y = _tabHeight + 350;
				} else if (e.id.equals("gameTypeChooser")){
					e.x = 37;
					e.y = _tabHeight + 525;
				} else if (e.id.equals("selectedMap")){
					e.x = 440;
					e.y = _tabHeight + 350;
				} else if (e.id.equals("mapChooser")){
					e.x = 423;
					e.y = _tabHeight + 525;
				}
			}


			if (_currentTab.id.equals("serverTab")){
				if (e.id.equals("selectedGameType")){
					e.x = 55;
					e.y = _tabHeight + 30;
				} else if (e.id.equals("gameTypeChooser")){
					e.x = 37;
					e.y = _tabHeight + 205;
				} else if (e.id.equals("selectedMap")){
					e.x = 430;
					e.y = _tabHeight + 30;
				} else if (e.id.equals("mapChooser")){
					e.x = 412;
					e.y = _tabHeight + 205;
				}
			}
		}
		for (int i = 0; i < _spells.length; i++){
			_spells[i].x = 85*(i%4) + 350;
			_spells[i].y = 85*(i/4) + _tabHeight + 75;
		}
	}

}
