package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPClientThread extends Thread {
	
	private Socket _client;
	private SyncedString _initData;
	private String _clientData;
	
	public TCPClientThread(Socket client, SyncedString initData, String clientData) {
		_client = client;
		_initData = initData;
		_clientData = clientData;
	}
	
	@Override
	public void run() {
		try {
			// send client data to server
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
			writer.write(_clientData + "EOT\n");
			writer.flush();
			
			// initialize reader stuff
			BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			String line = reader.readLine();
			String buffer = "";
			
			// get updates in chunks delineated by "EOT" lines
			while (line != null) {
				if (line.equals("EOT")) {
					_initData.setData(buffer);
					buffer = "";
				} else {
					buffer += line;
				}
				line = reader.readLine();
			}
			
			// disconnect without changing initData
			// initData should either soon read "DISCONNECTED" if socket was closed by client disconnect
			// contain something that indicates it was closed gracefully by the server.
			// random disconnects must be handled by timeouts in the main game loop checking to see if
			// the data was updated within the timeout period
		} catch (IOException e) { // includes SocketExceptions
			_initData.setData("DISCONNECTED");
		}
	}

}
