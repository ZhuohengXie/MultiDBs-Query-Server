package edu.pitt.sis.infsci2711.query.server.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URLDecoder;
import edu.pitt.sis.infsci2711.query.server.utils.SingletonConfig;

public class CatalogFileBuilder {
	
	private String connector;
	private String connectionString;
	private String username;	
	private String password;
	
	public static final String template1="connector.name=";
	public static final String template2="connection-url=";
	public static final String template3="connection-user=";
	public static final String template4="connection-password=";
	
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	
	public static String MysqlConnURL(String mip, String mport)
	{
		return "jdbc:mysql://"+mip+":"+mport;
	}
	
	// complicated
	public CatalogFileBuilder(String Connector, String JdbcString, String Username, String Password) throws Exception
	{
		// init basic
		if(checkConnector(Connector))
		{
			connector=Connector;
			username=Username;
			password=Password;
			connectionString=JdbcString;
		}
		else
		{
			throw new Exception("Invalid connector type");
		}
	}
	//only for mysql
	public CatalogFileBuilder(String mysqlConnStr, String Username, String Password) throws Exception
	{	
		// init basic
		if(checkConnector("MySQL"))
		{
			connector="mysql";
			username=Username;
			password=Password;
			connectionString=mysqlConnStr;	
		}
		else
		{
			throw new Exception("Invalid connector type");
		}		
	}
	public String write(String filetitle, String prestoRoot) throws Exception
	{
		if(prestoRoot=="")
		{
			prestoRoot=SingletonConfig.getPrestoRoot();
		}
		
		buildLines();
		
		String filepath=prestoRoot+"etc/catalog/"+filetitle+".properties";
		//URI uri=URI.create(filepath);
		File f=new File(filepath);
		if(!f.exists())
		{
			f.createNewFile();
			System.out.println("path is "+f.getAbsolutePath());
			FileOutputStream fop=new FileOutputStream(f,true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fop));
			bw.write(line1);
			bw.newLine();
			bw.write(line2);
			bw.newLine();
			bw.write(line3);
			bw.newLine();
			bw.write(line4);
			bw.newLine();
			bw.close();
			return filepath;
		}
		else 
		{
			throw new Exception("Catalog File Already Exists!");
		}
	}
	private boolean checkConnector(String connectorName)
	{
		connectorName=connectorName.toLowerCase();
		return true;
	}

	private void buildLines()
	{
		line1=template1+connector;
		line2=template2+connectionString;
		line3=template3+username;
		line4=template4+password;
	}
}
