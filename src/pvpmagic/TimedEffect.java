package pvpmagic;

public abstract class TimedEffect {
	Boolean _effectCompleted;
	Double _numberOfIntervals;
	Double _changePerInterval = null;
	Unit _target;

	public String _type;
	boolean _display = true;
	
	public static String TYPE;
	
	public abstract void effect();
	
	public static TimedEffect newTimedEffect(String type, Double numberOfIntervals,
			Unit target) {
		return newTimedEffect(type, numberOfIntervals, target, null);
	}
	
	public static TimedEffect newTimedEffect(String type, Double numberOfIntervals,
			Unit target, Double changePerInterval){
		if (type == null){
			System.out.println("Given a null name!");
			return null;
		}
		if (type.equals("FearEffect")){ return new ConfuseEffect(numberOfIntervals, (Player) target); }
		else if (type.equals("HealthEffect")) { return new HealthEffect(numberOfIntervals, changePerInterval, target); }
		else if (type.equals("HideEffect")) { return new HideEffect(numberOfIntervals, (Player) target); }
		else if (type.equals("ManaEffect")) { return new ManaEffect(numberOfIntervals, changePerInterval, target); }
		else if (type.equals("RootEffect")) { return new RootEffect(numberOfIntervals, target); }
		else if (type.equals("SilenceEffect")) { return new SilenceEffect(numberOfIntervals, target); }

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
		try {
			if (Boolean.parseBoolean(args[1]) && Integer.parseInt(args[5]) == target._netID) {
				return args[4].equals("null") ? 
						newTimedEffect(args[0], Double.parseDouble(args[2]), target) :
							newTimedEffect(args[0], Double.parseDouble(args[2]), target, Double.parseDouble(args[4]));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return _type;
	}
}
