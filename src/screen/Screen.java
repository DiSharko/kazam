package screen;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;



public abstract class Screen {
	public String _id = "";

	protected ScreenHolder _holder;
	
	// Constructor
	public Screen(ScreenHolder holder, String id){
		_holder = holder;
		_keysDown = new HashMap<Integer, Integer>();
		_id = id;
	}

	boolean _delete = false;

	// Selector
	InterfaceElement[][] _selectorGrid;
	int _selectorX = 0;
	int _selectorY = 0;

	// Transparency
	protected boolean _allowShowBehind = false;
	protected boolean _allowUpdateBehind = false;
	protected boolean _allowInputBehind = false;

	protected int _xMouse = 0;
	protected int _yMouse = 0;

	// Buttons
	public ArrayList<InterfaceElement> _interfaceElements;
	public InterfaceElement getElement(String _id){
		for (int i = 0; i < _interfaceElements.size(); i++){
			if (_interfaceElements.get(i).id.equals(_id)){
				return _interfaceElements.get(i);
			}
		}

		return null;
	}

	// TextInputLines
	InterfaceElement keyboardFocus;


	public abstract void setup();
	
	public void switchInto(){};


	// Updates
	public void update(){
		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				if (e.delete){
					_interfaceElements.remove(i);
					i--;
				} else if (e.visible && e.enabled) e.update();
			}
		}
	}




	public final void drawExternal(Graphics2D g){
		draw(g);
		if (_interfaceElements != null){
			for (int i = _interfaceElements.size()-1; i >= 0; i--){
				g.setStroke(new BasicStroke(1));
				_interfaceElements.get(i).draw(g);
			}
		}
		drawOnTop(g);
	}

	protected abstract void draw(Graphics2D g);
	protected void drawOnTop(Graphics2D g){};
	//	protected abstract void onKeyTyped(KeyEvent e);

	
	HashMap<Integer, Integer> _keysDown;
	protected void onKeyPressed(KeyEvent event){
		_keysDown.put(event.getKeyCode(), (_keysDown.containsKey(event.getKeyCode()) ? _keysDown.get(event.getKeyCode()) : 0) );
		if (keyboardFocus != null){
			keyboardFocus.handleKeyPressed(event);
			return;
		}
		for (int i = 0; _interfaceElements != null && i < _interfaceElements.size(); i++){
			if (_interfaceElements.get(i).type.equals("Button") && ((Button) _interfaceElements.get(i)).activationKeycode == event.getKeyCode()){
				handleElementReleased(_interfaceElements.get(i));
				return;
			}
		}
		if (_selectorGrid != null){
			if (event.getKeyCode() == KeyEvent.VK_LEFT){
				_selectorX--;
			}
			if (event.getKeyCode() == KeyEvent.VK_RIGHT){
				_selectorX++;
			}
			if (event.getKeyCode() == KeyEvent.VK_UP){
				_selectorY--;
			}
			if (event.getKeyCode() == KeyEvent.VK_DOWN){
				_selectorY++;
			}
			if (_selectorX < 0) _selectorX = _selectorGrid.length-1;
			if (_selectorX >= _selectorGrid.length) _selectorX = 0;
			if (_selectorY < 0) _selectorY = _selectorGrid[0].length-1;
			if (_selectorY >= _selectorGrid[0].length) _selectorY = 0;

			if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_Z){
				handleElementReleased(_selectorGrid[_selectorX][_selectorY]);
			}

			return;
		}
	}

	protected void onKeyReleased(KeyEvent event){
		_keysDown.remove(event.getKeyCode());
	}

	//	protected abstract void onMouseClicked(MouseEvent e);

	protected boolean onMousePressed(MouseEvent event){
		_xMouse = event.getX();
		_yMouse = event.getY();
		boolean found = false;
		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				if (!e.enabled || !e.visible) continue;
				if (!found && Function.within(e.x, e.y, e.w, e.h, _xMouse, _yMouse)){
					e.selected = true;
					e.handleMousePressed(event);
					found = true;
					if (!e.selected && keyboardFocus != null) keyboardFocus.onReleased(true);
					if (e.type.equals("TextInputLine"))	keyboardFocus = e;

				} else {
					e.selected = false;
				}
			}

			if (!found && keyboardFocus != null) keyboardFocus.onReleased(true);
		}
		return found;
	}


	boolean dragging = false;
	int previousDragX = 0;			int previousDragY = 0;
	protected int deltaDragX = 0;	protected int deltaDragY = 0;
	protected void onMouseDragged(MouseEvent event){
		_xMouse = event.getX();
		_yMouse = event.getY();
		
		if (dragging){
			deltaDragX = _xMouse - previousDragX;
			deltaDragY = _xMouse - previousDragY;
		} else {
			dragging = true;
			deltaDragX = 0;
			deltaDragY = 0;
		}
		previousDragX = _xMouse;
		previousDragY = _xMouse;

		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				if (!e.enabled) continue;
				e.handleMouseDragged(event);
			}
		}
	}

	protected void onMouseReleased(MouseEvent event){
		dragging = false;
		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				if (!e.enabled) continue;
				e.handleMouseReleased(event);
			}
		}
	}

	protected void onMouseMoved(MouseEvent event){
		_xMouse = event.getX();
		_yMouse = event.getY();
		boolean found = false;

		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				e.handleMouseMoved(event);
				if (!e.enabled || !e.visible) continue;
				if (!found && Function.within(e.x, e.y, e.w, e.h, _xMouse, _yMouse)){
					e.mousedOver = true; found = true;
					if (e.type.equals("Button")){
						_holder._window.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else e.mousedOver = false;
			}
		}
	}

	protected void onMouseWheelMoved(MouseWheelEvent event){
		if (_interfaceElements != null){
			for (int i = 0; i < _interfaceElements.size(); i++){
				InterfaceElement e = _interfaceElements.get(i);
				if (!e.enabled) continue;
				e.handleMouseWheelMoved(event);
			}
		}
	}


	public void nextTextBox(){
		if (_interfaceElements != null && _interfaceElements.size() > 1){
			for (int i = 0; i < _interfaceElements.size(); i++){
				if (_interfaceElements.get(i).selected){
					_interfaceElements.get(i).onReleased(true);
					_interfaceElements.get(i).selected = false;
					if (i < _interfaceElements.size()-1){
						_interfaceElements.get(i+1).selected = true;
						keyboardFocus = _interfaceElements.get(i+1);
					} else {
						_interfaceElements.get(0).selected = true;
						keyboardFocus = _interfaceElements.get(0);
					}
					if (!keyboardFocus.visible || !keyboardFocus.type.equals("TextInputLine")) nextTextBox();
					break;
				}
			}
		}
	}
	public void previousTextBox(){
		if (_interfaceElements != null && _interfaceElements.size() > 1){
			for (int i = 0; i < _interfaceElements.size(); i++){
				if (_interfaceElements.get(i).selected){
					_interfaceElements.get(i).onReleased(true);
					_interfaceElements.get(i).selected = false;
					if (i > 0){
						_interfaceElements.get(i-1).selected = true;
						keyboardFocus = _interfaceElements.get(i-1);
					} else {
						_interfaceElements.get(_interfaceElements.size()-1).selected = true;
						keyboardFocus = _interfaceElements.get(_interfaceElements.size()-1);
					}
					if (!keyboardFocus.visible || !keyboardFocus.type.equals("TextInputLine")) previousTextBox();
					break;
				}
			}
		}
	}


	protected void handleElementReleased(InterfaceElement e){
		if (_selectorGrid != null){
			for (int i = 0; i < _selectorGrid.length; i++){
				for (int j = 0; j < _selectorGrid[i].length; j++){
					if (_selectorGrid[i][j] == e){
						_selectorX = i;
						_selectorY = j;
					}
				}
			}
		}
	}

	protected abstract void onResize();
	
	protected void returnChoice(ChooserScreen s){
		
	}
}
