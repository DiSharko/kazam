package pvpmagic;

public class SilenceEffect extends TimedEffect {
	public static String TYPE = "SilenceEffect";

	public SilenceEffect(double numberOfIntervals, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
		_type = TYPE;
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
