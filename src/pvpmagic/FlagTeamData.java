package pvpmagic;

import java.util.HashMap;

public class FlagTeamData extends TeamData{
	public static String TYPE = "FlagTeamData";
	
	private FlagPedestal _pedestal;
	 	
	public FlagTeamData(int teamNum, GameData data) {
		super(teamNum, data);
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
			_pedestal._flag._delete = false;
			_pedestal._flag._data._units.add(_pedestal._flag);
			_pedestal._flag = null;
		}
	}

	@Override
	public String toNet() {
		return _type +
				"\t" + _teamScore;
	}

	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals("FlagTeamData") 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 3) {
			_teamScore = Integer.parseInt(networkString[2]);
		}
	}
}