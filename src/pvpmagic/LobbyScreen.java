package pvpmagic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import network.BadProtocolException;
import network.ClientNetworkable;
import network.ClientNetworker;
import network.Coder;
import network.NetworkException;
import network.SyncedString;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;
import screen.TextInputLine;
import screen.TransitionScreen.Transition;

public class LobbyScreen extends Screen {

	String _serverIP = "123.456.789.1337";
	public SetupScreen _settings;
	
	// shared network vars - setup in setup TODO
	boolean _isClient;
	boolean _isHost;
	HashMap<Integer,Unit> _staticMap;
	HashMap<Integer,Unit> _dynamicMap;
	public int _lobbyVersion;
	public ArrayList<Player> _playerList;
	
	// Client vars - set in setup TODO
	ConcurrentLinkedQueue<String> _netOutputs; // outputs added to by client
	AtomicBoolean _connected;
	AtomicBoolean _started;
	SyncedString _gameData;
	SyncedString _lobbyData;
	int _statePort;
	int _inputPort;
	AtomicInteger _focusID;
	ClientNetworkable _networker;
	ServerScreen _server; //only set if isHost is true

	public LobbyScreen(ScreenHolder holder) {
		super(holder, "lobby");
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();

		Button start = new Button(this, "disconnect", 150, 50, "Disconnect", -1);
		_interfaceElements.add(start);


		// setup lobby network vars
		_isClient = true;
		_isHost = false;  // reset in ServerScreen if hosting client
		_staticMap = new HashMap<Integer,Unit>();
		_dynamicMap = new HashMap<Integer,Unit>();
		_lobbyVersion = -1;
		_playerList = new ArrayList<Player>();
		
		// client only vars
		_netOutputs = new ConcurrentLinkedQueue<String>();
		_connected = new AtomicBoolean(true);
		_started = new AtomicBoolean(false);
		_gameData = new SyncedString();
		_lobbyData = new SyncedString();
		_gameData.setData("-1");
		_lobbyData.setData("-1");
		_statePort = 9001;
		_inputPort = 9002;
		_focusID = new AtomicInteger(0);
		_networker = new ClientNetworker(_statePort, _inputPort, _lobbyData, _gameData, _netOutputs, _connected, _started, _focusID);
		
		onResize();
	}
	
	public void getSettings(SetupScreen s){
		_settings = s;
		_serverIP = ((TextInputLine)s.getElement("ipAddress")).getText();
		try {
			connect();
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			_networker.disconnect();
			_holder.transitionToScreen(Transition.FADE, "setup");
		} catch (NetworkException e) {
			System.out.println(e.getMessage());
			_networker.disconnect();
			_holder.transitionToScreen(Transition.FADE, "setup");
		}
	}
	
	public void connect() throws UnknownHostException, NetworkException {

		String playerName = ((TextInputLine)_settings.getElement("playerName")).getText();
		//String characterName = _settings.getElement("selectedCharacter").name;
		String characterName = "andrew";
		
		String[] spells = new String[8];
		for (int i = 0; i < 8; i++){
			spells[i] = _settings._spells[i].name;
		}

		String spellStr = "";
		for (int i = 0; i < spells.length; i++) {
			spellStr += spells[i] + " ";
		}
		String clientData = "static" +
				"\t" + characterName + 
				"\t" + playerName + 
				"\t" + spellStr.substring(0, spellStr.length() - 1);
		_networker.connect(_serverIP, clientData);
	}
	
	@Override
	public void update() {
		if (_connected.get() && !_started.get()) {
			//update lobby
			String lobbyData = _lobbyData.getData();
			try {
				Coder.decodeLobby(lobbyData, this);
			} catch (BadProtocolException e) {
				end();
			}
		} else if (_connected.get()){
			// get very last lobby version
			String lobbyData = _lobbyData.getData();
			try {
				Coder.decodeLobby(lobbyData, this);
			} catch (BadProtocolException e) {
				end();
			}
			
			// set up game screen
			GameScreen gameScreen = (GameScreen) _holder.getScreen("game");
			gameScreen.setup();

			// shared network vars - setup manually by lobby screen
			gameScreen._isClient = _isClient;
			gameScreen._isHost = _isHost;
			gameScreen._staticMap = _staticMap;
			gameScreen._dynamicMap = _dynamicMap;
			gameScreen._playerList = _playerList;
			
			// Client vars - set up manually by lobby screen
			gameScreen._netOutputs = _netOutputs; // outputs added to by client
			gameScreen._connected = _connected;
			gameScreen._started = _started;
			gameScreen._gameData = _gameData;
			gameScreen._lobbyData = _lobbyData;
			gameScreen._networker = _networker;
			gameScreen._focusID = _focusID;
			gameScreen._lastTick = -1;
			gameScreen._clientTick = -1;
			gameScreen._server = _server;
			
			// initialize game data and switch to game screen
			gameScreen.initializeGame(_settings);
			_holder.transitionToScreen(Transition.FADE, "game");
			
		} else { // disconnect
			end();
		}
	}
	
	// end gracefully with this function
	public void end() {
		_networker.disconnect();
		if (!_isHost) {
			_holder.transitionToScreen(Transition.FADE, "setup");
		} else {
			try {
				_server._inputServer.kill();
			} catch (IOException e) {}
			try {
				_server._stateServer.kill();
			} catch (IOException e) {}
			_holder.transitionToScreen(Transition.FADE, "setup");
		}
	}

	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, _holder._w, _holder._h);
		if (_connected.get()){
			g.setColor(Color.black);

			g.setFont(new Font("Helvetica", Font.PLAIN, 36));
			String s = "Waiting for server to start the game...";
			int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (int)(_holder._w/2-sWidth/2), 150);
		} else {
			g.setColor(Color.black);

			g.setFont(new Font("Helvetica", Font.PLAIN, 36));
			String s = "Connecting to "+_serverIP + "...";
			int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			int sHeight = (int)g.getFontMetrics().getStringBounds(s, g).getHeight();
			g.drawString(s, (int)(_holder._w/2-sWidth/2), (int)(_holder._h/2-sHeight/2));
		}
	}

	@Override
	protected void handleElementReleased(InterfaceElement e){
		if (e.id.equals("disconnect")){
			end();
		}
	}

	@Override
	protected void onResize() {
		for (InterfaceElement e : _interfaceElements){
			if (e.id.equals("disconnect")){
				e.x = _holder._w/2 - e.w/2;
				e.y = _holder._h-e.h-50;
			}
		}
	}

}
