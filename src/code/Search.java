package code;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class Search {


    public static Node searchProcedure(SearchProblem problem, String strategy) throws IOException, ClassNotFoundException {

        if (problem instanceof Matrix) {
            Matrix mProblem = (Matrix) problem;

            //root node
            Node<MatrixState, MatrixOperator> root = new Node<>();
            root.setState(mProblem.initialState);

            //TODO: Please implement your search in separate functions to leave this portion tidy,
            // be careful when assigning path costs and node heuristics in your method. Assign the values
            // that best suit your strategy (i.e. BFS: heuristic = pathCost = -1)
            switch (strategy) {
                case "BF":
                    return BFS(mProblem, root);
                case "DF":
                    return DFS(mProblem, root);
                case "ID":
                    return IDS(mProblem, root);
                case "UC":
                    return UCS(mProblem, root);
                case "GR1":
                    return GR(mProblem, root, 1);
                case "GR2":
                    return GR(mProblem, root, 2);
                case "AS1":
                    return AS(mProblem, root, 1);
                case "AS2":
                    return AS(mProblem, root, 2);
                default:
                    break;
            }

        }

        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    BFS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException{
    	HashSet<MatrixState> expandedStates = new HashSet<MatrixState>();
        Queue<Node<MatrixState, MatrixOperator>> Q = new LinkedList<>();
        Q.add(root);
        expandedStates.add(root.getState());
        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Basant | Enqueue nodes
            ArrayList<MatrixOperator> possibleActions = problem.actions(head.getState());
            for(MatrixOperator a: possibleActions)
            {
            	MatrixState possibleState = problem.result(head.getState(), a);
            	if(!expandedStates.contains(possibleState))
            	{
            		int[] cost= new int[2];
            		cost[0]=head.getPathCost()[0] + problem.stepCost(head.getState(), a, possibleState)[0];
            		cost[1]=head.getPathCost()[1] + problem.stepCost(head.getState(), a, possibleState)[1];
            		Node<MatrixState, MatrixOperator> newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, 0, head.getDepth()+1);
            		Q.add(newNode);
            		expandedStates.add(possibleState);
            	}
            }
        }

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    DFS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException {
    	HashSet<MatrixState> expandedStates = new HashSet<MatrixState>();
        Stack<Node<MatrixState, MatrixOperator>> S = new Stack<>();
        S.add(root);
        expandedStates.add(root.getState());
        while (!S.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = S.pop();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Basant | Enqueue nodes
            ArrayList<MatrixOperator> possibleActions = problem.actions(head.getState());
            for(MatrixOperator a: possibleActions)
            {
            	MatrixState possibleState = problem.result(head.getState(), a);
            	if(!expandedStates.contains(possibleState))
            	{
            		int[] cost= new int[2];
            		cost[0]=head.getPathCost()[0] + problem.stepCost(head.getState(), a, possibleState)[0];
            		cost[1]=head.getPathCost()[1] + problem.stepCost(head.getState(), a, possibleState)[1];
            		Node<MatrixState, MatrixOperator> newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, 0, head.getDepth()+1);
            		S.push(newNode);
            		expandedStates.add(possibleState);
            	}
            }
        }

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    IDS(Matrix problem, Node<MatrixState, MatrixOperator> root) {
        //TODO: Ahmed

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    UCS(Matrix problem, Node<MatrixState, MatrixOperator> root) {
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>(Collections.reverseOrder());
        Q.add(root);

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Ahmed | Enqueue nodes
        }

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    GR(Matrix problem, Node<MatrixState, MatrixOperator> root, int heuristicNum) throws ClassNotFoundException, IOException {
//    	HashSet<Node<MatrixState,MatrixOperator>> expanded_nodes=new HashSet<Node<MatrixState,MatrixOperator>>();
    	HashSet<MatrixState> expanded_nodes=new HashSet<MatrixState>();
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>(Collections.reverseOrder());
        root.setgreedy(true);
        Q.add(root);

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;
            ArrayList <MatrixOperator> possible_actions = problem.actions(head.getState());
            for (MatrixOperator x : possible_actions)
            {
            	MatrixState new_state=problem.result(head.getState(), x);
            	if(!expanded_nodes.contains(new_state))
            	{
                	int[]cost = new int[2];
                	cost[0] = problem.stepCost(head.getState(), x, new_state)[0]+head.getPathCost()[0];
                	cost[1] = problem.stepCost(head.getState(), x, new_state)[1]+head.getPathCost()[1];
                	float hur_value=0;
                	
                	if(heuristicNum == 1)
                	{
                		hur_value = problem.GreedHeuristic1(new_state);
                	}
                	else
                	{
                		hur_value = problem.GreedHeuristic2(new_state);
                	}
                	
                	Node<MatrixState,MatrixOperator> result_node = new Node<MatrixState,MatrixOperator>(new_state,head,x,cost,hur_value,head.getDepth()+1);
                	result_node.setgreedy(true);
            		Q.add(result_node);
            		expanded_nodes.add(new_state);
            	}
//            	MatrixState new_state=problem.result(head.getState(), x);
//            	int[]cost = new int[2];
//            	cost[0] = problem.stepCost(head.getState(), x, new_state)[0]+head.getPathCost()[0];
//            	cost[1] = problem.stepCost(head.getState(), x, new_state)[1]+head.getPathCost()[1];
//            	float hur_value=0;
//            	
//            	if(heuristicNum == 1)
//            	{
//            		hur_value = problem.GreedHeuristic1(new_state);
//            	}
//            	else
//            	{
//            		hur_value = problem.GreedHeuristic2(new_state);
//            	}
//            	
//            	Node<MatrixState,MatrixOperator> result_node = new Node<MatrixState,MatrixOperator>(new_state,head,x,cost,hur_value,head.getDepth()+1);
//            	result_node.setgreedy(true);
//            	if(!expanded_nodes.contains(result_node))
//            	{
//            		Q.add(result_node);
//            		expanded_nodes.add(head);
//            	}
//            	
            }
        }

        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    AS(Matrix problem, Node<MatrixState, MatrixOperator> root, int heuristicNum) throws IOException, ClassNotFoundException {
    	HashSet<MatrixState> expandedStates = new HashSet<MatrixState>();
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>(Collections.reverseOrder());
        Q.add(root);
        expandedStates.add(root.getState());

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Ali | Enqueue nodes
            ArrayList<MatrixOperator> possibleActions = problem.actions(head.getState());
            for(MatrixOperator a: possibleActions)
            {
            	MatrixState possibleState = problem.result(head.getState(), a);
            	if(!expandedStates.contains(possibleState))
            	{
            		int[] cost= new int[2];
            		cost[0]=head.getPathCost()[0] + problem.stepCost(head.getState(), a, possibleState)[0];
            		cost[1]=head.getPathCost()[1] + problem.stepCost(head.getState(), a, possibleState)[1];
            		int heuristic = 0;
            		if(heuristicNum==1)
            		{
            			heuristic=problem.ASHeuristic1(possibleState);
            		}
            		else if(heuristicNum==2)
            		{
            			heuristic=problem.ASHeuristic2(possibleState);
            		}
            		Node<MatrixState, MatrixOperator> newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, heuristic, head.getDepth()+1);
            		Q.add(newNode);
            		expandedStates.add(possibleState);
            	}
            }
        }

        // null == failure
        return null;
    }


    public static void main(String[] args) {
    	System.out.print("lol");
    }
}
