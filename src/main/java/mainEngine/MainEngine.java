package mainEngine;

import serverOps.AdviceServer;

public class MainEngine {

	public static void main(String[] args) {

		AdviceServer server = new AdviceServer();
		server.go();
	}

}
