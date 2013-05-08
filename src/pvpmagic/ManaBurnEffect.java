package pvpmagic;

public class ManaBurnEffect extends TimedEffect {
	public static String TYPE = "ManaBurnEffect";

	public ManaBurnEffect(double numberOfIntervals, double changePerInterval, Unit u) {
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
