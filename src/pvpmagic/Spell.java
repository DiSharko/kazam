package pvpmagic;

public abstract class Spell extends Unit {
	
	Player _caster;
	
	String _name;
	
	double _damage;
	double _cooldown;
	
	double _velocity = 1;
	

	public Spell(Player caster, Vector pos, Vector dir) {
		_caster = caster;
		_pos = pos;
		
		_vel = dir.normalize().mult(_velocity);
	}
	
	
	public static Spell newSpell(String name, Player caster, Vector pos, Vector dir){
		if (name.equals("Stun")){ return new StunSpell(caster, pos, dir); }
		
		
		System.out.println("Spell name \""+name+"\" not found!");
		return null;
	}
	
	@Override
	public void update(){
		super.update();
		
	}
}
