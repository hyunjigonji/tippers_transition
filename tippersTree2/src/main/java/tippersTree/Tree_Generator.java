package tippersTree;

import java.util.ArrayList;

public class Tree_Generator extends Tree{
	
	public static void URgenerator0(URNode nowNode) {
		String nowE = nowNode.values.Entity;
		String nowC = nowNode.values.Condition;
		//System.out.println("URgenerator0 " + Integer.toString(nowNode.nodeNum)+ " " + nowE + " " + nowC);

		Node ConnectNode;
		ArrayList<String> sens = OntologyManager.findSensor(nowC);
		
		if(OntologyManager.hasMultiInput(sens)) { // multiple input -> + node
			ConnectNode = Tree.newPlusNode(); 
			Tree.appendChild(nowNode, ConnectNode);
		} 
		else { // not multiple input -> x node
			ConnectNode = Tree.newXNode();
			Tree.appendChild(nowNode, ConnectNode);
		}
		
		for(int i = 1 ; i < sens.size(); i++) {
			String nowSen = sens.get(i);
			//System.out.println(i + " "+ nowSen);
			SR newSR = new SR(nowSen, nowC, nowE);
			SRNode newSRNode = Tree.newSRNode(newSR);

			if(OntologyManager.isVS(nowSen)) newSRNode.type = types.typeVSR;
			else newSRNode.type = types.typePSR;
			
			SRs.add(newSRNode);

			Tree.appendChild(ConnectNode,newSRNode);
			generator1(newSRNode, nowE); // call other function once
		}
		return;
	}
	
	public static void UAgenerator0(UANode nowNode) {
		String nowE = nowNode.values.Entity;
		String nowP = nowNode.values.Property;
		//System.out.println(Integer.toString(nowNode.nodeNum)+ " " + nowE + " " + nowP);

		XNode XNode = Tree.newXNode();
		Tree.appendChild(nowNode, XNode);

		String nowObs = OntologyManager.findAction(nowP);
		String nowActuator = OntologyManager.findActuator(nowP);
		//System.out.println(nowActuator);

		SR newSR = new SR(nowActuator, nowObs, nowE);
		SRNode newSRNode = Tree.newSRNode(newSR);
		
		newSRNode.type = types.typeAC;
		
		SRs.add(newSRNode);
		Tree.appendChild(XNode,newSRNode);
			
		return;
	}
	
	// generate from SRNode using recursive algorithm
	public static void generator1(SRNode nowNode, String nowEnt) {
		//System.out.println("generator1  " + nowNode.values.Observation + " " + nowNode.values.Sensor);
		if(nowNode.type == types.typePSR) return;
		String nowObs = OntologyManager.findInput(nowNode.values.Sensor);
		// decide if it requires multiple input
		Node ConnectNode; 
		ArrayList<String> sens = OntologyManager.findSensor(nowObs);
		
		if(OntologyManager.hasMultiInput(sens)) { // multiple input -> + node
			ConnectNode = Tree.newPlusNode(); 
			Tree.appendChild(nowNode, ConnectNode);
		} 
		else { // not multiple input -> x node
			ConnectNode = Tree.newXNode();
			Tree.appendChild(nowNode, ConnectNode);
		}

		for(int j = 1 ; j < sens.size() ; j++) {
			String nowSen = sens.get(j);
			SR newSR = new SR(nowSen, nowObs, nowEnt);
			SRNode newSRNode = Tree.newSRNode(newSR);

			if(OntologyManager.isVS(nowSen)) newSRNode.type = types.typeVSR;
			else newSRNode.type = types.typePSR;
			
			SRs.add(newSRNode);
			Tree.appendChild(ConnectNode,newSRNode);
			generator1(newSRNode, nowEnt); // recursive
		}
		
		return;
	}
}
