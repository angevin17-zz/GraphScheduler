import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.jar.JarException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Filename:   CourseSchedulerUtil.java
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
 * Use this class for implementing Course Planner
 *
 * @param <T>
 *            represents type
 */

/**
 * This is the CourseSchedulerUtil class that lets us input a JSON file, parse
 * it, make a graph, and then use that graph to let us answer questions about
 * the data. We use the generic graph we made to act as our graph. We utilize
 * many graph traversal strategies to answer questions regarding the JSON data.
 * 
 * @author Vinnie Angellotti
 *
 * @param <T>
 */
public class CourseSchedulerUtil<T> {

	// can add private but not public members

	// fields
	private GraphImpl<T> graphImpl; // graph object
	private int total;//counts number of courses that need to be taken

	/**
	 * constructor to initialize a graph object
	 */
	public CourseSchedulerUtil() {
		this.graphImpl = new GraphImpl<T>();
	}

	/**
	 * createEntity method is for parsing the input json file
	 *
	 * @return array of Entity object which stores information about a single course
	 *         including its name and its prerequisites
	 * @throws Exception
	 *             like FileNotFound, JsonParseException
	 */
	@SuppressWarnings("rawtypes")
	public Entity[] createEntity(String fileName) throws Exception {
		ArrayList<Entity> list = new ArrayList<Entity>();
		Entity temp = new Entity();
		try {
			Object obj = new JSONParser().parse(new FileReader(fileName));
			// typecasting obj to JSONObject
			JSONObject jo = (JSONObject) obj;
			JSONArray ja = (JSONArray) jo.get("courses");
			Iterator itr = ja.iterator();

			while (itr.hasNext()) {
				JSONObject p = (JSONObject) itr.next();
				temp.setName((String) p.get("name"));
				JSONArray prereqs = (JSONArray) p.get("prerequisites");
				String[] tempArr = new String[prereqs.size()];
				for (int i = 0; i < tempArr.length; i++) {
					tempArr[i] = (String) prereqs.get(i);
				}
				temp.setPrerequisites(tempArr);
				list.add(temp);
				temp = new Entity();
			}
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
		}
		Entity[] entities = new Entity[list.size()];

		for (int i = 0; i < list.size(); i++) {
			entities[i] = list.get(i);
		}
		return entities;

	}

	/**
	 * Construct a directed graph from the created entity object
	 *
	 * @param entities
	 *            which has information about a single course including its name and
	 *            its prerequisites
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void constructGraph(Entity[] entities) {
		String[] temp;

		for (int i = 0; i < entities.length; i++) {
			graphImpl.addVertex((T) entities[i].getName());
			temp = (String[]) entities[i].getPrerequisites();
			for (int j = 0; j < temp.length; j++) {
				if (graphImpl.hasVertex((T) temp[j])) { // check for prereq
				} else {
					graphImpl.addVertex((T) temp[j]); // add prereq
				}
			
				graphImpl.addEdge((T) entities[i].getName(), (T) entities[i].getPrerequisites()[j]); 	
			}
		}
		
	}

	/**
	 * Returns all the unique available courses
	 *
	 * @return the sorted list of all available courses <-- You should ignore this
	 *         line "@return the sorted list of all available courses". You should
	 *         simply return the set of all available courses.
	 */
	public Set<T> getAllCourses() {
		return graphImpl.getAllVertices(); // return set of all vertices
	}

	/**
	 * Calls the order method on graphImpl
	 * 
	 * @return the order (number of vertices) of graphImpl
	 */
	private int getOrder() {
		return graphImpl.order();
	}

	/**
	 * Calls the size method on graphImpl
	 * 
	 * @return the size (number of edges) of graphImpl
	 */
	private int getSize() {
		return graphImpl.size();
	}

	/**
	 * To check whether all given courses can be completed or not
	 *
	 * @return boolean true if all given courses can be completed, otherwise false
	 * @throws Exception
	 */
	public boolean canCoursesBeCompleted() throws Exception {
		Set<T> set = graphImpl.getAllVertices();
		// T[] vertices = (T[]) set.toArray(); // array attempt
		List<T> vertices = new ArrayList<T>(); // arraylist attempt
		vertices.addAll(set);

		for (int r = 0; r < getOrder(); r++) {

			// boolean[] visited = new boolean[getOrder()];
			ArrayList<Boolean> visited = new ArrayList<Boolean>();
			ArrayList<Iterator> itr = new ArrayList<>();
			for (int j = 0; j < getOrder(); j++) {
				List<T> prereqs = graphImpl.getAdjacentVerticesOf(vertices.get(j));
				Iterator<T> i = prereqs.iterator();
				itr.add(j, i);
				visited.add(false);
			}
			Stack<String> stack = new Stack<String>();

			visited.set(r, true);
			stack.push((String) vertices.get(r));
			while (!stack.isEmpty()) {
				String c = stack.peek();
				
				int indC = vertices.indexOf(c);
				if (!itr.get(indC).hasNext()) {
					stack.pop();
				} else {
					T n = (T) itr.get(indC).next();
					if (visited.get(vertices.indexOf(n))) {
						return false;
					}
					visited.set(vertices.indexOf(n), true);
					stack.push((String) n);
				}
			}
		}
		return true;
	}

	/**
	 * The order of courses in which the courses has to be taken
	 *
	 * @return the list of courses in the order it has to be taken
	 * @throws Exception
	 *             when courses can't be completed in any order
	 */
	public List<T> getSubjectOrder() throws Exception {

		
		List<T> finish = new ArrayList<T>();
		List<T> vertices = new ArrayList<>();
		Set<T> set = graphImpl.getAllVertices();
		vertices.addAll(set);
		
	
		Stack<String> stack = new Stack<String>();
		ArrayList<Boolean> visited = new ArrayList<Boolean>();
		
		for(int i = 0; i<getOrder(); i++) {
			visited.add(false);
		}
		for(int k = 0; k< getOrder(); k++) {
			if(!visited.get(k)) {
				topological(k,vertices,visited,stack);
			}
		}
		for(int j = 0; j< getOrder();j++) {
			finish.add((T)stack.pop());
		}
		Collections.reverse(finish);
		return finish;
		
		

	}
	
	private void topological(int vertex, List<T> vert, ArrayList<Boolean> visited, Stack<String>stack) {
		visited.set(vertex, true);
		Iterator itr = graphImpl.getAdjacentVerticesOf(vert.get(vertex)).iterator();
		while(itr.hasNext()) {
			String prereq = (String)itr.next();
			if(!visited.get(vert.indexOf(prereq))){
				topological(vert.indexOf(prereq),vert,visited,stack);
			}
		}
		stack.push((String)vert.get(vertex));
	}


	/**
	 * The minimum course required to be taken for a given course
	 *
	 * @param courseName
	 * @return the number of minimum courses needed for a given course
	 */
	public int getMinimalCourseCompletion(T courseName) throws Exception {
		total = 0;
		completionHelper(courseName);
		return total;
	}

	/**
	 * This method recursively calls itself to see how many courses are needed to be
	 * taken before a specified course can be taken
	 * 
	 * @param courseName
	 */
	private void completionHelper(T courseName) {
		if (graphImpl.getAdjacentVerticesOf(courseName).size() != 0) {
			for (int i = 0; i < graphImpl.getAdjacentVerticesOf(courseName).size(); i++) {
				total++;
				completionHelper(graphImpl.getAdjacentVerticesOf(courseName).get(i));
			}
		}
	}

	public static void main(String[] args) { // TODO: DELETE THIS METHOD
		CourseSchedulerUtil test = new CourseSchedulerUtil();
		try {
			Entity[] entities;
			entities = test.createEntity("valid.json");
			test.constructGraph(entities);
			System.out.println(test.getAllCourses().toString());
			System.out.println(test.getMinimalCourseCompletion("CS200"));
			System.out.println(test.getSize());
			System.out.println(test.getOrder());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}