package edu.pitt.sis.infsci2711.query.server.utils;
import edu.pitt.sis.infsci2711.multidbs.utils.PropertiesManager;

public class PropertiesPlugin {
	
	   //get methods
	   public static String getMetastoreURL() throws Exception
	   {
			String metastore = PropertiesManager.getInstance().getStringProperty("metastore.rest.base");
			if(metastore==null)
			{ throw new Exception("The properties manager has not been initiallized correctly."); }
		   return metastore;
		   }
	   public static String getPrestoRoot() throws Exception
	   {
		   String prestohome = PropertiesManager.getInstance().getStringProperty("presto.root");
			if(prestohome==null)
			{ throw new Exception("The properties manager has not been initiallized correctly."); }
		   return prestohome;
	   }
	   public static String  getMetastoreRegister() throws Exception
	   {			
		   String metastoreRegister = PropertiesManager.getInstance().getStringProperty("metastore.rest.addDatasource");
			if(metastoreRegister==null)
			{ throw new Exception("The properties manager has not been initiallized correctly."); }
			//metastoreRegister = getMetastoreURL()+metastoreRegister;
	      return metastoreRegister;
	   }
	   
	public static int getMakeSureTablesIsCreatedNumberAttemps() {
		return PropertiesManager.getInstance().getIntProperty("makeSureTableIsCreatedNumberAttemps");
	}
	
	public static long getMakeSureTablesIsCreatedSleepMilliseconds() {
		return PropertiesManager.getInstance().getIntProperty("makeSureTableIsCreatedNumberSleepMilliseconds");
	}

}
