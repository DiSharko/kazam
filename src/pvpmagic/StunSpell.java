package pvpmagic;

public class StunSpell extends Spell {
	
	public StunSpell(Player caster, Vector pos, Vector dir){
		super(caster, pos, dir);
		_size = new Vector(30, 30);
	}

	@Override
	public void hit(Unit u){
		u.stun(10);
	}
	
	@Override
	public void draw(View v){
		v.fillCircle(_pos, 10);
	}
}
