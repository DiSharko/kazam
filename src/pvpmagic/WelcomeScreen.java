package pvpmagic;

import java.awt.Color;
import java.awt.Font;
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
	public void switchInto(){
		_holder.setFPS(25);
	}

	@Override
	public void setup() {
		_interfaceElements = new ArrayList<InterfaceElement>();

		Button b = new Button(this, "play", 200, 100, "Play", KeyEvent.VK_P);
		b.defaultFontSize = 24;
		_interfaceElements.add(b);

		String[] names = {"Andrew DiMarco", "Diego Morales", "Chris Morris", "Miraj Shah"};
		for (String n : names){
			b = new Button(this, n, 220, 40, n, -1);
			b.color = null;
			b.borderColor = null;
			b.defaultFontSize = 24;
			b.enabled = false;
			_interfaceElements.add(b);
		}

		onResize();
	}

	
	@Override
	protected void draw(Graphics2D g) {
		onResize();
		g.setColor(Color.gray);
		g.fillRect(0, 0, _holder._w, _holder._h);

//		g.setFont(new Font("Helvetica", Font.PLAIN, 72));
//		String s = "Kazam";
//		int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
//		
//		g.setColor(Color.black);
//		g.drawString(s, (int)(_holder._w/2-sWidth/2), 150);
		g.drawImage(Resource.get("splashlogo"), (int)(_holder._w/2-761/2), 90, null);

		if (hoverElement != null){
			int bx = (int) hoverElement.x;
			int by = (int) hoverElement.y;
			int bw = 10;
			String message = "";
			if (hoverElement.id.equals("Andrew DiMarco")){
				bx += 30;
				bw = 175;
				message = "Engine Programmer";
				g.drawImage(Resource.get("andrew"), bx, by-250, null);
			
			} else if (hoverElement.id.equals("Diego Morales")){
				bx += 25;
				bw = 183;
				message = "Content Programmer";
				g.drawImage(Resource.get("diego"), bx, by-250, null);
			
			} else if (hoverElement.id.equals("Chris Morris")){
				bx += 12;
				bw = 210;
				message = "Networking Programmer";
				g.drawImage(Resource.get("chris"), bx, by-250, null);

			} else if (hoverElement.id.equals("Miraj Shah")){
				bx += 25;
				bw = 182;
				message = "Content Programmer";
				g.drawImage(Resource.get("miraj"), bx, by-250, null);

			}
			if (!message.equals("")){
				int h = 30;
				int r = 40;
				g.setColor(Color.lightGray);
				g.fillRoundRect(bx-5, by-h-5, bw, h, r, r);
				g.setColor(Color.black);
				g.setFont(new Font("Helvetica", Font.PLAIN, 18));
				g.drawString(message, bx+3, by-h/2+1);
			}
		}

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
				e.y = h/2-e.h/2-10;
			}
			if (e.id.equals("Andrew DiMarco")){
				e.x = _holder._w*.12;
				e.y = _holder._h-50;
			} else if (e.id.equals("Diego Morales")){
				e.x = _holder._w*.32;
				e.y = _holder._h-50;
			} else if (e.id.equals("Chris Morris")){
				e.x = _holder._w*.52;
				e.y = _holder._h-50;
			} else if (e.id.equals("Miraj Shah")){
				e.x = _holder._w*.72;
				e.y = _holder._h-50;
			}
		}
	}

}
