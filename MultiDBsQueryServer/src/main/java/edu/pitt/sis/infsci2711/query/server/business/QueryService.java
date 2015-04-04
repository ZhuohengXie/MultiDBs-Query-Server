package edu.pitt.sis.infsci2711.query.server.business;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.models.QueryResultModel;
import edu.pitt.sis.infsci2711.query.server.models.Row;
import edu.pitt.sis.infsci2711.query.server.models.SaveQueryModel;
import edu.pitt.sis.infsci2711.query.server.models.Schema;
import edu.pitt.sis.infsci2711.query.server.utils.CatalogFileBuilder;
import edu.pitt.sis.infsci2711.query.server.utils.JdbcPresto;
import edu.pitt.sis.infsci2711.query.server.utils.SQLParser;


public class QueryService {
	
	private static final Logger logger = LogManager.getLogger(QueryService.class);

	public QueryResultModel run(final QueryModel convertViewModelToDB) throws SQLException, Exception {
		
		logger.info("Got query to run: " + convertViewModelToDB.getQuery());
		
		
		try (Connection connection = JdbcPresto.getConnection()) {
			String sql = convertViewModelToDB.getQuery() ;
			sql=SQLParser.rebuild(sql);
			logger.info("after parse:  "+sql );

			//DatabaseMetaData md = connection.getMetaData();
           // logger.info("support multi update?:    "+md.supportsBatchUpdates()); 
			try (Statement statement = connection.createStatement())
			{
				int nrow = 0;	
				int ncol=	0;
				String tmp=sql.toLowerCase();
				if(tmp.startsWith("create")||tmp.startsWith("insert")||tmp.startsWith("use"))
				{
					nrow=-1;;//nrow=0;
					throw new SQLFeatureNotSupportedException("'create', 'use', 'insert', 'drop' statements are not supported.");
				}
				else
				{
					ResultSet resultSet = statement.executeQuery(sql);
				
					logger.info("after trim: " + sql);
					//ResultSetMetaData rsmd=resultSet.getMetaData();

					try {
	//					resultSet.last();
	//					nrow = resultSet.getRow();
						if(sql.startsWith("create")||sql.startsWith("Create"))
						{
							nrow=-1;
						}
						else
						{
							while(resultSet.next())
							{
								nrow++;
							}
						}
					   // resultSet.beforeFirst();
					}
					catch(Exception ex) {
						nrow=0;
					    logger.info(ex.toString());
					}
				}
				
				ResultSet resultSet = statement.executeQuery(sql);
				ResultSetMetaData rsmd=resultSet.getMetaData();
				ncol=rsmd.getColumnCount();
				if(nrow==-1)
				{
				    String[] cols=new String[1];
				    cols[0]="status";
				    Schema schema = new Schema(cols);
				    String[] arr=new String[1];arr[0]="executed";
				    Row[] data=new Row[1];
				    data[0]=new Row(arr);
				    
					QueryResultModel queryResultModel = new QueryResultModel(schema, data);
					return queryResultModel;					
				}
				String[] cols=new String[ncol];
				for(int k=0;k<ncol;k++)
				{
					cols[k]=rsmd.getColumnLabel(k+1);
				}
				Schema schema = new Schema(cols);
				
				Row[] data=new Row[nrow];
				
				int cnt=0;
				while (resultSet.next()) {

							String []arr=new String[ncol];
							for(int i=0;i<ncol;i++)
							{
										arr[i]=resultSet.getString(i+1);
							}
							data[cnt]=new Row(arr);cnt++;
				}
				QueryResultModel queryResultModel = new QueryResultModel(schema, data);
				return queryResultModel;
			}
			//throw exception will be handled elsewhere;
		}

	}

	public boolean save(final SaveQueryModel convertViewModelToDB) throws SQLException, Exception {
		
		logger.info("Got query to run & save: " + convertViewModelToDB.getQuery());
		
		try (Connection connection = JdbcPresto.getConnection()) {
			String sql = convertViewModelToDB.getQuery() ;
			sql=SQLParser.rebuild(sql);
			logger.info("after parse:  "+sql );

			try (Statement statement = connection.createStatement())
			{
				String tmp=sql.toLowerCase();
				if(!tmp.startsWith("select"))
				{
					throw new SQLFeatureNotSupportedException("Only 'select' statements can be saved.");
				}
				else
				{
				    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
				    Calendar cal = Calendar.getInstance();
				    String strm=dateFormat.format(cal.getTime());//String.format("-%d.properties", cal.get(Calendar.MILLISECOND));
					CatalogFileBuilder ncatalog=new CatalogFileBuilder(CatalogFileBuilder.MysqlConnURL("localhost", "3306"),"root","proot");
					String fileurl=ncatalog.write(strm, "");
					File f=new File(fileurl);
					if(f.exists())
					{f.delete();}
					boolean x =false;
					//x=statement.execute(sql);				
					//logger.info("after trim: " + sql);
					return x;
				}
			}
//	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
//	    Calendar cal = Calendar.getInstance();
//	    String strm=".properties";//String.format("-%d.properties", cal.get(Calendar.MILLISECOND));
//	    System.out.println(dateFormat.format(cal.getTime())+strm);		
		
//		Schema schema = new Schema(new String[] {"A", "B", "C", "D"});
//		Row[] data = new Row[] { 	new Row(new String[] {"1", "2", "3", "D1"}),
//									new Row(new String[] {"4", "5", "6", "D2"}),
//									new Row(new String[] {"7", "8", "9", "D3"}),
//									new Row(new String[] {"10", "11", "12", "D4"})
//		};
//		
//		QueryResultModel queryResultModel = new QueryResultModel(schema, data);
//		
//		return queryResultModel;
		}
	}
}
