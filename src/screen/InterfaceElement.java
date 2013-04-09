package screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public abstract class InterfaceElement {
	protected Screen screen;
	
	public String id;
	public String type;

	public String name;

	public double x, y, w, h;

	// COLOR SETTINGS ///////////////
	protected Color borderColor 		= 	Color.black;
	protected Color color 		 	= 	new Color(1f,1f,1f);
	protected Color mouseOverColor 	= 	new Color(.6f,.6f,.6f,.2f);
	protected Color selectColor 	= 	new Color(.4f,.4f,.4f,.2f);
	public void setColors(Color _color, Color _mouseOverColor, Color _selectColor, Color _borderColor){
		if (_color != null) color = _color;
		if (_mouseOverColor != null) mouseOverColor = _mouseOverColor;
		if (_selectColor != null) selectColor = _selectColor;
		if (_borderColor != null) borderColor = _borderColor;
	}


	public boolean delete = false;


	public boolean visible = true;
	public boolean enabled = true;
	public void setEnabled(boolean _state){
		enabled = _state;
		if (!enabled){
			mousedOver = false;
			selected = false;
		}
	}
	
	public boolean acceptsKeyboardFocus = false;;
	
	public boolean mousedOver = false;
	public boolean selected = false;

	public abstract void draw(Graphics2D g);

	public void handleMouseMoved(MouseEvent event) {};
	public void handleMousePressed(MouseEvent event){};
	public void handleMouseReleased(MouseEvent event){};
	public void handleMouseDragged(MouseEvent event) {}
	public void handleMouseWheelMoved(MouseWheelEvent event) {}
	
	public void handleKeyPressed(KeyEvent event){};
	public void handleKeyReleased(KeyEvent event){};
	
	public void onReleased(boolean save){};
	public void update(){}

}

