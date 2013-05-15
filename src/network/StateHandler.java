package network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class StateHandler extends Thread {
	private Socket _client;
	private String _message;
	private ClientPool _clients;

	public StateHandler(Socket client, String message, ClientPool clients) {
		_client = client;
		_message = message;
		_clients = clients;
	}
	
	@Override
	public void run() {
		try {
			synchronized(_client) {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
//				System.out.println("State handler "+_message);
				writer.write(_message);
				writer.write("EOT\n");
				writer.flush();
			}
		} catch (IOException e) {
			closeSocket();
		}
	}
	
	public void closeSocket() {
		if (!_client.isClosed()) {
			try {
				_client.close();
			} catch (IOException e) {
				// ignore as disconnecting
			}
		}
		// remove from list - this should block until broadcast() or broadcastStart() is finished
		_clients.remove(_client);
	}

}
