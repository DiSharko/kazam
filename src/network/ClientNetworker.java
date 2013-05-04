package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientNetworker implements ClientNetworkable {

	private int _getPort;
	private int _sendPort;
	private Socket _clientGet;
	private Socket _clientSend;
	private ClientGetThread _getThread;
	private ClientSendThread _sendThread;
	private SyncedString _initData;
	private SyncedString _gameData;
	private ConcurrentLinkedQueue<String> _inputs;
	private AtomicBoolean _connected;
	private AtomicBoolean _started;
	
	public ClientNetworker(int getPort, int sendPort, SyncedString initData, SyncedString gameData, ConcurrentLinkedQueue<String> inputs, 
			AtomicBoolean connected, AtomicBoolean started) {
		_getPort = getPort;
		_sendPort = sendPort;
		_initData = initData;
		_gameData = gameData;
		_inputs = inputs;
		_connected = connected;
		_started = started;
	}
	
	@Override
	public void connect(String hostname, String clientData) throws UnknownHostException, NetworkException {
		try {
			// set up input sending connection
			_clientSend = new Socket(hostname, _sendPort);
			_sendThread = new ClientSendThread(_clientSend,_inputs,_connected,clientData);
			_sendThread.start();
			
			// set up game state connection
			_clientGet = new Socket(hostname, _getPort);
			_getThread = new ClientGetThread(_clientGet,_initData,_gameData,_connected,_started);
			_getThread.start();
		} catch (IOException e) {
			throw new NetworkException(e.getMessage());
		}
	}

	@Override
	public void disconnect() {
		// close clients
		_getThread.kill();
		_sendThread.kill();
	}

}
