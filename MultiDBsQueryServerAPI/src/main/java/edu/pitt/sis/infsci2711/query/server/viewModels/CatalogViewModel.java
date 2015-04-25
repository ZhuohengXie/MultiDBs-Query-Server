package edu.pitt.sis.infsci2711.query.server.viewModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CatalogViewModel {
	@XmlElement
	private String id;
	@XmlElement
	private String ip;
	@XmlElement
	private String port;
	@XmlElement
	private String dbtype;
	@XmlElement
	private String username;
	@XmlElement
	private String password;
	@XmlElement
	private String dbname;
	
	public CatalogViewModel()
	{}
	public CatalogViewModel(final String mid,final String mIP, final String mPort,final String mdbType, final String mUsername, final String mPassword, final String mDBname)
	{
		this.id=mid;
		this.dbtype=mdbType;
		this.ip=mIP;
		this.port=mPort;
		this.username=mUsername;
		this.password=mPassword;
		this.dbname=mDBname;
	}
	public String getId(){return this.id;}
	public String getIP(){return this.ip;}
	public String getPort(){return this.port;}
	public String getUsername(){return this.username;}
	public String getPassword(){return this.password;}
	public String getDBname(){return this.dbname;}
	public String getDBType(){return this.dbtype;}
	
	@Override
	public String toString() {
		return String.format("id: %s; ip: %s; port: %s; dbtype: %s; username: %s; password: %s; dbname: %s", 
				id, ip, port, dbtype, username, password, dbname);
	}
}
