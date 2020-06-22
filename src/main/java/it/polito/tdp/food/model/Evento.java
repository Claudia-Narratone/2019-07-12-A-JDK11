package it.polito.tdp.food.model;

public class Evento implements Comparable<Evento>{

	public enum EventType{
		INIZIO_PREPARAZIONE,
		FINE_PREPARAZIONE,
	}
	
	private Double time;
	private EventType type;
	private Stazione stazione;
	private Food food;
	
	public Evento(double time,EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.stazione = stazione;
		this.food = food;
		this.type=type;
	}

	public Double getTime() {
		return time;
	}

	public Stazione getStazione() {
		return stazione;
	}

	public Food getFood() {
		return food;
	}

	@Override
	public int compareTo(Evento o) {
		return this.time.compareTo(o.time);
	}

	public EventType getType() {
		return type;
	}
	
	
}
