package pvpmagic;

import java.awt.Graphics2D;
import java.util.ArrayList;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;

public class LobbyScreen extends Screen {

	public LobbyScreen(ScreenHolder holder) {
		super(holder, "lobby");
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();
		
		Button start = new Button(this, "disconnect", 150, 50, "Disconnect", -1);
		_interfaceElements.add(start);
	
	}
	
	@Override
	protected void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleElementReleased(InterfaceElement e){
		
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
