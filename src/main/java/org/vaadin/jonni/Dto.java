package org.vaadin.jonni;

import java.util.List;

public class Dto {

	private String name;

	private double low;
	private double mean;
	private double high;

	private List<String> emails;

	public Dto(String name, double low, double mean, double high, List<String> emails) {
		super();
		this.name = name;
		this.low = low;
		this.mean = mean;
		this.high = high;
		this.setEmails(emails);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

}
