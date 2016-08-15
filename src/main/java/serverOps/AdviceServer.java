package serverOps;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class AdviceServer {

	ArrayList<ObjectOutputStream> clientOutputStream;

	String[] adviceList = { "Take smaller bites", "Go for the tight jeans", "Eat your broccolli",
			"Peas and Gravy are NEVER a good idea to mix", "If you have to ask..." };

	public class ClientHandler implements Runnable {

		ObjectInputStream in;
		Socket clientSocket;

		public ClientHandler(Socket socket) {
			try {
				clientSocket = socket;
				in = new ObjectInputStream(clientSocket.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void run() {
			Object o1 = null;
			try {
				while ((o1 = in.readObject()) != null) {
					tellEveryone(o1);
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void tellEveryone(Object o){
		Iterator<ObjectOutputStream> it = clientOutputStream.iterator();
		while (it.hasNext()){
			ObjectOutputStream out = (ObjectOutputStream) it.next();
			try {
				out.writeObject(o);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void go() {
		clientOutputStream = new ArrayList<ObjectOutputStream>();
		
		try {
			ServerSocket serverSock = new ServerSocket(4242);

			while (true) {
				Socket clientSocket = serverSock.accept();
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
				clientOutputStream.add(out);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getAdvice() {
		int random = (int) (Math.random() * adviceList.length);
		return adviceList[random];
	}

}
