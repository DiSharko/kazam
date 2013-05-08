package pvpmagic;

import java.util.ArrayList;

public abstract class TeamData extends Unit{
	public static Boolean STATICOBJ = true;
	
	protected ArrayList<Vector> _spawnList;
	protected ArrayList<Player> _playerList;
	protected int _teamScore = 0;
	protected final int TEAM_NUM;
	
	private int spawnIndex;
	
	public TeamData (int teamNum, GameData data, String type) {
		super(data, type, STATICOBJ, null);
		_spawnList = new ArrayList<Vector>();
		_playerList = new ArrayList<Player>();
		TEAM_NUM = teamNum;
	}
	
	@Override
	public abstract void update();
	
	public void addSpawn(Vector pos) {
		_spawnList.add(pos);
	}
	
	public void addPlayer(Player player) {
		spawnIndex = _playerList.size();
		_playerList.add(player);
		player._spawn = _spawnList.get(spawnIndex);
		player._pos = player._spawn;
		player._teamNum = TEAM_NUM;
	}
}
