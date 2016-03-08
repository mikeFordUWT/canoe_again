package canoe;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
	private Node startNode;
	private LinkedList<Node> myNodes;
	
	public Graph(Node firstNode){
		startNode = firstNode;
		myNodes = new LinkedList<Node>();
	}
	
	public void addNode(Node inputNode){
		myNodes.add(inputNode);
	}
	
	public LinkedList<Node> getNodes(){
		return myNodes;
	}
	
	public int size(){
		return myNodes.size();
	}
	
	public Node getFirst(){
		return myNodes.getFirst();
	}
	
	
    public ArrayList<Node> BFS(){
        ArrayList<Node> toReturn = new ArrayList<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        
        Queue<Node> q = new PriorityQueue<Node>();
        
        q.add(myNodes.getFirst());
        while(!q.isEmpty()) {
            Node front = q.poll();
            if(!visited.contains(front)) {
                visited.add(front);
                for(int i = 0; i< front.getEdges().size(); i++){
                    if(!visited.contains(front.getEdges().get(i))){
                        Node current = front.getEdges().get(i).getNextNode();
                        q.add(current);
                    }
                }
            }
        }
        return toReturn;
    }
    
    public void Dijkstra(Node source, int[][] inputMatrix){
        int[] toReturn = new int[myNodes.size()];
        source.setMinDistance(inputMatrix[0][1]);
        toReturn[0] = source.getMinDistance();
        //		toReturn[1] = source.getMinDistance();
        for(int i = 1; i< myNodes.size(); i++){
            toReturn[i] = 99999;
        }
        
        PriorityQueue<Edge> edges = new PriorityQueue<Edge>();
        edges.addAll(source.getEdges());
        
        PriorityQueue<Node> nodes = new PriorityQueue<Node>();
        nodes.add(source);
        while(!nodes.isEmpty()){
            Node current = nodes.poll();
            
            for(Edge e: current.getEdges()){
                Node n = e.getNextNode();
                int weight = e.getWeight();
                int distance = current.getMinDistance() + weight;
                if(distance < n.getMinDistance()){
                    nodes.remove(n);
                    n.setMinDistance(distance);
                    //					toReturn[n.getElement()-1] = n.getMinDistance();
                    nodes.add(n);
                }
                
            }
        }
        //		return toReturn;
    }
	
	public void printGraph(){
		LinkedList<Node> nodes = getNodes();
		LinkedList<Edge> edges;
		Node cur;
		Edge edge;
		for(int i = 0; i < size(); i++){
			cur = nodes.get(i);
			edges = cur.getEdges();
			System.out.println("Node: " + cur.getElement());
			for(int j=0; j < edges.size(); j++){
				edge = edges.get(j);
				edge.printEdge();
			}
		}
	}
	
}

