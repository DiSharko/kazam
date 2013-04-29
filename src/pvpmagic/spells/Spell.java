package pvpmagic.spells;

import pvpmagic.*;

public abstract class Spell extends Unit {
	
	int time = 0;
	
	protected Player _caster;
	protected double _damage;
	protected double _cooldown;
	
	public double _castingTime = 5;
	
	String _name;
	
	double _velocity = 1;

	Vector _dir;
	

	public Spell(GameData data, Player caster, Vector dir) {
		super(data, "spell");
		_caster = caster;
		_pos = caster._pos;
		_dir = dir;
		setVelocity();
		_size = new Vector(10, 10);
		_shape = new Box(this, new Vector(0,0), _size);
	}
	public Spell(GameData data, Vector pos, Vector dir){
		super(data, "spell");
		_pos = pos;
		_dir = dir;
		setVelocity();
		_size = new Vector(10, 10);
		_shape = new Box(this, new Vector(0,0), _size);
	}
	public void setVelocity(){
		_vel = _dir.normalize().mult(_velocity);
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

		System.out.println("Spell name \""+name+"\" not found!");
		return null;
	}
	
	@Override
	public void update(){
		super.update();
		time++;
	}
	
	@Override
	public boolean canCollideWith(Unit u){
		if (u == _caster && time < 10) return false;
		return true;
	}
}
