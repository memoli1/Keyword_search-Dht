import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;




public class service {

	public static int calculate_sha1(String input, int size) throws NoSuchAlgorithmException{
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
	    byte[] result = mDigest.digest(input.getBytes());
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < result.length; i++) {
	    	sb.append(String.format("%02x", result[i]));
	    }
	    String asString = sb.toString(); // hexadecimal representation of hash
	    BigInteger value = new BigInteger(asString, 16);
	    value = value.mod(BigInteger.valueOf(size));
	    return value.intValue();

	}

  public static void main_forward_to(String message, int replicas, String hostname, int port){
  String message_final;
  String []message_with_replicas = message.split("-");
  if (message_with_replicas.length >= 3 ) message_final = message_with_replicas[0]+"-"+replicas+"-"+message_with_replicas[1]+"-"+message_with_replicas[2];
  else message_final = message;
  Socket socket = null;
  try {
    socket = new Socket(hostname, port);
  }
  catch (UnknownHostException e) {
       System.out.println("Unknown host");
       e.printStackTrace();
       System.exit(1);
  }
  catch (IOException e) {
    System.out.println("Cannot use this port");
    e.printStackTrace();
      System.exit(1);
  }
  OutputStream os = null;
  try {
    os = socket.getOutputStream();
  } catch (IOException e) {
    System.out.println("Couldn't get output stream");
    e.printStackTrace();
    System.exit(1);
  }
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);
      try {
        bw.write(message_final);
          bw.flush();
      } catch (IOException e) {
        System.out.println("Couldn't write to BufferWriter");
        e.printStackTrace();
        System.exit(1);
      }
  }

private static void fix_nodes(List<NodeDht> nodelist) {
  int len = nodelist.size();
  Collections.sort(nodelist);
  for (int i=0; i<len; i++) {
    if (i == 0){
      // first in ring
      nodelist.get(i).successor = nodelist.get(i+1);
      nodelist.get(i).predecessor = nodelist.get(len - 1);
    }
    else if (i == len - 1){
      // last in ring
      nodelist.get(i).successor = nodelist.get(0);
      nodelist.get(i).predecessor = nodelist.get(i-1);
    }
    else {
      nodelist.get(i).successor = nodelist.get(i+1);
      nodelist.get(i).predecessor = nodelist.get(i-1);
    }
  }
 }


    public static void main(String[]args) {

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		//System.out.println("Please enter the <number of desired nodes>: ");
		int number_of_nodes = 10;	//Integer.parseInt(input.readLine());
		//System.out.println("Please enter the <log of ring size>: ");
		int M = 6; //Integer.parseInt(input.readLine());
		double ring = Math.pow(2,M);//Chord ring size is defined to be a power of 2 (256,512,1024,.....)
		int ring_size = (int) Math.round(ring);
		int globalc; // global node counter
		int k = 1; //this is the replication factor
		List<NodeDht> nodelist = new ArrayList<NodeDht>();

		System.out.printf("Initial number of nodes: %d ring size: %d replication factor: %d\n",number_of_nodes,ring_size,k);

		// create initial ring
		for (globalc=1; globalc<=number_of_nodes; globalc++){
			NodeDht n = new NodeDht("localhost", Integer.toString(globalc), ring_size ,k);
			nodelist.add(n);
		}
		fix_nodes(nodelist);
		for (NodeDht n: nodelist){
			n.start();
		}
		try {
			Thread.sleep(2000);
		    } catch (InterruptedException e1) {
			    System.out.println("Main couldn't sleep!");
		    }

					String command;
					try {
						do {
						System.out.println("Insert somenthing in the network and then type stop :");
						command = input.readLine();
					//	System.out.println("Type your command: ");
						String option = command.split(",")[0]; //insert,query,delete,join,depart
						if (option.equals("insert") || option.equals("delete") || option.equals("query")) {
							int len = nodelist.size();

							int randomNum = ThreadLocalRandom.current().nextInt(0, len-1);
							NodeDht init = nodelist.get(randomNum);
							main_forward_to(command + "-" + init.getMyname()+"-"+ init.getmyPort()+"\n", k, init.getMyname(), init.getmyPort());
							System.out.println("Main says: I forwarded the command to Node with ID: " + init.getmyId());

							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								System.out.println("Main couldn't sleep!");
							}

						}
					}  while(!(command.equals("stop")));
					} catch (IOException e) {
		        System.out.println("Errore nell'inserimento");
					}



        /*System.out.println("Inserisci le dimensione dell'ipercubo:");
        Scanner scanner = new Scanner(System. in);
        int r=scanner.nextInt();*/

        System.out.println("Creo un ipercubo a 8 dimensioni");
        System.out.println("Le parole chiave disponibili sono: { a, b, c, d, e, f, g, h }");
        int r = 8;

        //creo l'ipercubo di r-dimensioni
        Hypercube hypercube = new Hypercube(r);

        //Simulo la connessione ad un nodo dell'ipercubo
        Node connectedNode = new Node();
        connectedNode = hypercube.getNode(randomNode(r));

        printLog(hypercube, connectedNode);
        //loadCsv(hypercube, connectedNode);
        String choice;

        do{
            System.out.println("\n****MENU****");
			    //  System.out.println("1. INSERT");
			      System.out.println("2. SUPERSET SEARCH");
      			System.out.println("3. PIN SEARCH");
			      System.out.println("4. DELETE");
            System.out.println("5. EXIT");
            System.out.print("Fai una scelta (1, 2, 3, 4, 5): ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextLine();

            switch(choice){


              //  case "1": insert(hypercube, connectedNode);
              //  break;

                case "2": search(hypercube, connectedNode, r);
                break;

                case "3": pinSearch(hypercube, connectedNode, r);
                break;

                case "4":	//Exit
                break;

                case "5":	//Exit

					System.out.println("EXIT");
					break;

				default:
					break;

            }

        } while(!(choice.equals("5")));


    }

    private static void search(Hypercube hypercube, Node connectedNode, int r) {

        System.out.print("2. SUPERSET SEARCH: ");

        //set utilizzato per rappresentare il set di keyword
        Set<String> kStringSet = new HashSet<String>();
        //variabile per log
        String targetNodeId;
        ArrayList<String> idObjects;
        //chiedo le keyword da inserire all'utente
        kStringSet = insertKeywords();

        //visualizzo a schermo le keyword inserite
        System.out.print("Il set di keyword e': " + kStringSet);

        //visualizzo a schermo il nodo che si occupa della keyword
        targetNodeId = getStringSearched(connectedNode.generateBitSet(kStringSet), r);
        System.out.println("\nNodo che si occupa della keyword: " + targetNodeId);
        System.out.println("Cerco il nodo: " + targetNodeId);

        try {
            idObjects = new ArrayList<String>(connectedNode.requestObjects(kStringSet, 100));
            System.out.println(connectedNode.getObjects(hypercube, idObjects));
        } catch (NullPointerException e) {
            System.out.println("Nessun oggetto trovato");
        }
    }

    private static void pinSearch(Hypercube hypercube, Node connectedNode, int r){
        //set utilizzato per rappresentare il set di keyword
        Set<String> kStringSet = new HashSet<String>();
        //variabile per log
        String targetNodeId;
        ArrayList<String> idObjects;
        //chiedo le keyword da inserire all'utente
        kStringSet = insertKeywords();

        //visualizzo a schermo le keyword inserite
        System.out.print("Il set di keyword e': " + kStringSet);

        //visualizzo a schermo il nodo che si occupa della keyword
        targetNodeId = getStringSearched(connectedNode.generateBitSet(kStringSet), r);
        System.out.println("\nNodo che si occupa della keyword: " + targetNodeId);
        System.out.println("Cerco il nodo: " + targetNodeId);

        try {
            idObjects = new ArrayList<String>(connectedNode.requestObjects(kStringSet));
            System.out.println(connectedNode.getObjects(hypercube, idObjects));
        } catch (NullPointerException e) {
            System.out.println("Nessun oggetto trovato");
        }
    }

    // private static void insert(Hypercube hypercube, Node connectedNode){
		//
    //     System.out.print("1. INSERT: ");
		//
    //     Set<String> key = new HashSet<String>(insertKeywords());
		//
    //     Scanner scan = new Scanner(System.in);
    //     System.out.println("Inserisci il contenuto dell'oggetto");
    //     String valueObject  = scan.nextLine();
    //     connectedNode.addObject(hypercube, key, valueObject);
    // }


    private static Set<String> insertKeywords(){
        String decision;
        boolean yn = true;
        Set<String> kStringSet = new HashSet<String>();
        Scanner scannerKey = new Scanner(System.in);
        System.out.println("Inserisci la parola chiave");

        while(yn){

            //recupero parola chiave inserita dall'utente
            kStringSet.add(scannerKey.nextLine());


            System.out.println("Inserire altra keyword?  yes or no");
            decision = scannerKey.nextLine();

            switch(decision){
            case "yes":
                yn = true;
                break;
            case "no":
                yn = false;
                break;
            default :
                System.out.println("please enter again ");
            }
        }
        return kStringSet;
    }

    private static String getStringSearched(BitSet bs, int r){
        String searched="";
        for (int i = 0; i < r; i++){
            if(bs.get(i) == true){
                searched = "1" + searched;
            }
            else searched = "0" + searched;
        }
        return searched;
    }

    public static String randomNode(int r){
    String idString = Integer.toBinaryString((int)(Math.random()*(Math.pow(2,r))));
        while (idString.length() < r){
            idString = "0" + idString;
        }
        return idString;
    }

    private static void loadCsv(Hypercube hypercube, Node connectedNode){
        String fileName = "script/test.csv";
        File file = new File(fileName);
        try{
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String data = inputStream.next();
                String [] values = data.split(",");
                Set<String> keySet = new HashSet<String>();
                for(String key : values[1].split("")){
                    keySet.add(key);
                }
                connectedNode.addObject(hypercube, keySet, values[0]);
            }
            inputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void printTree(NodeSBT root, String bo){
        if (root.getFather() == null){
            System.out.println(root.getId());
        }
        for (NodeSBT entry : root.getChildren()){
            System.out.println("     " + bo + entry.getId());
        if(!entry.getChildren().isEmpty()){
            printTree(entry, "      " + bo);
        }
        }
    }

    private static void printLog(Hypercube hypercube, Node connectedNode){
         //stampe debug
         System.out.println("Ipercubo creato");

        System.out.println("Connesso al nodo: " + connectedNode.getId() + "\nI suoi neighbors sono:");
        for (Node node : connectedNode.getNeighbors()){
            System.out.print("  " + node.getId());
            }
    }
}
