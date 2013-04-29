package pvpmagic.spells;

import pvpmagic.*;

public abstract class Spell extends Unit {
	
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
		_pos = _pos.plus(_vel.mult(_size.mag()*2));
		_shape = new Box(this, new Vector(0,0), _size);
	}
	public Spell(GameData data, Vector pos, Vector dir){
		super(data, "spell");
		_pos = pos;
		_dir = dir;
		setVelocity();
		_size = new Vector(10, 10);
		_pos = _pos.plus(_vel.mult(_size));
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
		/*switch (name) {
			case "Stun": return new StunSpell(data, caster, dir);
			case "Disarm": return new DisarmSpell(data, caster, dir);
			case "Burn": return new BurnSpell(data, caster, dir);
			case "Root": return new RootSpell(data, caster, dir);
			case "Blind": return new BlindSpell(data, caster, dir);
			case "Push": return new PushSpell(data, caster, dir);
			case "Abracadabra": return new AbracadabraSpell(data, caster, dir);
			default: System.out.println("Spell name \""+name+"\" not found!");
		}*/
		
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
		
	}
}
