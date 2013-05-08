package pvpmagic;

public class ManaBoostEffect extends TimedEffect {
	public static String TYPE = "ManaBoostEffect";

	public ManaBoostEffect(double numberOfIntervals, double changePerInterval, 
			Player caster, Unit target) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_caster = caster;
		_target = target;
		_type = TYPE;
		_toBeCleansed = (_changePerInterval < 0) ? true : false;
	}
	
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target.changeMana(_changePerInterval, _caster);
			_numberOfIntervals -= 1;
		} else {
			_effectCompleted = true;
		}
	}

}
