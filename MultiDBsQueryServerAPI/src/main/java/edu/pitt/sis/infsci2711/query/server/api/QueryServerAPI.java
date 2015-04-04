package edu.pitt.sis.infsci2711.query.server.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyJettyServer;
import edu.pitt.sis.infsci2711.query.server.utils.SingletonConfig;

public class QueryServerAPI {
	
	public static void main(final String[] args) throws Exception {
		String url="";
		if(args.length==1)
		{
			url=args[0];
			//System.out.println(url);
			if(url.matches("help"))
			{
				System.out.println("There is only one parameter which is the absolute file path of your environment.conf file.");
				System.out.println("If you have no parameter input, we will try to access '/opt/project/MultiDBs-Query-Server/environment.conf'");
				return;
			}
		}
		else
		{
			url="/opt/project/MultiDBs-Query-Server/environment.conf";//default, should be there if followed setup.sh
		}	
		File f= new File(url);
		
		//System.out.println(f.exists());
		if(!f.exists())
		{
			System.out.println("Cannot find 'environment.conf' file.");
			return;
		}
		System.out.println("found the config file.");
		FileInputStream fin=new FileInputStream(f);
		BufferedReader br1=new BufferedReader(new InputStreamReader(fin));
		String tmp="";
		String prestohome="";
		String metastore="";
		tmp=br1.readLine();
		while(tmp!=null)
		{
			String[] arr=tmp.split("=");
			if(arr.length==2)
			{
					String tmp2=arr[0].replaceAll("\\s", "");
					String tmp3=arr[1].replaceAll("\\s", "");
					if(tmp2.matches("presto-home"))
					{prestohome=tmp3;}
					else if(tmp2.matches("metastore-url"))
					{metastore=tmp3;}
					else
					{;}
			}
			tmp=br1.readLine();
		}
		br1.close();
		if(prestohome.length()*metastore.length()==0)
		{
			System.out.println("Information in config file has lost. Check both 'presto-home' and 'metastore-url' exist and are correct.");
			return;
		}
		System.out.println("config file valid. loading...");
		SingletonConfig.onlyInit(metastore, prestohome);
		String cmd=SingletonConfig.getPrestoRoot()+"bin/launcher start";
		
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
