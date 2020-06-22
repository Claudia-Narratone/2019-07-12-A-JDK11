package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Evento.EventType;
import it.polito.tdp.food.model.Food.statoPreparazione;

public class Simulator {
	
	//modello del mondo
	private List<Stazione> stazioni;
	private List<Food> cibi;
	
	private Graph<Food, DefaultWeightedEdge> graph;
	private Model model;
	
	//parametri di simulazione
	private int K=5; //num. staz. disponibili
	
	//risultati
	private Double tempoPreparazione;
	private int cibiPreparati;
	
	//coda degli eventi
	private PriorityQueue<Evento> queue;
	
	
	
	public Simulator(Graph<Food, DefaultWeightedEdge> graph, Model model) {
		this.graph=graph;
		this.model=model;
	}

	public void init(Food partenza) {
		this.cibi=new ArrayList<Food>(this.graph.vertexSet());
		for(Food cibo:cibi) {
			cibo.setPreparazione(statoPreparazione.DA_PREPARARE);
		}
		stazioni=new ArrayList<Stazione>();
		for(int i=0; i<this.K; i++) {
			this.stazioni.add(new Stazione(true, null));
		}
		
		this.tempoPreparazione=0.0;
		this.cibiPreparati=0;
		this.queue=new PriorityQueue<Evento>();
		
		List<FoodCalories> vicini=model.elencoCibiConnessi(partenza);
		for(int i=0; i<this.K && i<vicini.size(); i++) {
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setFood(vicini.get(i).getFood());
			
			Evento e=new Evento(vicini.get(i).getCalories(), EventType.FINE_PREPARAZIONE, this.stazioni.get(i), vicini.get(i).getFood());
			queue.add(e);
		}
	}
	
	public void run() {
		
		while(!queue.isEmpty()) {
			Evento e=queue.poll();
			processEvent(e);
		}
	}
	

	private void processEvent(Evento e) {
		switch (e.getType()) {
		case INIZIO_PREPARAZIONE:
			List<FoodCalories> vicini=model.elencoCibiConnessi(e.getFood());
			FoodCalories prossimo=null;
			for(FoodCalories vicino:vicini) {
				if(vicino.getFood().getPreparazione()==statoPreparazione.DA_PREPARARE) {
					prossimo=vicino;
					break; //non proseguire nel ciclo
				}
			}
			
			if(prossimo!=null) {
				prossimo.getFood().setPreparazione(statoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getFood());
				
				Evento e2=new Evento(e.getTime()+prossimo.getCalories(), 
						EventType.FINE_PREPARAZIONE, 
						e.getStazione(),
						prossimo.getFood());
				this.queue.add(e2);
			}
			break;

		case FINE_PREPARAZIONE:
			this.cibiPreparati++;
			this.tempoPreparazione=e.getTime();
			
			e.getStazione().setLibera(true);
			e.getFood().setPreparazione(statoPreparazione.PREPARATO);
			
			Evento e2=new Evento(e.getTime(), EventType.INIZIO_PREPARAZIONE, e.getStazione(), e.getFood());
			this.queue.add(e2);
			break;
		}
		
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public Double getTempoPreparazione() {
		return tempoPreparazione;
	}

	public int getCibiPreparati() {
		return cibiPreparati;
	}

	
}
