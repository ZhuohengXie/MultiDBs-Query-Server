package edu.pitt.sis.infsci2711.query.server.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyJettyServer;


public class QueryServerAPI {
	
	public static void main(final String[] args) throws Exception {
	
	

	
	String cmd="/Applications/presto-server-0.93/bin/launcher restart";

	    Process p=null;
	    String s=null;
	    try{
	        p=Runtime.getRuntime().exec(cmd);
	        p.waitFor();
	        BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
	        
	        while((s=br.readLine())!=null)
	            System.out.println("line: "+s);
	        p.waitFor();
	        System.out.println("exit: "+p.exitValue());
	        p.destroy();
	        
	    }catch(Exception e){System.out.println("wrong");}
	  

	
		final JerseyJettyServer server = new JerseyJettyServer(7654, "edu.pitt.sis.infsci2711.query.server.rest");
		
		server.start();
		
	}
}
