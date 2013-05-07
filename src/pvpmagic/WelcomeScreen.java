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

	
	int chrisTime = 0;
	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, _holder._w, _holder._h);

		g.setFont(new Font("Helvetica", Font.PLAIN, 72));
		String s = "Kazam";
		int sWidth = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
		
		int disp = 7;
		g.drawString(s, (int)(_holder._w/2-sWidth/2)+(int)(Math.random()*disp), 150+(int)(Math.random()*disp));
		g.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
		g.drawString(s, (int)(_holder._w/2-sWidth/2)+(int)(Math.random()*disp), 150+(int)(Math.random()*disp));
		g.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
		g.drawString(s, (int)(_holder._w/2-sWidth/2)+(int)(Math.random()*disp), 150+(int)(Math.random()*disp));

		if (hoverElement != null){
			int bx = (int) hoverElement.x;
			int by = (int) hoverElement.y;
			int bw = 10;
			String message = "";
			if (hoverElement.id.equals("Andrew DiMarco")){
				bx += 30;
				bw = 175;
				message = "Engine Programmer";
			} else if (hoverElement.id.equals("Diego Morales")){
				bx += 25;
				bw = 183;
				message = "Content Programmer";
			} else if (hoverElement.id.equals("Chris Morris")){
				bx += 12;
				bw = 210;
				message = "Networking Programmer";
				chrisTime++;
				if (chrisTime > 20){
					bx += 50;
					bw = 115;
					message = "Just Kidding";
				}
				if (chrisTime > 40){
					bx += 10;
					bw = 70;
					message = "Retard";
				}
				
			} else if (hoverElement.id.equals("Miraj Shah")){
				bx += 80;
				bw = 70;
				message = "Retard";
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
				e.y = h/2-e.h/2+50;
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
