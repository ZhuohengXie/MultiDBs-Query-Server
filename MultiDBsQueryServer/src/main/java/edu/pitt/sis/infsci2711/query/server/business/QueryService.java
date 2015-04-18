package edu.pitt.sis.infsci2711.query.server.business;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyClientUtil;
import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.models.QueryResultModel;
import edu.pitt.sis.infsci2711.query.server.models.Row;
import edu.pitt.sis.infsci2711.query.server.models.SaveQueryModel;
import edu.pitt.sis.infsci2711.query.server.models.Schema;
import edu.pitt.sis.infsci2711.query.server.utils.CatalogFileBuilder;
import edu.pitt.sis.infsci2711.query.server.utils.JdbcMysql;
import edu.pitt.sis.infsci2711.query.server.utils.JdbcPresto;
import edu.pitt.sis.infsci2711.query.server.utils.PrestoCmdManager;
import edu.pitt.sis.infsci2711.query.server.utils.SQLParser;
import edu.pitt.sis.infsci2711.query.server.utils.PropertiesPlugin;
import edu.pitt.sis.infsci2711.query.server.viewModels.RegisterViewModel;


public class QueryService {
	
	private static final Logger logger = LogManager.getLogger(QueryService.class);

	public QueryResultModel run(final QueryModel convertViewModelToDB) throws SQLException, Exception {
		
		logger.info("Got query to run: " + convertViewModelToDB.getQuery());	
		//run presto in local 
		try (Connection connection = JdbcPresto.getConnection()) {
			String sql = convertViewModelToDB.getQuery() ;
			//parse sql
			sql=SQLParser.rebuild(sql);
			logger.info("after parse:  "+sql );
			
			try (Statement statement = connection.createStatement())
			{
				int nrow = 0;	
				int ncol=0;
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

	public boolean save(final SaveQueryModel saveInfo) throws SQLException, Exception {
		
		logger.info("Got query to run & save: " + saveInfo.getQuery());
		
		String sql = saveInfo.getQuery() ;
		sql = SQLParser.rebuild(sql);
		
		logger.info("after parse:  " + sql);		
		
		if (!sql.toLowerCase().startsWith("select")) {
			throw new SQLFeatureNotSupportedException("Only 'select' statements can be saved.");
		}
		else
		{
			// a file name based on milliseconds
			//PrestoCmdManager.stop(null);	
		    String catalogTempFileName = generateTempCatalogFileName();
		    
		    // Build a catalog file
		    String dbconnUrl = CatalogFileBuilder.getConnectionURL(saveInfo.getDBType(), saveInfo.getIP(), saveInfo.getPort());
			CatalogFileBuilder ncatalog = new CatalogFileBuilder(dbconnUrl, saveInfo.getUsername(), saveInfo.getPassword());
			
			String fileurl = ncatalog.write(catalogTempFileName, "");
			
			//here just simply restart, but later, please change it to double-threading to reduce the response time.
			PrestoCmdManager.deepRestart(null);	
			
			//Connector to mysql to create db first
			createDatabaseIfNeeded(saveInfo);
		
			executeCreateTable(saveInfo, sql, catalogTempFileName, fileurl);
			
			makeSureTablesIsCreated(catalogTempFileName, saveInfo.getDBname(), saveInfo.gettableName());
			
//			Thread.sleep(5000);
			//boolean regSuccess = true;
			boolean regSuccess = registerInMetastore(saveInfo);
			
			deleteCatalogIfExists(fileurl);

			return regSuccess;
		}
	}

	private void makeSureTablesIsCreated(String catalogName, String dbName, String tableName) throws SQLException, Exception {
		// TODO Auto-generated method stub
		try (Connection connection = JdbcPresto.getConnection()) {
			
			String sql = String.format("select * from %s.%s.%s", catalogName, dbName, tableName);
			logger.info("SQL to test if table was created: " + sql);
			
			for (int i = 0; i < PropertiesPlugin.getMakeSureTablesIsCreatedNumberAttemps(); i++) {
				logger.info(String.format("Going to retry %d the query %s", i + 1, sql));
				
				try (Statement statement = connection.createStatement()) {
					logger.info("About to execute query");
					ResultSet result = statement.executeQuery(sql);
					logger.info("exectued successfully, now going to break");
					break;
				}
				catch (Exception e) {
					// Ignoring since we need to retry the query for several times.
					logger.error("Sleep before trying again", e);
					Thread.sleep(PropertiesPlugin.getMakeSureTablesIsCreatedSleepMilliseconds());
				}
			}
		}
	}

	private String generateTempCatalogFileName() {
		DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-SSS");
		Calendar cal = Calendar.getInstance();
		String strm = dateFormat.format(cal.getTime());
		strm = strm.replaceAll("-", "");
		strm = "tmp" + strm;
		return strm;
	}

	private void createDatabaseIfNeeded(final SaveQueryModel saveInfo)
			throws Exception {
		try
		{
			//later this should be wrapped into a class for doing this stuff for different kinds of databases: cassandra, hive, etc.
			if(saveInfo.getDBType().toLowerCase().equals("mysql"))
			{
				Connection mysqlConn=JdbcMysql.getConnection(saveInfo.getIP(), saveInfo.getPort(), saveInfo.getUsername(), saveInfo.getPassword());
				Statement mysqlStatement=mysqlConn.createStatement();
				String mysqlCreateDB=String.format("CREATE DATABASE IF NOT EXISTS `%s`",saveInfo.getDBname());
				mysqlStatement.execute(mysqlCreateDB);
			}
			else
			{
				throw new Exception("Database type: "+saveInfo.getDBType()+" currently not supported;");
			}
		}
		catch (Exception e)
		{throw e;}
	}

	private void executeCreateTable(final SaveQueryModel saveInfo, String sql,
			String catalogName, String fileurl) throws Exception, SQLException {
		try (Connection connection = JdbcPresto.getConnection()) {
			
			try (Statement statement = connection.createStatement()) {
				String sql2 = String.format("create table %s.%s.\"%s\" as %s", catalogName,
						saveInfo.getDBname(), saveInfo.gettableName(), sql);
				logger.info("after manipulate:  " + sql2 );	
				statement.executeQuery(sql2);																				
			}
			catch(Exception e) {
				deleteCatalogIfExists(fileurl);
				throw e;
			}
		}
	}

	private void deleteCatalogIfExists(String fileurl) {
		File f=new File(fileurl);
		if(f.exists())
		{f.delete();}
	}

	public boolean registerInMetastore(final SaveQueryModel saveInfo) {
		int intport=Integer.parseInt(saveInfo.getPort());
		RegisterViewModel model = new RegisterViewModel( saveInfo.getDBType(),saveInfo.getIP(),intport,saveInfo.getUsername(),
			saveInfo.getPassword(), saveInfo.getDBname(), saveInfo.getTitle(), saveInfo.getDescription());
		
		Response result2;
		try {
			result2 = JerseyClientUtil.doPut(PropertiesPlugin.getMetastoreURL(), PropertiesPlugin.getMetastoreRegister(), model);
			logger.info("conpleted doPut");
			if (result2.getStatus() != 200) {
				return false;
			}
			else {
				return true;
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("failed at doPut to metastore. error:",e);
			return false;
		}

	}
}
