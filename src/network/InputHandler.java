package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class InputHandler extends Thread {
	private Socket _client;
	private PriorityBlockingQueue<String> _inputs;
	private AtomicBoolean _running;
	private int _id;
	
	public InputHandler(Socket client, PriorityBlockingQueue<String> inputs, AtomicBoolean running,
			AtomicInteger idCounter) {
		// TODO add id counter
		_client = client;
		_inputs = inputs;
		_running = running;
		_id = idCounter.incrementAndGet();
	}
	
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			
			// get/set client data and declare connection
			String clientData = reader.readLine();
			_inputs.put("CONNECTION\t" + _id + "\t" + clientData);
			
			// get inputs and pass along
			while(_running.get()) {
				String input = reader.readLine();
				_inputs.put(_id + "\t" + input);
			}
			
			// clean up
			closeSocket();
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
		// declare disconnect
		_inputs.put("DISCONNECTION\t" + _id);
	}
	
}
