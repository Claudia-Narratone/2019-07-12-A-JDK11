package it.polito.tdp.food.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private List<Food> cibi;
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> graph;
	
	public Model() {
		dao= new FoodDao();
		graph=new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}

	public List<Food> getFoods(int portions) {
		this.cibi=(List<Food>) dao.getFoodsByPortions(portions);
		
		//aggiungo vertici
		Graphs.addAllVertices(graph, this.cibi);
		
		//aggiungo archi
		for(Food f1:cibi) {
			for(Food f2:cibi) {
				if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code()) {
					Double peso=dao.calorieCongiunte(f1, f2);
					if(peso!=null) {
						Graphs.addEdge(graph, f1, f2, peso);
					}
				}
			}
		}
		System.out.println(graph);
		
		return this.cibi;
	}
	
	
}
