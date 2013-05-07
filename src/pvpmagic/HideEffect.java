package pvpmagic;

<<<<<<< HEAD
public class HideEffect extends TimedEffect {
	private double _numberOfIntervals;
	private double _disappearVals;
	private double _increment;
	private double _appearVals;
	private Player _target;
	
=======
public class HideEffect extends TimedEffect {	
	public static String TYPE = "HideEffect";

	private double _disappearVals;
	private double _increment;
	private double _appearVals;

>>>>>>> 9d7789a7d8e12ac2c5ef72ed44ec73147bb0a13e
	public HideEffect(double numberOfIntervals, Player u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
		_appearVals = 40.0;
<<<<<<< HEAD
		_disappearVals = _numberOfIntervals - _appearVals;
=======
		_disappearVals = _numberOfIntervals - _appearVals + 1;
>>>>>>> 9d7789a7d8e12ac2c5ef72ed44ec73147bb0a13e
		_increment = 1.0/_appearVals;
	}

	@Override
	public void effect() {
		effectCompleted = false;
<<<<<<< HEAD
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
=======
		if (_numberOfIntervals > _disappearVals) {
			((Player) _target)._hidden -= _increment;
			_numberOfIntervals -= 1;
		} else if (_numberOfIntervals >= _appearVals && _numberOfIntervals <= _disappearVals) {
			((Player) _target)._hidden = 0.0;
			_numberOfIntervals -= 1;
		} else if (_numberOfIntervals <= 0.0) {
			effectCompleted = true;
			((Player) _target)._hidden = 1.0;
		} else if (_numberOfIntervals < _appearVals) {
			((Player) _target)._hidden += _increment;
			_numberOfIntervals -= 1;
		}
>>>>>>> 9d7789a7d8e12ac2c5ef72ed44ec73147bb0a13e
	}
}