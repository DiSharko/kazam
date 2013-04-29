package pvpmagic;

import java.util.LinkedList;


public abstract class Unit {
	protected GameData _data;
	
	public Vector _pos;
	protected Vector _size;
	protected Vector _vel = new Vector(0,0), _impulse = new Vector(0,0), _force = new Vector(0,0);
	boolean _movable = true;
	
	public void applyForce(Vector _force){
		if (_movable) _force = _force.plus(_force);
	}
	public void applyForce(float _x, float _y){
		if (_movable) _force = _force.plus(new Vector(_x, _y));
	}

	public void applyImpulse(Vector impulse){
		if (_movable) _impulse = impulse.plus(impulse);
	}
	public void applyImpulse(float _x, float _y){
		if (_movable) _impulse = _impulse.plus(new Vector(_x, _y));
	}	
	
	boolean _collidable = true;
	double _restitution = 0.7;
	double _mass = 1;
	
	public double _maxHealth = 100;
	public double _maxMana = 100;
	
	protected double _health = _maxHealth;
	protected double _mana = _maxMana;
		
	private String _type;
	public Unit(GameData data, String type){ _data = data; _type = type; }
	public String type(){ return _type; }
	
	boolean _canBeRooted = false;
	boolean _isRooted = false;
	
	public void root(long time){
		if (_canBeRooted) _isRooted = true; 
		timedEffects.add(new RootEffect(numberOfIntervals(time), this));
	}

	boolean _canBeSilenced = false;
	boolean _isSilenced = false;
	
	public void silence(long time){ 
		if (_canBeSilenced) _isSilenced = true; 
		timedEffects.add(new RootEffect(numberOfIntervals(time), this));
	}

	boolean _delete = false;
	
	protected Shape _shape;
	
	private LinkedList<TimedEffect> timedEffects = new LinkedList<TimedEffect>();
	private final int MILLISECONDS_PER_TICK = 25;
	
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u._movable){
			u._pos = u._pos.plus(c.mtv(u).div(2));
		}
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
	}
	
	public void changeHealth(double amount) {
		_health += amount;
		if (_health < 0) _health = 0;
		else if (_health > _maxHealth) _health = _maxHealth;
	}
	
	/**
	 * Increase/decrease mana over time
	 * @param amount Amount to change mana (negative denotes decrease)
	 * @param time Duration of effect in milliseconds
	 */
	public void changeMana(int amount, long time) {
		double intervals = numberOfIntervals(time);
		timedEffects.add(new ManaEffect(changePerInterval(amount, intervals), intervals, this));
	}
	/**
	 * Increase/decrease health over time
	 * @param amount Amount to change health (negative denotes decrease)
	 * @param time Duration of effect in milliseconds
	 */
	public void changeHealth(int amount, long time) {
		double intervals = numberOfIntervals(time);
		timedEffects.add(new HealthEffect(changePerInterval(amount, intervals), intervals, this));
	}
	
	private double numberOfIntervals(long time) {
		return Math.ceil(time/MILLISECONDS_PER_TICK);
	}
	
	private double changePerInterval(int amount, double numberOfIntervals) {
		return amount/numberOfIntervals;
	}
}
