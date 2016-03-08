package canoe;


public class Edge implements Comparable<Edge>{
    
    private int weight;
    private Node nextNode;
    
    //constructor
    public Edge(int w, Node n){
        weight = w;
        nextNode = n;
    }
    
    public Node getNextNode(){
        return nextNode;
    }
    
    public int getWeight(){
        return weight;
    }
    
    public void printEdge(){
        System.out.println("Edge weight: " + weight + "  \tDestination: " + nextNode.getElement());
    }
    
    @Override
    public int compareTo(Edge o) {
        int toReturn;
        if(weight > o.getWeight()){
            toReturn = 1;
        } else if (weight< o.getWeight()){
            toReturn = -1;
        } else {
            toReturn = 0;
        }
        return toReturn;
    }
    
    @Override
    public String toString(){
    	return String.valueOf(nextNode.getElement());
    }
}
