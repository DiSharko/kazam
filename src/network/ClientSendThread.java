package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientSendThread extends Thread {
	private Socket _client;
	private ConcurrentLinkedQueue<String> _inputs;
	private AtomicBoolean _connected;
	private AtomicInteger _focusID;
	private String _clientData;

	public ClientSendThread(Socket client, ConcurrentLinkedQueue<String> inputs, AtomicBoolean connected, AtomicInteger focusID,
			String clientData) {
		_client = client;
		_inputs = inputs;
		_connected = connected;
		_focusID = focusID;
		_clientData =  clientData;
	}
	
	@Override
	public void run() {
		try {
			// get writer and sent client data
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
			writer.write(_clientData);
			writer.flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			String line = reader.readLine();
			try {
				_focusID.set(Integer.parseInt(line));
			} catch (NumberFormatException e) {
				throw new IOException();
			}
			
			// loop through commands
			while (_connected.get()) {
				String command = _inputs.poll();
				if (command != null) {
					writer.write(command + "\n");
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
