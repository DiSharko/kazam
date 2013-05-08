package pvpmagic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;
import screen.TextInputLine;
import screen.TransitionScreen.Transition;

public class LobbyScreen extends Screen {

	boolean _connectedToServer = false;
	String _serverIP = "123.456.789.1337";
	SetupScreen _settings;

	public LobbyScreen(ScreenHolder holder) {
		super(holder, "lobby");
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();

		Button start = new Button(this, "disconnect", 150, 50, "Disconnect", -1);
		_interfaceElements.add(start);


		// connect to server...
		
		onResize();
	}
	
	public void getSettings(SetupScreen s){
		_settings = s;
		_serverIP = ((TextInputLine)s.getElement("ipAddress")).getText();
		
	}

	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, _holder._w, _holder._h);
		if (_connectedToServer){
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
			_holder.transitionToScreen(Transition.FADE, "setup");
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
