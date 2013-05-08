package pvpmagic;

public class HealthBurnEffect extends TimedEffect {
	public static String TYPE = "HealthBurnEffect";

	public HealthBurnEffect(double numberOfIntervals, double changePerInterval, 
			Player caster, Unit target) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_caster = caster;
		_target = target;
		_type = TYPE;
		_toBeCleansed = true;
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
