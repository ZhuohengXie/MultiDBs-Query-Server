package edu.pitt.sis.infsci2711.query.server.viewModels;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class QueryResultViewModel {

	SchemaViewModel schema;
	
	RowViewModel[] data;
	
	public QueryResultViewModel(final SchemaViewModel schemaP, final RowViewModel[] dataP) {
		schema = schemaP;
		data = dataP;
	}
	
	public SchemaViewModel getSchema() {
		return schema;
	}
	
	public RowViewModel[] getData() {
		return data;
	}
}
