package pvpmagic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import screen.Button;
import screen.InterfaceElement;
import screen.Screen;
import screen.ScreenHolder;
import screen.TransitionScreen.Transition;

public class WelcomeScreen extends Screen {

	public WelcomeScreen(ScreenHolder holder) {
		super(holder, "welcome");
		setup();
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();
		
		Button b = new Button(this, "play", 200, 100, "Play", KeyEvent.VK_P);
		_interfaceElements.add(b);
		
		onResize();
	}

	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, _holder._w, _holder._h);
	}
	
	@Override
	protected void handleElementReleased(InterfaceElement e){
		if (e.id.equals("play")){
			_holder.transitionToScreen(Transition.FADE, "setup");
		}
	}
	
	@Override
	protected void onResize(){
		int w = _holder._w;
		int h = _holder._h;
		for (InterfaceElement e : _interfaceElements){
			if (e.id.equals("play")){
				e.x = w/2-e.w/2;
				e.y = h/2-e.h/2;
			}
		}
	}

}
