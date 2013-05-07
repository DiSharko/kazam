package screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class TransitionScreen extends Screen {

	public static enum Transition {FADE, BORDER};
	
	Transition transitionType = Transition.FADE;
	Screen newScreen;
	int counter = 0;

	public TransitionScreen(ScreenHolder holder) {
		super(holder, "transition");
		_allowShowBehind = true;
		_allowUpdateBehind = true;

		setup();
	}

	@Override
	public void setup() {
	}
	
	@Override
	public void update(){
		if (transitionType == Transition.BORDER && counter > _holder._w/20+1) _holder.switchToScreen(newScreen._id);
		if (transitionType == Transition.FADE){
			if (counter == 5){
				_holder.switchToScreen(newScreen._id);
				newScreen.switchInto();
				_holder.switchToScreen(_id);
			}
			if (counter == 10){
				_holder.switchToScreen(newScreen._id);
			}
		}
		counter++;
	}

	public void transition(Transition _transitionType, Screen _newScreen){
		transitionType = _transitionType;
		newScreen = _newScreen;
		counter = 0;
	}

	@Override
	protected void draw(Graphics2D g) {
		if (transitionType == Transition.BORDER){
			g.setColor(Color.black);
			int _speed = 10;
			g.fillRect(0, 0, counter*_speed, _holder._h);
			g.fillRect(_holder._w-counter*_speed, 0, counter*_speed, _holder._h);
			g.fillRect(0, 0, _holder._w, counter*_speed);
			g.fillRect(0, _holder._h-counter*_speed, _holder._w, counter*_speed);
		} else if (transitionType == Transition.FADE){
			if (counter <= 5){
				g.setColor(new Color(0,0,0,Math.min(0.2f*counter, 1)));
				g.fillRect(0, 0, _holder._w, _holder._h);
			} else {
				g.setColor(new Color(0,0,0,Math.min(0.2f*(10-counter), 1)));
				g.fillRect(0, 0, _holder._w, _holder._h);
			}
		}
	}

	@Override
	protected void onKeyPressed(KeyEvent event){
		super.onKeyPressed(event);
	}

	@Override
	protected void handleElementReleased(InterfaceElement e) {
		super.handleElementReleased(e);
	}
	
	@Override
	protected void onResize(){}

}
