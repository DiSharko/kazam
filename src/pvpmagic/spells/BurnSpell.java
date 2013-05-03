package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;


public class BurnSpell extends Spell {
	public static String TYPE = "BurnSpell";

	double _scale = 80;
	
	public BurnSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Burn";

		Image sprite = Resource._gameImages.get("BurnSpell");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(_scale);
		
		_shape = new Circle(this, new Vector(0, 0), 8);
		_cooldown = 1000;
		_manaCost = 10;
		setProperties(_size, 15);
	}
	

	public void collide(Collision c){
		Unit target = c.other(this);
		System.out.println("Spell hit "+target._type);
		target.changeHealth(-30,5000);
		target.changeMana(-30,5000);
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos.plus(_shape.getBounds()._size.x/2));
		v.drawImage(Resource._gameImages.get("BurnSpell"), _pos, _size);
		v.unrotate();
	}
}
