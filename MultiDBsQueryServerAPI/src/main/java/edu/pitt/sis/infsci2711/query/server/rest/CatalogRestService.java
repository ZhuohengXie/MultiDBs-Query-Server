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

import edu.pitt.sis.infsci2711.query.server.business.CatalogService;
import edu.pitt.sis.infsci2711.query.server.models.CatalogModel;
import edu.pitt.sis.infsci2711.query.server.viewModels.CatalogViewModel;

@Path("Catalog/")
public class CatalogRestService {
	
	private static final Logger logger = LogManager.getLogger(CatalogRestService.class);
	
	@Path("add/")
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(final CatalogViewModel catalog) {
		
		logger.info(String.format("Got PUT request to add catalog: %s", catalog.toString()));
		
		CatalogService service = new CatalogService();
		
		try {
			boolean queryResult = service.add(convertViewModelToDB(catalog));
			
			if (!queryResult){
				logger.error("Query resutl is false, apparently something is wrong with the catalog model.");
				return Response.status(500).entity("{\"error\":\"please check your input and try again\"}").build();
			}
			logger.info("Added new catalog successfully");
			return Response.status(200).entity("{\"success\":\"the record has been saved\"}").build();
		} catch (Exception e) {
			//return Response.status(500).build();
			String errmsg = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
			logger.error("error: ", e);
			return Response.status(500).entity("{\"error\":\"" + e.getClass().getSimpleName() + "\",\"message\":\"" + errmsg + "\"}").build();
		}
		
	}
	
	@Path("drop/{id}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response drop(@PathParam("id") final String id) {
		
		CatalogService service = new CatalogService();
		
		try {
			boolean queryResult = service.drop(id);
			if(!queryResult)
			{
				return Response.status(500).entity("{\"error\":\"please check your input and try again\"}").build();
			}
			return Response.status(200).entity("{\"success\":\"the record has been deleted\"}").build();
		} catch (Exception e) {
			String errmsg=e.getCause()==null?e.getMessage():e.getCause().getMessage();
			logger.error("error:",e);
			return Response.status(500).entity("{\"error\":\""+e.getClass().getSimpleName()+"\",\"message\":\""+errmsg+"\"}").build();
		}
	}
	
	private CatalogModel convertViewModelToDB(final CatalogViewModel model) {
		return new CatalogModel(model.getId(),model.getIP(),model.getPort(),model.getDBType(),model.getUsername(),model.getPassword(),model.getDBname());
	}
}
