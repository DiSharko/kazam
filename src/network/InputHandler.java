package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class InputHandler extends Thread {
	private Socket _client;
	private PriorityBlockingQueue<String> _inputs;
	private AtomicBoolean _running;
	private int _id;
	private AtomicInteger _playerCounter;
	
	public InputHandler(Socket client, PriorityBlockingQueue<String> inputs, AtomicBoolean running, int id, AtomicInteger playerCounter) {
		// TODO add id counter
		_client = client;
		_inputs = inputs;
		_running = running;
		_id = id;
		_playerCounter = playerCounter;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
			
			// get/set client data and declare connection
			String clientData = reader.readLine();
			_inputs.put("CONNECTION\t" + _id + "\t" + clientData);
			
			// send back id
			writer.write(_id + "\n");
			writer.flush();
			
			System.out.println("starting to read inputs");
			
			// get inputs and pass along
			while(_running.get()) {
				System.out.println("blocking read?");
				String input = reader.readLine();
				System.out.println("INPUTHANDLER" + _id + "\t" + input);
				_inputs.put(_id + "\t" + input);
			}
			
			System.out.println("SOCKET CLOSING");
			
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
		_playerCounter.decrementAndGet();
	}
	
}
