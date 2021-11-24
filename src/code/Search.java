package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

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
    	HashSet<Node<MatrixState, MatrixOperator>> expandedNodes = new HashSet<Node<MatrixState,MatrixOperator>>();
        Queue<Node<MatrixState, MatrixOperator>> Q = new LinkedList<>();
        Q.add(root);
        expandedNodes.add(root);
        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Basant | Enqueue nodes
            ArrayList<MatrixOperator> possibleActions = problem.actions(head.getState());
            for(MatrixOperator a: possibleActions)
            {
            	MatrixState possibleState = problem.result(head.getState(), a);
            	if(!expandedNodes.contains(possibleState))
            	{
            		int[] cost= new int[2];
            		cost[0]=head.getPathCost()[0] + problem.stepCost(head.getState(), a, possibleState)[0];
            		cost[1]=head.getPathCost()[1] + problem.stepCost(head.getState(), a, possibleState)[1];
            		Node newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, 0, head.getDepth()+1);
            		Q.add(newNode);
            		expandedNodes.add(newNode);
            	}
            }
        }

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    DFS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException {
    	HashSet<Node<MatrixState, MatrixOperator>> expandedNodes = new HashSet<Node<MatrixState,MatrixOperator>>();
        Stack<Node<MatrixState, MatrixOperator>> S = new Stack<>();
        S.add(root);
        expandedNodes.add(root);
        while (!S.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = S.pop();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Basant | Enqueue nodes
            ArrayList<MatrixOperator> possibleActions = problem.actions(head.getState());
            for(MatrixOperator a: possibleActions)
            {
            	MatrixState possibleState = problem.result(head.getState(), a);
            	if(!expandedNodes.contains(possibleState))
            	{
            		int[] cost= new int[2];
            		cost[0]=head.getPathCost()[0] + problem.stepCost(head.getState(), a, possibleState)[0];
            		cost[1]=head.getPathCost()[1] + problem.stepCost(head.getState(), a, possibleState)[1];
            		Node newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, 0, head.getDepth()+1);
            		S.push(newNode);
            		expandedNodes.add(newNode);
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
    UCS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException {
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>(Collections.reverseOrder());
        HashSet<MatrixState> visitedStates = new HashSet<>();

        visitedStates.add(root.getState());
        Q.add(root);

        int expandedNodes = 0;

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // Not a goal, expand node
            expandedNodes++;

            // All possible actions from current state
            ArrayList<MatrixOperator> actions = problem.actions(head.getState());

            //current action
            for (MatrixOperator action : actions) {
                MatrixState result = problem.result(head.getState(), action); //state resulting from action
                int[] stepCost = problem.stepCost(head.getState(), action, result);
                int[] pathCost = head.getPathCost();
                pathCost[0] += stepCost[0];
                pathCost[1] += stepCost[1];

                // if state is not repeated
                if (!visitedStates.contains(result)) {
                    Node<MatrixState, MatrixOperator> child = new Node<>(result, head, action, pathCost,
                            0, head.getDepth() + 1);
                    Q.add(child); //added to queue
                    visitedStates.add(result); // state marked as visited to avoid adding it to queue again
                }
            }
        }

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    GR(Matrix problem, Node<MatrixState, MatrixOperator> root, int heuristicNum) {
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>(Collections.reverseOrder());
        Q.add(root);

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Moataz | Enqueue nodes
        }

        // null == failure
        return null;
    }

    public static Node<MatrixState, MatrixOperator>
    AS(Matrix problem, Node<MatrixState, MatrixOperator> root, int heuristicNum) throws IOException, ClassNotFoundException {
    	HashSet<Node<MatrixState, MatrixOperator>> expandedNodes = new HashSet<Node<MatrixState,MatrixOperator>>();
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>(Collections.reverseOrder());
        Q.add(root);
        expandedNodes.add(root);

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return head;

            // TODO: Ali | Enqueue nodes
            ArrayList<MatrixOperator> possibleActions = problem.actions(head.getState());
            for(MatrixOperator a: possibleActions)
            {
            	MatrixState possibleState = problem.result(head.getState(), a);
            	if(!expandedNodes.contains(possibleState))
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
            		Node newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, heuristic, head.getDepth()+1);
            		Q.add(newNode);
            		expandedNodes.add(newNode);
            	}
            }
        }

        // null == failure
        return null;
    }


    public static void main(String[] args) {

    }
}
