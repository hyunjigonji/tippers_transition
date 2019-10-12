package tippersTree;

import java.util.*;

import tippersOntology.OntologyManager;

/*
 Tree level
 
 1. UA = <E, P, C>
	UAi = <ei, P, C>
 2. UR = <E, C>
 	URij = <ei, Cj>
 3. UAj = <ei, p, c> <- computed c
 */
public class Main {

	public static void main(String args[]) {
		OntologyManager.startOntologyManager();

		Tree myTree = new Tree();
		UA myUA = new UA("meetingroom", "Turn on AC,Turn on Light", "Occupancy>50%Connectivity");

		myTree = Tree_Flattening.flattening(myUA);
<<<<<<< HEAD
		Tree_Display.displayTree(myTree);
		for(int i = 0 ; i < Tree.URij.size() ; i++) {
			//System.out.println("hello");
=======
		// Tree_Display.displayTree(myTree);
		for (int i = 0; i < Tree.URij.size(); i++) {
			// System.out.println("hello");
>>>>>>> master
			URNode now = Tree.URij.get(i);
			Tree_Generator.URgenerator0(now);
		}
		// Tree_Display.displayTree(myTree);
		for (int i = 0; i < Tree.UAij.size(); i++) {
			UANode now = Tree.UAij.get(i);
			Tree_Generator.UAgenerator0(now);
		}
		// Tree_Display.displayTree(myTree);

		// print leaf nodes
		ArrayList<Node> leaves = Tree.findLeafNode(myTree);
		for (int i = 0; i < leaves.size(); i++) {
			Node now = leaves.get(i);
			SRNode now2 = Tree.findSRNode(myTree, now.nodeNum);
			Tree_Remove.check(now2);
		}
		// Tree_Display.displayTree(myTree);

		// find feasible plan
		Tree feasibleTree = new Tree(myUA);
		feasibleTree = Tree_Calculate.check(myTree);
		Tree_Display.displayTree(feasibleTree);

		// execute sensor data
		ArrayList<Node> leaves2 = Tree.findLeafNode(feasibleTree);
		for (int i = 0; i < leaves2.size(); i++) {
			Node now = leaves2.get(i);
			SRNode now2 = Tree.findSRNode(feasibleTree, now.nodeNum);
			System.out.println(now2.nodeNum);
		} 
		Tree_Execute.executeTree(feasibleTree);
	}
}