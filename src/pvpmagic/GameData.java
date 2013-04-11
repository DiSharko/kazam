package pvpmagic;

import java.util.ArrayList;


public class GameData {

	ArrayList<Unit> _units;

	ArrayList<Player> _players;

	public GameData(){
		_units = new ArrayList<Unit>();
		_players = new ArrayList<Player>();

		for (int i = 0; i < 20; i++){
			Vector pos = new Vector(Math.random()*600-300, Math.random()*600-300);
			Vector size = new Vector(Math.random()*50+20, Math.random()*50+20);
			_units.add(new Rock(pos, size));
		}
	}

	public Player addPlayer(SetupScreen s){
		String characterName = s.getElement("selectedCharacter").name;

		String[] spells = new String[8];
		for (int i = 0; i < 8; i++){
			spells[i] = s._spells[i].name;
		}

		Player p = new Player(characterName, null, spells);
		_players.add(p);
		_units.add(p);

		return p;
	}

	public void castSpell(Player caster, String name, Vector dir){
		caster.stop();
		Spell s = Spell.newSpell(name, caster, dir);
		if (s != null){
			_units.add(s);
		}
	}

	public void update(){

		for (Unit u : _units){
			// Collision
			for (Unit u2 : _units){
				if (Function.collides(u._pos, u._size, u2._pos, u2._size)){
					u.hit(u2);
				}
			}
		}

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
	
}
