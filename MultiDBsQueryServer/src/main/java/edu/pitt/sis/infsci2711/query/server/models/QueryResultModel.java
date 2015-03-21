package edu.pitt.sis.infsci2711.query.server.models;

public class QueryResultModel {
	Schema schema;
	
	Row[] data;
	
	public QueryResultModel(final Schema schemaP, final Row[] dataP) {
		schema = schemaP;
		data = dataP;
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public Row[] getData() {
		return data;
	}
}
