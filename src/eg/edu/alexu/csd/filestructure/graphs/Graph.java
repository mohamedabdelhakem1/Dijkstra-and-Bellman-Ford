package eg.edu.alexu.csd.filestructure.graphs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class Graph implements IGraph {
	private int num_v;
	private int num_e;
	/**
	 * 
	 * el key da el ID bta3 el node w el value edges list kul edge feh source el hwa
	 * el node nfsha w fe dist el hwa el node el ry7lha w fe weight bta3 el edge (u,
	 * v, w) ---> (source, dist, weight)
	 **/
	private Map<Integer, List<Edge>> adj_list = new HashMap<Integer, List<Edge>>();
	private List<Edge> edges= new ArrayList<>();
	private List<Integer> order; // de bta3t dijkstra mlhash lazma gher fe function el order

	@Override
	public void readGraph(File file) {
		Scanner reader = null;
		try {
			reader = new Scanner(file);
			String line = reader.nextLine();
			String[] splited = line.split(" ");
			num_v = Integer.parseInt(splited[0]);
			num_e = Integer.parseInt(splited[1]);
			for (int i = 0; i < num_v; i++) {
				adj_list.put(i, new ArrayList<Edge>());
			}
			for (int i = 0; i < num_e; i++) {
				line = reader.nextLine();
				splited = line.split(" ");
				Edge e = new Edge();
				e.setSource(Integer.parseInt(splited[0]));
				e.setDist(Integer.parseInt(splited[1]));
				e.setWeight(Integer.parseInt(splited[2]));
				if (e.getSource() >= num_v || e.getDist() >= num_v) {
					reader.close();
					throw new RuntimeErrorException(new Error());
				}
				edges.add(e);
				adj_list.get(e.getSource()).add(e);
			}
			reader.close();

		} catch (Exception e) {
			if (reader != null)
				reader.close();
			throw new RuntimeErrorException(new Error());
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return num_e;
	}

	@Override
	public ArrayList<Integer> getVertices() {
		// TODO Auto-generated method stub
		return new ArrayList<Integer>(adj_list.keySet());
	}

	@Override
	public ArrayList<Integer> getNeighbors(int v) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		for (Edge e : adj_list.get(v)) {
			neighbors.add(e.getDist());
		}
		return neighbors;
	}

	@Override
	public void runDijkstra(int src, int[] distances) {
		if (src >= num_v)
			throw new RuntimeErrorException(new Error());
		int usedInf = Integer.MAX_VALUE / 2;
		order = new ArrayList<Integer>();
		for (int i = 0; i < distances.length; i++) {
			distances[i] = usedInf;
		}
		distances[src] = 0;
		Set<Integer> unvisited = new HashSet<Integer>(adj_list.keySet());
		while (!unvisited.isEmpty()) {
			int u = nextMin(distances, unvisited);
			unvisited.remove(u);
			order.add(u);
			for (Edge e : adj_list.get(u)) {
				if (!unvisited.contains(e.getDist()))
					continue;
				if (distances[u] == usedInf)
					continue;
				if (distances[u] + e.getWeight() < distances[e.getDist()]) {
					distances[e.getDist()] = distances[u] + e.getWeight();
				}
			}
		}
	}

	private int nextMin(int[] dist, Set<Integer> unvisited) {
		int index = -1;
		for (int i = 0; i < dist.length; i++) {
			if (!unvisited.contains(i))
				continue;
			if (index == -1)
				index = i;
			if (dist[i] < dist[index]) {
				index = i;
			}
		}
		return index;
	}

	@Override
	public ArrayList<Integer> getDijkstraProcessedOrder() {
		// TODO Auto-generated method stub
		return (ArrayList<Integer>) order;
	}

	@Override
	public boolean runBellmanFord(int src, int[] distances) {
		int inf = Integer.MAX_VALUE / 2;
		for (int i = 0; i < distances.length; i++) {
			distances[i] = inf;
		}
		distances[src] = 0;
		for (int i = 0; i < num_v - 1; i++) {
			for(Edge e : edges) {
				if (distances[e.source] == inf) continue;
				if(distances[e.source]+ e.weight < distances[e.dist]) {
					distances[e.dist] = distances[e.source]+ e.weight;
				}
			}
		}
		for(Edge e : edges) {
			if (distances[e.source] == inf) continue;
			if(distances[e.source]+ e.weight < distances[e.dist]) {
				return false;
			}
		}
			return true;
	}

	private class Edge {
		private int source;
		private int dist;
		private int weight;
		public int getSource() {
			return source;
		}

		public void setSource(int source) {
			this.source = source;
		}

		public int getDist() {
			return dist;
		}

		public void setDist(int dist) {
			this.dist = dist;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

	}

}
