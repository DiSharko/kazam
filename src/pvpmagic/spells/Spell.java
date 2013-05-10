package pvpmagic.spells;

import java.util.HashMap;

import pvpmagic.*;

public abstract class Spell extends Unit {
	static Boolean STATICOBJ = false;
	
	/**
	 * The caster of the spell
	 */
	protected Player _caster;
	/**
	 * The name of the spell
	 */
	public String _name;
	/**
	 * The amount of time taken to cast the spell (wind-up)
	 */
	public double _castingTime = 5;
	/**
	 * The amount of waiting time before the spell can be used again
	 */
	public double _cooldown = 0;
	/**
	 * The cost in mana for casting the spell
	 */
	public double _manaCost = 0;
	
	/**
	 * The velocity of the spell object itself
	 */
	double _velocity = 1;
	/**
	 * The direction vector in which it moves
	 */
	Vector _dir;
	/**
	 * The amount of intervals/frames since the casting of the spell
	 */
	int _timeSinceCast = 0;
	
	public Spell(GameData data, String type, Player caster, Vector target) {
		super(data, type, STATICOBJ, null);
		if (data == null || type == null || caster == null || target == null) return;
		_appliesRestitution = false;
		_caster = caster;
		_shape = new Circle(this, new Vector(0,0), 10);
		
		_dir = target;
		// Set initial guess at position for use in dir calculation
	}
	
	public void setProperties(Vector size, double velocity){
		if (_caster == null || _dir == null) return;
		_size = size;
		_velocity = velocity;
		
		// Spell trajectory starts at center of player
		
		_pos = _caster._pos.plus(_caster._size.mult(0.5));
		_dir = _dir.minus(_pos).normalize();

		_vel = _dir.mult(_velocity);

		// Adjust spell to start outside player
	}
	/**
	 * Instantiates new spell based on parameters
	 * @param data The backing GameData object in which the spell is instantiated
	 * @param name The name of the spell
	 * @param caster The caster of the spell, as a Player object
	 * @param dir The direction vector in which the spell is moving
	 * @return Specific new subclass of spell with fields filled in with parameters
	 */
	public static Spell newSpell(GameData data, String name, Player caster, Vector dir){
		if (name == null) return null;

		if (name.equals("Stun")){ return new StunSpell(data, caster, dir); }
		else if (name.equals("Disarm")) { return new DisarmSpell(data, caster, dir); }
		else if (name.equals("Burn")) { return new BurnSpell(data, caster, dir); }
		else if (name.equals("Root")) { return new RootSpell(data, caster, dir); }
		else if (name.equals("Blind")) { return new BlindSpell(data, caster, dir); }
		else if (name.equals("Push")) { return new PushSpell(data, caster, dir); }
		else if (name.equals("Abracadabra")) { return new AbracadabraSpell(data, caster, dir); }
		else if (name.equals("Open")) { return new OpenSpell(data, caster, dir); }
		else if (name.equals("Lock")) { return new LockSpell(data, caster, dir); }
		else if (name.equals("Confuse")) { return new ConfuseSpell(data, caster, dir); }
		else if (name.equals("Rejuvenate")) { return new RejuvenateSpell(data, caster, dir); }
		else if (name.equals("Cleanse")) { return new CleanseSpell(data, caster, dir); }
		else if (name.equals("Summon")) { return new SummonSpell(data, caster, dir); }
		else if (name.equals("Clone")) { return new CloneSpell(data, caster, dir); }
		else if (name.equals("Hide")) {  return new HideSpell(data, caster, dir); }
		else if (name.equals("Dash")) {  return new DashSpell(data, caster, dir); }
		else if (name.equals("Felify")) {  return new FelifySpell(data, caster, dir); }
		else if (name.equals("Shield")) {  return new ShieldSpell(data, caster, dir); }

		System.out.println("Spell name \""+name+"\" not found!");
		return null;
	}
	
	@Override
	public void update(){
		super.update();
		changeHealth(-1.5, null);
		_timeSinceCast++;
	}
	
	@Override
	public boolean canCollideWith(Unit u){
		if (u == _caster && _timeSinceCast < 15) return false;
		return true;
	}
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		_netID = Integer.parseInt(networkString[0]);
		_pos = Vector.fromNet(networkString[4]);
		_health = Double.parseDouble(networkString[5]);
		_vel = Vector.fromNet(networkString[6]);
	}
	@Override
	public String toNet() {
		return _netID +
				"\t" + _name +
				"\t" + _caster._netID +
				"\t" + _dir.toNet() +
				"\t" + _pos.toNet() +
				"\t" + _health +
				"\t" + _vel.toNet();
	}
}
