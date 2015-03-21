package edu.pitt.sis.infsci2711.query.server.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.models.QueryResultModel;
import edu.pitt.sis.infsci2711.query.server.models.Row;
import edu.pitt.sis.infsci2711.query.server.models.Schema;

public class QueryService {
	
	private static final Logger logger = LogManager.getLogger(QueryService.class);

	public QueryResultModel run(final QueryModel convertViewModelToDB) {
		
		logger.info("Got query to run: " + convertViewModelToDB.getQuery());
		
		Schema schema = new Schema(new String[] {"A", "B", "C", "D"});
		Row[] data = new Row[] { 	new Row(new String[] {"1", "2", "3", "D1"}),
									new Row(new String[] {"4", "5", "6", "D2"}),
									new Row(new String[] {"7", "8", "9", "D3"}),
									new Row(new String[] {"10", "11", "12", "D4"})
		};
		
		QueryResultModel queryResultModel = new QueryResultModel(schema, data);
		
		return queryResultModel;
	}

}
