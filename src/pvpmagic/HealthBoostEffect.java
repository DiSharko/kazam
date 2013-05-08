package pvpmagic;

public class HealthBoostEffect extends TimedEffect {
	public static String TYPE = "HealthBoostEffect";

	public HealthBoostEffect(double numberOfIntervals, double changePerInterval, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_target = u;
		_type = TYPE;
		_toBeCleansed = false;
	}
	
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target.changeHealth(_changePerInterval);
			_numberOfIntervals -= 1;
		} else {
			_effectCompleted = true;
		}
	}

}
