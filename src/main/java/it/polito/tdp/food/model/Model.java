package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
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
		
	}

	public List<Food> getFoods(int portions) {
		this.cibi=(List<Food>) dao.getFoodsByPortions(portions);
		
		graph=new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
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
	
	public String simula(Food cibo, int K) {
		Simulator simulator=new Simulator(graph, this);
		simulator.setK(K);
		simulator.init(cibo);
		simulator.run();
		String messaggio=String.format("Preparati %d cibi in %f minuti\n", simulator.getCibiPreparati(), simulator.getTempoPreparazione());
		return messaggio;
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		
	List<FoodCalories> result=new ArrayList<FoodCalories>();
	
	List<Food> vicini=Graphs.neighborListOf(graph, f);
	for(Food v:vicini) {
		Double calorie=this.graph.getEdgeWeight(this.graph.getEdge(f, v));
		result.add(new FoodCalories(v, calorie));
	}
	Collections.sort(result);
	return result;
	
	}
	
}
