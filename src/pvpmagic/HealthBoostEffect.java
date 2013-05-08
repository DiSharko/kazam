package pvpmagic;

public class HealthBoostEffect extends TimedEffect {
	public static String TYPE = "HealthBoostEffect";

	public HealthBoostEffect(double numberOfIntervals, double changePerInterval, 
			Player caster, Unit target) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_target = target;
		_type = TYPE;
		_toBeCleansed = false;
	}
	
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target.changeHealth(_changePerInterval, _caster);
			_numberOfIntervals -= 1;
		} else {
			_effectCompleted = true;
		}
	}

}
