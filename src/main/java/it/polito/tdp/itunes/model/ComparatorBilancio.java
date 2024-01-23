package it.polito.tdp.itunes.model;

import java.util.Comparator;

public class ComparatorBilancio implements Comparator<Album>{

	public int compare(Album o1, Album o2) {
		return -(o1.getBilancio() - o2.getBilancio());
	}

}
