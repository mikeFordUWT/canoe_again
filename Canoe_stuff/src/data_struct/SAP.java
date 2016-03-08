package data_struct;
/*
 *Author: Michael Ford
 *TCSS 342
 *Autumn 2015
 *
 * A class that can find the common ancestor of two vertices, with the shortest path.
 * Has a Main method at bottom that prompts user for input
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;



//SAP stands for Shortest Ancestral Path
public class SAP {
	private static WordNet myNet;
	//Constructor takes a digraph
	public SAP(WordNet theNet){
		myNet=theNet;
	}

	//helper method to see if a a node is in the dfs path of another node
	private static int inPath(ArrayList<Noun> list, Noun v){
		int i = 0;
		while(list.get(i)!=v){
			i++;
		}
		return i;

	}

	//length of ancestral path between v and w
	//returns -1 if no path;
	public static int length(Integer v, Integer w){

		int l =-1;
		if(v>myNet.getVertices().size()-1){
			throw new IndexOutOfBoundsException("Your first vertex is too large");
		}else if(v<0||w<0){
			throw new IndexOutOfBoundsException("A vertex's ID must be >=0");
		}else if(w>myNet.getVertices().size()){
			throw new IndexOutOfBoundsException("Your second vertex is too large");
		}else if(v==null||w==null){
			throw new NullPointerException("One of those vertex IDs was empty!");
		}else{
			if(v==w){
				l=0;
			}else if(v>=0 && w>=0){
				l =0;
				ArrayList<Noun> vH = depthFirst(myNet, v);
				ArrayList<Noun> wH = depthFirst(myNet, w);		
				int a = ancestor(v,w);
				Noun vA= myNet.getVertices().get(a);
				Noun vV = myNet.getVertices().get(v);
				Noun vW = myNet.getVertices().get(w);

				if(wH.contains(vV)){
					int i =0;
					if(wH.size()>1&&wH.get(1).equals(vV)){
						i=1;
					}else if(wH.get(0).equals(vV)){
						i=1;
					}else{
						i=1;
						for(int j = 0; j< vV.getHypernyms().size();j++){
							if(wH.contains(vV.getHypernyms().get(j))){
								i++;
							}else{
								i =inPath(wH,vV);
							}
						}

					}
					l+=i;

				}else if(vH.contains(vW)){
					int i=0;
					if(vH.get(1).equals(vW)){
						i=1;
					}else{
						i=1;
						for(int j = 0; j<vW.getHypernyms().size();j++){
							if(vH.contains(vW.getHypernyms().get(j))){
								i++;
							}else{
								i = inPath(vH,vW);
							}

						}
					}


					l+=i;
				}else if((wH.size()>0 && vH.size()>0) && wH.get(0).equals(vH.get(0))){
					l=2;
				}
				else if(vW.getHypernyms().contains(vA)) {
					for(Noun n: vV.getHypernyms()){
						if(n.equals(vA)){
							l = 2;
						}
					}


				}else if(vV.getHypernyms().contains(vA)){
					for(Noun n: vW.getHypernyms()){
						if(n.equals(vA)){
							l=2;
						}
					}
				}else{
					l=0;
					//find the common ancestor of the vertices

					List<Noun> vL = new ArrayList<Noun>();
					List<Noun> wL = new ArrayList<Noun>();
					vL = vH.subList(0, vH.indexOf(vA));
					wL = wH.subList(0, wH.indexOf(vA));



					l= vL.size()+wL.size();


				}
			}
		}




		return l;
	}

	//A common ancestor of v and w that participates
	//in a shortest ancestral path; -1 if no such path
	public static int ancestor(Integer v, Integer w){
		if(v>myNet.getVertices().size()-1){
			throw new IndexOutOfBoundsException("Your first vertex is too large");
		}else if(v<0||w<0){
			throw new IndexOutOfBoundsException("A vertex's ID must be >=0");
		}else if(w>myNet.getVertices().size()){
			throw new IndexOutOfBoundsException("Your second vertex is too large");
		}else if(v==null||w==null){
			throw new NullPointerException("One of those vertex IDs was empty!");
		}
		int ancestor=-1;
		ArrayList<Noun> vH = depthFirst(myNet, v);
		ArrayList<Noun> wH = depthFirst(myNet, w);
		Noun vV = vH.get(0);
		Noun wV = wH.get(0);
		if(v==w&&vH.size()>1){
			ancestor = vH.get(1).getID();
		}
		else{
			if(vH.size()==1){
				ancestor = vH.get(0).getID();
			}else if(wH.size()==1){
				ancestor = wH.get(0).getID();
			}else if(wH.size()>0 && vH.size()>0 && wH.get(0).equals(vH.get(0))){
				ancestor = wH.get(0).getID();
			}else if(wH.contains(vV)||wH.contains(vH.get(0))){
				ancestor=vH.get(1).getID();
			}else if(vH.contains(wV)||vH.contains(wH.get(0))){
				ancestor=wH.get(1).getID();
			}else{
				List<Noun> nA = new ArrayList<Noun>();

				for(Noun v1: wH){
					if(vH.contains(v1)){
						ancestor = myNet.getVertices().indexOf(v1);
						return ancestor;
					}
				}

				List<Noun> nB = new ArrayList<Noun>();

				for(Noun v1: wH){
					if(vH.contains(v1)){
						List<Noun> temp = wH.subList(0, wH.indexOf(v1)+1);

						nB=temp;
						break;


					}


				}
				Noun a1K = nA.get(nA.size()-1);
				Noun b1K = nB.get(nB.size()-1);

				if(a1K.equals(b1K)){
					ancestor = a1K.getID();
				}else{
					ancestor=-1;
				}
			}
		}
		return ancestor;
	}

	//A depth first search that returns an array list of the path to the root
	private static ArrayList<Noun> depthFirst(WordNet theNet, int theInt){
		ArrayList<Noun> aH = new ArrayList<Noun>();
		ArrayList<Noun> visited= new ArrayList<Noun>();
		Noun a = theNet.getVertices().get(theInt);
		Stack<Noun> hypStack = new Stack<Noun>();
		hypStack.push(a);
		while(!hypStack.isEmpty()){
			Noun temp = hypStack.pop();

			if(!visited.contains(temp)){
				aH.add(temp);
				visited.add(temp);
				for(Noun v1: temp.getHypernyms()){
					hypStack.push(v1);
				}
			}
		}
		return aH;
	}

	
	//MAIN METHOD for testing and running methods
	public static void main(String[] args) throws IOException, FileNotFoundException{

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a synsets .txt file name: ");
		String syn = scanner.nextLine();
		System.out.println("Enter a hypernyms .txt file name: ");
		String hyp = scanner.nextLine();

		//Optional string values for harcoding files
		//		String syn = "synsets.txt";
		//		String hyp ="hypernyms.txt";
		//		String syn = "music_synsets.txt";
		//		String hyp = "music_hypernyms.txt";
		WordNet theNet = null;
		try {
			theNet =new WordNet(syn, hyp);
		} catch (IOException e ) {
			throw new IOException("One of those wasn't an accurate file name");
		}
		
		//prompt user to input first id number to check
		System.out.println("Enter the first synset ID: ");
		int vI = scanner.nextInt();
		scanner.nextLine();
		try{
			theNet.getVertices().get(vI);
		}catch(IndexOutOfBoundsException e){
			e.getMessage();
		}

		//prompts user to enter second id number to check
		System.out.println("Enter the second synset ID: ");
		int vII = scanner.nextInt();
		scanner.nextLine();
		try{
			theNet.getVertices().get(vII);
		}catch(IndexOutOfBoundsException e){
			e.getMessage();
		}
		
		//Needed to access the methdods in class
		@SuppressWarnings("unused")
		SAP theSAP = new SAP(theNet);

		long start = System.currentTimeMillis();

		System.out.println("\n% more digraph1.txt");
		//only print the first 2000 hypernyms if size is over 2000
		if(theNet.getVertices().size()>2000){
			for(int i=0;i<2000;i++){
				for(int j=0; j<theNet.getVertices().get(i).getHypernyms().size(); j++){
					System.out.println(i+ "  "+ theNet.getVertices().get(i).getHypernyms().get(j).getID());
				}
			}
		}else{
			for(Noun v: theNet.getVertices()){
				for(int i =0;i< v.getHypernyms().size(); i++){
					System.out.println(v.getID()+ "  "+v.getHypernyms().get(i).getID());
				}
			}
		}
		
		int sap = length(vI,vII);
		int ancestor = ancestor(vI,vII);
		System.out.println("\n% java SAP digraph.txt");
		System.out.println(vI + "  "+vII);
		System.out.println("sap = "+sap+", ancestor = "+ancestor);

		long end = System.currentTimeMillis();
		System.out.println(end-start+" ms");
	}
}

