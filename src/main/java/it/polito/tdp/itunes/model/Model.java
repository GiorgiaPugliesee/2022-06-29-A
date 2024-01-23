package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Album, DefaultWeightedEdge> grafo; 
	private ItunesDAO dao;
	private List<Album> albumNTracks;
	private Map<Integer, Album> idMap;
	List<Album> bestPath;
	int bestScore;
	
	public Model() {
		dao = new ItunesDAO();
		idMap = new HashMap<Integer, Album>();
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

	}
	
	public void creaGrafo(int n) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.albumNTracks = this.dao.getAlbumNTracks(n);
		
		if(this.idMap.isEmpty()) {
			for(Album a : albumNTracks) {
				idMap.put(a.getAlbumId(), a);
			}
		}
		
		Graphs.addAllVertices(grafo, albumNTracks);
		
		for(Album a1 : this.grafo.vertexSet()) {
			for(Album a2 : this.grafo.vertexSet()) {
				int peso = a1.getNumSongs() - a2.getNumSongs();
				
				if(peso > 0) {
					Graphs.addEdgeWithVertices(grafo, a2, a1, peso);
				}
			}
		}

	}
	
	public List<Album> getAdiacenti(Album a) {
		List<Album> successori = Graphs.successorListOf(grafo, a);
		
		for(Album al : successori) {
			this.getBilancio(al);
		}
		Collections.sort(successori, new ComparatorBilancio());
		return successori;
	}
	
	public int getBilancio(Album a) {
		int bilancio = 0;
		
		List<DefaultWeightedEdge> edgesIn = new ArrayList<DefaultWeightedEdge>(this.grafo.incomingEdgesOf(a));
		List<DefaultWeightedEdge> edgesOut = new ArrayList<DefaultWeightedEdge>(this.grafo.outgoingEdgesOf(a));
		
		for(DefaultWeightedEdge e : edgesIn) {
			bilancio += this.grafo.getEdgeWeight(e);
		}
		
		for(DefaultWeightedEdge e : edgesOut) {
			bilancio -= this.grafo.getEdgeWeight(e);
		}
		
		a.setBilancio(bilancio);
		return bilancio;
	}
	
	//determina il percorso minimo tra le 2 fermate (grafo pesato): calcolo cammini minimi
//		public List<Fermata> percorso(Fermata partenza, Fermata arrivo) {
//			
//			//oggetto che calcola i cammini minimi
//			DijkstraShortestPath<Fermata, DefaultWeightedEdge> sp = new DijkstraShortestPath<>(this.grafo);
//			
//			GraphPath<Fermata, DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo);
//			
//			return gp.getVertexList();
//		}
	
	public List<Album> buildPath(Album source, Album target, int x) {
		List<Album> parziale = new ArrayList<>();
		this.bestPath = new ArrayList<>();
		this.bestScore = 0;
		
		parziale.add(source);
		
		ricorsione(parziale, target, x);
		
		return this.bestPath;
	}
	
	private void ricorsione(List<Album> parziale, Album target, int x) {
		Album current = parziale.get(parziale.size()-1);
		
		//condizione di uscita
		if(current.equals(target)) {
			
			//controllo se questa soluzione è migliore del best
			if(getScore(parziale) > this.bestScore) {
				this.bestScore = getScore(parziale);
				this.bestPath = new ArrayList<>(parziale);
			}
			return;
		}
		
		//continuo ad aggiungere elementi a parziale
		List<Album> successori = Graphs.successorListOf(grafo, current);
		
		for(Album a : successori) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(current, a)) >= x) {
				parziale.add(a);
				ricorsione(parziale, target, x);
				parziale.remove(a);   //backtracking
			}

		}
	}

	private int getScore(List<Album> parziale) {
		int score = 0;
		Album source = parziale.get(0);
		
		for(Album a : parziale.subList(1, parziale.size()-1)) {
			if(getBilancio(a) > getBilancio(source)) {
				score += 1;
			}
		}
		return score;
	}

	public List<Album> allVertex() {
		List<Album> nodes = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(nodes);
		return nodes;
	}
	
	public Integer getVertex() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer getEdge() {
		return this.grafo.edgeSet().size();
	}
	
}
