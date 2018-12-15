package com.jackdelia.colonialism.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.jackdelia.colonialism.resource.Resource;

public class Stockpile extends Observable {
	private HashMap<Resource, Double> resources;
	private HashMap<Resource, Double> stockpileTargets;
	
	protected Stockpile() {
		this.resources = new HashMap<Resource, Double>();
		this.stockpileTargets = new HashMap<Resource, Double>();
	}
	
	protected Double getResourceStockpile(Resource resource) {
		Double resourceStockpile = this.resources.get(resource);
		if(resourceStockpile == null) {
			resourceStockpile = 0.0;
		}
		
		return resourceStockpile;
	}
	
	protected Double getResourceStockpileExcess(Resource resource) {
		Double resourceStockpile = getResourceStockpile(resource);
		Double targetStockpile = getStockpileTarget(resource);
		double excess = Double.max(0.0, resourceStockpile - targetStockpile);
		
		return excess;
	}
	
	protected void addToStockpile(Resource resource, Double amount) {		
		if(amount > 0) {
			Double currentAmount = getResourceStockpile(resource);
			this.resources.put(resource, currentAmount + amount);
		}
	}
	
	protected Double removeFromStockpile(Resource resource, Double amount) {
		Double currentAmount = this.resources.get(resource);
		if (currentAmount == null) {
			currentAmount = 0.0;
		}
		
		if(amount > currentAmount) {
			amount = currentAmount;
		}
		
		this.resources.put(resource, currentAmount - amount);
		
		return amount;
	}
	
	protected Double transferResource(Resource resource, Double amount, Stockpile destination) {
		Double transfered = this.removeFromStockpile(resource, amount);
		destination.addToStockpile(resource, transfered);
		
		return transfered;
	}
	
	protected boolean hasResource(Resource resource) {
		return getResourceStockpile(resource) > 0;
	}
	
	protected ArrayList<Resource> getResourceTypes(){
		return this.resources.entrySet().stream()
				.map(Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
	}
	
	protected Double getStockpileTarget(Resource resource) {
		Double target = this.stockpileTargets.get(resource);
		if(target == null) {
			target = 10.0;
		}
		
		return target;
	}
	
	protected void setStockpileTarget(Resource resource, Double amount) {
		this.stockpileTargets.put(resource,  amount);
	}
}
