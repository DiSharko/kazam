package pvpmagic;

public class KittyEffect extends TimedEffect {
	public static String TYPE = "KittyEffect";

	public KittyEffect(double numberOfIntervals, 
			Player caster, Unit target) {
		_numberOfIntervals = numberOfIntervals;
		_caster = caster;
		_target = target;
		_type = TYPE;
	}

	@Override
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isSilenced = true;
			_target._basicImage = "cat";
			_numberOfIntervals -= 1;
		} else {
			_target._isSilenced = false;
			_target._basicImage = _target._oldImage;
			_effectCompleted = true;
		}
	}
}