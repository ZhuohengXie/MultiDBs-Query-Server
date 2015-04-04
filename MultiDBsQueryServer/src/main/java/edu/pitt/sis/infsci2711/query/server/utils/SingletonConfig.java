package edu.pitt.sis.infsci2711.query.server.utils;


public class SingletonConfig {
	  private static SingletonConfig instance = new SingletonConfig();
	   //make the constructor private so that this class cannot be instantiated
	  
	  //constructor
	   private SingletonConfig(){}
	   
	   // environment vars
	   private static String metastore="";
	   private static String prestohome="";
	   
	   //only init once when there's nothing
	   public static void onlyInit(final String metastoreURL, final String prestoRoot)
	   {
		   if(metastore.length()*prestohome.length()==0)
		   {
			   metastore=metastoreURL;
			   prestohome=prestoRoot;
		   }
	   }
	   //get methods
	   public static String getMetastoreURL(){return metastore;}
	   public static String getPrestoRoot(){return prestohome;}
	   //Get the only object available
	   public static SingletonConfig  getInstance(){
	      return instance;
	   }

//	   public void showMessage(){
//	      System.out.println("Hello World!");
//	   }
}
