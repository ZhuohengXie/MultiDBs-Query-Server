package edu.pitt.sis.infsci2711.query.server.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import edu.pitt.sis.infsci2711.query.server.utils.JdbcMysql;

public class SQLParser {
	public static String rebuild(final String sql) throws Exception
	{
		String result=sql;
		while(result.endsWith(";")||result.endsWith("\n"))
		{
			//logger.info("ends with: " + sql.endsWith(";") );
			result=result.substring(0, result.length()-1);
		}	
		while(result.startsWith(";")||result.startsWith("\n")||result.startsWith(" ")||result.startsWith("\t")||result.startsWith("\r"))
		{
			//logger.info("ends with: " + sql.endsWith(";") );
			result=result.substring(1);
		}
		try{
			result=replaceType1(result);
			result=replaceType2(result);
			return result;
		}
		catch(MySQLSyntaxErrorException e)
		{
			throw new Exception("SQL parser have met a problem.");
		}
		
	}
	//Type1 means a space followed by did.tablename
	private static String replaceType1(String sql) throws Exception
	{
		Connection conn=JdbcMysql.getConnection();
		Statement st=conn.createStatement();
		Pattern p1=Pattern.compile("(\\s\\d+\\.\\w)");
		Matcher m1=p1.matcher(sql);
		int i=0;
		while (m1.find())
		{
			String tmp=m1.group(i);
			String[] arr=tmp.split("\\.");
			if(arr.length>1)
			{
				arr[0]=arr[0].replaceAll("\\s", "");
				String query=String.format("select catalog, dbname from matcher where did=%s", arr[0]);
				ResultSet rs=st.executeQuery(query);
				String replace="";
				while(rs.next())
				{
					String catalog=rs.getString(1);
					String dbname=rs.getString(2);
					if(catalog.length()*dbname.length()!=0)
					{
						replace=catalog+"."+dbname;
						System.out.println("replace:"+replace);
					}
					else
					{
						throw new Exception(" error in results from matcher ");
					}
				}
				if(replace!="")
				{
					replace=" "+replace+".";
					for(int j=1;j<arr.length;j++)
					{
						replace+=arr[j];
					}
					sql=sql.replaceAll(tmp,replace);
				}
				else
				{
					throw new Exception("did '"+arr[0]+"' does not exist");
				}	
			}
			i++;
		}
		return sql;
	}
	//Type2 means a comma followed by did.tablename
	private static String replaceType2(String sql) throws Exception
	{
		Connection conn=JdbcMysql.getConnection();
		Statement st=conn.createStatement();
		Pattern p2=Pattern.compile("(,\\d+\\.\\w)");
		Matcher m2=p2.matcher(sql);
		int i=0;
		while (m2.find())
		{
			String tmp=m2.group(i);
			String[] arr=tmp.split("\\.");
			if(arr.length>1)
			{
				arr[0]=arr[0].replaceAll(",", "");
				String query=String.format("select catalog, dbname from matcher where did=%s", arr[0]);
				ResultSet rs=st.executeQuery(query);
				String replace="";
				while(rs.next())
				{
					String catalog=rs.getString(1);
					String dbname=rs.getString(2);
					if(catalog.length()*dbname.length()!=0)
					{
						replace=catalog+"."+dbname;
						System.out.println("replace:"+replace);
					}
					else
					{
						throw new Exception(" error in results from matcher ");
					}
				}
				if(replace!="")
				{
					replace=","+replace+".";
					for(int j=1;j<arr.length;j++)
					{
						replace+=arr[j];
					}
					sql=sql.replaceAll(tmp,replace);
				}
				else
				{
					throw new Exception("did '"+arr[0]+"' does not exist");
				}	
			}
			i++;
		}
		return sql;
	}
}
