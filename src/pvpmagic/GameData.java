package pvpmagic;

import java.util.ArrayList;

import pvpmagic.spells.Spell;


public class GameData {

	ArrayList<Unit> _units;

	ArrayList<Player> _players;

	public GameData(){
		_units = new ArrayList<Unit>();
		_players = new ArrayList<Player>();

		for (int i = 0; i < 20; i++){
			Vector pos = new Vector(Math.random()*600-300, Math.random()*600-300);
			Vector size = new Vector(Math.random()*50+20, Math.random()*50+20);
			_units.add(new Rock(this, pos, size));
		}
	}

	public Player addPlayer(SetupScreen s){
		String characterName = s.getElement("selectedCharacter").name;

		String[] spells = new String[8];
		for (int i = 0; i < 8; i++){
			spells[i] = s._spells[i].name;
		}

		Player p = new Player(this, characterName, null, spells);
		_players.add(p);
		_units.add(p);

		return p;
	}

	public void startCastingSpell(Player caster, String name, Vector dir){
		Spell s = Spell.newSpell(this, name, caster, dir);
		if (s != null){
			caster.castSpell(s);
		}
	}
	public void finishCastingSpell(Spell s){
		_units.add(s);
	}

	public void update(){

		collideEntities();
		applyMovement();
		
		// Deleting must be separate, after all updates and collisions
		for (int i = 0; i < _units.size(); i++){
			Unit u = _units.get(i);
			if (u._delete){
				_units.remove(i);
				i--;
			} else {
				u.update();
			}
		}
	}
	
	public void oldCollision(){
		for (Unit u : _units){
			// Collision
			for (Unit u2 : _units){
				if (Function.collides(u._pos, u._size, u2._pos, u2._size)){
					u.hit(u2);
				}
			}
		}
	}
	
	
	public void collideEntities(){
		for (int i = 0; i < _units.size(); i++){
			Unit e1 = _units.get(i);
			if (!e1._collidable || e1._shape == null) continue;
			for (int j = i+1; j < _units.size(); j++){
				Unit e2 = _units.get(j);
				if (!e2._collidable || e1 == e2 || e2._shape == null) continue;
				Collision c = Shape.collide(e1._shape, e2._shape);

				if (c != null){
					double ve1 = e1._vel.dot(c.mtv(e1).normalize());
					double ve2 = e2._vel.dot(c.mtv(e2).normalize());

					double cor = Math.sqrt(e1._restitution*e2._restitution);

					if (e1._movable && e2._movable){
						e1._pos = e1._pos.plus(c.mtv(e1).div(2));
						e2._pos = e2._pos.plus(c.mtv(e2).div(2));

						e1.applyImpulse(c.mtv(e1).normalize().mult((c.mtv(e1).normalize().dot(e2._vel.minus(e1._vel)))*e1._mass*e2._mass*(1+cor)/(e1._mass+e2._mass)));
						e2.applyImpulse(c.mtv(e2).normalize().mult((c.mtv(e2).normalize().dot(e1._vel.minus(e2._vel)))*e1._mass*e2._mass*(1+cor)/(e1._mass+e2._mass)));
					} else if (e1._movable){
						e1._pos = e1._pos.plus(c.mtv(e1));
						e1.applyImpulse(c.mtv(e1).normalize().mult((ve2-ve1)*e1._mass*(1+cor)));

					} else if (e2._movable){
						e2._pos = e2._pos.plus(c.mtv(e2));
						e2.applyImpulse(c.mtv(e2).normalize().mult((ve1-ve2)*e2._mass*(1+cor)));
						System.out.println(c.mtv(e2).normalize().mult((ve1-ve2)*e2._mass*(1+cor)));
					}

					e1.collide(c);
					e2.collide(c);
					e1._shape.colliding = true;
					e2._shape.colliding = true;
				}
			}
		}
	}


	public void applyMovement(){
		for (int i = 0; i < _units.size(); i++){
			Unit e = _units.get(i);
			if (!e._movable) continue;

			
			e._vel = e._vel.plus(e._force.div(e._mass)).plus(e._impulse.div(e._mass));
			
//			e._vel = new Vector(Math.min(Math.max(e._vel.x, -30), 30),Math.min(Math.max(e._vel.y, -50), 50));

			e._pos = e._pos.plus(e._vel);
			e._impulse = new Vector(0,0);
			e._force = new Vector(0,0);
		}
	}
	
}
