package pvpmagic.spells;

import pvpmagic.*;

public class StunSpell extends Spell {
	public static String TYPE = "StunSpell";
	double scale = .2;
	
	
	public StunSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_cooldown = 1000;
		int w = Resource._gameImagesAlpha.get("StunSpell").getWidth(null);
		int h = Resource._gameImagesAlpha.get("StunSpell").getHeight(null);
		_size = new Vector(w*scale, h*scale);
		setVelocity(10);
	}

	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.root(2000);
		target.silence(2000);
		target.changeHealth(-5);
	}
	
	@Override
	public void draw(View v){
//		System.out.println(StunSpell._type + ", "+_type);
		v.rotate(_vel, _pos.plus(5));
		v.drawImage(Resource._gameImagesAlpha.get("StunSpell"), _pos, _size);
		v.unrotate();
	}
}