package tippersTree;

import java.util.*;
import tippersOntology.OntologyManager;

public class Tree_Execute extends Tree {
	public static Tree myTree = new Tree();
	public static Statement request = new Statement();
	public static String action = "PREPARE";
	public static HashMap<Integer, Statement> checkDup = new HashMap<Integer, Statement>();
	public static HashMap<String, String> response = new HashMap<String, String>();

	public static void executeTree(Tree feasibleTree, Node nowNode) {
		myTree = feasibleTree;

		ArrayList<Node> leaves = Tree.findLeafNode(nowNode);
		for (int i = 0; i < leaves.size(); i++) {
			SRNode now = Tree.findSRNode(feasibleTree, leaves.get(i).nodeNum);
			if (now.Children.isEmpty()) {
				action = "PREPARE";
				Statement state1 = new Statement(now.values.Sensor, now.values.Observation, now.values.Entity,
						OntologyManager.getAddr(now.values.Sensor));

				System.out.println(action + now.nodeNum + " <" + state1.Entity + ", " + state1.Observation + ", "
						+ state1.Sensor + "(" + OntologyManager.getAddr(state1.Sensor) + ")>");
				System.out.println();

				checkDup.put(now.nodeNum, state1);

				createStatement(now, state1);
			}

		}
		if (request != null) {
			System.out.println(action + " <" + request.Entity + ", " + request.Observation + ", " + request.Sensor + "("
					+ OntologyManager.getAddr(request.Sensor) + ")>"); 	// request
																		// to
																		// wrapper
			System.out.println();

			// System.out.println("response:	"+Call.callVS(request));
			response.put(request.Observation, Call.callVS(request));
			System.out.println("hashMap "+response);
		}
		return;
	}

	public static void createStatement(SRNode nowSR, Statement state1) {
		Statement state2 = new Statement();

		Node parent = nowSR.Parents.get(0).Parents.get(0);

		if (parent.type == types.typeUR || parent.type == types.typeUA) {
			action = "CALL";
			request = state1;
		} else {
			SRNode nowSR2 = findSRNode(myTree, parent.nodeNum);
			if (checkDup.containsKey(nowSR2.nodeNum)) {
				state2 = checkDup.get(nowSR2.nodeNum);
			} else {
				state2 = new Statement(nowSR2.values.Sensor, nowSR2.values.Observation, nowSR2.values.Entity,
						OntologyManager.getAddr(nowSR2.values.Sensor));
			}

			for (int i = 0; i < state2.Former.size(); i++) {
				if (state2.Former.get(i).equals(state1))
					state2.Former.remove(state2.Former.get(i));
			}
			state2.Former.add(state1);

			checkDup.put(nowSR2.nodeNum, state2);

			System.out.println(action + nowSR2.nodeNum + " <" + state2.Entity + ", " + state2.Observation + ", "
					+ state2.Sensor + "(" + OntologyManager.getAddr(state2.Sensor) + ")>");
			System.out.println();

			createStatement(nowSR2, state2);
		}
	}
}
