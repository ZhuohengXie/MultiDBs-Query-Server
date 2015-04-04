package edu.pitt.sis.infsci2711.query.server.viewModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SaveQueryViewModel {
		@XmlElement
		private String ip;
		@XmlElement
		private String port;
		//private String dbtype;
		@XmlElement
		private String username;
		@XmlElement
		private String password;
		@XmlElement
		private String dbname;
		@XmlElement
		private String title;
		@XmlElement
		private String description;
		@XmlElement		
		private String query;
		
		public SaveQueryViewModel()
		{}
		public SaveQueryViewModel(String mIP, String mPort, String mUsername, String mPassword, String mDBname, String mTitle, String mDescription, String mQuery)
		{
			//this.dbtype="MySQL";
			this.query=mQuery;
			this.ip=mIP;
			this.port=mPort;
			this.username=mUsername;
			this.password=mPassword;
			this.dbname=mDBname;
			this.title=mTitle;
			this.description=mDescription;
		}
		public String getQuery(){return this.query;}
		public String getIP(){return this.ip;}
		public String getPort(){return this.port;}
		public String getUsername(){return this.username;}
		public String getPassword(){return this.password;}
		public String getDBname(){return this.dbname;}
		public String getTitle(){return this.title;}
		public String getDescription(){return this.description;}
		//public String getDBType(){return this.dbtype;}
}
