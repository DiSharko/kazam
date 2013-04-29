package pvpmagic;

public class SilenceEffect extends TimedEffect {
	private double _numberOfIntervals;
	private Unit _target;
	
	public SilenceEffect(double numberOfIntervals, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
	}

	@Override
	public void effect() {
		effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isSilenced = true;
		} else {
			System.out.println("FIN SILENCE: CAN CAST AGAIN");
			_target._isSilenced = false;
			effectCompleted = true;
		}
	}

}
