package edu.pitt.sis.infsci2711.query.server.api;

import java.io.File;
import edu.pitt.sis.infsci2711.multidbs.utils.JerseyJettyServer;
import edu.pitt.sis.infsci2711.multidbs.utils.PropertiesManager;

import edu.pitt.sis.infsci2711.query.server.utils.PrestoCmdManager;
import edu.pitt.sis.infsci2711.query.server.utils.CatalogMatcherManager;

public class QueryServerAPI {
	private final static String PROPERTY_PORT = "port";
	private final static int DEFAULT_PORT = 7654;
	public static void main(final String[] args) throws Exception {
		
		String url="";
		if(args.length==1)
		{
			url=args[0];
			//System.out.println(url);
			if(url.matches("help"))
			{
				System.out.println("There is only one parameter which is the absolute file path of your config.properties file.");
				System.out.println("If you have no parameter input, we will try to access '/opt/project/MultiDBs-Query-Server/config.properties'");
				return;
			}
		}
		else
		{
			url="/opt/project/MultiDBs-Query-Server/config.properties";//default, should be there if followed setup.sh
		}	
		File f= new File(url);
		//System.out.println(f.exists());
		if(!f.exists())
		{
			System.out.println("Cannot find 'environment.conf' file.");
			return;
		}
		System.out.println("found the config file.");
		PropertiesManager.getInstance().loadProperties(f);
		
		String prestohome=PropertiesManager.getInstance().getStringProperty("presto.root", "");
		String metastore=PropertiesManager.getInstance().getStringProperty("metastore.rest.base", "");
		String metastoreRegister=PropertiesManager.getInstance().getStringProperty("metastore.rest.addDatasource", "");

		if(prestohome.length()*metastore.length()*metastoreRegister.length()==0)
		{
			System.out.println("Information in config file has lost.\n Check if 'presto.root', 'metastore.rest.addDatasource' and 'metastore.rest.base' exist and are correct.");
			return;
		}
		System.out.println("config file valid. loading...");
		
		String[] ids=CatalogMatcherManager.checkCatalogs();
		if(ids!=null&&ids.length>0)
		{
			try{
			CatalogMatcherManager.fixLostCatalogs(ids);
			CatalogMatcherManager.flushChanges();
			}
			catch(Exception e)
			{
				System.out.println("Error when fixing catalogs:"+e.toString());
				return;
			}
		}
		//start it from cmd, path is default (the one recorded in SingletonConfig). See PrestoCmdManager class for more information
		if(PrestoCmdManager.start(null))	
		{
			final JerseyJettyServer server = new JerseyJettyServer(PropertiesManager.getInstance().getIntProperty(PROPERTY_PORT, DEFAULT_PORT), "edu.pitt.sis.infsci2711.query.server.rest");		
			server.start();
		}
		else
		{
			System.out.println("will not start the server because of presto starting failure");
		}
	}
}
