package data_struct;
/*
 *Author: Michael Ford
 *TCSS 342
 *Autumn 2015
 *
 *A WordNet class that constructs a WordNet from reading in 2 txt files
 *Has a main method at the bottom that prompts user for input
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class WordNet {
	private static ArrayList<String> myStringNouns;
	@SuppressWarnings("unused")
	private Noun myRootVertex;
	private List<Noun> myVertexNouns;
	private List<LinkedList<Integer>> myHypers;
	private static String mySynsets;
	private static String myHypernyms;

	//Constructor of WordNet;
	public WordNet(String synsets, String hypernyms) throws IOException{
		try{
			mySynsets = synsets;
			myHypernyms = hypernyms;
			readIn(mySynsets, myHypernyms);//read in txt files
			addHyps();//apply hypernyms to appropriate synset nouns
		}catch(NullPointerException|IllegalArgumentException e){
			e.getMessage();
		}



	}

	//Helper method for reading in from text file or URL
	private void readIn(String synsets, String hypernyms) throws NumberFormatException, IOException{
		String http ="http://";
		BufferedReader brS;
		BufferedReader brH;
		//checks if strings contain hyper-text transfer protocol acronym
		if(synsets.contains(http)&&hypernyms.contains(http)){
			URL syns = new URL(synsets);//for use with URL input
			URL hyps = new URL(hypernyms);//for use with URL input
			brS = new BufferedReader(
					new InputStreamReader(syns.openStream()));
			brH = new BufferedReader(
					new InputStreamReader(hyps.openStream()));
		}else{
			brS = new BufferedReader(new FileReader(synsets));//reads in synsets
			brH = new BufferedReader(new FileReader(hypernyms));//reads in hypernyms
		}


		myVertexNouns = new ArrayList<Noun>();
		myHypers = new ArrayList<LinkedList<Integer>>();
		myStringNouns = new ArrayList<String>();


		String synLine;
		String hypLine;

		//reads in the both hypernyms and synsets files in a parallel manner
		while((synLine=brS.readLine())!=null&&(hypLine=brH.readLine())!=null){
			String delim =",";
			//make a String array of each line per file
			String[] synArr = synLine.split(delim);
			String[] hypArr = hypLine.split(delim);
			//assign each part of the line to a temp variable
			String code = synArr[0];
			String noun = synArr[1].toLowerCase();
			String description = synArr[2];
			//construct new Noun from line
			Noun v = new Noun(Integer.parseInt(code),noun,description);
			//add Noun into list of nouns.
			myVertexNouns.add(v);
			//A seperate list of just strings, that are each nouns.
			myStringNouns.add(noun);
			//new list to house ints
			LinkedList<Integer> hypAdd = new LinkedList<Integer>();
			//parse each string into an int in the hypernym array
			for(int j =1; j<hypArr.length; j++){
				//add each of those into into new list
				hypAdd.add(Integer.parseInt(hypArr[j]));
			}
			//add the new list into an ArrayList of LinkedLists of ints
			myHypers.add(hypAdd);

		}
		//close both bufferedReaders
		brS.close();
		brH.close();
	}

	//returns all WordNet nouns
	public Iterable<String> nouns(){
		return myStringNouns;
	}

	//is the word a WordNet noun?
	public static boolean isNoun(String word){
		boolean exists = false;
		//iterate over the list of strings
		for(int i=0; i< myStringNouns.size(); i++){
			//split the string into an array of strings
			String[] newN = myStringNouns.get(i).split("\\s+");
			//iterate over new array
			for(int j=0; j<newN.length;j++){
				//check if that string is equal to the word being searched for
				if(newN[j].equalsIgnoreCase(word)){
					exists =true;
					//break because no need to continue
					break;
				}
			}
		}
		return exists;
	}

	//distance between nounA and nounB(defined below)
	public static int distance(String nounA, String nounB) throws IOException{
		int d = -1;
		//checks to make sure both nouns exist in the WordNet
		//Throws an IllegalArgumentException if either A or B is not present
		if(!isNoun(nounA)){
			throw new IllegalArgumentException("Your first noun is not in the WordNet");
		}else if(!isNoun(nounB)){
			throw new IllegalArgumentException("Your second noun is not in the WordNet");
		}else{
			//obtains index of each noun
			int a = myStringNouns.indexOf(nounA);
			int b = myStringNouns.indexOf(nounB);
			@SuppressWarnings("unused")
			SAP sappy=null;
			try {
				sappy = new SAP(new WordNet(mySynsets, myHypernyms));
			} catch (IOException e) {
				throw new IOException("One of the files does not exist in that directory");
			}
			d = SAP.length(a, b);
			//			System.out.println("A: "+ myNouns.get(a).toString());
			//			System.out.println("B: "+ myNouns.get(b).toString());
		}
		return d;

	}

	//Add hypernyms
	private void addHyps(){
		for(int i =0; i<myHypers.size(); i++){
			Noun current = myVertexNouns.get(i);
			for(int j = 0; j<myHypers.get(i).size(); j++){
				current.addHypernym(myVertexNouns.get(myHypers.get(i).get(j)));
			}
			if(current.hypernymsAmount()==0){
				//Has 0 hypernyms, must be root
				myRootVertex = myVertexNouns.get(i);
			}
		}
	}

	//A sysnset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path(defined below)
	@SuppressWarnings("static-access")
	public static String sap(String nounA, String nounB) throws IOException{
		String ancestor="";//String to build 
		int anc;
		if(!isNoun(nounA)){
			throw new IllegalArgumentException("Your first noun is not in the WordNet");
		}else if(!isNoun(nounB)){
			throw new IllegalArgumentException("Your second noun is not in the WordNet");
		}else{
			int a = myStringNouns.indexOf(nounA);
			int b = myStringNouns.indexOf(nounB);

			SAP sappy = null;
			try {
				sappy = new SAP(new WordNet(mySynsets, myHypernyms));
			} catch (IOException e) {
				throw new IOException("One of those files was not typed correctly");
				
			}
			anc = sappy.ancestor(a, b);
			if(anc<0){
				ancestor+="That's the same word!";
				//								System.out.println("That's the same word!");
			}else{
				ancestor += myStringNouns.get(anc);
			}

		}

		return ancestor;
	}

	public List<Noun> getVertices(){
		return myVertexNouns;
	}

	//returns the size of the WordNet
	public int size(){
		return myVertexNouns.size();
	}


	//MAIN METHOD FOR TESTING AND RUNNING methods
	public static void main(String[] args) throws IOException{
		//for use in reading console input
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		//prompts user for synsets file name
		System.out.println("Please enter synset file name:");
		String s = scanner.nextLine();
		
		//prompts user for hypernyms file name
		System.out.println("Please enter hypernym file name: ");
		String h = scanner.nextLine();
		
		//optional file name for hardcoding
//		String s = "synsets.txt";
//		String h ="hypernyms.txt";
//		String s = "music_synsets.txt";
//		String h = "music_hypernyms.txt";
		
		//needs to be set to null outside try catch
		@SuppressWarnings("unused")
		WordNet theNet = null;
		
		//checks to make sure that those files exists
		try {
			theNet =new WordNet(s, h);
		} catch (IOException e) {

			e.getMessage();
		}
		
		//prompts user for first noun they would like to use
		System.out.println("Please enter first noun:");
		String a = scanner.nextLine().toLowerCase();//turns letters into lower case
		
		//prompts user for second noun they would like to use
		System.out.println("Please enter second noun: ");
		String b = scanner.nextLine().toLowerCase();//turns all letters into lower case
		//check to make sure an exception will not be thrown before printing anything
		String noun = sap(a,b);
		int distance;
		try {
			distance = distance(a,b);
		} catch (IOException e) {
			throw new IOException("one of the ints does not represent a Noun in the WordNet");
		}
		
		//header for the output
		System.out.println("\n% java WordNet " +a +" "+b);
		System.out.println("Ancestor = "+ noun);
		System.out.println("Length = "+ distance);
	}
}
