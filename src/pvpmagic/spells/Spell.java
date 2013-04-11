package pvpmagic.spells;

import pvpmagic.*;

public abstract class Spell extends Unit {
	
	protected Player _caster;
	protected double _damage;
	protected double _cooldown;
	
	public double _castingTime = 10;
	
	String _name;
	
	double _velocity = 1;
	
	Vector _dir;
	

	public Spell(GameData data, Player caster, Vector dir) {
		super(data, "spell");
		_caster = caster;
		_pos = caster._pos;
		_dir = dir;
		setVelocity();
	}
	public Spell(GameData data, Vector pos, Vector dir){
		super(data, "spell");
		_pos = pos;
		_dir = dir;
		setVelocity();
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
		
		System.out.println("Spell name \""+name+"\" not found!");
		return null;
	}
	
	@Override
	public void update(){
		super.update();
		
	}
}
