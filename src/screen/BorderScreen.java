package screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pvpmagic.Resource;

import screen.Screen;

public class BorderScreen extends Screen {

	public BorderScreen(ScreenHolder holder) {
		super(holder, "border");
		setup();
	}
	
	boolean _fadingIn = false;
	boolean _fadingOut = false;
	int _fadeTime = 0;
	int _fadeTimeTotal = 20;
	

	static int _windowedTopBarHeight = 40;
	static int _windowedBorderSize = 4;
	
	static int _fullscreenTopBarHeight = 20;
	static int _fullscreenBorderSize = 2;
	
	public static int _topBarHeight = _windowedTopBarHeight;
	int _borderSize = _windowedBorderSize;
	int _cornerSize = 8; // not visible
	Color _frameColor = Color.black;
	

	Rectangle _screenBounds;

	int _savedWindowX;
	int _savedWindowY;
	int _savedWindowW;
	int _savedWindowH;


	boolean _mouseDown;
	int _mouseDownX;
	int _mouseDownY;
	int _mouseX;
	int _mouseY;

	HashSet<String> _resizingSides;


	@Override
	public void setup() {
		_allowShowBehind = true;
		_allowUpdateBehind = true;
		_allowInputBehind = true;

		_mouseDown = false;
		_mouseX = 0;
		_mouseY = 0;

		_savedWindowW = _holder._window.getWidth();
		_savedWindowH = _holder._window.getHeight();

		_resizingSides = new HashSet<String>();

		// Code that finds the actual available screen real-estate, not total resolution (aka minus taskbar, etc.)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			_screenBounds = ge.getMaximumWindowBounds();
			
			// If that fails for some reason, use the guaranteed way to find just the basic total size.
		} catch (ClassNotFoundException e){	_screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (InstantiationException e){	_screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (IllegalAccessException e){	_screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (UnsupportedLookAndFeelException e){ _screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());			
		}
		
		_interfaceElements = new ArrayList<InterfaceElement>();
		Button close = new Button(this, "close", Resource.get("close"));
		close.roundness = 0;
		close.setColors(new Color(0,0,0,0f), new Color(1f,1f,1f,0.3f), new Color(1f, 1f, 1f, 0.4f), new Color(0,0,0,0f));
		_interfaceElements.add(close);
		
		Button fullscreen = new Button(this, "fullscreen", Resource.get("fullscreen"));
		fullscreen.roundness = 0;
		fullscreen.setColors(new Color(0,0,0,0f), new Color(1f,1f,1f,0.3f), new Color(1f, 1f, 1f, 0.4f), new Color(0,0,0,0f));
		_interfaceElements.add(fullscreen);
		
		onResize();
	}

	public void fadeIn(){
		_fadingIn = true;
		_fadingOut = false;
		
		_fadeTime = 0;
	}
	public void fadeOut(){
		_fadingIn = true;
		_fadingOut = false;
		
		_fadeTime = _fadeTimeTotal;
	}
	
	
	@Override
	public void update(){
		super.update();
		
		if (_fadingIn){
			_fadeTime++;
		} else if (_fadingOut){
			_fadeTime--;
		}
		if (_lastMousePress > 0) _lastMousePress--;
	}
	
	@Override
	protected void draw(Graphics2D g) {
		g.setColor(_frameColor);
		g.fillRect(0, 0, _borderSize, _holder._h);
		g.fillRect(_holder._w - _borderSize, 0, _borderSize, _holder._h);
		g.fillRect(0, _holder._h-_borderSize, _holder._w, _borderSize);

		g.fillRect(0, 0, _holder._w, _topBarHeight);
		
		g.setColor(Color.lightGray);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.drawString(Resource._title, 15, _topBarHeight*2/3);
	}


	long _lastMousePress = 0;
	@Override
	public boolean onMousePressed(MouseEvent e) {
		super.onMousePressed(e);
		_mouseDown = true;
		
		if (_lastMousePress > 0){
			if (_holder._w == _screenBounds.width && _holder._h == _screenBounds.height){
				_holder.setWindowBounds(_savedWindowX, _savedWindowY, _savedWindowW, _savedWindowH);
			} else {
				saveSize();
				_holder._window.setLocation(_screenBounds.x, _screenBounds.y);
				_holder.setWindowSize(_screenBounds.width, _screenBounds.height);
			}
		}
		_lastMousePress = 5;

		_mouseDownX = e.getX();
		_mouseDownY = e.getY();
		
		return true;
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		super.onMouseReleased(e);
		_mouseDown = false;

		_resizingSides.clear();
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		super.onMouseDragged(e);
		_mouseX = e.getX();
		_mouseY = e.getY();

		if (_mouseDown && !_fullscreen){
			
			if (_resizingSides.contains("northwest") || (_mouseDownX < _cornerSize && _mouseDownY < _cornerSize && _resizingSides.size() == 0)){
				int differenceX = _holder._window.getX()-e.getXOnScreen();
				int differenceY = _holder._window.getY()-e.getYOnScreen();
				_holder.setWindowBounds(_holder._window.getX()-differenceX, _holder._window.getY()-differenceY, _holder._w+differenceX, _holder._h+differenceY);
				_resizingSides.add("northwest");
				_holder._window.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));

			} else if (_resizingSides.contains("northeast") || (_mouseDownX > _holder._w-_cornerSize && _mouseDownY < _cornerSize && _resizingSides.size() == 0)){
				int differenceX = _holder._window.getX()+_holder._w-e.getXOnScreen();
				int differenceY = _holder._window.getY()-e.getYOnScreen();
				_holder.setWindowBounds(_holder._window.getX(), _holder._window.getY()-differenceY, _holder._w-differenceX+1, _holder._h+differenceY);
				_resizingSides.add("northeast");
				_holder._window.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
				
			} else if (_resizingSides.contains("southwest") || (_mouseDownX < _cornerSize && _mouseDownY > _holder._h-_cornerSize && _resizingSides.size() == 0)){
				int differenceX = _holder._window.getX()-e.getXOnScreen();
				int differenceY = _holder._window.getY()+_holder._h-e.getYOnScreen();
				_holder.setWindowBounds(_holder._window.getX()-differenceX, _holder._window.getY(), _holder._w+differenceX, _holder._h-differenceY);
				_resizingSides.add("southwest");
				_holder._window.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));

			} else if (_resizingSides.contains("southeast") || (_mouseDownX > _holder._w-_cornerSize && _mouseDownY > _holder._h-_cornerSize && _resizingSides.size() == 0)){
				int differenceX = _holder._window.getX()+_holder._w-e.getXOnScreen();
				int differenceY = _holder._window.getY()+_holder._h-e.getYOnScreen();
				_holder.setWindowSize(_holder._w-differenceX+1, _holder._h-differenceY);
				_resizingSides.add("southeast");
				_holder._window.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				
			} else if (_resizingSides.contains("west") || (_mouseDownX < _borderSize && _resizingSides.size() == 0)){
				int differenceX = _holder._window.getX()-e.getXOnScreen();
				_holder.setWindowBounds(_holder._window.getX()-differenceX, _holder._window.getY(), _holder._w+differenceX, _holder._h);
				_resizingSides.add("west");
				_holder._window.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));

			} else if (_resizingSides.contains("east") || (_mouseDownX > _holder._w-_borderSize && _resizingSides.size() == 0)){
				int differenceX = _holder._window.getX()+_holder._w-e.getXOnScreen();
				_holder.setWindowSize(_holder._w-differenceX+1, _holder._h);
				_resizingSides.add("east");
				_holder._window.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));

			} else if (_resizingSides.contains("north") || (_mouseDownY < _borderSize && _resizingSides.size() == 0)){
				int differenceY = _holder._window.getY()-e.getYOnScreen();
				_holder.setWindowBounds(_holder._window.getX(), _holder._window.getY()-differenceY, _holder._w, _holder._h+differenceY);
				_resizingSides.add("north");
				_holder._window.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));

			} else if (_resizingSides.contains("south") || (_mouseDownY > _holder._h-_borderSize && _resizingSides.size() == 0)){
				int differenceY = _holder._window.getY()+_holder._h-e.getYOnScreen();
				_holder.setWindowSize(_holder._w, _holder._h-differenceY);
				_resizingSides.add("south");
				_holder._window.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));


			} else if (_resizingSides.contains("move") || (_mouseDownY < _topBarHeight && _resizingSides.size() == 0)){
				if (e.getXOnScreen() <= 2){
					_holder.setWindowSize(_screenBounds.width/2, _screenBounds.height);
					_holder._window.setLocation(_screenBounds.x, _screenBounds.y);
				} else if (e.getXOnScreen() >= _screenBounds.width-3){
					_holder.setWindowSize(_screenBounds.width/2, _screenBounds.height);
					_holder._window.setLocation(_screenBounds.width-_holder._w, _screenBounds.y);
				} else if (e.getYOnScreen() <= _screenBounds.y+2){
					_holder._window.setLocation(_screenBounds.x, _screenBounds.y);
					_holder.setWindowSize(_screenBounds.width, _screenBounds.height);
				} else {
					if (_holder._w != _savedWindowW || _holder._h != _savedWindowH){ // If the window is being dragged away from snap
						_holder.setWindowSize(_savedWindowW, _savedWindowH);
						_mouseDownX = _savedWindowW/2;
						_mouseDownY = _topBarHeight/2;
					}
					_holder._window.setLocation(e.getXOnScreen()-_mouseDownX, e.getYOnScreen()-_mouseDownY);
				}
				_resizingSides.add("move");
			}
			
			if (_resizingSides.size() > 0 && !_resizingSides.contains("move")){
				saveSize();
				_holder.repaint();
			}
		}
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		super.onMouseMoved(e);
		_mouseX = e.getX();
		_mouseY = e.getY();
		if (_resizingSides.contains("northwest") || (_mouseX < _cornerSize && _mouseY < _cornerSize)){
			_holder._window.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		} else if (_resizingSides.contains("northeast") || (_mouseX > _holder._w-_cornerSize && _mouseY < _cornerSize)){
			_holder._window.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
		} else if (_resizingSides.contains("southwest") || (_mouseX < _cornerSize && _mouseY > _holder._h-_cornerSize)){
			_holder._window.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
		} else if (_resizingSides.contains("southeast") || (_mouseX > _holder._w-_cornerSize && _mouseY > _holder._h-_cornerSize)){
			_holder._window.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
		} else if (_resizingSides.contains("west") || _mouseX < _borderSize){
			_holder._window.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		} else if (_resizingSides.contains("east") || _mouseX > _holder._w-_borderSize){
			_holder._window.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		} else if (_resizingSides.contains("north") || _mouseY < _borderSize){
			_holder._window.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		} else if (_resizingSides.contains("south") || _mouseY > _holder._h-_borderSize){
			_holder._window.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		} else if (_mouseY < _topBarHeight){
//			_holder._window.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else {
			_holder._window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void saveSize(){
		_savedWindowX = _holder._window.getX();
		_savedWindowY = _holder._window.getY();
		_savedWindowW = _holder._w;
		_savedWindowH = _holder._h;
	}

	
	@Override
	public void onResize(){
		for (InterfaceElement e : _interfaceElements){
			if (e.id.equals("close")){
				e.w = e.h = _topBarHeight-2*_borderSize;
				e.y = _borderSize;
				e.x = _holder._w-e.w-_borderSize;
			} else if (e.id.equals("fullscreen")){
				e.w = e.h = _topBarHeight-2*_borderSize;
				e.y = _borderSize;
				e.x = _holder._w-(e.w+2)*2-_borderSize;
			}
		}
	};
	
	@Override
	public void handleElementReleased(InterfaceElement e){
		if (e.id.equals("close")){
			System.exit(0);
		} else if (e.id.equals("fullscreen")){
			if (_fullscreen){
				((Button)e).image = Resource.get("fullscreen");
				goWindowed();
			} else {
				((Button)e).image = Resource.get("windowed");
				goFullscreen();
			}
		}
	}
	
	boolean _fullscreen = false;
	public void goFullscreen(){
		_fullscreen = true;
		_holder._window.setVisible(false);
		_topBarHeight = _fullscreenTopBarHeight;
		_borderSize = _fullscreenBorderSize;
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		gd.setFullScreenWindow(_holder._window);
		_holder._window.requestFocus();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

		saveSize();
		_holder.setWindowSize(size.width, size.height);
		_holder._window.setVisible(true);
	}
	
	public void goWindowed(){
		_fullscreen = false;
		_holder._window.setVisible(false);
		_topBarHeight = _windowedTopBarHeight;
		_borderSize = _windowedBorderSize;
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		_holder.setWindowSize(_savedWindowW, _savedWindowH);

		_holder._window.setLocationRelativeTo(null);
		gd.setFullScreenWindow(null);
		_holder._window.setVisible(true);
		_holder._window.toFront();
	}

}
