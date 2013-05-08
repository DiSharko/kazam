package pvpmagic;

import java.awt.Graphics2D;
import java.util.ArrayList;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;

public class ServerScreen extends Screen {

	public ServerScreen(ScreenHolder holder) {
		super(holder, "server");
		
		setup();
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();
		
		Button start = new Button(this, "start", 200, 100, "Start Game", -1);
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
			if (e.id.equals("start")){
				e.x = _holder._w/2 - e.w/2;
				e.y = 400;
			}
		}
	}

}
