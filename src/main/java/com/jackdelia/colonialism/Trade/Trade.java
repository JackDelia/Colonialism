package com.jackdelia.colonialism.Trade;

import java.util.Observable;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.resource.Resource;

public class Trade extends Observable{
	private City exporter;
	private City importer;
	private Double amount;
	private Resource resource;
	
	public Trade(City exporter, City importer, Double amount, Resource resource) {
		this.exporter = exporter;
		this.importer = importer;
		this.amount = amount;
		this.resource = resource;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
		setChanged();
        notifyObservers(amount);
	}

	public City getExporter() {
		return exporter;
	}

	public City getImporter() {
		return importer;
	}

	public Resource getResource() {
		return resource;
	}

}
