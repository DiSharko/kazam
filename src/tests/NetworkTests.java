package tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import network.ClientNetworkable;
import network.ClientNetworker;
import network.CommandComparator;
import network.InputServer;
import network.NetworkException;
import network.StateServer;
import network.SyncedString;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class NetworkTests {
	public NetworkTests() {
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	/*********************************
	 * Tests for package network *
	 *********************************/
	
	/**
	 * Tests network's ability to concurrently send and receive properly formatted data over sockets.
	 */
	@Test
	public void networkTest() {
		// initialize client
		int clientGetPort = 1337;
		int clientSendPort = 1338;
		SyncedString initData = new SyncedString();
		initData.setData("");
		SyncedString gameData = new SyncedString();
		gameData.setData("");
		ConcurrentLinkedQueue<String> outputs = new ConcurrentLinkedQueue<String>();
		AtomicBoolean started = new AtomicBoolean(false);
		AtomicBoolean connected = new AtomicBoolean(true);
		AtomicInteger focusID = new AtomicInteger(0);
		ClientNetworkable client = new ClientNetworker(clientGetPort, clientSendPort, initData, gameData, outputs, connected, started, focusID);
		
		// initialize server
		int serverSendPort = clientGetPort;
		int serverGetPort = clientSendPort;
		AtomicBoolean running = new AtomicBoolean(true);
		AtomicBoolean startedS = new AtomicBoolean(false);
		PriorityBlockingQueue<String> inputs= new PriorityBlockingQueue<String>(8,new CommandComparator());
		InputServer inputServer = null;
		StateServer stateServer = null;
		try {
			inputServer = new InputServer(serverGetPort, inputs, running, startedS);
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			kill(client, inputServer, stateServer);
			e.printStackTrace();
		}
		try {
			stateServer = new StateServer(serverSendPort, running, startedS);
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			kill(client, inputServer, stateServer);
			e.printStackTrace();
		}
		
		// start server
		inputServer.start();
		stateServer.start();
		
		// connect as client
		String clientData = "clientData";
		try {
			client.connect("localhost", clientData);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			kill(client, inputServer, stateServer);
			e.printStackTrace();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			kill(client, inputServer, stateServer);
			e.printStackTrace();
		}
		
		// wait for server and client to be up-to-date (not necessary at this stage generally, just here to do some comparisons)
		while (inputs.isEmpty()||focusID.get() == 0) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		String connectionData = inputs.poll();
		assertTrue(connectionData.equals("CONNECTION\t" + focusID + "\t" + clientData));
		
		// broadcast lobby
		stateServer.broadcast("message\n");
		
		// read lobby on client
		while (!initData.getData().equals("message\n")) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		assertTrue(initData.getData().equals("message\n"));
		
		// broadcast start
		startedS.set(true);
		stateServer.broadcastStart();
		
		// check start on client
		while (!started.get()) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		assertTrue(started.get());
		
		// add some commands to the queue
		outputs.add("1 command1");
		outputs.add("2 command2");
		outputs.add("3 command3");
		
		// read them on the server
		while (inputs.isEmpty()) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		assertTrue(inputs.poll().equals(focusID + "\t1 command1"));
		while (inputs.isEmpty()) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		assertTrue(inputs.poll().equals(focusID + "\t2 command2"));
		while (inputs.isEmpty()) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		assertTrue(inputs.poll().equals(focusID + "\t3 command3"));
		
		// broadcast game state
		String state = "state\n";
		stateServer.broadcast(state);
		
		// read state on client
		while (!gameData.getData().equals(state)) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				kill(client, inputServer, stateServer);
				e.printStackTrace();
			}
		}
		assertTrue(gameData.getData().equals(state));
		
		// kill everything
		kill(client, inputServer, stateServer);
		
	}
	
	private void kill(ClientNetworkable client, InputServer inputServer, StateServer stateServer) {
		client.disconnect();
		try {
			if (stateServer != null) stateServer.kill();
			if (inputServer != null) inputServer.kill();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
