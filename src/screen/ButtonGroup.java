package screen;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ButtonGroup extends InterfaceElement {


	public float defaultButtonSize;
	public float defaultButtonGap;
	public int buttonsPerRow;

	public int minTopBuffer = 100;
	public int minBottomBuffer = 200;
	public int minLeftBuffer = 25;
	public int minRightBuffer = 25;

	public int leftBuffer = 0;
	public int rightBuffer = 0;


	public ArrayList<Button> buttons;
	Screen screen;

	public ButtonGroup(Screen _screen, String _id, float _defaultButtonSize, float _defaultButtonGap,  int _left, int _right, int _top, int _bottom){
		screen = _screen;
		type = "buttonGroup";
		id = _id;

		defaultButtonSize = _defaultButtonSize;
		defaultButtonGap = _defaultButtonGap;

		minTopBuffer = _top;
		minBottomBuffer = _bottom;
		minLeftBuffer = _left;
		minRightBuffer = _right;
		
		buttons = new ArrayList<Button>();
	}

	public void addButton(Button b){
		b.screen = screen;
		buttons.add(b);
		reposition();
	}

	@Override
	public void draw(Graphics2D g) {
		for (int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			b.draw(g);
		}
	}

	@Override
	public void update(){
		
	}


	public void reposition(){
		int height = 100000000;
		int xCentering = 0;
		float buttonSize = defaultButtonSize;
		float buttonGap = defaultButtonGap;
		while (height > screen._holder._h){
			buttonsPerRow = (int)Math.floor((screen._holder._w-minLeftBuffer-minRightBuffer)/(buttonSize+buttonGap));
			xCentering = (int)((screen._holder._w-minLeftBuffer-minRightBuffer)-buttonsPerRow*(buttonSize+buttonGap))/2;
			height = (int)(Math.ceil((float)(buttons.size())/buttonsPerRow)*(buttonSize+buttonGap)+minTopBuffer+minBottomBuffer);
			if (height > screen._holder._h) buttonSize--; buttonGap-=0.03f;
		}
		leftBuffer = minLeftBuffer + xCentering;
		rightBuffer = minRightBuffer + xCentering;

		int lastRowCentering = (int)((screen._holder._w-minLeftBuffer-minRightBuffer)-(buttons.size()%buttonsPerRow)*(buttonSize+buttonGap))/2;
		int lastRow = (int)(buttons.size()/buttonsPerRow);
		
		x = 0;
		y = 0;
		w = 0;
		h = 0;
		
		int r = 0;
		int c = 0;
		for (int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			
			b.x = (buttonSize+buttonGap)*c+minLeftBuffer+(r == lastRow ? lastRowCentering : xCentering);
			b.y = minTopBuffer+(buttonSize+buttonGap)*r;
			b.w = buttonSize;
			b.h = buttonSize;
			b.recalculateText();
			
			c++;
			if (c >= buttonsPerRow){
				c = 0;
				r++;
			}
			
			if (i == 0 || b.x < x){
				x = b.x;
			}
			if (i == 0 || b.y < y){
				y = b.y;
			}
			if (i == 0 || b.x+b.w > x + w){
				w = b.x+b.w-x;
			}
			if (i == 0 || b.y+b.h > y + h){
				h = b.y+b.h-y;
			}
		}
	}



	@Override
	public void handleMousePressed(MouseEvent event){
		for (int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			if (!b.enabled || !b.visible) continue;
			if (Function.within(b.x, b.y, b.w, b.h, event.getX(), event.getY())){
				b.selected = true;
				b.handleMousePressed(event);
			}
		}
	}

	@Override
	public void handleMouseReleased(MouseEvent event){
		for (int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			if (!b.enabled || !b.visible) continue;
			b.handleMouseReleased(event);
		}
	}


	@Override
	public void handleMouseMoved(MouseEvent event){
		for (int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			if (!b.enabled || !b.visible) continue;
			b.handleMouseMoved(event);
			if (Function.within(b.x, b.y, b.w, b.h, event.getX(), event.getY())){ b.mousedOver = true;}
			else b.mousedOver = false;
		}
	}

	@Override
	public void handleMouseDragged(MouseEvent event){
		for (int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			if (!b.enabled || !b.visible) continue;
			b.handleMouseDragged(event);
		}
	}
}
