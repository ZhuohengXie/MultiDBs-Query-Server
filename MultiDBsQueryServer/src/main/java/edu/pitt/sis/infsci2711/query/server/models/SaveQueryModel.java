package edu.pitt.sis.infsci2711.query.server.models;

public class SaveQueryModel {
	private String ip;
	private String port;
	private String dbtype;
	
	private String username;
	private String password;

	private String dbname;
	private String title;
	private String description;
	
	private String tablename;
	
	private String query;
	
	public SaveQueryModel()
	{}
	public SaveQueryModel(String mIP, String mPort,String mdbType, String mUsername, String mPassword, String mDBname, String mTitle, String mDescription,String mtablename, String mQuery)
	{
		this.dbtype=mdbType;
		this.query=mQuery;
		this.ip=mIP;
		this.port=mPort;
		this.username=mUsername;
		this.password=mPassword;
		this.dbname=mDBname;
		this.title=mTitle;
		this.description=mDescription;
		this.tablename=mtablename;
	}
	public SaveQueryModel(String mIP, String mPort,String mdbType, String mUsername, String mPassword, String mDBname, String mtablename,String mQuery)
	{
		this.dbtype=mdbType;
		this.query=mQuery;
		this.ip=mIP;
		this.port=mPort;
		this.username=mUsername;
		this.password=mPassword;
		this.dbname=mDBname;
		this.tablename=mtablename;
		this.title="new database";
		this.description="save result for a query";
	}
	public String getQuery(){return this.query;}
	public String getIP(){return this.ip;}
	public String getPort(){return this.port;}
	public String getUsername(){return this.username;}
	public String getPassword(){return this.password;}
	public String getDBname(){return this.dbname;}
	public String getTitle(){return this.title;}
	public String getDescription(){return this.description;}
	public String getDBType(){return this.dbtype;}
	public String gettableName(){return this.tablename;}
}
