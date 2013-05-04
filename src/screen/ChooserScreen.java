package screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ChooserScreen extends Screen {

	public String _name;

	public Button _chosen;
	public ArrayList<Button> _chosens;
	public int _totalToChoose = 1;

	public boolean _acceptOnChoosing = true;
	public boolean _numberChoices = false;
	public String[] _choiceNumberings;
	
	public Color _chosenColor = new Color(0.4f,0.7f,0.4f);
	public Color _unchosenColor = new Color(0.8f, 0.8f, 0.8f);
	public Color _disabledColor = new Color(1f, 0.9f, 0.9f);

	public Button _accept;
	public Button _cancel;

	public Screen _caller;
	public InterfaceElement _callingElement;

	public ButtonGroup _choices;

	public ChooserScreen(ScreenHolder holder, String name) {
		super(holder, "chooser");
		_name = name;
		setup();
	}

	@Override
	public void setup() {
		_allowShowBehind = true;
		_allowUpdateBehind = false;
		_allowInputBehind = false;

		// Default parameters:
		_accept = null; //new Button(this, "accept", 100, 50, "Ok", KeyEvent.VK_ENTER);
		_cancel = new Button(this, "cancel", 100, 50, "Cancel", KeyEvent.VK_ESCAPE);
		_choices = new ButtonGroup(this, "choices", 200, 5, 100, 100, 200, 200);

		reset(this);
	}

	public void reset(ChooserScreen c){
		_name = c._name;
		_chosen = null;
		_chosens = new ArrayList<Button>();
		_totalToChoose = c._totalToChoose;
		_acceptOnChoosing = c._acceptOnChoosing;
		_numberChoices = c._numberChoices;
		_choiceNumberings = c._choiceNumberings;

		_interfaceElements = new ArrayList<InterfaceElement>();
		_allowShowBehind = c._allowShowBehind;
		_allowInputBehind = c._allowInputBehind;
		_allowUpdateBehind = c._allowUpdateBehind;

		_accept = c._accept;
		_cancel = c._cancel;

		_caller = c._caller;
		_callingElement = c._callingElement;

		_choices = c._choices;
		if (_choices != null){
			for (Button b : _choices.buttons){
				b.color = _unchosenColor;
				if (!b.enabled) b.color = _disabledColor;
				b.screen = this;
			}
			_interfaceElements.add(_choices);
		}

		_accept = c._accept;
		_cancel = c._cancel;

		if (_accept != null) _interfaceElements.add(_accept);
		if (_cancel != null) _interfaceElements.add(_cancel);

		onResize();
	}

	@Override
	protected void draw(Graphics2D g) {
		if (_choices != null){
			g.setColor(Color.lightGray);
			g.fillRoundRect((int)_choices.x-10, (int)_choices.y-10, (int)_choices.w+20, (int)_choices.h+20+(_cancel != null ? (int)_cancel.h+10 : 0), 10, 10);
			g.setColor(Color.black);
			g.drawRoundRect((int)_choices.x-10, (int)_choices.y-10, (int)_choices.w+20, (int)_choices.h+20+(_cancel != null ? (int)_cancel.h+10 : 0), 10, 10);
		}
	}

	@Override
	protected void drawOnTop(Graphics2D g){
		if (_numberChoices){
			g.setColor(Color.black);
			for (int i = 0; i < _chosens.size(); i++){
				String label = ""+i;
				if (_choiceNumberings != null && _choiceNumberings.length >= _totalToChoose) label = _choiceNumberings[i];
				g.drawString(label, (int)(_chosens.get(i).x+10), (int)(_chosens.get(i).y+23));
			}
		}
		if (_choices != null){
			g.setColor(new Color(1f, 0f, 0f, 0.5f));
			for (Button b : _choices.buttons){
				if (!b.enabled){
					g.fillRoundRect((int)b.x, (int)b.y, (int)b.w, (int)b.h, b.roundness, b.roundness);
				}
			}
		}
	}
	
	@Override
	protected void onResize() {
		if (_choices != null){
			_choices.w = _holder._w;
			_choices.h = _holder._h;
			_choices.reposition();

			if (_accept != null) {
				_accept.x = _choices.x+_choices.w/2-_accept.w-2;
				_accept.y = _choices.y + _choices.h+10;
			}
			if (_cancel != null){
				_cancel.x = _choices.x+_choices.w/2-_cancel.w/2 + (_accept != null ? _cancel.w/2+2 : 0);
				_cancel.y = _choices.y + _choices.h + 10;
			}
		}
	}

	@Override
	protected void handleElementReleased(InterfaceElement e){
		if (e == _accept){
			if (_caller != null) _caller.returnChoice(this);
			_holder.putScreenAway();
		} else if (e == _cancel){
			if (_chosen != null) _chosen.color = _unchosenColor;
			_chosen = null;
			if (_caller != null) _caller.returnChoice(this);
			_holder.putScreenAway();

		} else {
			if (_chosens.size() < _totalToChoose){
				if (!_chosens.contains(e)){ // choose it
					_chosen = (Button) e;
					_chosens.add(_chosen);
					_chosen.color = _chosenColor;
				} else { // unchoose it
					e.color = _unchosenColor;
					_chosens.remove(e);
				}

				if (_acceptOnChoosing && _chosens.size() == _totalToChoose){
					if (_caller != null) _caller.returnChoice(this);
					_holder.putScreenAway();
				}
			} else {
				if (_chosens.contains(e)){ // unchoose it
					_chosen.color = _unchosenColor;
					_chosen = null;
					_chosens.remove(e);
				}
			}
		}
	}

}
