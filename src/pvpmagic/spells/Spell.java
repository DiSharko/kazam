package pvpmagic.spells;

import pvpmagic.*;

public abstract class Spell extends Unit {
	
	int time = 0;
	
	protected Player _caster;
	protected double _damage;
	public double _cooldown = 0;
	public double _manaCost = 0;
	
	public double _castingTime = 5;
	
	public String _name;
	
	double _velocity = 1;

	Vector _dir;
	

	public Spell(GameData data, String type, Player caster, Vector dir) {
		super(data, type);
		_caster = caster;
		_dir = dir;
//		_shape = new Circle(this, new Vector(0,0), 100);
//		setPosition();
	}
	public void setPosition(){
		_pos = _caster._pos.plus(_caster._size.div(2)).minus(_size.div(2)).plus(_vel.normalize().mult(_caster._size.mag()));
	}
	public void setVelocity(double velocity){
		_velocity = velocity;
		_vel = _dir.normalize().mult(_velocity);
	}
	
	public static Spell newSpell(GameData data, String name, Player caster, Vector dir){
		if (name == null){
			System.out.println("Given a null name!");
			return null;
		}
		
		if (name.equals("Stun")){ return new StunSpell(data, caster, dir); }
		else if (name.equals("Disarm")) { return new DisarmSpell(data, caster, dir); }
		else if (name.equals("Burn")) { return new BurnSpell(data, caster, dir); }
		else if (name.equals("Root")) { return new RootSpell(data, caster, dir); }
		else if (name.equals("Blind")) { return new BlindSpell(data, caster, dir); }
		else if (name.equals("Push")) { return new PushSpell(data, caster, dir); }
		else if (name.equals("Abracadabra")) { return new AbracadabraSpell(data, caster, dir); }
		else if (name.equals("Open")) { return new OpenSpell(data, caster, dir); }
		else if (name.equals("Lock")) { return new LockSpell(data, caster, dir); }
		else if (name.equals("Fear")) { return new FearSpell(data, caster, dir); }
		System.out.println("Spell name \""+name+"\" not found!");
		return null;
	}
	
	@Override
	public void update(){
		super.update();
		changeHealth(-1.5);
		time++;
	}
	
	@Override
	public boolean canCollideWith(Unit u){
		if (u == _caster && time < 10) return false;
		return true;
	}
	
	@Override
	public void fromNet(String networkString) {
		
	}
	
	@Override
	public String toNet() {
		return "d\t" + _dir.toNet();
	}
}
