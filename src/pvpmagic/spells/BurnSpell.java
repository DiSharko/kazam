package pvpmagic.spells;

import pvpmagic.*;


public class BurnSpell extends Spell {
	public static String TYPE = "BurnSpell";

	double _scale = .2;
	
	public BurnSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Burn";
		double w = Resource._gameImagesAlpha.get("BurnSpell").getWidth(null);
		double h = Resource._gameImagesAlpha.get("BurnSpell").getHeight(null);
		_size = new Vector(w*_scale, h*_scale);
		_shape = new Circle(this, new Vector(0, 0), 8);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(15);
		setPosition();
	}
	

	public void collide(Collision c){
		Unit target = c.other(this);
		System.out.println("Spell hit "+target.type());
		target.changeHealth(-30,5000);
		target.changeMana(-30,5000);
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos.plus(_shape.getBounds()._size.x/2));
		v.drawImage(Resource._gameImagesAlpha.get("BurnSpell"), _pos, _size);
		v.unrotate();
	}
}
