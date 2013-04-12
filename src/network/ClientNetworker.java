package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNetworker implements ClientNetworkable {

	private int PORT;
	private String _hostname;
	private Socket _initClient;
	private TCPClientThread _tcpThread;
	private SyncedString _initData;
	private SyncedString _gameData;
	
	public ClientNetworker(int PORT, String hostname, SyncedString initData, SyncedString gameData) {
		this.PORT = PORT;
		_hostname = hostname;
		_initData = initData;
		_gameData = gameData;
	}
	
	@Override
	public void connect(String hostname, String clientData) throws UnknownHostException, NetworkException {
		try {
			// tcp connection for game lobby connection
			_initClient = new Socket(hostname, PORT);
			_tcpThread = new TCPClientThread(_initClient,_initData,clientData);
			_tcpThread.run();
		} catch (IOException e) {
			throw new NetworkException(e.getMessage());
		}

	}

	@Override
	public void disconnect() {
		_hostname = null;
		try {
			_initClient.close();
		} catch (IOException e) {
			
		}
		_initData.setData("DISCONNECTED");
	}

}
