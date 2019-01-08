import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Filename:   GraphImpl.java
 * Project:    p4
 * Authors:    Charles Houghtaling (choughtaling@wisc.edu -- Lecture 001) and Vincent Angellotti (angellotti@wisc.edu -- Lecture 001), Debra Deppeler
 *
 * Semester:   Fall 2018
 * Course:     CS400
 *
 * Due Date:   FINAL SUBMISSION DUE before 10:00 PM on Monday, November 19th
 * Version:    1.0
 *
 * Credits:    N/A
 *
 * Bugs:       No known bugs
 * 
 * T is the label of a vertex, and List<T> is a list of adjacent vertices for
 * that vertex.
 *
 * @param <T>
 *            type of a vertex
 */

/**
 * This is the generic graph class that implements the GraphADT and is used to
 * create a graph. For this graph we used a hash map with the key representing
 * the vertices and the value being a list to store all of the vertices that are
 * adjacent to that vertex. Therefore whenever there is a new vertex added, we
 * create a new hash map value and if we are added an edge we just put vertex2
 * into vertex1's adjacency list.
 * 
 * @author Vinnie Angellotti
 *
 * @param <T>
 */
public class GraphImpl<T> implements GraphADT<T> {

	// fields
	private Map<T, List<T>> verticesMap; // store the vertices and the vertice's adjacent vertices
	private int count; // the number of vertices
	private int size; // the number of edges

	/**
	 * Construct and initialize and empty Graph.
	 */
	public GraphImpl() {
		verticesMap = new HashMap<T, List<T>>();
		count = 0;
		size = 0;
	}

	/**
	 * Add new vertex to the graph.
	 *
	 * If vertex is null or already exists, method ends without adding a vertex or
	 * throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 * 
	 * @param vertex
	 *            the vertex to be added
	 */
	public void addVertex(T vertex) {
		List<T> temp = new ArrayList<T>();
		if (vertex == null) {
			return;
		}
		if (verticesMap.containsKey(vertex)) {
			return;
		}
		verticesMap.put(vertex, temp);
		count++;
	}

	/**
	 * Remove a vertex and all associated edges from the graph.
	 * 
	 * If vertex is null or does not exist, method ends without removing a vertex,
	 * edges, or throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 * 
	 * @param vertex
	 *            the vertex to be removed
	 */
	@SuppressWarnings("unchecked")
	public void removeVertex(T vertex) {
		int deletedEdges = 0;
		if (vertex == null) {
			return;
		}
		if (!(verticesMap.containsKey(vertex))) {
			return;
		}

		deletedEdges = verticesMap.get(vertex).size();

		Set<T> set = getAllVertices();
		T[] temp; // array of vertices
		temp = (T[]) set.toArray();
		for (int i = 0; i < temp.length; i++) {
			if (containsEdge(temp[i], vertex)) {
				removeEdge(temp[i], vertex);
			}
		}

		verticesMap.remove(vertex);
		count--;
		size = size - deletedEdges;
	}

	/**
	 * Compares the adjacent vertices of a given vertex to another vertex that is
	 * being searched for.
	 * 
	 * @param vertex1
	 *            current vertex to check against
	 * @param vertex2
	 *            vertex that is being searched for
	 * @return true/false depending on if the vertex is found
	 */
	private boolean containsEdge(T vertex1, T vertex2) {
		List<T> adjacent = getAdjacentVerticesOf(vertex1);
		if (adjacent.contains(vertex2)) {
			return true;
		}
		return false;
	}

	/**
	 * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and
	 * unweighted) If either vertex does not exist, no edge is added and no
	 * exception is thrown. If the edge exists in the graph, no edge is added and no
	 * exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge is not in the graph
	 * 
	 * @param vertex1
	 *            the first vertex (src)
	 * @param vertex2
	 *            the second vertex (dst)
	 */
	public void addEdge(T vertex1, T vertex2) {
		// if the vertices don't exist, you can't add an edge between them
		if (!(verticesMap.containsKey(vertex1)) || !(verticesMap.containsKey(vertex2))) {
			return;
		}
		List<T> temp = new ArrayList<T>();

		temp = verticesMap.get(vertex1);
		temp.add(vertex2);
		verticesMap.remove(vertex1);
		verticesMap.put(vertex1, temp);
		size++;
	}

	/**
	 * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed
	 * and unweighted) If either vertex does not exist, or if an edge from vertex1
	 * to vertex2 does not exist, no edge is removed and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge from vertex1 to vertex2 is in the graph
	 * 
	 * @param vertex1
	 *            the first vertex
	 * @param vertex2
	 *            the second vertex
	 */
	public void removeEdge(T vertex1, T vertex2) {
		if (!(verticesMap.containsKey(vertex1)) || !(verticesMap.containsKey(vertex2))) {
			return;
		}
		List<T> temp = new ArrayList<T>();
		temp = verticesMap.get(vertex1);
		if (!(temp.contains(vertex2))) {
			return;
		}
		temp.remove(vertex2);
		verticesMap.remove(vertex1);
		verticesMap.put(vertex1, temp);
		size--;
	}

	/**
	 * Returns a Set that contains all the vertices.
	 * 
	 * @return a java.util.Set<T> where T represents the vertex type
	 */
	public Set<T> getAllVertices() {
		Set<T> set;
		set = verticesMap.keySet();
		return set;
	}

	/**
	 * Get all the neighbor (adjacent) vertices of a vertex.
	 * 
	 * @param vertex
	 *            the specified vertex
	 * @return an List<T> of all the adjacent vertices for specified vertex
	 */
	public List<T> getAdjacentVerticesOf(T vertex) {
		return verticesMap.get(vertex);
	}

	/**
	 * Searches through the vertices map for a given vertex.
	 * 
	 * @param vertex
	 *            vertex being searched for
	 * @return true/false depending on if the vertices map contains the vertex
	 */
	public boolean hasVertex(T vertex) {
		if (verticesMap.containsKey(vertex)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of vertices in this graph.
	 * 
	 * @return number of vertices in graph.
	 */
	public int order() {
		return count;
	}

	/**
	 * Returns the number of edges in this graph.
	 * 
	 * @return number of edges in the graph.
	 */
	public int size() {
		return size;
	}

	/**
	 * Prints the graph for the reference DO NOT EDIT THIS FUNCTION DO ENSURE THAT
	 * YOUR verticesMap is being used to represent the vertices and edges of this
	 * graph.
	 */
	public void printGraph() {
		for (T vertex : verticesMap.keySet()) {
			if (verticesMap.get(vertex).size() != 0) {
				for (T edges : verticesMap.get(vertex)) {
					System.out.println(vertex + " -> " + edges + " ");
				}
			} else {
				System.out.println(vertex + " -> " + " ");
			}
		}
	}
}