package pvpmagic;

public class FearEffect extends TimedEffect {
	public static String TYPE = "FearEffect";
	
	public FearEffect(double numberOfIntervals, Player u) {
		_numberOfIntervals = numberOfIntervals;
		_target = u;
	}

	@Override
	public void effect() {
		effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isSilenced = true;
			_target._isRooted = true;
			if (_numberOfIntervals % 20 == 0) {
				double xOffset = -200 + Math.random()*400 + 1;
				double yOffset = -200 + Math.random()*400 + 1;
				((Player) _target)._destination = 
						new Vector(_target._pos.x + xOffset, _target._pos.y + yOffset);
			}
			_numberOfIntervals -= 1;
		} else {
			_target._isSilenced = false;
			_target._isRooted = false;
			effectCompleted = true;
		}
	}

}
