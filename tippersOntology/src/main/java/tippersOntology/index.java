package tippersOntology;

import org.semanticweb.owlapi.model.OWLException;

public class index {

	public static void main(String[] args) throws OWLException {
		OntologyManager.startOntologyManager();
		
		OntologyManager.createOWLReasoner();
		OntologyManager.showOntology();		//show ontology path
		OntologyManager.showClasses();		//show ontology classes
		System.out.println("\n[test showSubclasses]\n"+OntologyManager.showSubclasses("Sensor"));		//test >> show all of subclasses with super classes
																									//이거는 대소문자 구분 필요, 혼자 사용할 거 아니고 다른 메소드에서 불러서 쓸거라서 대소문자 구분 안해도 된다고 생각
		System.out.println(OntologyManager.extractEnt("office"));
		System.out.println(OntologyManager.findSensor("occupancy"));
		System.out.println(OntologyManager.findSensor("connectivity"));

		System.out.println(OntologyManager.isVS("conn2occu"));
		System.out.println(OntologyManager.isVS("wifi1"));
		System.out.println(OntologyManager.getIndividual("meetingRoom"));
		System.out.println(OntologyManager.getIndividual("office"));
		System.out.println(OntologyManager.getIndividual("GPS"));
		
		System.out.println(OntologyManager.findInput("location2connectivity"));

		System.out.println(OntologyManager.checkCoverage("camera1", "meetingroom1"));		//true
		System.out.println(OntologyManager.checkCoverage("camera1", "meetingroom2"));		//false
		System.out.println(OntologyManager.checkCoverage("camera2", "meetingroom2"));		//true
		System.out.println(OntologyManager.checkCoverage("camera3", "meetingroom2"));		//true
		
//		System.out.println(OntologyManager.getOntoObjProperty("captures"));		//object property
//		System.out.println(OntologyManager.getOntoDataProperty("captures"));		//data property	
	}
}
