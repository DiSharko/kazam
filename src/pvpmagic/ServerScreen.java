package pvpmagic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;
import screen.TransitionScreen.Transition;

public class ServerScreen extends Screen {

	String _serverIP = "Error getting IP Address";
	String _mapName = "";
	
	SetupScreen _settings;

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
		
		onResize();
	}

	public void getSettings(SetupScreen s){
		_mapName = s.getElement("selectedMap").name;
		_settings = s;
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
			_holder.transitionToScreen(Transition.FADE, "setup");
		} else if (e.id.equals("start")){
			_holder.getScreen("game").setup();
			((GameScreen)(_holder.getScreen("game"))).initializeGame(_settings);
			_holder.transitionToScreen(Transition.FADE, "game");
			
			e.visible = false;
			e.enabled = false;
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
