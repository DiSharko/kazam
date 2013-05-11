package pvpmagic;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Unit {
	/**
	 * Network ID and type definition
	 */
	public Integer _netID = null;
	public String _type;
	public Boolean _staticObj;
	
	/**
	 * Internal attributes for drawing and update
	 */
	protected GameData _data;
	protected boolean _delete = false;
	protected boolean _drawUnder = false;
	protected Shape _shape;
	public String _basicImage;
	protected String _oldImage;
	
	public Unit(GameData data, String type, Boolean staticObj, String basic){
		_data = data; 
		_type = type; 
		_staticObj = staticObj;
		_basicImage = basic;
		_oldImage = basic;
	}
	
	public Unit(GameData data, String type, Boolean staticObj){
		_data = data; 
		_type = type; 
		_staticObj = staticObj;
	}
	
	/**
	 * Position and size vectors
	 */
	public Vector _pos;
	public Vector _size;
	
	/**
	 * Velocity and force vectors
	 */
	public Vector _vel = new Vector(0,0),
				_force = new Vector(0,0);
	
	/**
	 * Movement characteristics
	 */
	protected boolean _movable = true;
	boolean _appliesFriction = false;
	public boolean _appliesRestitution = true;
	
	/**
	 * Collision characteristics
	 */
	boolean _collidable = true;
	protected double _restitution = 0.5;
	double _mass = 1;
	
	public void applyForce(Vector force){
		if (_movable) _force = _force.plus(force);
	}
	public void applyForce(float _x, float _y){
		if (_movable) _force = _force.plus(new Vector(_x, _y));
	}
	
	/**
	 * Health and Mana
	 */
	public double _maxHealth = 100;
	public double _maxMana = 100;
	
	public double _health = _maxHealth;
	public double _mana = _maxMana;
	
	/**
	 * Effects that occur over time 
	 */
	public HashMap<String, TimedEffect> _timedEffects = 
			new HashMap<String, TimedEffect>();
	private final int MILLISECONDS_PER_TICK = 25;
	
	public void die() {
		this._delete = true;
		_timedEffects = new HashMap<String, TimedEffect>();
	}
	
	/**
	 * Root effects
	 */
	boolean _canBeRooted = false;
	public boolean _isRooted = false;
	
	public RootEffect root(long time, Player caster){
		if (_canBeRooted) _isRooted = true;
		_vel = new Vector(0,0);
		RootEffect t = new RootEffect(numberOfIntervals(time), caster, this);
		_timedEffects.put(t._type, t);
		return t;
	}
	
	/**
	 * Silence effects
	 */
	boolean _canBeSilenced = false;
	boolean _isSilenced = false;
	
	public void silence(long time, Player caster){ 
		if (_canBeSilenced) _isSilenced = true; 
		TimedEffect t = new SilenceEffect(numberOfIntervals(time), caster, this);
		_timedEffects.put(t._type, t);
	}
	public void kitty(long time, Player caster){ 
		if (_canBeSilenced) _isSilenced = true; 
		TimedEffect t = new KittyEffect(numberOfIntervals(time), caster, this);
		_timedEffects.put(t._type, t);
	}

	public void collide(Collision c){
//		Unit u = c.other(this);
//		if (u._movable){
//			u._pos = u._pos.plus(c.mtv(u).div(2));
//		}
	}
	
	public void update(){	
		for (Entry<String, TimedEffect> e : _timedEffects.entrySet()) {
			if (e.getValue() == null) continue;
			e.getValue().effect();
			if (e.getValue()._effectCompleted) {
				e.setValue(null);
			}
		}
	}
	
	public void draw(View v){}
	
	public void changeMana(double amount, Player caster) {
		_mana += amount;
		if (_mana < 0) _mana = 0;
		else if (_mana > _maxMana) _mana = _maxMana;
		//System.out.println("REDUCED MANA BY: " + amount + " MANA: " + _health);
	}
	
	public void changeHealth(double amount, Player caster) {
		_health += amount;
		if (_health > _maxHealth) _health = _maxHealth;
		//System.out.println("REDUCED HEALTH BY: " + amount + " HP: " + _health);
	}
	
	/**
	 * Increase/decrease mana over time
	 * @param amount Amount to change mana (negative denotes decrease)
	 * @param time Duration of effect in milliseconds
	 */
	public void changeMana(int amount, long time, Player caster) {
		double intervals = numberOfIntervals(time);
		TimedEffect t;
		if (amount < 0) {
			t = new ManaBurnEffect(intervals, changePerInterval(amount, intervals), 
					caster, this);	
		} else {
			t = new ManaBoostEffect(intervals, changePerInterval(amount, intervals), 
					caster, this);
		}
		_timedEffects.put(t._type, t);
		
	}
	/**
	 * Increase/decrease health over time
	 * @param amount Amount to change health (negative denotes decrease)
	 * @param time Duration of effect in milliseconds
	 */
	public void changeHealth(int amount, long time, Player caster) {
		double intervals = numberOfIntervals(time);
		TimedEffect t;
		if (amount < 0) {
			t = new HealthBurnEffect(intervals, changePerInterval(amount, intervals), 
					caster, this);
		} else {
			t = new HealthBoostEffect(intervals, changePerInterval(amount, intervals), 
					caster, this);
		}
		_timedEffects.put(t._type, t);	
	}
	
	protected double numberOfIntervals(long time) {
		return Math.ceil(time/MILLISECONDS_PER_TICK);
	}
	
	private double changePerInterval(int amount, double numberOfIntervals) {
		return amount/numberOfIntervals;
	}
	public void cleanse() {
		for (Entry<String, TimedEffect> e : _timedEffects.entrySet()) {
			if (e.getValue() == null) continue;
			if (e.getValue()._toBeCleansed) {
				e.setValue(null);
			}
		}
	}
	
	public boolean canCollideWith(Unit u){
		return true;
	}
	
	public void setNetID(Integer netID) {
		this._netID = netID;
	}
	
	public abstract String toNet(); //{;
//		return _pos.toNet() +
//				"\t" + _size.toNet() +
//				"\t" + _vel.toNet() +
//				"\t" + _force.toNet() +
//				"\t" + _health +
//				"\t" + _mana +
//				"\t" + _isRooted +
//				"\t" + _isSilenced;
//				
//	}
	public abstract void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap); //{
		//TO-DO: VALIDATION ON STRING
//		_pos = Vector.fromNet(networkString[0]);
//		_size = Vector.fromNet(networkString[1]); 
//		_vel = Vector.fromNet(networkString[2]);
//		_force = Vector.fromNet(networkString[3]);
//		_health = Double.parseDouble(networkString[4]);
//		_mana = Double.parseDouble(networkString[5]);
//		_isRooted = Boolean.parseBoolean(networkString[6]);
//		_isSilenced = Boolean.parseBoolean(networkString[7]);
//	}
}
