package network;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class ClientPool {
	private LinkedList<Socket> _clients;
	
	/**
	 * Initialize a new Client Pool.
	 */
	public ClientPool() {
		_clients = new LinkedList<Socket>();
	}
	
	/**
	 * Add a new client to the pool.
	 * 
	 * @param client to add
	 */
	public synchronized void add(Socket client) {
		_clients.add(client);
	}
	
	/**
	 * Remove a client from the pool. Only do this if you intend to clean up
	 * that client later.
	 * 
	 * @param client to remove
	 * @return true if the client was removed, false if they were not there.
	 */
	public synchronized boolean remove(Socket client) {
		return _clients.remove(client);
	}
	
	/**
	 * Send a message (game/lobby state) to clients in the pool.
	 * 
	 * @param message to send
	 */
	public synchronized void broadcast(String message) {
		for (Socket client : _clients) {
			new StateHandler(client,message,this).start();
		}
	}
	
	/**
	 * Close all sockets and empty the pool
	 */
	public synchronized void killall() {
		for (Socket client : _clients) {
			if (!client.isClosed()) {
				try {
					client.close();
				} catch (IOException e) {
					// ignore as disconnecting
				}
			}
		}
		_clients.clear();
	}
}
