package pvpmagic;

public class ManaBoostEffect extends TimedEffect {
	public static String TYPE = "ManaBoostEffect";

	public ManaBoostEffect(double numberOfIntervals, double changePerInterval, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_target = u;
		_type = TYPE;
		_toBeCleansed = (_changePerInterval < 0) ? true : false;
	}
	
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target.changeMana(_changePerInterval);
			_numberOfIntervals -= 1;
		} else {
			_effectCompleted = true;
		}
	}

}
