package edu.pitt.sis.infsci2711.query.server.api;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyJettyServer;


public class QueryServerAPI {
	
	public static void main(final String[] args) throws Exception {
		final JerseyJettyServer server = new JerseyJettyServer(7654, "edu.pitt.sis.infsci2711.query.server.rest");
		
		server.start();
	}
}
