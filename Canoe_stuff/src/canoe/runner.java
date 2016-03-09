package canoe;

/**
 * A runner class, which has a main method for running our algorithms.
 * 
 * @author Mike Ford and Matt Seto
 * 
 * GROUP 3
 * TCSS 343 B
 * UW Tacoma 
 * Winter 2016
 */

import java.util.ArrayList;

public class runner {
	
	//Change this constant to change the size of the matrix
	private static int SIZE = 5;

	/**
	 * Main method to run code.
	 */
	public static void main(String[] args) {
		Algorithm alg = new Algorithm(SIZE);
		int[][] randomM = alg.randomMatrixGenerate();
		if(SIZE < 1801){
			printMatrix(randomM);
			int[][] path = alg.minCost(randomM);
			printMatrix(path);
			System.out.println("Dynamic Min Subset: ");
			ArrayList<Integer> canoes = alg.whichCanoes(path);
			System.out.println(canoes.toString());
		} else {
			int[][] path = alg.minCost(randomM);
			

			ArrayList<Integer> canoes = alg.whichCanoes(path);
			System.out.println("Dynamic Min Subset: ");
			System.out.println(canoes.toString());
		}
		alg.bForceCanoes(randomM);

		System.out.println();
		System.out.println("Recursive cost: ");


		int x = alg.divideAndConquer(randomM);
		
		System.out.println("Cost:" + x);
	
		
	}
	
	
	/**
	 * A helper method that will print out a matrix.
	 * 
	 * @param inputMatrix
	 */
	private static void printMatrix(int[][] inputMatrix){
		int width = inputMatrix.length;
		System.out.println(inputMatrix.length + "x" + inputMatrix.length);
		for(int i =0; i<width; i++){
			System.out.print("[");
			for(int j = 0; j < width; j++){
				System.out.print(String.format("%7d", inputMatrix[i][j]));
				if(j < inputMatrix[i].length - 1) System.out.print(", ");
			}
			System.out.println("]");
		}
		System.out.println();
	}
}
