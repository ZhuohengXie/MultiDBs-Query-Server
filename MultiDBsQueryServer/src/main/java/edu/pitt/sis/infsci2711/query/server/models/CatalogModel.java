package edu.pitt.sis.infsci2711.query.server.models;


public class CatalogModel {
	private String did;
	private String ip;
	private String port;
	private String dbtype;
	private String username;
	private String password;
	private String dbname;
	
	public CatalogModel()
	{}
	public CatalogModel(String mid,String mIP, String mPort,String mdbType, String mUsername, String mPassword, String mDBname)
	{
		this.did=mid;
		this.dbtype=mdbType;
		this.ip=mIP;
		this.port=mPort;
		this.username=mUsername;
		this.password=mPassword;
		this.dbname=mDBname;
	}
	public String getId(){return this.did;}
	public String getIP(){return this.ip;}
	public String getPort(){return this.port;}
	public String getUsername(){return this.username;}
	public String getPassword(){return this.password;}
	public String getDBname(){return this.dbname;}
	public String getDBType(){return this.dbtype;}
}
