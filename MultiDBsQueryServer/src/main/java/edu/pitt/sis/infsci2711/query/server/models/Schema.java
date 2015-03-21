package edu.pitt.sis.infsci2711.query.server.models;

public class Schema {
	String[] columnNames;
	
	public Schema(final String[] columnNamesP) {
		columnNames = columnNamesP;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
}
