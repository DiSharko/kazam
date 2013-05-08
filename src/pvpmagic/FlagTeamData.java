package pvpmagic;

import java.util.HashMap;

public class FlagTeamData extends TeamData{
	public static String TYPE = "FlagTeamData";
	
	private FlagPedestal _pedestal;
	 	
	public FlagTeamData(int teamNum, GameData data) {
		super(teamNum, data, TYPE);
	}
	
	public void setPed (FlagPedestal ped) {
		_pedestal = ped;
	}

	@Override
	public void update() {
		//update scores
		if (_pedestal._flag != null) {
			_teamScore += 1;
			_pedestal._flag._pos = _pedestal._flag._originalPos;
			_pedestal._flag._basicImage = "flag";
			_pedestal._flag._collidable = true;
			_pedestal._flag._drawUnder = false;
			_pedestal._flag = null;
		}
	}

	@Override
	public String toNet() {
		String pedestal;
		if (_pedestal == null) { 
			throw new RuntimeException("ERROR: Team " + TEAM_NUM + "'s pedestal is null.");
		} else { 
			pedestal = Integer.toString(_pedestal._netID);
		}
		return _netID +
				"\t" + (_staticObj ? "static" : _type) +
				"\t" + _teamScore +
				"\t" + pedestal;
	}

	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals("FlagTeamData") 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 4) {
			_teamScore = Integer.parseInt(networkString[2]);
			_pedestal = (FlagPedestal) objectMap.get(Integer.parseInt(networkString[3]));
		}
	}
}