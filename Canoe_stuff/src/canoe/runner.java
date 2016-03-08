package canoe;

import java.util.ArrayList;

public class runner {
	private static int SIZE = 5;

	public static void main(String[] args) {
		Algorithm alg = new Algorithm(SIZE);
		int[][] randomM = alg.randomMatrixGenerate();
		if(SIZE < 20){
			printMatrix(randomM);
			int[][] path = alg.minCost(randomM);
			printMatrix(path);
//			printFirst(path);
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
		
	}

	
	private static void printFirst(int[][] inputMatrix){
		System.out.print("[");
		for(int i = 0; i <inputMatrix.length; i++){
			System.out.print(inputMatrix[0][i]+", ");
		}
		System.out.println("]");
		
	}
	
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
