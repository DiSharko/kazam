package pvpmagic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pvpmagic.spells.Spell;


public class GameData {
	private final int NEEDED = 3;
	ArrayList<Unit> _units;
	ArrayList<Player> _players;

	TeamData _teamdata;
	
	int _lastTick;
	int _clientTick;

	ArrayList<TeamData> _teams;
	ArrayList<Player> _spawning;


	public GameData(){
		_units = new ArrayList<Unit>();
		_players = new ArrayList<Player>();
		_teams = new ArrayList<TeamData>();
		_spawning = new ArrayList<Player>();
	}

	public void setup(SetupScreen s){
		if (s._currentTab.id.equals("hostTab") ||  s._currentTab.id.equals("dedicatedServer")){
			if (s.getElement("selectedMap").name == null || s.getElement("selectedMap").name.equals("Random")) {
				for (int i = 0; i < 20; i++){
					Vector pos = new Vector(Math.random()*600-300, Math.random()*600-300);

					_units.add(new Rock(this, pos, Math.random()*50+20));
				}
			}
			else {
				System.out.println("map name was not random: "+s.getElement("selectedMap").name);
				try {
					readInMap(s.getElement("selectedMap").name);
				} catch (IOException e) {
					System.out.println("IOException in setup.");
					e.printStackTrace();
				}
			}
		}

		if (s._currentTab.id.equals("hostTab")){
			String characterName = s.getElement("selectedCharacter").name;

			String[] spells = new String[8];
			for (int i = 0; i < 8; i++){
				spells[i] = s._spells[i].name;
			}

			Player p = new Player(this, characterName, null, spells);
			Player dummy = new Player(this, "bob", "bobby", null);

			_players.add(p);
			_players.add(dummy);
			
			_teams.get(0).addPlayer(p);
			_teams.get(1).addPlayer(dummy);
			
			_units.add(p);
			_units.add(dummy);
			dummy._pos = new Vector(-50, -30);
			
			// initialize network ticks to invalid values - game starts at 0
			_lastTick = -1;
			_clientTick = -1;

		}
	}

	public void startCastingSpell(Player caster, String spellName, Vector dir){
		if (!caster._isSilenced && caster._spellToCast == null) {
			Spell s = Spell.newSpell(this, spellName, caster, dir);
			if (s != null){
				Long previousCastTime = caster._spellCastingTimes.get(s._name);
				if (previousCastTime == null) previousCastTime = (long) 0;
				if ((System.currentTimeMillis() - previousCastTime) >= s._cooldown && caster._mana > s._manaCost) {
					caster.castSpell(s);
				}
			}
		}
	}
	public void finishCastingSpell(Spell s){
		_units.add(s);
	}


	public void update(){
		String[] useableSpells = {"Lock", "Open", "Summon", "Rejuvenate", "Push", "Fear", "Abracadabra"};
		if (Math.random() < 0.1){
			startCastingSpell(_players.get(1), useableSpells[(int)(Math.random()*useableSpells.length)], _players.get(0)._pos.plus(_players.get(0)._size.div(2)));
		}
		if (Math.random() < 0.09){
			_players.get(1)._mana += 15;
		}
		
		//		String[] useableSpells = {"Lock", "Open", "Summon", "Rejuvenate", "Push", "Fear"};
		//		if (Math.random() < 0.1){
		//			startCastingSpell(_players.get(1), useableSpells[(int)(Math.random()*useableSpells.length)], _players.get(0)._pos.plus(_players.get(0)._size.div(2)));
		//		}
		//		if (Math.random() < 0.09){
		//			_players.get(1)._mana += 15;
		//		}


		// Deleting must be separate, after all updates and collisions
		for (int i = 0; i < _units.size(); i++){
			Unit u = _units.get(i);
			if (u._health <= 0) u.die();
			if (u._delete){
				_units.remove(i);
				i--;
			} else {
				u.update();
			}
		}
		
		//update teams' teamdatas, then check if anyone has one
		for (TeamData td : _teams) td.update();
		for (TeamData td : _teams) {
			td.update();
			if(td._teamScore == NEEDED) {
				//one team has won!!!
				System.out.println("Team "+td.TEAM_NUM+" has won the game!");
			}
		}
		
		//update spawing players
		Player p;
		for(int i = 0; i < _spawning.size(); i++) {
			p = _spawning.get(i);
			if (p._spawnTimer == 0) {
				//done spawning
				System.out.println("player done spawning");
				p._pos = p._spawn;
				p._delete = false;
				p._health = p._maxHealth;
				p._health = p._maxMana;
				_units.add(p);
				_spawning.remove(i);
				i--;
			} else {
				p._spawnTimer -= 1;
			}
		}
		// THIS ORDER IS NECESSARY so that players don't get stuck in walls
		// as they 
		applyMovement();
		collideEntities();
	}


	public void collideEntities(){
		for (int i = 0; i < _units.size(); i++){
			Unit e1 = _units.get(i);
			if (!e1._collidable || e1._shape == null) continue;
			for (int j = i+1; j < _units.size(); j++){
				Unit e2 = _units.get(j);
				if (!e2._collidable || e1 == e2 || e2._shape == null) continue;
				if (!e1.canCollideWith(e2) || !e2.canCollideWith(e1)) continue;
				Collision c = Shape.collide(e1._shape, e2._shape);

				if (c != null && !c.mtv(e1).equals(Vector.NaN) && !c.mtv(e2).equals(Vector.NaN)
						&& !c.mtv(e1).equals(Vector.ZERO) && !c.mtv(e2).equals(Vector.ZERO)){
					double ve1 = e1._vel.dot(c.mtv(e1).normalize());
					double ve2 = e2._vel.dot(c.mtv(e2).normalize());

					double cor = Math.sqrt(e1._restitution*e2._restitution);

					if (e1._movable && e2._movable){
						e1._pos = e1._pos.plus(c.mtv(e1).div(2));
						e2._pos = e2._pos.plus(c.mtv(e2).div(2));

						if (e2._appliesRestitution) e1.applyForce(c.mtv(e1).normalize().mult((c.mtv(e1).normalize().dot(e2._vel.minus(e1._vel)))*e1._mass*e2._mass*(1+cor)/(e1._mass+e2._mass)));
						if (e1._appliesRestitution) e2.applyForce(c.mtv(e2).normalize().mult((c.mtv(e2).normalize().dot(e1._vel.minus(e2._vel)))*e1._mass*e2._mass*(1+cor)/(e1._mass+e2._mass)));
					} else if (e1._movable){
						e1._pos = e1._pos.plus(c.mtv(e1));
						if (e2._appliesRestitution) e1.applyForce(c.mtv(e1).normalize().mult((ve2-ve1)*e1._mass*(1+cor)));
					} else if (e2._movable){
						e2._pos = e2._pos.plus(c.mtv(e2));
						if (e1._appliesRestitution) e2.applyForce(c.mtv(e2).normalize().mult((ve1-ve2)*e2._mass*(1+cor)));
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

			e._vel = e._vel.plus(e._force.div(e._mass));
			if (e._appliesFriction) e._vel = e._vel.mult(0.96);

			e._pos = e._pos.plus(e._vel);
			e._force = new Vector(0,0);
		}
	}

	public void readInMap(String mapname) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/maps/"+mapname+".txt")));
		String line; String[] linearr;
		
		line = br.readLine();
		linearr = line.split(",");
		if(linearr[0].equals("NUMTEAMS")) {
			for (int i = 0; i < Integer.parseInt(linearr[1]); i++) {
				_teams.add(new FlagTeamData(i, this));
			}
		} else {
			System.out.println("NUMBER OF TEAMS NOT IN MAP, invalid map file.");
			System.exit(1);
		}
		
		while((line = br.readLine()) != null) {
			//FOR NOW, WE ARE ALWAYS MULTIPLYING Y BY NEGATIVE, EVERY SINGLE ONE
			linearr = line.split(",");
			if(linearr[0].equals("ROCK")) {
				//line represents a rock: ROCK,500,500,150
				Vector pos = new Vector(Double.parseDouble(linearr[1]),-1.0*Double.parseDouble(linearr[2]));
				_units.add(new Rock(this, pos, Double.parseDouble(linearr[3])));

			} else if(linearr[0].equals("SPAWN")) {
				//line represents a spawn point
				Vector spawn = new Vector(Double.parseDouble(linearr[2]),-1.0*Double.parseDouble(linearr[3]));
				_teams.get(Integer.parseInt(linearr[1])).addSpawn(spawn);
			} else if (linearr[0].equals("PILLAR")) {
				Vector pos = new Vector(Double.parseDouble(linearr[1]),-1.0*Double.parseDouble(linearr[2]));
				_units.add(new Pillar(this, pos, Double.parseDouble(linearr[3])));

			} else if (linearr[0].equals("HWALL")) {
				Vector pos = new Vector(Double.parseDouble(linearr[1]),-1.0*Double.parseDouble(linearr[2]));
				_units.add(new Wall(this, pos, Double.parseDouble(linearr[3]), false));

			} else if (linearr[0].equals("VWALL")) {
				Vector pos = new Vector(Double.parseDouble(linearr[1]),-1.0*Double.parseDouble(linearr[2]));
				_units.add(new Wall(this, pos, Double.parseDouble(linearr[3]), true));

			} else if(linearr[0].equals("DOOR")) {
				//line represents a door: DOOR,500,500,250,250,50
				Vector lockpos = new Vector(Double.parseDouble(linearr[1]), -1.0*Double.parseDouble(linearr[2]));
				_units.add(new Door(this, lockpos, Double.parseDouble(linearr[3])));

			} else if(linearr[0].equals("FLAG")) {
				//line represents a flag: FLAG,500,500,50
				Vector pos = new Vector(Double.parseDouble(linearr[1]), -1.0*Double.parseDouble(linearr[2]));
				_units.add(new Flag(this, pos, Double.parseDouble(linearr[3])));

			} else if (linearr[0].equals("PEDASTAL")) {
				Vector pos = new Vector(Double.parseDouble(linearr[2]), -1.0*Double.parseDouble(linearr[3]));
				FlagPedestal pd = new FlagPedestal(this, pos, Double.parseDouble(linearr[4]));
				_units.add(pd);

				FlagTeamData ft = (FlagTeamData) _teams.get(Integer.parseInt(linearr[1]));
				ft.setPed(pd);
			} else {
				System.out.println(line);
				System.out.println("Not enough types in map file being checked for.");
			}
		}
		br.close();
	}

}
