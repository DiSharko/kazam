package pvpmagic;

public class FearEffect extends TimedEffect {
	private double _numberOfIntervals;
	private Player _target;
	
	public FearEffect(double numberOfIntervals, Player u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
	}

	@Override
	public void effect() {
		effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isSilenced = true;
			if (_numberOfIntervals % 20 == 0) {
				double offset = -3 + (Math.random() * ((3 - (-3)) + 1));
				_target._destination = new Vector(_target._pos.x + offset, _target._pos.y + offset);
			}
			_numberOfIntervals -= 1;
		} else {
			_target._isSilenced = false;
			effectCompleted = true;
		}
	}

}
