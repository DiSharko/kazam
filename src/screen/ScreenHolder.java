package screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import pvpmagic.Resource;

import screen.TransitionScreen.Transition;

@SuppressWarnings("serial")
public abstract class ScreenHolder extends JPanel implements KeyListener, MouseInputListener, WindowListener {

	public JFrame _window;
	protected ArrayList<Screen> _screenList;
	protected TransitionScreen _transitionScreen;
	protected ChooserScreen _chooserScreen;
	protected BorderScreen _borderScreen;

	boolean _showBorder = true;

	public int _w;
	public int _h;

	public int _minW;
	public int _minH;

	int _timerDelay = 25;
	public void setFPS(int fps){
		_timerDelay = 1000/fps;
	}

	Timer _timer = new Timer(_timerDelay, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			update();
			repaint();

			_timer.setInitialDelay(_timerDelay);

			_timer.restart();
		}
	});


	public abstract void addScreens();


	public ScreenHolder(){
		createWindow();

		_window.addWindowListener(this);
		_window.addKeyListener(this);
		_window.setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);

		addScreens();

		_timer.start();
	}

	public abstract void createWindow();

	public void update(){
		int current = 0;
		while (_screenList != null && current < _screenList.size()){
			_screenList.get(current).update();
			if (_screenList.get(current)._allowUpdateBehind) current++;
			else break;
		}
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		byte drawing = 1;
		for (int i = 0; _screenList != null &&  i < _screenList.size() && i >= 0; i+=drawing){
			Screen s = _screenList.get(i);
			if (drawing == 1){
				if (!s._allowShowBehind || i == _screenList.size()-1) drawing = -1;
			}
			if (drawing == -1){
				s.drawExternal(g2D);
			}
		}
	}


	// Screen control
	public void addScreen(Screen s){
		_screenList.add(s);
	}
	public Screen getScreen(String _id){
		for (int i = 0; i < _screenList.size(); i++){
			if (_screenList.get(i)._id.equals(_id)){
				return _screenList.get(i);
			}
		}
		return null;
	}

	public void putScreenAway(){
		int screen = ((!_showBorder || _borderScreen == null) ? 0 : 1);
		Screen temp = _screenList.get(screen);
		_screenList.remove(screen);
		_screenList.add(temp);
	}

	public boolean switchToScreen(String _id){
		for (int i = 0; i < _screenList.size(); i++){
			if (_screenList.get(i)._id.equals(_id)){
				Screen temp = _screenList.get(i);
				_screenList.remove(i);
				_screenList.add(0, temp);
				temp.switchInto();

				if (_showBorder && _borderScreen != null){
					_screenList.remove(_borderScreen);
					_screenList.add(0, _borderScreen);
				}
				return true;
			}
		}
		return false;
	}

	public boolean transitionToScreen(Transition transition, String id){
		Screen screen = getScreen(id);
		if (screen == null) return false;

		_transitionScreen.transition(transition, screen);
		switchToScreen("transition");

		if (_showBorder && _borderScreen != null){
			_screenList.remove(_borderScreen);
			_screenList.add(0, _borderScreen);
		}

		return true;
	}

	public void showBorder(){
		_showBorder = true;
		_screenList.remove(_borderScreen);
		_screenList.add(0, _borderScreen);
	}

	public void hideBorder(){
		_showBorder = false;
		_screenList.remove(_borderScreen);
		_screenList.add(_borderScreen);
	}

	public void showChooser(ChooserScreen c){
		_chooserScreen.reset(c);
		switchToScreen("chooser");
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (_screenList != null){
			for (Screen s : _screenList){
				s.onKeyPressed(e);
				if (!s._allowInputBehind) break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (_screenList != null){
			for (Screen s : _screenList){
				s.onKeyReleased(e);
				if (!s._allowInputBehind) break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int current = 0;
		while (_screenList != null && current < _screenList.size()){
			_screenList.get(current).onMousePressed(e);
			if (_screenList.get(current)._allowInputBehind) current++;
			else break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int current = 0;
		while (_screenList != null && current < _screenList.size()){
			_screenList.get(current).onMouseReleased(e);
			if (_screenList.get(current)._allowInputBehind) current++;
			else break;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (_screenList != null && _screenList.size() > 0) _screenList.get(0).onMouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (Resource._useNativeBorder) setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		int current = 0;
		while (_screenList != null && current < _screenList.size()){
			_screenList.get(current).onMouseMoved(e);
			if (_screenList.get(current)._allowInputBehind) current++;
			else break;
		}
	}

	public void onResize(int _w, int _h) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public void setWindowSize(int w, int h){
		w = Math.max(w, _minW);
		h = Math.max(h, _minH);
		_window.setSize(w, h);
		_w = w;
		_h = h;
		for (Screen s : _screenList){
			s.onResize();
		}
	}

	public void setWindowBounds(int x, int y, int w, int h){
		w = Math.max(w, _minW);
		h = Math.max(h, _minH);
		_window.setBounds(x, y, w, h);
		_w = w;
		_h = h;
		for (Screen s : _screenList){
			s.onResize();
		}
	}


	@Override
	public void windowDeactivated(WindowEvent e){
		if (_borderScreen != null){
			_borderScreen._frameColor = Color.darkGray;
		}
	}

	@Override
	public void windowActivated(WindowEvent e){
		if (_borderScreen != null){
			_borderScreen._frameColor = Color.black;
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
