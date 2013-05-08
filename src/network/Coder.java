package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pvpmagic.*;
import pvpmagic.spells.Spell;

public class Coder {
	
	private final static int ID = 0;
	private final static int TYPE = 1;

	/**
	 * Called by server game loop on receiving queued update to lobby.
	 * @param players list of players in lobby
	 * @param update command
	 * @throws BadProtocolException poorly formed commands and anything other than connection or DC
	 */
	public static void updateLobby(List<Player> players, String update) throws BadProtocolException {
		try {
			String[] updateArr = update.split("[\t]");
			if (updateArr[0].equals("CONNECTION")) {
				String[] clientData = Arrays.copyOfRange(updateArr, 1, updateArr.length);
				players.add(Player.fromNetInit(clientData));
			} else if (updateArr[0].equals("DISCONNECTION")) {
				Integer ID = Integer.parseInt(updateArr[1]);
				int DCindex = 0;
				int index = 0;
				for (Player player : players) {
					if (player._netID == ID) {
						DCindex = index;
						break;
					}
					index++;
				}
				players.remove(DCindex);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new BadProtocolException("ERROR: Bad protocol.");
		}
	}
	
	/**
	 * Called by server game loop to encode lobby for broadcasting
	 * @param players list of players
	 * @param map integer representing which map is being played
	 * @param lobbyVersion version of lobby since start of hosting
	 * @return lobby encoded as string
	 */
	public static String encodeLobby(List<Player> players, String map, int lobbyVersion) {
		String lobby = lobbyVersion + "\n";
		lobby += map + "\n";
		for (Player player : players) {
			lobby += player._netID + "\t" + player.toNetInit() + "\n";
		}
		return lobby;
	}
	
	/**
	 * Called by client's lobby screen update() to decode lobby update and update lobby if it is indeed
	 * an update over the current lobby version
	 * @param lobbyData Encoded lobby data from server
	 * @param lobby Lobby screen for client
	 * @throws BadProtocolException on protocol from server being invalid, recommend disconnect
	 */
	public static void decodeLobby(String lobbyData, LobbyScreen lobby) throws BadProtocolException {
		try {
			String[] lobbyUpdate = lobbyData.split("[\n]");
			if (Integer.parseInt(lobbyUpdate[0]) > lobby._lobbyVersion) {
				lobby._settings.getElement("selectedMap").name = lobbyUpdate[1];
				lobby._playerList.clear();
				for (int i = 2; i < lobbyUpdate.length; i++) {
					Player player = Player.fromNetInit(lobbyUpdate[i].split("[\t]"));
					lobby._playerList.add(player);
				}
			}
		} catch (Exception e) {
			throw new BadProtocolException("ERROR: Bad protocol.");
		}
	}
	
	/**
	 * Called by server game loop to encode game state for broadcasting.  Adds IDs to
	 * units without IDs at call time (like spells).
	 * @param units list of units in the game state - AFTER update() has been called
	 * @param counter counter storing next available free id
	 * @param tick tick of update. 
	 * @return game state encoded as string
	 */
	public static String encodeGame(List<Unit> units, AtomicInteger counter, int tick) {
		String game = tick + "\n";
		for (Unit u : units) {
			if (u._netID == null) {
				u._netID = counter.getAndIncrement();
			}
			game += u.toNet() + "\n";
		}
		return game;
	}
	
	/**
	 * Called by the client game loop to decode game state update and update GameData's internal unit list.
	 * @param data Client GameData object holding game state.
	 * @param gameState Encoded game state sent from server.
	 * @param staticMap Map containing units that exist from game start to end.
	 * @param dynamicMap Map to contain objects that may disappear between updates (spells).
	 * @throws BadProtocolException If received update deviates from protocol.  Recommend disconnect.  
	 */
	public static void decodeGame(GameData data, String gameState, HashMap<Integer,Unit> staticMap, HashMap<Integer,Unit> dynamicMap) throws BadProtocolException {
		try {
			// delete old spells
			dynamicMap.clear();
			String[] update = gameState.split("[\n]");
			for (int i = 1; i < update.length; i++) { // skip tick line
				String[] objInfo = update[i].split("[\t]");
				Unit obj = staticMap.get(objInfo[ID]);
				if (obj != null) { // update static object
					obj.fromNet(objInfo, staticMap);
				} else { // spell
					// extract values necessary for construction					
					String name = objInfo[TYPE];
					Player caster = (Player) staticMap.get(Integer.parseInt(objInfo[2]));
					Vector dir = Vector.fromNet(objInfo[3]);
					
					// construct new spell
					Spell spell = Spell.newSpell(data, name, caster, dir);
					
					// update other values (id, pos, health) and add to dynamicMap
					spell.fromNet(objInfo, staticMap);
					dynamicMap.put(spell._netID, spell);
				}
			}
			// check for disconnected players and remove them from lobby list and static map
			// leave priority queue and map alone
			ArrayList<Player> removing = new ArrayList<Player>(data._playerList.size());
			for (Player player : data._playerList) {
				if (!player._connected) {
					removing.add(player);
				}
			}
			for (Player player : removing) {
				data._playerList.remove(player);
				staticMap.remove(player._netID);
			}
			
			// update game state
			data.clearList();
			data.addAll(staticMap.values());
			data.addAll(dynamicMap.values());
		} catch (Exception e) {
			throw new BadProtocolException("ERROR: Bad protocol.");
		}
	}
	
	/**
	 * Called by server upon reception of time-appropriate input from client.
	 * @param eventString Encoded click/key press string passed on from client by InputHandler.
	 * @param p The caster, looked up via player id.
	 * @param data Server GameData object to be modified.
	 * @param playerMap Map of players to IDs.
	 * @throws BadProtocolException If the input string deviates protocol.  Recommend disconnection of or ignoring client.
	 */
	public static void handleEvent(String[] eventString, GameData data, HashMap<Integer,Player> playerMap) throws BadProtocolException {
		try {
			// check for disconnection
			if (eventString[0].equals("DISCONNECTION")) {
				Integer ID = Integer.parseInt(eventString[1]);
				playerMap.get(ID)._connected = false;
			} else {
				//eventString is in the format netID \t timestamp \t <data>
				Vector target = Vector.fromNet(eventString[3]);
				Player p = playerMap.get(Integer.parseInt(eventString[0]));
				if (eventString[2].equals("Q")) {
					data.startCastingSpell(p, p._spells[0], target);
				} else if (eventString[2].equals("W")){
					data.startCastingSpell(p, p._spells[1], target);
				} else if (eventString[2].equals("E")){
					data.startCastingSpell(p, p._spells[2], target);
				} else if (eventString[2].equals("R")){
					data.startCastingSpell(p, p._spells[2], target);
				} else if (eventString[2].equals("A")){
					data.startCastingSpell(p, p._spells[4], target);
				} else if (eventString[2].equals("S")){
					data.startCastingSpell(p, p._spells[5], target);
				} else if (eventString[2].equals("D")){
					data.startCastingSpell(p, p._spells[6], target);  
				} else if (eventString[2].equals("F")){
					data.startCastingSpell(p, p._spells[7], target);
				} else if (eventString[2].equals("CLICK")) {
					if (!p._isRooted) {
						p._destination = target;
					}
				}
			}
		} catch (Exception e) {
			throw new BadProtocolException("ERROR: Bad protocol.");
		}
	 }
}
