package network;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A chat server, listening for incoming connections and passing them
 * off to StateHandlers.
 */
public class StateServer extends Thread {

	private int _port;
	private ServerSocket _socket;
	private ClientPool _clients;
	private AtomicBoolean _started;
	private AtomicBoolean _running;

	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public StateServer(int port, AtomicBoolean running, AtomicBoolean started) throws NetworkException {
		_port = port;
		_running = running;
		_started = started;
		_clients = new ClientPool();
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
		// accept connections before game start
		while(!_started.get() && _running.get()){
			Socket clientConnection;
			try {
				clientConnection = _socket.accept();
				// send socket to pool for broadcasting if game hasn't started
				if (!_started.get()){
					_clients.add(clientConnection);
				} else { // else reject connection
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
	 * Broadcast a lobby or game state message to clients.
	 * @param message Encoded lobby or game state. 
	 */
	public void broadcast(String message) {
		_clients.broadcast(message);
	}
	
	/**
	 * Broadcasts game start notification to clients.  
	 */
	public void broadcastStart() {
			_clients.broadcast("START\n");
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

