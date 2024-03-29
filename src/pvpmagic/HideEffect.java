package pvpmagic;

public class HideEffect extends TimedEffect {	
	public static String TYPE = "HideEffect";

	private double _disappearVals;
	private double _increment;
	private double _appearVals;

	public HideEffect(double numberOfIntervals, Player caster, Player target) {
		_numberOfIntervals = numberOfIntervals;
		_caster = caster;
		_target = target;
		_appearVals = 40.0;
		_disappearVals = _numberOfIntervals - _appearVals + 1;
		_increment = 1.0/_appearVals;
		_type = TYPE;
	}

	@Override
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > _disappearVals) {
			((Player) _target)._hidden -= _increment;
			_numberOfIntervals -= 1;
		} else if (_numberOfIntervals >= _appearVals && _numberOfIntervals <= _disappearVals) {
			((Player) _target)._hidden = 0.0;
			_numberOfIntervals -= 1;
		} else if (_numberOfIntervals <= 0.0) {
			_effectCompleted = true;
			((Player) _target)._hidden = 1.0;
		} else if (_numberOfIntervals < _appearVals) {
			((Player) _target)._hidden += _increment;
			_numberOfIntervals -= 1;
		}
		if (((Player) _target)._hidden < 0) ((Player) _target)._hidden = 0.0;
	}
}