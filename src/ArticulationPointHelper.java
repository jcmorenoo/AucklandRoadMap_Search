import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class ArticulationPointHelper {
	
	
	public static Set<Node> findArtPts(Collection<Node> collection){
		//set to be returned
		Set<Node> articulationPoints = new HashSet<Node>();
		//set of unvisited nodes
		Set<Node> unvisited = new HashSet<Node>();
		for(Node n : collection){
			unvisited.add(n);
		}
		
		while(!unvisited.isEmpty()){
			int numSubtrees = 0;
			Node start = unvisited.iterator().next();
			start.setCount(0);
			Set<Node> neighbours = start.getNeighbours();
			if(!neighbours.isEmpty()){
				for(Node neigh : neighbours){
					if(neigh.getCount() == Integer.MAX_VALUE){
						iterArtPts(neigh, 1, start, articulationPoints);
						numSubtrees = numSubtrees + 1;
					}
				}
			}
			
			if(numSubtrees > 1){
				articulationPoints.add(start);
			}
			
			unvisited.remove(start);
		}
		
		
		return articulationPoints;
	}
	
	
	public static void iterArtPts(Node first, int count, Node root, Set<Node> articulationPoints){
		Stack<StackElement> stack = new Stack<StackElement>();
		//first element
		StackElement startElem = new StackElement(first, count, new StackElement(root, 0, null));
		//push into stack<firstNode, count, root>
		stack.push(startElem);
		
		while(!stack.isEmpty()){
			StackElement currentElem = stack.peek();
			Node currentNode = currentElem.node;
		
			//first
			if(currentNode.getCount() == Integer.MAX_VALUE){
				currentNode.setCount(count);
				currentElem.reachBack = count;
				currentElem.children = new ArrayList<Node>();
				
				Set<Node> neighbours = currentNode.getNeighbours();
				for(Node neigh : neighbours){
					//not the same node
					if(neigh.getId() != root.getId()){
						currentElem.children.add(neigh);
					}
					
				}
			}
			//second
			else if(!currentElem.children.isEmpty()){
				if(!currentElem.children.isEmpty()){
					Node currentChild = currentElem.children.remove(0);
					if(currentChild.getCount() < Integer.MAX_VALUE){
						currentElem.reachBack = Math.min(currentElem.reachBack, currentChild.getCount());
					}
					else{
						StackElement childElem = new StackElement(currentChild, count + 1, currentElem);
					}
				}
			}
			//third
			else{
				if(currentNode.getId() != first.getId()){
					if(currentElem.reachBack >= currentElem.root.node.getCount()){
						articulationPoints.add(currentElem.root.node);
					}
					currentElem.root.reachBack = Math.min(currentElem.root.reachBack, currentElem.reachBack);
				}
				
				stack.pop();
				
				
			}
		}
		
	}

	
	public static class StackElement{
		
		
		Node node;
		int count;
		int reachBack;
		StackElement root;
		List<Node> children;
		
		
		public StackElement(Node node, int count, StackElement root){
			this.node = node;
			this.count = count;
			this.children = new ArrayList<Node>();
			
		}
		
	}
}
