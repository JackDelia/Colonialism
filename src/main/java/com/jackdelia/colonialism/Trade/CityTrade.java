package com.jackdelia.colonialism.Trade;

import java.util.ArrayList;
import java.util.Observable;

public class CityTrade extends Observable {
	private ArrayList<Trade> imports;
	private ArrayList<Trade> exports;
	
	public CityTrade() {
		imports = new ArrayList<Trade>();
		exports = new ArrayList<Trade>();
	}

	public ArrayList<Trade> getImports() {
		return imports;
	}
	
	public void addImport(Trade newImport) {
		imports.add(newImport);
		setChanged();
        notifyObservers(imports);
	}

	public ArrayList<Trade> getExports() {
		return exports;
	}
	
	public void addExport(Trade newExport){
		exports.add(newExport);
		setChanged();
        notifyObservers(exports);
	}
	
	

}
