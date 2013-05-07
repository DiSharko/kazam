package pvpmagic;

import java.awt.Image;
import java.util.HashMap;

public class Pillar extends Unit {
		public static String TYPE = "Pillar";
		
		public Pillar(GameData data, Vector pos, double size){
			super(data, TYPE);
			_pos = pos;
			Image sprite = Resource.get(TYPE);
			_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(size);
			
			_movable = false;
			
			_shape = new Box(this, new Vector(0,0), _size);
			_restitution = 0.8;
		}
		
		public void draw(View v){
			v.drawImage(Resource.get(TYPE), _pos, _size);
		}

		@Override
		public void collide(Collision c){
			//nothing happens when something collides with it
		}
		
		@Override
		public void changeHealth(double health){}

		@Override
		public String toNet() {
			// TODO Auto-generated method stub
			return _type;
		}

		@Override
		public void fromNet(String[] networkString,
				HashMap<Integer, Unit> objectMap) {}

}
