package network;

import java.net.UnknownHostException;

public interface ClientNetworkable {
	
	/**
	 * Connects to specified host.
	 * @param hostname Valid host name, URL, etc.
	 * @param clientData Encoded string containing data about client (name, spells, etc.) for use by the server.  
	 * @throws NetworkException 
	 * @throws UnknownHostException 
	 */
	public void connect(String hostname, String clientData) throws UnknownHostException, NetworkException;
	
	/**
	 * Disconnects from current host.
	 */
	public void disconnect();

}
