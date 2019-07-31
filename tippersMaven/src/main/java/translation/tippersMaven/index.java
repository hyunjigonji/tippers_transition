package translation.tippersMaven;

import org.semanticweb.owlapi.model.OWLException;

public class index {

	public static void main(String[] args) throws OWLException {

		OntologyManager.startOntologyManager();
		OntologyManager.createOWLReasoner();
		OntologyManager.showOntology();		//show ontology path
		OntologyManager.showClasses();		//show ontology classes
		OntologyManager.showSubclasses("http://www.semanticweb.org/kimkimin/ontologies/2019/6/untitled-ontology-12#Room");	//show all of subclasses with superclasses
		OntologyManager.printIndividualsByclass("http://www.semanticweb.org/kimkimin/ontologies/2019/6/untitled-ontology-12#Sensor");
		OntologyManager.findSensor("http://www.semanticweb.org/kimkimin/ontologies/2019/6/untitled-ontology-12#Occupancy");
		System.out.println(OntologyManager.findObs("connProp")); 
		System.out.println(OntologyManager.isVS("Wifi"));
		
		System.out.println("\n" + OntologyManager.getOntoobjProperty("captures"));		//object property
		System.out.println("\n" + OntologyManager.getOntoDataProperty("captures"));		//data property	
	}
}