package edu.pitt.sis.infsci2711.query.server.business;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.models.QueryResultModel;
import edu.pitt.sis.infsci2711.query.server.models.Row;
import edu.pitt.sis.infsci2711.query.server.models.Schema;
import edu.pitt.sis.infsci2711.query.server.utils.JdbcPresto;


public class QueryService {
	
	private static final Logger logger = LogManager.getLogger(QueryService.class);

	public QueryResultModel run(final QueryModel convertViewModelToDB) throws SQLException, Exception {
		
		logger.info("Got query to run: " + convertViewModelToDB.getQuery());
		
		try (Connection connection = JdbcPresto.getConnection()) {
			String sql = convertViewModelToDB.getQuery() ;
			//logger.info("ends with: " + sql.endsWith(";")+"actual"+sql.charAt( sql.length()-1) );
			while(sql.endsWith(";")||sql.endsWith("\n"))
			{
				//logger.info("ends with: " + sql.endsWith(";") );
				sql=sql.substring(0, sql.length()-1);
			}	
			while(sql.startsWith(";")||sql.startsWith("\n")||sql.startsWith(" ")||sql.startsWith("\t")||sql.startsWith("\r"))
			{
				//logger.info("ends with: " + sql.endsWith(";") );
				sql=sql.substring(1);
			}
			DatabaseMetaData md = connection.getMetaData();
            logger.info("support multi update?:    "+md.supportsBatchUpdates()); 
			try (Statement statement = connection.createStatement())
			{
				int nrow = 0;	
				int ncol=	0;
				if(sql.startsWith("create")||sql.startsWith("Create"))
				{
					nrow=-1;;//nrow=0;
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
			catch (Exception ex) {
				
			    logger.info(ex.toString());
			    String[] cols=new String[1];
			    cols[0]="error message";
			    Schema schema = new Schema(cols);
			    String[] arr=new String[1];arr[0]=ex.toString();
			    Row[] data=new Row[1];
			    data[0]=new Row(arr);
			    
				QueryResultModel queryResultModel = new QueryResultModel(schema, data);
				return queryResultModel;
			}
		}
		
		
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
