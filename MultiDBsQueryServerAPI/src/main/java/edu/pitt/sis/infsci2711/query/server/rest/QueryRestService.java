package edu.pitt.sis.infsci2711.query.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.infsci2711.query.server.business.QueryService;
import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.models.QueryResultModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.QueryResultViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.QueryViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.RowViewModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.SchemaViewModel;

@Path("Query/")
public class QueryRestService {

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
	
//	@Path("{id}")
//	@GET
//    @Produces(MediaType.APPLICATION_JSON)
//	public Response personById(@PathParam("id") final int id) {
//		
//		PersonService personService = new PersonService();
//		
//		try {
//			PersonDBModel personsDB = personService.findById(id);
//		 
//			if (personsDB != null) {
//				Person person = convertDbToViewModel(personsDB);
//			
//				return Response.status(200).entity(person).build();
//			}
//			return Response.status(404).entity("Person not found").build();
//		} catch (Exception e) {
//			return Response.status(500).build();
//		}
//		
//	}
	
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPerson(final QueryViewModel query) {
		
		QueryService queryService = new QueryService();
		
		try {
			QueryResultModel queryResult = queryService.run(convertViewModelToDB(query));
		
			QueryResultViewModel personInserted = convertDbToViewModel(queryResult);
			
			return Response.status(200).entity(personInserted).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
		
	}

	private QueryModel convertViewModelToDB(final QueryViewModel query) {
		return new QueryModel(query.getQuery());
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
}
