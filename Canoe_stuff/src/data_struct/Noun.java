package data_struct;
/*
 *Author: Michael Ford
 *TCSS 342
 *Autumn 2015
 *
 */

import java.util.LinkedList;
import java.util.List;

public class Noun {
	private List<Noun> myHypers;
	private String myNoun;
	private int hypers =0;
	private Integer myID;
	private String myDescription;
	
	//parameterized constructor
	public Noun(Integer theID, String theNoun, String theDescription){
		myNoun = theNoun;
		myID = theID;
		myDescription = theDescription;
		myHypers = new LinkedList<Noun>();
	}
	
	//Adds a hypernym to the list of hypernyms
	public void addHypernym(Noun theNoun){
		myHypers.add(theNoun);
		hypers++;
	}
	
	//Return noun of the vertex
	public String getNoun(){
		String theNoun = myNoun;
		return theNoun;
	}
	
	//return integer reresentation of synset ID
	public Integer getID(){
		Integer id = myID;
		return id;
	}
	
	//returns description of nouns
	public String getDescription(){
		String descript = myDescription;
		return descript;
	}
	
	//does the vertex contain a certain hypernym
	public boolean containsHyper(Noun theHyp){
		return myHypers.contains(theHyp);
	}
	
	//is the vertex empty
	public boolean isEmpty(){
		return myNoun.equals(null);
	}
	
	//returns the amount of hypernyms
	public int hypernymsAmount(){
		int hyps = hypers;
		return hyps;
	}
	
	//returns the list of hypernyms
	public List<Noun> getHypernyms(){
		return myHypers;
	}
	
	//returns the nouns of the hypernyms
	public String getHyperNouns(){
		String s = "";
		for(int i = 0; i< myHypers.size(); i++){
			if(i==myHypers.size()-1){
				s=s+myHypers.get(i).getNoun();
			}else{
				s= s + myHypers.get(i).getNoun()+", ";
			}
			
		}
		return s;
	}
	
	
	//Overidden toString method
	@Override
	public String toString(){
		return "["+myID+", "+ myNoun+"]";
	}
}
