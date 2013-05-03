package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientGetThread extends Thread {
	
	private Socket _client;
	private SyncedString _initData;
	private SyncedString _gameData;
	private AtomicBoolean _connected;
	private AtomicBoolean _started;
	
	public ClientGetThread(Socket client, SyncedString initData, SyncedString gameData, AtomicBoolean connected, AtomicBoolean started) {
		_client = client;
		_initData = initData;
		_gameData = gameData;
		_connected = connected;
		_started = started;
	}
	
	@Override
	public void run() {
		try {			
			// initialize reader stuff
			BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			String line = "";
			String buffer = "";
			
			SyncedString curData = _initData;
			
			// get updates in chunks delineated by "EOT" lines
			while (_connected.get()) {
				line = reader.readLine();
				if (line != null) {
					if (line.equals("START")) {
						curData = _gameData;
						_started.set(true);
					} else if (line.equals("EOT")) {
						curData.setData(buffer);
						buffer = "";
					} else {
						buffer += line;
					}
				}
			}
			
			// execute cleanup
			kill();
			
		} catch (IOException e) { // includes SocketExceptions thrown by blocked I/O on closed socket
			kill();
		}
	}
	
	public void kill() {
		if (!_client.isClosed()) {
			try {
				_client.close();
			} catch (IOException e1) {
				// should be unreachable
				// ignore as disconnecting
			}
		}
		_connected.set(false);
	}
}
