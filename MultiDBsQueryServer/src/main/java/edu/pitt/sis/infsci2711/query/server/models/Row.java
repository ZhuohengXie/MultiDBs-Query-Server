package edu.pitt.sis.infsci2711.query.server.models;

public class Row {
	String[] data;
	
	public Row(final String[] dataP) {
		data = dataP;
	}
	
	public String[] getRow() {
		return data;
	}
}
