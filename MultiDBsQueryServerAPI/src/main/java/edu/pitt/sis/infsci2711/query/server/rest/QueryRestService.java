package edu.pitt.sis.infsci2711.query.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.infsci2711.query.server.business.QueryService;
import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.models.QueryResultModel;
import edu.pitt.sis.infsci2711.query.server.models.SaveQueryModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.QueryResultViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.QueryViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.RowViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.SaveQueryViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.SchemaViewModel;

@Path("Query/")
public class QueryRestService {
	private static final Logger logger = LogManager.getLogger(QueryService.class);
	
	@Path("{id}/{tableName}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response tableFullContent(@PathParam("id") final int id, @PathParam("tableName") final String tableName) {
		
		QueryService queryService = new QueryService();
		
		try {
		  //see function convertViewModelToDB() below
					QueryResultModel queryResult=queryService.run(convertViewModelToDB(id,tableName));
					QueryResultViewModel viewModel = convertDbToViewModel(queryResult);
					return Response.status(200).entity(viewModel).build();
		} catch (Exception e) {
			String errmsg=e.getCause()==null?e.getMessage():e.getCause().getMessage();
			logger.error("error:",e);
			return Response.status(500).entity("{\"error\":\""+e.getClass().getSimpleName()+"\",\"message\":\""+errmsg+"\"}").build();
		}
	}
	
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response runQuery(final QueryViewModel query) {
		
		QueryService queryService = new QueryService();
		//see function convertViewModelToDB() below
		try {
			QueryResultModel queryResult = queryService.run(convertViewModelToDB(query));
		
			QueryResultViewModel queryResultViewModel = convertDbToViewModel(queryResult);
			
		//	GenericEntity<QueryResultViewModel> entity = new GenericEntity<QueryResultViewModel>(queryResultViewModel) {};
			
			return Response.status(200).entity(queryResultViewModel).build();
		} catch (Exception e) {
			//return Response.status(500).build();
			logger.error("error:",e);
			String errmsg=e.getCause()==null?e.getMessage():e.getCause().getMessage();
			return Response.status(500).entity("{\"error\":\""+e.getClass().getSimpleName()+"\",\"message\":\""+errmsg+"\"}").build();
		}
	}
	
	@Path("Save/")
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(final SaveQueryViewModel query) {
		
		QueryService queryService = new QueryService();
		
		try {
			boolean queryResult = queryService.save(convertViewModelToDB(query));
		
			if(!queryResult)
//			{return Response.status(500).entity("{\"error\" : \"error creating table\"}").build();}
//			Thread.sleep(8000);
//			boolean registerResult = queryService.registerInMetastore(convertViewModelToDB(query));
//			if(!registerResult)
			{return Response.status(500).entity("{\"error\":\"error registering datasource on Metastore\"}").build();}
			
			return Response.status(200).entity("{\"success\":\"the table has been saved\"}").build();
		} catch (Exception e) {
			//return Response.status(500).build();
			String errmsg=e.getCause()==null?e.getMessage():e.getCause().getMessage();
			logger.error("error:",e);
			return Response.status(500).entity("{\"error\":\""+e.getClass().getSimpleName()+"\",\"message\":\""+errmsg+"\"}").build();
		}
		
	}
	
	private QueryModel convertViewModelToDB(final QueryViewModel query) {
		return new QueryModel(query.getQuery());
	}
	
	private QueryModel convertViewModelToDB(final int id, final String table) {
		String querystr=String.format("select * from %d.%s", id,table);
		return new QueryModel(querystr);
	}
	
	private SaveQueryModel convertViewModelToDB(final SaveQueryViewModel query) {
		return new SaveQueryModel(query.getIP(),query.getPort(),query.getDBType(),query.getUsername(),query.getPassword(),query.getDBname(),query.getTitle(),query.getDescription(),query.getTableName(),query.getQuery());
	}
	
	private QueryResultViewModel convertDbToViewModel(final QueryResultModel queryResult) {
		SchemaViewModel schema = new SchemaViewModel(queryResult.getSchema().getColumnNames());
		RowViewModel[] data = new RowViewModel[queryResult.getData().length];
		
		for (int i = 0; i < data.length; i++) {
			data[i] = new RowViewModel(queryResult.getData()[i].getRow());
		}
		
		QueryResultViewModel result = new QueryResultViewModel(schema, data);
		
		return result;
	}

//	@GET
//    @Produces(MediaType.APPLICATION_JSON)
//	public Response allPersons() {
//		
//		PersonService personService = new PersonService();
//		
//		List<PersonDBModel> personsDB;
//		try {
//			personsDB = personService.getAll();
//		
//			List<Person> persons = convertDbToViewModel(personsDB);
//			
//			GenericEntity<List<Person>> entity = new GenericEntity<List<Person>>(persons) {};
//			
//			return Response.status(200).entity(entity).build();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			return Response.status(500).build();
//		}
//		
//	}
	
}
