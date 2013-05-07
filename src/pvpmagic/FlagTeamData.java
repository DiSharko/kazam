package pvpmagic;

public class FlagTeamData extends TeamData{
	private FlagPedastal _team1Ped;
	private FlagPedastal _team2Ped;
	 	
	public FlagTeamData(int winNum) {
		super(winNum);
	}
	
	public void setTeam1Ped (FlagPedastal ped) {
		_team1Ped = ped;
	}
	public void setTeam2Ped (FlagPedastal ped) {
		_team2Ped = ped;
	}

	@Override
	public void update() {
		//update scores
		if (_team1Ped._score != null) {
			_team1Score += 1;
			_team1Ped._score._pos = _team1Ped._score._originalPos;
			_team2Ped._score._delete = false;
			_team1Ped._score._data._units.add(_team1Ped._score);
			_team1Ped._score = null;
		} else if (_team2Ped._score != null) {
			_team2Score += 1;
			_team2Ped._score._pos = _team2Ped._score._originalPos;
			_team2Ped._score._delete = false;
			_team2Ped._score._data._units.add(_team2Ped._score);
			_team2Ped._score = null;
		}
		
		//check if someone has won
		if (_team1Score == NEEDED) {
			_winTeam = "Team 1";
		} else if (_team2Score == NEEDED) {
			_winTeam = "Team 2";
		}
	}
}