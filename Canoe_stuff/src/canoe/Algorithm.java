package canoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {
	private int mySize;
	private int max;
	private Graph g;
	private static int MIN = 1;
	private static int INF = 99999;

	public Algorithm(int size) {
		mySize = size;
		if (mySize <= 400) {
			max = 2500;
		} else if (mySize > 400 && mySize <= 600) {
			max = 5000;
		} else {
			max = 10000;
		}
	}

	public int[][] randomMatrixGenerate() {
		int[][] A = new int[mySize][mySize];
		for (int i = 0; i < mySize; i++) {
			for (int j = 0; j < mySize; j++) {
				if (i == j) {
					A[i][j] = 0;
				} else if (i > j) {
					A[i][j] = INF;
				} else {
					int upper;
					int lower;
					double dj;
					double upperDouble;
					double lowerDouble;
					if (i == 0) {
						if (j == 1) {
							lower = 1;
							dj = j;
							upperDouble = (dj / (mySize - 1) * max);
							upper = (int) upperDouble;
							A[i][j] = ThreadLocalRandom.current().nextInt(lower, upper);
						} else {
							lower = A[i][j - 1] + 1;
							dj = j;
							upperDouble = (dj / (mySize - 1) * max);
							upper = (int) upperDouble;
							A[i][j] = ThreadLocalRandom.current().nextInt(lower, upper);
						}
					} else { // i>0
						if (A[i][j - 1] == 0) {
							lower = 1;
							upper = A[i - 1][j];

							A[i][j] = ThreadLocalRandom.current().nextInt(lower, upper);
						} else {
							lower = A[i][j - 1] + 1;
							upper = A[i - 1][j];
							A[i][j] = ThreadLocalRandom.current().nextInt(lower, upper);
						}
					}
				}
			}
		}

		return A;
	}

	/**
	 * finds the min cost of taking canoes from start to finish using the costs
	 * supplied by the input matrix
	 * 
	 * @param inputMatrix
	 *            [][]
	 * @return int[][] that shows the min possible cost to reach each cell in
	 *         the matrix
	 */
	public int[][] minCost(int[][] inputMatrix) {
		int[][] toReturn = new int[inputMatrix.length][inputMatrix.length];
		for (int j = 0; j < inputMatrix.length; j++) {
			toReturn[0][j] = inputMatrix[0][j];
			toReturn[j][0] = inputMatrix[0][0];
		}
		int i;
		for (int j = 1; j < inputMatrix.length; j++) {
			for (i = 1; i < inputMatrix.length; i++) {
				//if the spot is zero, then just pull the value from directly above it
				if (inputMatrix[i][j] == 0) {
					toReturn[i][j] = toReturn[i - 1][j];
				} else {
					//Assume as you build the matrix, you only have access to those two stations
					//ex: row 1, only get to use boat 1
					//	  row 2, get to use boat 1 and 2
					//	  row 3, get to use boat 1, 2 or 3 etc....
					//therefore, take the min of the cell above it, OR the sum of cost current from the guaranteed best score to the cur station
					toReturn[i][j] = Math.min(toReturn[i-1][j], (inputMatrix[i][j] + toReturn[i][i]));
				}
			}
		}
		return toReturn;
	}

	/**
	 * Using the mincost matrix, determines which stations were stopped at when
	 * finding the least cost route from start to finish
	 * 
	 * @param inputMatrix
	 *            [][]
	 * @return an arrayList of int values representing stations that are
	 *         required stops
	 */
	public ArrayList<Integer> whichCanoes(int[][] inputMatrix) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>();
		int j = inputMatrix.length - 1;
		toReturn.add(1);// must take canoe from station 1
		toReturn.add(j + 1);
		for(int i = inputMatrix.length -1; i > 0; i--){
			if(inputMatrix[i][j] != inputMatrix[i - 1][j]){
				toReturn.add(i+1);
				j = i;
				continue;
			}
			if (j == 0) {// TODO add functionality for hitting top of matrix
				toReturn.add(j);
			}
		}
		if (toReturn.size() == 0) {
			toReturn.add(inputMatrix.length);
		}
		Collections.sort(toReturn);
		System.out.println("Cost: " + inputMatrix[inputMatrix.length-1][inputMatrix.length-1]);
		return toReturn;
	}

	/**
	 * Creates a station Graph for the given input matrix
	 * 
	 * @param inputMatrix
	 * @return a graph
	 */
	public Graph createStationGraph(int[][] inputMatrix) {
		// set up a graph with nodes as stations and weighted cost edges to
		// other stations
		Node station = new Node(1);
		Graph graph = new Graph(station);

		for (int i = 1; i < inputMatrix.length + 1; i++) {
			graph.addNode(new Node(i));
		}

		// add the nodes and edges
		for (int i = 0; i < inputMatrix.length; i++) {
			// get the station we're connecting
			station = graph.getNodes().get(i);
			for (int j = 0; j < inputMatrix.length; j++) {
				if ((inputMatrix[i][j] != INF) && (inputMatrix[i][j] != 0)) {
					// add the edges to that station
					station.addEdge(new Edge(inputMatrix[i][j], graph.getNodes().get(j)));
				}
			}
		}
		//graph.printGraph();
		return graph;
	}

	public void bForceCanoes(int[][] inputMatrix) {

		Graph graph = createStationGraph(inputMatrix);
		// generate a matrix of the powerset
		int stations = inputMatrix.length;
		int[] A = new int[stations-2];
		for (int i = 1; i < stations-1; i++) {
			A[i-1] = i+1;
		}
		System.out.println();
		int[][] subsets = getSubsets(A);
		int[] curSubset = new int[A.length+2];
		int curCost = 0;
		int[] minSubset = new int[A.length+2];
		int minCost = INF;
		
		for(int i = 0; i < subsets.length; i++){
			for(int j = 0; j < curSubset.length; j++){
				if (j == 0){
					curSubset[j] = 1;
					continue;
				}
				else if (j == curSubset.length-1){
					curSubset[j] = curSubset.length;
					continue;
				}
				else {
					curSubset[j] = subsets[i][j-1];
				}
			}
			
			//check the cost of that subset, make it the minsubset if neccessary
			curCost = 0;
			int p;
			for(int r = 0; r < curSubset.length; r++){
				p = r - 1;
				if (r==0){
					continue;
				}
				if (curSubset[p] == 0){
					while(curSubset[p] == 0){
						p--;
					}
				}
				if (curSubset[r] != 0){
					curCost += inputMatrix[p][r];
					//System.out.println(inputMatrix[p][r]);
				}
			}
			
			if (curCost <= minCost){
				minCost = curCost;
				for(int u = 0; u < minSubset.length; u++){
					minSubset[u] = curSubset[u];
				}
			}
			
//			System.out.print("CUR SUBSET: [ "  );
//			for(int q = 0; q<curSubset.length; q++){
//				System.out.print(curSubset[q]+ " ");
//			}
//			System.out.println("]");
//			System.out.println("COST "+ curCost);
//			System.out.println();	
		}
		System.out.println("Brute Force Min Subset: ");
		System.out.print("MIN SUBSET: [ "  );
		for(int q = 0; q<minSubset.length; q++){
			System.out.print(minSubset[q]+ " ");
		}
		System.out.println("]");
		System.out.println("COST "+ minCost);
		System.out.println();	
	}

	/**
	 * A function to return the powerset of a given array
	 */
	private int[][] getSubsets(int[] A){
		int[][] toReturn = new int[(int) Math.pow(2, A.length)][A.length];
		
		for(int i = 0; i < Math.pow(2,  A.length); i++){
			int j = 0;
			StringBuilder binary = new StringBuilder(Integer.toBinaryString(i));
	        for(int k = binary.length(); k < A.length; k++) {
	            binary.insert( 0, '0' );
	        }
	        for(int r = 0; r<binary.length(); r++){
	        	if(binary.charAt(r) == '1'){
	        		toReturn[i][j] = A[r];
	        	}
		        j++;
	        }
		}
		//printRectangularMatrix(toReturn, A.length);
		return toReturn;
	}
	
	private static void printRectangularMatrix(int[][] inputMatrix, int size){
		int rows = inputMatrix.length;
		int cols = size;
		
		for(int i = 0; i < rows; i ++){
			System.out.print("[");
			for(int j = 0; j < cols; j++){
				System.out.print(String.format("%4d", inputMatrix[i][j]));
				if(j < inputMatrix[i].length - 1) System.out.print(", ");
			}
			System.out.println("]");
		}
		System.out.println();
	}
	


	
}
