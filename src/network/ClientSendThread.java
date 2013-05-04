package network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSendThread extends Thread {
	private Socket _client;
	private ConcurrentLinkedQueue<SyncedString> _inputs;
	private AtomicBoolean _connected;
	private String _clientData;

	public ClientSendThread(Socket client, ConcurrentLinkedQueue<SyncedString> inputs, AtomicBoolean connected, String clientData) {
		_client = client;
		_inputs = inputs;
		_connected = connected;
		_clientData =  clientData;
	}
	
	@Override
	public void run() {
		try {
			// get writer and sent client data
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
			writer.write(_clientData + "EOT\n");
			writer.flush();
			
			// loop through commands
			while (_connected.get()) {
				SyncedString command = _inputs.poll();
				if (command != null) {
					writer.write(command.getData());
					writer.flush();
				}
			}
			
			// run cleanup code
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
				// ignore as disconnecting
				// should be unreachable
			}
		}
		// signal disconnect to other threads
		_connected.set(false);
	}

}
