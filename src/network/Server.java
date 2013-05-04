package network;

import java.io.*;
import java.net.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A chat server, listening for incoming connections and passing them
 * off to {@link ClientHandler}s.
 */
public class Server extends Thread {

	private int _port;
	private ServerSocket _socket;
	private ClientPool _clients;
	private PriorityBlockingQueue<String> _inputs;
	private AtomicBoolean _started;
	private AtomicBoolean _running;
	private ServerType _serverType;
	private AtomicInteger _idCounter;
	
	public static enum ServerType {INPUT,STATE};

	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int port, ServerType serverType, PriorityBlockingQueue<String> inputs, AtomicBoolean running,
			AtomicBoolean started) throws NetworkException {
		
		_port = port;
		_serverType = serverType;
		_inputs = inputs;
		_running = running;
		_started = started;
		_clients = new ClientPool();
		_idCounter = new AtomicInteger(0);
		try {
			_socket = new ServerSocket(_port);
		} catch (IOException e) {
			throw new NetworkException("ERROR: Server socket not created.");
		}
	}

	/**
	 * Wait for and handle connections until game start.
	 */
	@Override
	public void run() {
		_running.set(true);
		_started.set(false);
		while(!_started.get()){
			Socket clientConnection;
			try {
				clientConnection = _socket.accept();
				// if input handling server, fork input handler
				if (_serverType == ServerType.INPUT) {
					InputHandler inputHandler = new InputHandler(clientConnection,_inputs,_running,_idCounter);
					inputHandler.start();
				} else { // else send socket to pool for broadcasting
					_clients.add(clientConnection);			
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
	 * Broadcast a lobby or game state message to clients.
	 * @param message Encoded lobby or game state.  
	 * @throws NetworkException If the server is for input rather than state connections
	 */
	public void broadcast(String message) throws NetworkException {
		if (_serverType == ServerType.STATE) {
			_clients.broadcast(message);
		} else {
			throw new NetworkException("ERROR: Called broadcast() on INPUT server.");
		}
	}
	
	public void broadcastStart() throws NetworkException {
		if (_serverType == ServerType.STATE) {
			_clients.broadcast("START\n");
		} else {
			throw new NetworkException("ERROR: Called broadcastStart() on INPUT server.");
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
		_clients.killall();
		_socket.close();
	}
}

