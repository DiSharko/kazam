package network;

import java.io.*;
import java.net.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A chat server, listening for incoming connections and passing them
 * off to InputHandlers.
 */
public class InputServer extends Thread {

	private int _port;
	private ServerSocket _socket;
	private PriorityBlockingQueue<String> _inputs;
	private AtomicBoolean _started;
	private AtomicBoolean _running;
	private int _idCounter;
	private AtomicInteger _playerCounter;
	
	private static int MAX_PLAYERS = 6;
	
	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public InputServer(int port, PriorityBlockingQueue<String> inputs, AtomicBoolean running,
			AtomicBoolean started) throws NetworkException {
		
		_port = port;
		_inputs = inputs;
		_running = running;
		_started = started;
		_idCounter = 0;
		_playerCounter = new AtomicInteger(0);
		try {
			_socket = new ServerSocket(_port);
		} catch (IOException e) {
			throw new NetworkException("ERROR: Server socket not created.");
		}
	}

	/**
	 * Wait for and handle connections until game start.
	 * _started should be set to false and _running should be set
	 * to true in the parent thread prior to start, otherwise thread
	 * may abort with no notice.  
	 */
	@Override
	public void run() {
		
		// allow incoming connections before start of game
		while(!_started.get() && _running.get()){
			Socket clientConnection;
			try {
				clientConnection = _socket.accept();
				
				// fork input handler if not started and not too many players
				if (!_started.get() && _playerCounter.get() < MAX_PLAYERS) {
					_idCounter--;
					_playerCounter.incrementAndGet();
					InputHandler inputHandler = new InputHandler(clientConnection,_inputs,_running,_idCounter,_playerCounter);
					inputHandler.start();
				} else { // else close socket
					try {
						clientConnection.close();
					} catch (IOException e) {
						// ignore as disconnecting
					}
				}
			} catch (IOException e) {
				try {
					kill();
				} catch (IOException e1) {
					// ignore as shutting down
				}
			}
		}
	}
	
	/**
	 * Stop waiting for connections, close all connected clients, and close
	 * this server's {@link ServerSocket}.
	 * 
	 * @throws IOException if any socket is invalid.
	 */
	public void kill() throws IOException {
		_running.set(false);
		_socket.close();
	}
}

