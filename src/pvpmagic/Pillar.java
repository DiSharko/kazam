package pvpmagic;

import java.awt.Image;
import java.util.HashMap;

public class Pillar extends Unit {
		public static Boolean STATICOBJ = true;
		public static String TYPE = "pillar";
		
		public Pillar(GameData data, Vector pos, double size, String basicImage){
			super(data, TYPE, STATICOBJ, basicImage);
			_pos = pos;
			Image sprite = Resource.get(_basicImage);
			_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(size);
			
			_movable = false;
			
			_shape = new Box(this, new Vector(0,0), _size);
			_restitution = 0.8;
		}
		
		public void draw(View v){
			super.draw(v);
			v.drawImage(Resource.get(_basicImage), _pos, _size);
		}

		@Override
		public void collide(Collision c){
			//nothing happens when something collides with it
		}
		
		@Override
		public void changeHealth(double health){}

		@Override
		public String toNet() {
			return _netID + 
					"\t" + (_staticObj ? "static" : _type);
		}

		@Override
		public void fromNet(String[] networkString,
				HashMap<Integer, Unit> objectMap) {}

}
