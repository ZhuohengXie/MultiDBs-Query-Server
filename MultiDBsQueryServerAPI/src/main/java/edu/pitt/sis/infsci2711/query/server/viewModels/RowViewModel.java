package edu.pitt.sis.infsci2711.query.server.viewModels;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RowViewModel {
	@XmlElementWrapper(name="row")
	String[] data;
	
	public RowViewModel() {
		
	}
	
	public RowViewModel(final String[] dataP) {
		data = dataP;
	}
	
	public String[] getRow() {
		return data;
	}
}
