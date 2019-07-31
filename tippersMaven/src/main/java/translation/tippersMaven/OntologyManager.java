package translation.tippersMaven;

import java.io.File;
import java.lang.invoke.*;
import java.lang.invoke.StringConcatFactory;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.semanticweb.owlapi.reasoner.*;
//import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

//import edu.uci.isg.mobipedia.parsing.HTMLParser;
//import translation.ontologyManager;
import uk.ac.manchester.cs.jfact.JFactFactory;

public class OntologyManager {
	public static OWLOntologyManager manager;
	public static OWLDataFactory factory;
	public static OWLOntology ontology;
	public static String ontologyURL = "C:\\Users\\KIM KI MIN\\Desktop\\research project\\ontology\\ontology.owl";
	public static OWLReasoner reasoner;
	public static IRI ontologyIRI = IRI
			.create("C:\\Users\\KIM KI MIN\\Desktop\\research project\\ontology\\ontology.owl");
	public static BidirectionalShortFormProvider bidiShortFormProvider;
	public static String ONTOLOGYURL = "http://www.semanticweb.org/kimkimin/ontologies/2019/6/untitled-ontology-12#";

	public OntologyManager() {
		startOntologyManager();
	}

	public static void startOntologyManager() {
		manager = OWLManager.createOWLOntologyManager();
		factory = OWLManager.getOWLDataFactory();

		System.out.println(" Starting...");
		System.out.println("test");
		try {
			System.out.println("Loading ontology   " + ontologyURL + "...");
			ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyURL));

			reasoner = createOWLReasoner();

			@SuppressWarnings("deprecation")
			Set<OWLOntology> importsClosure = ontology.getImportsClosure();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// show information of ontology
	public static OWLOntology showOntology() throws OWLOntologyCreationException {
		System.out.println("Loaded ontology: " + ontology);
		return ontology;
	}

	// show classes of ontology
	public static void showClasses() throws OWLException { // show classes
		OWLOntology o = showOntology();

		System.out.println("\n Print classes...");
		for (OWLClass cls : o.getClassesInSignature()) {
			System.out.println(cls);
		}
	}

	// get OWL object property
	public static OWLObjectProperty getOwlObjProp(String str) {
		return factory.getOWLObjectProperty(IRI.create(str));
	}

	// get OWL class
	public static OWLClass getOwlClass(String str) {
		return factory.getOWLClass(IRI.create(str));
	}

	// show subclasses
	public static void showSubclasses(String str) {
		System.out.println("\n print subclasses...");
		OWLClass c = getOwlClass(str);
		for (OWLSubClassOfAxiom cls : ontology.getSubClassAxiomsForSuperClass(c)) {
			System.out.println(cls.getSubClass());
		}
	}

	// get individuals
	public static ArrayList<String> printIndividualsByclass(String owlClass) {
		System.out.println("\n show individuals...");
		ArrayList<String> instance = null;
		for (OWLClass c : ontology.getClassesInSignature()) {
			if (c.getIRI().toString().equals(owlClass)) {
				NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
				for (OWLNamedIndividual i : instances.getFlattened()) {
					System.out.println(i.getIRI());
					StringTokenizer str = new StringTokenizer(i.getIRI().toString(), "#");
					String temp = null;
					while (str.hasMoreElements()) {
						temp = str.nextToken();
					}
					System.out.println(temp);
				}
			}
		}
		return instance;
	}

	// if there is object property
	// return that property, else return null
	public static OWLObjectProperty getOntoobjProperty(String name) {
		for (OWLObjectProperty p : ontology.getObjectPropertiesInSignature())
			if (p.getIRI().getFragment().equals(name))
				return p;
		return null;
	}

	// if there is data property
	// return that property, else return null
	public static OWLDataProperty getOntoDataProperty(String name) {
		for (OWLDataProperty p : ontology.getDataPropertiesInSignature())
			if (p.getIRI().getFragment().equals(name))
				return p;
		return null;
	}

	public static ArrayList<OWLObjectProperty> getsubProp(String prop) {
		// TODO Auto-generated method stub
		ArrayList<OWLObjectProperty> objprop = new ArrayList<OWLObjectProperty>();
		OWLObjectProperty p = getOwlObjProp(ONTOLOGYURL + prop);
		for (final OWLSubObjectPropertyOfAxiom subProp : ontology.getObjectSubPropertyAxiomsForSuperProperty(p)) {
			if (subProp.getSuperProperty() instanceof OWLProperty && subProp.getSubProperty() instanceof OWLProperty) {
				if (subProp.getSuperProperty().toString().equals("<" + ONTOLOGYURL + prop + ">")) {
					for (final OWLSubObjectPropertyOfAxiom subPrope : ontology
							.getObjectSubPropertyAxiomsForSuperProperty(subProp.getSubProperty())) {
						if (subProp.getSuperProperty() instanceof OWLProperty
								&& subProp.getSubProperty() instanceof OWLProperty) {
							objprop.addAll(subPrope.getSubProperty().getObjectPropertiesInSignature());
						}
					}
				}
			}
		}
		return objprop;
	}

	//find Sensor
	public static ArrayList<OWLClass> findSensor(String obs) {
		ArrayList<OWLClass> sen = new ArrayList<OWLClass>();
		System.out.println("\n[Print Sensor by Observation]");
		ArrayList<OWLObjectProperty> objprop = getsubProp("captures");
		for (int i = 0; i < objprop.size(); i++) {
			if (reasoner.getObjectPropertyRanges(objprop.get(i), true).toString().contains(obs)) {
				System.out.println(reasoner.getObjectPropertyDomains(objprop.get(i), true).getFlattened());
				sen.addAll(reasoner.getObjectPropertyDomains(objprop.get(i), true).getFlattened());
			}
		}
		return sen;
	}

	//find Observation
	public static ArrayList<OWLClass> findObs(String prop) {
		ArrayList<OWLClass> sen = new ArrayList<OWLClass>();
		System.out.println("\n[Print Observation by Observation Property]");
		ArrayList<OWLObjectProperty> objprop = getsubProp("captures");
		for (int i = 0; i < objprop.size(); i++) {
			if (reasoner.getObjectPropertyRanges(objprop.get(i), true).toString().contains(prop)) {
				System.out.println(reasoner.getObjectPropertyDomains(objprop.get(i), true).getFlattened());
				sen.addAll(reasoner.getObjectPropertyDomains(objprop.get(i), true).getFlattened());
			}
		}
		return sen;
	}

	// is VS
	// find a type of specific sensor
	public static boolean isVS(String Sensor) {
		boolean flag = false;
		System.out.println("\n[Find Sensor Type]\n" + Sensor + "  is Virtual Sensor?");
		OWLClass cls = factory.getOWLClass(IRI.create(Sensor));
		for (OWLSubClassOfAxiom sub : ontology.getSubClassAxiomsForSubClass(cls)) {
			if (sub.toString().contains("VirSensor")) {
				flag = true;
			}
		}
		return flag;
	}

	private static OWLOntologyManager create() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Creates a HermiT OWLReasoner with the given ontology.
	 * 
	 * @param OWLOntology The ontology
	 * @return OWLReasoner The reasoner created
	 */
	public static OWLReasoner createOWLReasoner() throws IllegalArgumentException {

		OWLReasonerFactory jfact = new JFactFactory();
		return jfact.createReasoner(ontology, new SimpleConfiguration(50000));
	}

	/**
	 * Creates a query engine to process DL queries.
	 * 
	 * @param OWLOntology The ontology
	 * @return DLQueryEngine The engine to process DL queries
	 */

	public static void addAxiomsOntology(Set<OWLAxiom> axioms) {
		manager.addAxioms(ontology, axioms);
	}

}