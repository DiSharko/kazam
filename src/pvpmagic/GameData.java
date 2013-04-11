package pvpmagic;

import java.util.ArrayList;


public class GameData {
	
	ArrayList<Unit> _units;
	
	ArrayList<Player> _players;
	
	public GameData(){
		_units = new ArrayList<Unit>();
		_players = new ArrayList<Player>();
		
		
		_units.add(Spell.newSpell("Stun", null, new Vector(20,20), new Vector(1,1)));
		
		
	}
	
	public void addPlayer(SetupScreen s){
		String characterName = s.getElement("selectedCharacter").name;
		
		String[] spells = new String[8];
		for (int i = 0; i < 8; i++){
			spells[i] = s._spells[i].name;
		}
		
		Player p = new Player(characterName, null, spells);
		_players.add(p);
		_units.add(p);
	}
	
	
	public void update(){
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
