package pvpmagic;

public class RootEffect extends TimedEffect {
	private double _numberOfIntervals;
	private Unit _target;
	
	public RootEffect(double numberOfIntervals, Unit u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
	}

	@Override
	public void effect() {
		effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isRooted = true;
		} else {
			System.out.println("FIN ROOT: CAN MOVE AGAIN");
			_target._isRooted = false;
			effectCompleted = true;
		}
	}

}
