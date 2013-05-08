package pvpmagic;

public abstract class TimedEffect {
	Boolean _effectCompleted = false;
	Boolean _toBeCleansed = false;
	
	Double _numberOfIntervals;
	Double _changePerInterval = null;
	Unit _target;

	public String _type;
	public boolean _display = true;
	
	public abstract void effect();
	
	public static TimedEffect newTimedEffect(String type, Double numberOfIntervals,
			Unit target) {
		return newTimedEffect(type, numberOfIntervals, target, null);
	}
	
	public static TimedEffect newTimedEffect(String type, Double numberOfIntervals,
			Unit target, Double changePerInterval){
		if (type == null || target == null){
			System.out.println("ERROR: newTimedEffect given a null argument!");
			return null;
		}
		if (type.equals("ConfuseEffect")){ return new ConfuseEffect(numberOfIntervals, (Player) target); }
		else if (type.equals("HealthBurnEffect")) { return new HealthBurnEffect(numberOfIntervals, changePerInterval, target); }
		else if (type.equals("HealthBoostEffect")) { return new HealthBoostEffect(numberOfIntervals, changePerInterval, target); }
		else if (type.equals("HideEffect")) { return new HideEffect(numberOfIntervals, (Player) target); }
		else if (type.equals("ManaBurnEffect")) { return new ManaBurnEffect(numberOfIntervals, changePerInterval, target); }
		else if (type.equals("ManaBoostEffect")) { return new ManaBoostEffect(numberOfIntervals, changePerInterval, target); }
		else if (type.equals("RootEffect")) { return new RootEffect(numberOfIntervals, target); }
		else if (type.equals("SilenceEffect")) { return new SilenceEffect(numberOfIntervals, target); }
		else if (type.equals("KittyEffect")) { return new KittyEffect(numberOfIntervals, target); }

		System.out.println("Effect name \""+ type +"\" not found!");
		return null;
	}
	
	public String toNet() {
		return _type + 
				"," + _effectCompleted +
				"," + _numberOfIntervals +
				"," + _changePerInterval +
				"," + _target._netID;
	}
	
	public static TimedEffect fromNet(String effectNetString, Unit target) {
		String[] args = effectNetString.split(",");
		if (Boolean.parseBoolean(args[1]) && Integer.parseInt(args[5]) == target._netID) {
			return args[4].equals("null") ? 
					newTimedEffect(args[0], Double.parseDouble(args[2]), target) :
						newTimedEffect(args[0], Double.parseDouble(args[2]), target, Double.parseDouble(args[4]));
		}
		return null;
	}
	
	@Override
	public String toString() {
		return _type + 
				"," + _effectCompleted +
				"," + _numberOfIntervals +
				"," + _changePerInterval +
				"," + _target._netID;
	}
}
