package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;


public class BurnSpell extends Spell {
	public static String TYPE = "BurnSpell";
	
	public BurnSpell(GameData data, Player caster, Vector target) {
		super(data, TYPE, caster, target);
		_name = "Burn";

		Image sprite = Resource.get("BurnSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(80), 25);
		
		_shape = new Circle(this, new Vector(-8, -8), 8);
		_cooldown = 1500;
		_manaCost = 12;
	}
	

	public void collide(Collision c){
		Unit target = c.other(this);
//		System.out.println("Spell hit "+target._type);
		target.changeHealth(-30,5000);
		target.changeMana(-30,5000);
		_health = 0;
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("BurnSpell"), _pos.minus(8,8), _size);
		v.unrotate();
	}
}
