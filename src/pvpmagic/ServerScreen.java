package pvpmagic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import network.BadProtocolException;
import network.Coder;
import network.CommandComparator;
import network.InputServer;
import network.NetworkException;
import network.StateServer;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;
import screen.TransitionScreen.Transition;

public class ServerScreen extends Screen {

	String _serverIP = "Error getting IP Address";
	String _mapName = "";
	
	SetupScreen _settings;
	
	// shared network vars - setup in setup TODO
	boolean _isClient;
	boolean _isHost;
	HashMap<Integer,Unit> _staticMap;
	HashMap<Integer,Unit> _dynamicMap;
	int _lobbyVersion;
	ArrayList<Player> _playerList;
	
	// Server vars - set in setup TODO
	public PriorityBlockingQueue<String> _netInputs; // inputs used by server
	HashMap<Integer,Player> _playerMap;
	AtomicBoolean _running;
	AtomicBoolean _started;
	boolean _transitioned;
	int _getPort;
	int _sendPort;
	GameData _data;
	int _tick;
	StateServer _stateServer;
	InputServer _inputServer;

	public ServerScreen(ScreenHolder holder) {
		super(holder, "server");
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();

		Button main = new Button(this, "main", 150, 50, "Close Server", -1);
		_interfaceElements.add(main);


		Button start = new Button(this, "start", 150, 50, "Start Game", -1);
		_interfaceElements.add(start);

		try {
			_serverIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		
		// setup lobby network vars
		_isClient = false;
		_isHost = true;
		_staticMap = new HashMap<Integer,Unit>();
		_dynamicMap = new HashMap<Integer,Unit>();
		_lobbyVersion = 0;
		_playerList = new ArrayList<Player>();
		
		// server only vars
		_netInputs = new PriorityBlockingQueue<String>(32,new CommandComparator());
		_playerMap = new HashMap<Integer,Player>();
		_running = new AtomicBoolean(true);
		_started = new AtomicBoolean(false);
		_transitioned = false;
		_getPort = 9001;
		_sendPort = 9002;
		_tick = 0;
		
		// start servers
		try {
			_stateServer = new StateServer(_getPort, _running, _started);
			_inputServer = new InputServer(_sendPort, _netInputs, _running, _started);
			_stateServer.start();
			_inputServer.start();
		} catch (NetworkException e) {
			System.out.println(e.getMessage());
			_holder.transitionToScreen(Transition.FADE, "setup");
		}
		
		// setup client to connect with this server
		LobbyScreen lobby = (LobbyScreen) _holder.getScreen("lobby");
		lobby.setup();
		lobby._isHost = true;
		lobby._isClient = true;
		lobby._server = this;
		lobby._serverIP = "localhost"; // instead of call to lobby.getSettings()
		lobby._settings = _settings; // instead of call to lobby.getSettings()
		
		onResize();
	}

	public void getSettings(SetupScreen s){
		_mapName = s.getElement("selectedMap").name;
		_settings = s;
	}
	
	@Override
	public void update() {
		// update lobby
		if (_running.get() && !_started.get()) {
			String input = _netInputs.poll();
			// execute input and broadcast result
			if (input != null) {
				try {
					Coder.updateLobby(_playerList, input);
					_lobbyVersion++;
					String lobbyData = Coder.encodeLobby(_playerList, _mapName, _lobbyVersion);
					_stateServer.broadcast(lobbyData);
				} catch (BadProtocolException e) {
					//ignore
				}
			}
		} else if (_running.get() && !_transitioned) {
			// update lobby to final form and broadcast
			String input = _netInputs.poll();
			while (input != null) {
				try {
					Coder.updateLobby(_playerList, input);
				} catch (BadProtocolException e) {
					//ignore
				}
				input = _netInputs.poll();
			}
			_lobbyVersion++;
			String lobbyData = Coder.encodeLobby(_playerList, _mapName, _lobbyVersion);
			_stateServer.broadcast(lobbyData);
			
			// initialize game data
			_data = new GameData(_playerList,_isClient);
			_data.setup(_settings);
			for (Unit unit : _data._units) {
				_staticMap.put(unit._netID, unit);
			}
			
			// broadcast start and switch to client lobby
			_stateServer.broadcastStart();
			_transitioned = true;
			_holder.transitionToScreen(Transition.FADE, "lobby");
			
		} else if (_running.get()) {
			
		} else {
			_holder.transitionToScreen(Transition.FADE, "setup");
		}
	}
	
	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, _holder._w, _holder._h);

		g.setColor(Color.black);

		g.setFont(new Font("Helvetica", Font.PLAIN, 36));
		String s = "Waiting for players to join...";
		int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (int)(_holder._w/2-sWidth/2), 150);
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 24));
		s = "Server IP: "+_serverIP;
		sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (int)(_holder._w/2-sWidth/2), 250);

	}

	@Override
	protected void handleElementReleased(InterfaceElement e){
		if (e.id.equals("main")){
			_running.set(false);
		} else if (e.id.equals("start")) {
			_started.set(true);
			
			e.visible = false;
			e.enabled = false;
			/**
			_holder.getScreen("game").setup();
			((GameScreen)(_holder.getScreen("game"))).initializeGame(_settings);
			_holder.transitionToScreen(Transition.FADE, "game");
			
			e.visible = false;
			e.enabled = false;
			**/
		}
	}

	@Override
	protected void onResize() {
		for (InterfaceElement e : _interfaceElements){
			if (e.id.equals("main")){
				e.x = _holder._w/2 - e.w/2;
				e.y = _holder._h-e.h-50;
			} else if (e.id.equals("start")){
				e.x = _holder._w/2 - e.w/2;
				e.y = _holder._h-e.h-150;
			}
		}
	}

}
