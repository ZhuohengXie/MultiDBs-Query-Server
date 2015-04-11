package edu.pitt.sis.infsci2711.query.server.business;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.infsci2711.query.server.models.CatalogModel;
import edu.pitt.sis.infsci2711.query.server.utils.CatalogFileBuilder;
import edu.pitt.sis.infsci2711.query.server.utils.JdbcMysql;
import edu.pitt.sis.infsci2711.query.server.utils.PrestoCmdManager;
import edu.pitt.sis.infsci2711.query.server.utils.PropertiesPlugin;

public class CatalogService {
	
		private static final Logger logger = LogManager.getLogger(CatalogService.class);
		
		public boolean add(final CatalogModel saveInfo) throws SQLException, Exception {
			try
			{
				Connection mysqlConn=JdbcMysql.getConnection();
				Statement mysqlStatement=mysqlConn.createStatement();
				String mysqladd=String.format("select * from prestomatch.matcher where did=%s",saveInfo.getId());
				ResultSet rs=mysqlStatement.executeQuery(mysqladd);
				int i=0;
				while(rs.next())
				{i++;}
				if(i>0)
				{throw new Exception("The record for did="+saveInfo.getId()+" already exists. \nTo overwirte or change, drop it first and add again.");}
			}
			catch (Exception e)
			{throw e;}				
				String dbconnUrl=CatalogFileBuilder.getConnectionURL(saveInfo.getDBType(),saveInfo.getIP(),saveInfo.getPort());
				CatalogFileBuilder ncatalog=new CatalogFileBuilder(dbconnUrl,saveInfo.getUsername(),saveInfo.getPassword());
				String strm="db"+saveInfo.getId();
				String fileurl=PropertiesPlugin.getPrestoRoot()+"etc/catalog/"+strm+".properties";
				File f=new File(fileurl);
				if(f.exists())
				{f.delete();}						
				//restart is enough, in this case, because the user won't be using it so fast;
				PrestoCmdManager.restart(null);	
				//Connect to prestomatch.matcher to log
				try
				{
					Connection mysqlConn=JdbcMysql.getConnection();
					Statement mysqlStatement=mysqlConn.createStatement();
					String mysqladd=String.format("insert into prestomatch.matcher values(%s,'%s','%s')",saveInfo.getId(),strm,saveInfo.getDBname());
					mysqlStatement.execute(mysqladd);
					
					fileurl=ncatalog.write(strm, "");
					return true;
				}
				catch (Exception e)
				{throw e;}
		}
		
		public boolean drop(final String did) throws SQLException, Exception {
			
			try
			{
				Connection mysqlConn=JdbcMysql.getConnection();
				Statement mysqlStatement=mysqlConn.createStatement();
				String mysqladd=String.format("select * from prestomatch.matcher where did=%s",did);
				ResultSet rs=mysqlStatement.executeQuery(mysqladd);
				int i=0;
				while(rs.next())
				{i++;}
				if(i==0)
				{throw new Exception("The record for did="+did+" does not exist.");}
			}
			catch (Exception e)
			{throw e;}	
			String strm="db"+did;
			String fileurl=PropertiesPlugin.getPrestoRoot()+"etc/catalog/"+strm+".properties";
			File f=new File(fileurl);
			//Connect to prestomatch.matcher to remove log
			try
			{
				Connection mysqlConn=JdbcMysql.getConnection();
				Statement mysqlStatement=mysqlConn.createStatement();
				String mysqldelete=String.format("delete from prestomatch.matcher where did=%s",did);
				mysqlStatement.execute(mysqldelete);
				if(!f.exists())
				{
					throw new Exception("the catalog file has already been lost");
				}
				else
				{
					f.delete();
				}
				//restart is enough, in this case, because the user won't be using it so fast;
				PrestoCmdManager.restart(null);	
				return true;
			}
			catch (Exception e)
			{throw e;}
	}
}
