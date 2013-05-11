package pvpmagic;

import java.util.HashMap;

public class DeathmatchTeamData extends TeamData{
	public static String TYPE = "DeathmatchTeamData";
	 	
	public DeathmatchTeamData(int teamNum, GameData data) {
		super(teamNum, data, TYPE);
	}

	@Override
	public void update() {
		//update scores
		int total = 0;
		for(Player p : _playerList) {
			total += p._kills;
		}
		_teamScore = total;
	}

	@Override
	public String toNet() {
		return _netID +
				"\t" + (_staticObj ? "static" : _type) +
				"\t" + _teamScore;
	}

	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
			_teamScore = Integer.parseInt(networkString[2]);
	}
}