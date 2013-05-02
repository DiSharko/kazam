package pvpmagic;

import java.util.LinkedList;

public abstract class Unit {
	protected GameData _data;
	
	public Vector _pos;
	protected Vector _size;
	protected Vector _vel = new Vector(0,0), _force = new Vector(0,0);
	boolean _movable = true;
	
	public void applyForce(Vector force){
		if (_movable) _force = _force.plus(force);
	}
	public void applyForce(float _x, float _y){
		if (_movable) _force = _force.plus(new Vector(_x, _y));
	}

	boolean _collidable = true;
	double _restitution = 0.5;
	double _mass = 1;
	
	public double _maxHealth = 100;
	public double _maxMana = 100;
	
	protected double _health = _maxHealth;
	protected double _mana = _maxMana;
	
	public String _type;
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
		timedEffects.add(new SilenceEffect(numberOfIntervals(time), this));
	}

	boolean _delete = false;
	
	protected Shape _shape;
	
	protected LinkedList<TimedEffect> timedEffects = new LinkedList<TimedEffect>();
	private final int MILLISECONDS_PER_TICK = 25;
	
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
				System.out.println("ENDING TIMED SPELL: HP - " + _health + " MANA - " + _mana);
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
		if (_health < 0) _health = 0;
		else if (_health > _maxHealth) _health = _maxHealth;
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
		timedEffects.removeAll(toBeCleansed);
	}
	
	public boolean canCollideWith(Unit u){
		return true;
	}
	
	public String toNet(){
		return "";
	}
	public void fromNet(String s){
		
	}
}
