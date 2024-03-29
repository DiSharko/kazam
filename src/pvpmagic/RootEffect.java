package pvpmagic;

public class RootEffect extends TimedEffect {
	public static String TYPE = "RootEffect";
	
	public RootEffect(double numberOfIntervals, 
			Player caster, Unit target) {
		_numberOfIntervals = numberOfIntervals;
		_caster = caster;
		_target = target;
		_type = TYPE;
		_toBeCleansed = true;
	}

	@Override
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isRooted = true;
			_target._movable = false;
			_numberOfIntervals -= 1;
		} else {
			_target._isRooted = false;
			_target._movable = true;
			_effectCompleted = true;
		}
	}

}
