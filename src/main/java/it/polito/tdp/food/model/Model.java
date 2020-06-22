package it.polito.tdp.food.model;

import java.util.List;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private List<Food> cibi;
	private FoodDao dao;
	
	public Model() {
		dao= new FoodDao();
	}

	public List<Food> getFoods(int portions) {
		this.cibi=(List<Food>) dao.getFoodsByPortions(portions);
		return this.cibi;
	}
}
