package pvpmagic;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import screen.Button;
import screen.Screen;
import screen.ScreenHolder;
import screen.InterfaceElement;
import screen.TransitionScreen.Transition;

public class EndScreen extends Screen {

	boolean _winner = false;
	
	public EndScreen(ScreenHolder holder) {
		super(holder, "end");
		
		Button main = new Button(this, "main", 200, 50, "Return to Main", -1);
		_interfaceElements = new ArrayList<InterfaceElement>();
		_interfaceElements.add(main);
	}

	@Override
	public void setup() {
		
	}

	@Override
	protected void draw(Graphics2D g) {
		if (_winner){
			g.setFont(new Font("Times New Roman", Font.PLAIN, 36));
			
			String s = "Victory!";
			int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, _holder._w/2-sWidth/2, 100);
		} else {
			g.setFont(new Font("Times New Roman", Font.PLAIN, 36));
			
			String s = "Defeat...";
			int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, _holder._w/2-sWidth/2, 100);
		}
	}

	@Override
	protected void handleElementReleased(InterfaceElement e){
		if (e.id.equals("main")){
			_holder.transitionToScreen(Transition.FADE, "welcome");
		}
	}
	
	@Override
	protected void onResize() {
		if (_interfaceElements != null){
			for (InterfaceElement e : _interfaceElements){
				if (e.id.equals("main")){
					e.x = _holder._w/2-e.w/2;
					e.y = _holder._h/2;
				}
			}
		}
	}

}
