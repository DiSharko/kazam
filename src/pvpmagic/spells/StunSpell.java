package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;

public class StunSpell extends Spell {
	public static String TYPE = "StunSpell";
	double scale = 50;
	
	
	public StunSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_cooldown = 1000;
		Image sprite = Resource._gameImagesAlpha.get("StunSpell");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(scale);
		_name = "Stun";
		_cooldown = 1000;
		_manaCost = 10;
		_shape = new Circle(this, new Vector(0,0), 8);
		setVelocity(10);
		setPosition();
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
		v.rotate(_vel, _pos.plus(7));
		v.drawImage(Resource._gameImagesAlpha.get("StunSpell"), _pos, _size);
		v.unrotate();
	}
}