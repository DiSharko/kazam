package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import pvpmagic.Vector;

public class MMessageScreen extends Screen {

	int dialogW = 0;
	int dialogH = 0;

	int _w = 400;
	int _h = 400;
	
	String title = "";
	String message = "";
	
	public MMessageScreen(ScreenHolder holder) {
		super(holder, "message");
		setup();
	}
	
	public void setMessage(String _title, String _message){
		title = _title;
		message = _message;
		dialogW = 200;
		dialogH = Math.min(Math.max(180, message.length()*3), _h);
		setup();
	}
	
	public void setMessage(Vector _size, String _title, String _message){
		title = _title;
		message = _message;
		
		dialogW = (int)_size.x;
		dialogH = (int)_size.y;	
		setup();
	}

	@Override
	public void setup() {
		_allowShowBehind = true;
		
		_interfaceElements = new ArrayList<InterfaceElement>();
		Button ok = new Button(this, "ok", 80, 40, "Ok", KeyEvent.VK_ENTER);
		_interfaceElements.add(ok);
		
		TextBlock info = new TextBlock("message", dialogW-20, dialogH-80-10, message, 18);
		_interfaceElements.add(info);
		
		onResize();
	}


	@Override
	protected void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillRoundRect((int)_w/2 - dialogW/2,(int)_h/2-dialogH/2, dialogW, dialogH, 10, 10);
		g.setColor(Color.black);
		g.drawRoundRect((int)_w/2 - dialogW/2,(int)_h/2-dialogH/2, dialogW, dialogH, 10, 10);

		g.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		String string = title;
		g.drawString(string, _w/2-(int)g.getFontMetrics().getStringBounds(string, g).getWidth()/2, _h/2-dialogH/2-(int)g.getFontMetrics().getStringBounds(string, g).getHeight()/2+50);
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		super.onKeyPressed(e);
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE){
			_holder.putScreenAway();
		}

	}

	@Override
	protected void onKeyReleased(KeyEvent e) {

	}

	@Override
	protected void handleElementReleased(InterfaceElement e) {
		if (e.id.equals("ok")) _holder.putScreenAway();
	}

	@Override
	public void onResize(){
		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				if (e.id.equals("ok")){
					e.x = _w/2-e.w/2;
					e.y = _h/2+dialogH/2-e.h*3/2;
				} else if (e.id.equals("message")){
					e.x = _w/2-e.w/2;
					e.y = _h/2-dialogH/2+50;
				}
			}
		}
	}

}
