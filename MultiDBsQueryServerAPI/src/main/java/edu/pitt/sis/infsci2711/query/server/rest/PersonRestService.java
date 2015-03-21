package edu.pitt.sis.infsci2711.query.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.infsci2711.query.server.business.QueryService;
import edu.pitt.sis.infsci2711.query.server.models.QueryModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.QueryViewModel;

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
		
			Person personInserted = convertDbToViewModel(personsDB);
			
			return Response.status(200).entity(personInserted).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
		
	}

	private QueryModel convertViewModelToDB(final QueryViewModel query) {
		return new QueryModel(query.getQuery());
	}
//
//	private List<Person> convertDbToViewModel(final List<PersonDBModel> personsDB) {
//		List<Person> result = new ArrayList<Person>();
//		for(PersonDBModel personDB : personsDB) {
//			result.add(convertDbToViewModel(personDB));
//		}
//		
//		return result;
//	}
//	
//	private Person convertDbToViewModel(final PersonDBModel personDB) {
//		return new Person(personDB.getId(), personDB.getFirstName(), personDB.getLastName());
//	}
}
