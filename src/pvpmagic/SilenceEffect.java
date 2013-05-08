package pvpmagic;

public class SilenceEffect extends TimedEffect {
	public static String TYPE = "SilenceEffect";

	public SilenceEffect(double numberOfIntervals, 
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
			_target._isSilenced = true;
			_numberOfIntervals -= 1;
		} else {
			_target._isSilenced = false;
			_effectCompleted = true;
		}
	}
}
