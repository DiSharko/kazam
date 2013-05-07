package pvpmagic;

public class SilenceEffect extends TimedEffect {
	public static String TYPE = "SilenceEffect";

	public SilenceEffect(double numberOfIntervals, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
	}

	@Override
	public void effect() {
		effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isSilenced = true;
			_numberOfIntervals -= 1;
		} else {
			_target._isSilenced = false;
			effectCompleted = true;
		}
	}
	
	public String toNet() {
		return "n " + Double.toString(_numberOfIntervals) + " t " + _target._netID;
	}

}
