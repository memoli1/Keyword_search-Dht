import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;



public class NodeDht extends Thread implements Comparable<NodeDht> {
	public static final int PORT_BASE = 3000;
	private int myid = 0;
	NodeDht successor, predecessor;
	private int myport = 0; // takes values PORT_BASE+1, PORT_BASE+2, ...
	private String myname; // "localhost" here
	private String seira;
	private int global_rep;
	int ring_size;
	private int arrived = 0;
	private boolean IamInit = false;
	Map<String, Integer> files = new HashMap<>();



	public NodeDht(String name, String seiratou, int size, int replicasNumbers) {
		myname = name;
		ring_size = size;
		seira = seiratou;
		myport = PORT_BASE + Integer.parseInt(seiratou);
		try {
			myid = service.calculate_sha1(seira, ring_size);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		global_rep = replicasNumbers;
	}

	public String getSeira(){
		return seira;
	}

	private boolean iAmResponsibleForId (int song_id){
		if (song_id > predecessor.getmyId() && song_id <= myid) {
			return true;
		}
		else if (myid < predecessor.getmyId() && (song_id <= myid || song_id > predecessor.getmyId())){
			return true;
		}
		else {
			return false;
		}
	}

	public int getmyId() {
			return myid;
	}


	public int getmyPort(){
		return myport;
	}

	public int getRing_size() {
		return ring_size;
	}

	public void setRing_size(int ring_size) {
		this.ring_size = ring_size;
	}

	public String getMyname() {
		return myname;
	}

	public void setMyname(String myname) {
		this.myname = myname;
	}


  @Override
	public int compareTo(NodeDht nd) {
		int compareId = (int) nd.getmyId();
		//ascending order
		return (int) (this.getmyId() - compareId);

	}
  ///////////////
  public void run () {
  System.out.println("Node-Thread with id " + myid + " and port " + myport + " started!\n");
  /* This function is executed by each thread in Chord Ring.
   * It setups a socket for each thread (node) and then waits (remains open
   * and listens for incoming connections) until a depart query
   * for this node arrives.
   */

  // The port in which the connection is set up.
  // A valid port value is between 0 and 65535
  ServerSocket serverSocket = null;
  InputStream is = null;
  InputStreamReader isr;
  BufferedReader br = null;
  String message_to_handle = null;
  Socket channel = null;
  int replica_counter = 0;
  /* Creates a Server Socket with the computer name (hostname)
   * and port number (port).
   * Each node has a server socket in order to send and receive
   * queries.
   */
  }
}
