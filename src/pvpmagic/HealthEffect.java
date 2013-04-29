package pvpmagic;

public class HealthEffect extends TimedEffect {
	private double _numberOfIntervals;
	public final double _changePerInterval;
	private Unit _target;

	public HealthEffect(double numberOfIntervals, double changePerInterval, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_changePerInterval = changePerInterval;
		_target = u;
	}
	
	public void effect() {
		effectCompleted = false;
		System.out.println("HEALTH NUM INTV: " + _numberOfIntervals + " CHG: " + _changePerInterval);
		if (_numberOfIntervals > 0) {
			_target.changeHealth(_changePerInterval);
			_numberOfIntervals -= 1;
		} else {
			effectCompleted = true;
		}
	}

}
