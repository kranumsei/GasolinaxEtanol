package com.plz.nerf.gasolinaxalcool;

public class FuelPicker {

	private double alcoholPrice;
	private double gasPrice;
	private double equivalentAlcoholPrice;
	private double equivalentGasPrice;
	private double alcoholGasRelation;
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
			this.setAlcoholGasRelation();
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
		if (this.alcoholGasRelation >= 70) {
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

	public double getAlcoholGasRelation() {
		String formated = alcoholGasRelation+"";
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
			return alcoholGasRelation;
		}
	}

	private void setAlcoholGasRelation() {
		this.alcoholGasRelation = alcoholPrice * 100 / gasPrice;
	}

	public double getEquivalentAlcoholPrice() {
			return equivalentAlcoholPrice;
	}

	public void setEquivalentAlcoholPrice(double equivalentAlcoholPrice) {
		this.equivalentAlcoholPrice = equivalentAlcoholPrice;
	}

	public double getEquivalentGasPrice() {
		return equivalentGasPrice;
	}

	public void setBalancedGasPrice(double equivalentGasPrice) {
		this.equivalentGasPrice = equivalentGasPrice;
	}
	
}
