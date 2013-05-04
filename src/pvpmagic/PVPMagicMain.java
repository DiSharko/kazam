package pvpmagic;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import screen.*;

public class PVPMagicMain extends ScreenHolder {
	private static final long serialVersionUID = -7691706430673177918L;

	public static void main(String[] args){
		new Resource();
		new PVPMagicMain();
	}

	public PVPMagicMain(){
		super();
		_window.setVisible(true);
	}
	
	@Override
	public void createWindow(){
		_w = 1120;
		_h = 700;
		_minW = 1120;
		_minH = 700;
		
		_window = new JFrame();
		_window.setSize(_w, _h);
		_window.setMinimumSize(new Dimension(_minW, _minH));
		_window.setLocationRelativeTo(null);
		if (!Resource._useNativeBorder) {
			_window.setUndecorated(true);
		} else {
			_window.setTitle(Resource._title);
		}
		_window.setResizable(true);
		_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		_window.add(this);
	}



	@Override
	public void addScreens() {
		_screenList = new ArrayList<Screen>();
		_screenList.add(_transitionScreen = new TransitionScreen(this));
		if (!Resource._useNativeBorder) _screenList.add(_borderScreen = new BorderScreen(this));
		_screenList.add(new WelcomeScreen(this));
		_screenList.add(new SetupScreen(this));
		_screenList.add(_chooserScreen = new ChooserScreen(this, "default"));
		_screenList.add(new GameScreen(this));
		_screenList.add(new PauseScreen(this));

		switchToScreen("welcome");
	}
	
}
