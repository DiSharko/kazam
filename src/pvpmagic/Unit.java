package pvpmagic;

import java.util.LinkedList;

public abstract class Unit {
	/**
	 * Network ID and type definition
	 */
	public Integer _netID = null;
	public String _type;
	
	/**
	 * Internal attributes for drawing and update
	 */
	protected GameData _data;
	protected boolean _delete = false;
	protected Shape _shape;
	
	public Unit(GameData data, String type){ _data = data; _type = type; }
	
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
	boolean _movable = true;
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
	
	protected double _health = _maxHealth;
	protected double _mana = _maxMana;
	
	/**
	 * Effects that occur over time 
	 */
	public LinkedList<TimedEffect> timedEffects = new LinkedList<TimedEffect>();
	private final int MILLISECONDS_PER_TICK = 25;
	
	/**
	 * Root effects
	 */
	boolean _canBeRooted = false;
	boolean _isRooted = false;
	
	public void root(long time){
		if (_canBeRooted) _isRooted = true;
		_vel = new Vector(0,0);
		timedEffects.add(new RootEffect(numberOfIntervals(time), this));
	}
	
	/**
	 * Silence effects
	 */
	boolean _canBeSilenced = false;
	boolean _isSilenced = false;
	
	public void silence(long time){ 
		if (_canBeSilenced) _isSilenced = true; 
		timedEffects.add(new SilenceEffect(numberOfIntervals(time), this));
	}


	public void collide(Collision c){
//		Unit u = c.other(this);
//		if (u._movable){
//			u._pos = u._pos.plus(c.mtv(u).div(2));
//		}
	}
	
	public void update(){
		LinkedList<TimedEffect> completedEffects = new LinkedList<TimedEffect>();
		
		for (TimedEffect e : timedEffects) {
			e.effect();
			if (e.effectCompleted) {
				completedEffects.add(e);
			}
		}
		
		timedEffects.removeAll(completedEffects);
	
	}
	
	public void draw(View v){}
	
	public void changeMana(double amount) {
		_mana += amount;
		if (_mana < 0) _mana = 0;
		else if (_mana > _maxMana) _mana = _maxMana;
		//System.out.println("REDUCED MANA BY: " + amount + " MANA: " + _health);
	}
	
	public void changeHealth(double amount) {
		_health += amount;
		if (_health > _maxHealth) _health = _maxHealth;
		//System.out.println("REDUCED HEALTH BY: " + amount + " HP: " + _health);
	}
	
	/**
	 * Increase/decrease mana over time
	 * @param amount Amount to change mana (negative denotes decrease)
	 * @param time Duration of effect in milliseconds
	 */
	public void changeMana(int amount, long time) {
		//System.out.println("STARTING MANA SPELL: HP - " + _health + " MANA - " + _mana);
		double intervals = numberOfIntervals(time);
		timedEffects.add(new ManaEffect(intervals,changePerInterval(amount, intervals), this));
	}
	/**
	 * Increase/decrease health over time
	 * @param amount Amount to change health (negative denotes decrease)
	 * @param time Duration of effect in milliseconds
	 */
	public void changeHealth(int amount, long time) {
		//System.out.println("STARTING HEALTH SPELL: HP - " + _health + " MANA - " + _mana);
		double intervals = numberOfIntervals(time);
		timedEffects.add(new HealthEffect(intervals, changePerInterval(amount, intervals), this));
	}
	
	protected double numberOfIntervals(long time) {
		return Math.ceil(time/MILLISECONDS_PER_TICK);
	}
	
	private double changePerInterval(int amount, double numberOfIntervals) {
		return amount/numberOfIntervals;
	}
	public void cleanse() {
		LinkedList<TimedEffect> toBeCleansed = new LinkedList<TimedEffect>();
		for (TimedEffect e : timedEffects) {
			if (e instanceof HealthEffect) {
				HealthEffect temp = (HealthEffect) e;
				if (temp._changePerInterval < 0) {
					toBeCleansed.add(e);
				}
			} else if (e instanceof ManaEffect) {
				ManaEffect temp = (ManaEffect) e;
				if (temp._changePerInterval < 0) {
					toBeCleansed.add(e);
				}
			} else if (e instanceof SilenceEffect || e instanceof RootEffect
					|| e instanceof FearEffect) {
				toBeCleansed.add(e);
			}
		}
		
		System.out.println("TO BE CLEANSED: " + toBeCleansed.toString());
		timedEffects.removeAll(toBeCleansed);
	}
	
	public boolean canCollideWith(Unit u){
		return true;
	}
	
	public void setNetID(Integer netID) {
		this._netID = netID;
	}
	
	public String toNet() {;
		return _pos.toNet() +
				"\t" + _size.toNet() +
				"\t" + _vel.toNet() +
				"\t" + _force.toNet() +
				"\t" + _health +
				"\t" + _mana +
				"\t" + _isRooted +
				"\t" + _isSilenced;
				
	}
	public void fromNet(String[] networkString) {
		//TO-DO: VALIDATION ON STRING
		_pos = Vector.fromNet(networkString[0]);
		_size = Vector.fromNet(networkString[1]); 
		_vel = Vector.fromNet(networkString[2]);
		_force = Vector.fromNet(networkString[3]);
		_health = Double.parseDouble(networkString[4]);
		_mana = Double.parseDouble(networkString[5]);
		_isRooted = Boolean.parseBoolean(networkString[6]);
		_isSilenced = Boolean.parseBoolean(networkString[7]);
	}
}
