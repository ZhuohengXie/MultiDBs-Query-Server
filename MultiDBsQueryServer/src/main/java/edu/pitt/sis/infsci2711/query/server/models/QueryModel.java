package edu.pitt.sis.infsci2711.query.server.models;

public class QueryModel {
	private String query;
	
	public QueryModel(final String queryP) {
		query = queryP;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(final String gueryP) {
		query = gueryP;
	}
}
