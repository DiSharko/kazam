package pvpmagic;

public class ManaEffect extends TimedEffect {
	public static String TYPE = "ManaEffect";

	public ManaEffect(double numberOfIntervals, double changePerInterval, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_target = u;
		_type = TYPE;
	}
	
	public void effect() {
		effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target.changeMana(_changePerInterval);
			_numberOfIntervals -= 1;
		} else {
			effectCompleted = true;
		}
	}

}
