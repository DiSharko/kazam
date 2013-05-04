package pvpmagic;

public class HideEffect extends TimedEffect {
	private double _numberOfIntervals;
	private double _disappearVals;
	private double _increment;
	private double _appearVals;
	private Player _target;
	
	public HideEffect(double numberOfIntervals, Player u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
		_appearVals = 40.0;
		_disappearVals = _numberOfIntervals - _appearVals;
		_increment = 1.0/_appearVals;
	}

	@Override
	public void effect() {
		effectCompleted = false;
		System.out.println(_numberOfIntervals);
		if (_numberOfIntervals > _disappearVals) {
			_target._hidden -= _increment;
			_numberOfIntervals -= 1;
		} else if (_numberOfIntervals >= _appearVals && _numberOfIntervals <= _disappearVals) {
			_target._hidden = 0.0;
			_numberOfIntervals -= 1;
		} else if (_numberOfIntervals <= 0.0) {
			effectCompleted = true;
			_target._hidden = 1.0;
		} else if (_numberOfIntervals < _appearVals) {
			_target._hidden += _increment;
			_numberOfIntervals -= 1;
		}
		System.out.println(_target._hidden);
	}
}