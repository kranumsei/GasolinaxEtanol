package com.plz.nerf.gasolinaxalcool;

public class FuelPicker {

	private double alcoholPrice;
	private double gasPrice;
	private double equivalentAlcoholPrice;
	private double equivalentGasPrice;
	private double alcoholRelation;
	private double gasRelation;
	private boolean shouldUseGas;
	
	public FuelPicker(String alcoholPrice, String gasPrice) {
		if(!alcoholPrice.equals("")){
			this.alcoholPrice = Double.parseDouble(alcoholPrice);
		}else{
			this.alcoholPrice = 0.0;
		}

		if(!gasPrice.equals("")){
			this.gasPrice = Double.parseDouble(gasPrice);
		}else{
			this.gasPrice = 0.0;
		}

		
		//both prices are informed
		if (this.alcoholPrice > 0 && this.gasPrice > 0) {
			this.setAlcoholRelation();
			this.setGasRelation();
			this.setIsGasCheaper(this.alcoholPrice, this.gasPrice);

		
		//only alcohol price is informed
		} else if (this.alcoholPrice > 0 && this.gasPrice == 0) {
			this.equivalentGasPrice = this.alcoholPrice / 0.7;
			this.equivalentAlcoholPrice = this.alcoholPrice;
			
		//only gas price is informed	
		} else if (this.alcoholPrice == 0 && this.gasPrice > 0) {
			this.equivalentAlcoholPrice = this.gasPrice * 0.7;
			this.equivalentGasPrice = this.gasPrice;
		}
	}

	public double gasPercentage(){

		return 0.0;
	}

	public double alcoholPercentage(){

		return 0.0;
	}

	public double getGasPrice() {
		return gasPrice;
	}

	public double getAlcoholPrice() {
		return alcoholPrice;
	}

	public boolean shouldUseGas() {
		return shouldUseGas;
	}
	
	private void setIsGasCheaper(double alcoholPrice, double gasPrice) {
		if (this.alcoholRelation >= 70) {
			this.shouldUseGas = true;
			this.equivalentAlcoholPrice = gasPrice * 0.7;
			this.equivalentGasPrice = gasPrice;
			//System.out.println("Alcohol should cost less than " + this.equivalentAlcoholPrice + " to be cheaper than gas.");
		} else {
			this.shouldUseGas = false;
			this.equivalentGasPrice = alcoholPrice / 0.7;
			this.equivalentAlcoholPrice = alcoholPrice;
			//System.out.println("Gas should cost no more than " + this.equivalentGasPrice + " to be cheaper than gas.");
		}		
	}

	public double getAlcoholRelation() {
		String formated = alcoholRelation +"";
        System.out.println(formated);
		try{
			String units = formated.split("\\.")[0];
            System.out.println(units);
			String numberOfDecimals = formated.split("\\.")[1];
            System.out.println(numberOfDecimals);
			if(numberOfDecimals.length() >2){
				numberOfDecimals = numberOfDecimals.substring(0, 2);
                System.out.println(numberOfDecimals +" available");
			}
			formated = units+"."+numberOfDecimals;
			return Double.parseDouble(formated);
		}catch (ArrayIndexOutOfBoundsException e){
		    e.printStackTrace();
			return alcoholRelation;
		}
	}

	private void setAlcoholRelation() {
		this.alcoholRelation = alcoholPrice * 100 / gasPrice;
	}

	public double getGasRelation(){
		String formated = gasRelation +"";
		System.out.println(formated);
		try{
			String units = formated.split("\\.")[0];
			System.out.println(units);
			String numberOfDecimals = formated.split("\\.")[1];
			System.out.println(numberOfDecimals);
			if(numberOfDecimals.length() >2){
				numberOfDecimals = numberOfDecimals.substring(0, 2);
				System.out.println(numberOfDecimals +" available");
			}
			formated = units+"."+numberOfDecimals;
			return Double.parseDouble(formated);
		}catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			return gasRelation;
		}
	}

	private void setGasRelation() {
		this.gasRelation = gasPrice * 100 / alcoholPrice;
	}

	public double getEquivalentAlcoholPrice() {
			return equivalentAlcoholPrice;
	}

	public void setEquivalentAlcoholPrice(double equivalentAlcoholPrice) {
		this.equivalentAlcoholPrice = equivalentAlcoholPrice;
	}

	public double absoluteSavingsPer40Liters(){
		double absoluteSavings;
		if(shouldUseGas()){
			absoluteSavings = (gasPrice*getSavings())/100;
		}else{
			absoluteSavings = (alcoholPrice*getSavings())/100;
		}
		return absoluteSavings*40;
	}

	public double getSavings(){
		double absoluteSavings;
		if(shouldUseGas()){
			absoluteSavings = alcoholPrice - (gasPrice*0.7);
			return (absoluteSavings*100)/alcoholPrice;
		}else{
			absoluteSavings = gasPrice - (alcoholPrice/0.7);
			return (absoluteSavings*100)/gasPrice;
		}
	}

	public double getEquivalentGasPrice() {
		return equivalentGasPrice;
	}

	public void setBalancedGasPrice(double equivalentGasPrice) {
		this.equivalentGasPrice = equivalentGasPrice;
	}
	
}
