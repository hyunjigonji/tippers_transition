package tippersOntology;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.jfact.JFactFactory;

public class OntologyManager {
	public static OWLOntologyManager manager;
	public static OWLDataFactory factory;
	public static OWLOntology ontology;
	public static String ontologyURL = "../ontology.owl";
	public static OWLReasoner reasoner;
	public static String ONTOLOGYURL = "http://www.semanticweb.org/kimkimin/ontologies/2019/6/untitled-ontology-12#";
	public static String Captures = "captures";

	public OntologyManager() {
		startOntologyManager();
	}

	public static void startOntologyManager() {
		manager = OWLManager.createOWLOntologyManager();
		factory = OWLManager.getOWLDataFactory();

		System.out.println(" Starting...");

		try {
			System.out.println("Loading ontology   " + ontologyURL + "...");
			ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyURL));

			reasoner = createOWLReasoner();

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates a JFact OWLReasoner with the given ontology.
	 * 
	 * @param OWLOntology The ontology
	 * @return OWLReasoner The reasoner created
	 *
	 *         create owl reasoner
	 **/
	public static OWLReasoner createOWLReasoner() throws IllegalArgumentException {
		OWLReasonerFactory jfact = new JFactFactory();
		return jfact.createReasoner(ontology, new SimpleConfiguration(50000));
	}

	public static String strToken0(String string) {
		// TODO Auto-generated method stub
		StringTokenizer str = new StringTokenizer(string, "#");
		String temp = null;
		while (str.hasMoreElements()) {
			temp = str.nextToken();
		}
		StringTokenizer str2 = new StringTokenizer(temp, ">");
		temp = str2.nextToken();
		return temp;
	}

	public static ArrayList<String> strToken1(String string) {
		// TODO Auto-generated method stub
		StringTokenizer str = new StringTokenizer(string, ",");
		ArrayList<String> temp = new ArrayList<String>();
		while (str.hasMoreElements()) {
			temp.add(str.nextToken());
		}
		return temp;
	}

	// show information of ontology
	public static OWLOntology showOntology() throws OWLOntologyCreationException {
		System.out.println("Loaded ontology: " + ontology);
		return ontology;
	}

	// show classes of ontology
	public static void showClasses() throws OWLException { // show classes
		OWLOntology o = showOntology();
		System.out.println("\n[showClasses: Print all classes]");
		for (OWLClass cls : o.getClassesInSignature()) {
			System.out.println(cls);
		}
	}

	// get OWL object property
	public static OWLObjectProperty getOwlObjProp(String str) {
		return factory.getOWLObjectProperty(IRI.create(ONTOLOGYURL + str));
	}

	// get OWL class
	public static OWLClass getOwlClass(String str) {
		return factory.getOWLClass(IRI.create(ONTOLOGYURL + str));
	}

	// show subclasses
	public static ArrayList<OWLClassExpression> showSubclasses(String str) {
		ArrayList<OWLClassExpression> sub = new ArrayList<OWLClassExpression>();
		OWLClass c = getOwlClass(str);
		for (OWLSubClassOfAxiom cls : ontology.getSubClassAxiomsForSuperClass(c)) {
			sub.add(cls.getSubClass());
		}
		return sub;
	}

	public static OWLIndividual getidv(String str) {
		OWLIndividual idv = null;
		for (OWLIndividual i : ontology.getIndividualsInSignature()) {
			if (strToken0(i.toString()).equalsIgnoreCase(str)) {
				idv = i;
			}
		}
		return idv;
	}

	// extract entity
	// extract individual
	public static ArrayList<String> getIndividuals(String className) {
		System.out.println("\n[extractEnt: Extract Entity {" + className + "}]");
		ArrayList<String> instance = new ArrayList<String>();
		String ent0 = getOwlClass(className).toString();
		for (OWLClass c : ontology.getClassesInSignature()) {
			if (c.toString().equalsIgnoreCase(ent0)) {
				NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
				for (OWLNamedIndividual i : instances.getFlattened()) {
					String temp = strToken0(i.getIRI().toString());
					instance.add(temp);
				}
			}
		}
		return instance;
	}

	// if there is object property
	// return that property, else return null
	public static OWLObjectProperty getOntoObjProperty(String name) {
		OWLObjectProperty prop = null;
		System.out.println("\n[getOntoObjProperty: get object property that has same name with input]");
		for (OWLObjectProperty p : ontology.getObjectPropertiesInSignature())
			if (p.getIRI().getFragment().equalsIgnoreCase(name))
				prop = p;
		return prop;
	}

	// if there is data property
	// return that property, else return null
	public static OWLDataProperty getOntoDataProperty(String name) {
		System.out.println("\n[getOntoDataProperty: get data property that has same name with input]");
		OWLDataProperty prop = null;
		for (OWLDataProperty p : ontology.getDataPropertiesInSignature())
			if (p.getIRI().getFragment().equalsIgnoreCase(name))
				prop = p;
		return prop;
	}

	// go to sub property
	public static ArrayList<OWLObjectProperty> getsubProp(String prop) {
		ArrayList<OWLObjectProperty> objprop = new ArrayList<OWLObjectProperty>();
		OWLObjectProperty p = getOwlObjProp(prop);
		String str = reasoner.getSubObjectProperties(getOwlObjProp(prop), true).toString();
		for (final OWLSubObjectPropertyOfAxiom subProp : ontology.getObjectSubPropertyAxiomsForSuperProperty(p)) {
			if (subProp.getSuperProperty() instanceof OWLProperty && subProp.getSubProperty() instanceof OWLProperty) {
				if (subProp.getSuperProperty().toString().equalsIgnoreCase("<" + ONTOLOGYURL + prop + ">")) {
					objprop.addAll(subProp.getSubProperty().getObjectPropertiesInSignature());
				}
			}
		}
		return objprop;
	}

	// getAptDevice
	// get individual of class
	// 대소문자 구분 X
	public static ArrayList<String> getAptDevice(String cls) {
		// TODO Auto-generated method stub
		System.out.println("\n[getAptDevice: get individuals of {" + cls + "} ]");
		ArrayList<String> idv = new ArrayList<String>();
		for (Node<OWLNamedIndividual> i : reasoner.getInstances(getOwlClass(cls), false)) {
			idv.add(strToken0(i.toString()));
		}
		return idv;
	}

	// find Sensor
	// return names of classes in hashSet
	public static Set<String> findSensor(String obs) {
		Set<String> sen = new HashSet<String>();
		System.out.println("\n[findSensor: Print Sensor by {" + obs + "}]");
		for (OWLObjectPropertyExpression p : getsubProp("captures")) {
			if (strToken0(reasoner.getObjectPropertyRanges(p, true).toString()).equalsIgnoreCase(obs)
					& strToken0(p.toString()).contains(Captures)) {
				for (Node<OWLClass> c : reasoner.getObjectPropertyDomains(p, true)) {
					if (strToken0(c.toString()).contains("Node"))
						continue;
					sen.add(strToken0(c.toString()));
				}
			}
		}
		return sen;
	}

	// find input
	// input array of class name
	public static ArrayList<String> findInput(String vs) {
		ArrayList<String> input = new ArrayList<String>();
		System.out.println("\n[findInput: find VS{" + vs + "} input]");
		OWLObjectProperty p = getOwlObjProp("input");
		for (Node<OWLObjectPropertyExpression> subProp : reasoner.getSubObjectProperties(p, false)) {
			if (!strToken0(subProp.toString()).contains("Node")) {
				ArrayList<String> domain = getDomain(strToken0(subProp.toString()));
				for (int i = 0; i < domain.size(); i++) {
					if (domain.get(i).equalsIgnoreCase(vs)) {
						input = getRange(strToken0(subProp.toString()));
					}
				}
			}
		}
		return input;
	}

	// get range of property
	public static ArrayList<String> getRange(String property) {
		ArrayList<String> Range = new ArrayList<String>();
//		System.out.println("\n[find Range]: " + property);
		OWLObjectProperty p = getOwlObjProp(property); // find property
		for (Node<OWLClass> r : reasoner.getObjectPropertyRanges(p, true)) { // find range
			if (!strToken0(r.toString()).contains("Node"))
				Range.add(strToken0(r.toString()));
		}
		return Range;
	}

	// get domain of property
	public static ArrayList<String> getDomain(String property) {
		ArrayList<String> Domain = new ArrayList<String>();
//		System.out.println("\n[find Domain]: " + property);
		OWLObjectProperty p = getOwlObjProp(property); // find property
		for (Node<OWLClass> r : reasoner.getObjectPropertyDomains(p, true)) { // find range
			if (!strToken0(r.toString()).contains("Node"))
				Domain.add(strToken0(r.toString()));
		}
		return Domain;
	}

	// is VS
	// find a type of specific sensor
	// A parameter(Sensor) is class name
	public static boolean isVS(String Sensor) {
		boolean flag = false;
		System.out.println("\n[isVS: Find Sensor Type]\n" + Sensor + " is Virtual Sensor?");
		if (reasoner.getSuperClasses(getOwlClass(Sensor), true).toString().contains("VirSensor"))
			flag = true;

		return flag;
	}

	// check a device in a room
	// both are individual name
	public static boolean checkCoverage(String sen, String ent) {
		boolean chk = false;
		System.out.println("\n[checkCoverage: check a sensor{" + sen + "} in a room{" + ent + "}]");
		for (OWLIndividual i : ontology.getIndividualsInSignature()) {
			if (strToken0(i.toString()).equalsIgnoreCase(ent)) {
				for (OWLObjectPropertyAssertionAxiom o : ontology.getObjectPropertyAssertionAxioms(i)) {
					if (strToken0(o.toString()).equalsIgnoreCase(sen))
						chk = true;
				}
			}
		}
		return chk;
	}
	// 센서를 넣으면 인디비주얼 이름 주는 메소드 활용할 것
	// 이름 바꿀 것 sen -> dev
	// name of the property 이름을 한번 걸러야대 hasSensor

	// get Time
	// get time cost of device
	// Virtual Sensor input is class name
	// Rest of device are individual name
	public static Integer getTime(String dev) {
		NodeSet<OWLNamedIndividual> idv;
		String in = new String();
		int num = -9999;
		System.out.println("\n[getTimecost: get a time cost of {" + dev + "}]");
		if (showSubclasses("VirSensor").toString().contains(dev)) {
			for (OWLClass c : ontology.getClassesInSignature()) {
				System.out.println("cccc" + c);
				if (strToken0(c.toString()).equalsIgnoreCase(dev)) {
					idv = reasoner.getInstances(c, false);
					System.out.println("idv" + idv);
					in = strToken0(idv.toString());
					System.out.println("in   " + in);
//					num = Integer.parseInt(getCost(in, "Time"));
					System.out.println("1111" + getCost(in, "Time"));
				}
			}
		} else {
//			num = Integer.parseInt(getCost(dev, "Time"));
			System.out.println("2222" + getCost(dev, "Time"));
		}
		return num;
	}
//isVS 활용할 것

	// get Money
	// get money cost of device
	// Virtual Sensor input is class name
	// Rest of device are individual name
	public static Integer getMoney(String dev) {
		NodeSet<OWLNamedIndividual> idv;
		String in = new String();
		int num = -9999;
		System.out.println("\n[getTimecost: get a money cost of {" + dev + "}]");
		if (showSubclasses("VirSensor").toString().contains(dev)) {
			for (OWLClass c : ontology.getClassesInSignature()) {
				if (strToken0(c.toString()).equalsIgnoreCase(dev)) {
					idv = reasoner.getInstances(c, false);
					in = strToken0(idv.toString());
					// num = Integer.parseInt(getCost(in, "Money"));
					System.out.println("3333" + getCost(in, "Money"));
				}
			}
		} else {
//			num = Integer.parseInt(getCost(dev, "Money"));
			System.out.println("4444" + getCost(dev, "Money"));
		}
		return num;
	}

	// get both time and money
	public static String getCost(String dev, String cost) {
		String time = new String();
		for (OWLIndividual i : ontology.getIndividualsInSignature()) {
			for (OWLObjectPropertyAssertionAxiom p : ontology.getObjectPropertyAssertionAxioms(i)) {
				if (p.toString().contains("hasCost") && p.toString().contains(dev)) {
					System.out.println("123123123" + p.getIndividualsInSignature());
					if (ontology.getDataPropertyAssertionAxioms(getidv(strToken0(p.toString()))).toString()
							.contains(cost)) {
						time = ontology.getDataPropertyAssertionAxioms(getidv(strToken0(p.toString()))).toString();
						StringTokenizer tok = new StringTokenizer(time, "\"");
						time = tok.nextToken();
						while (tok.hasMoreElements()) {
							time = tok.nextToken();
							break;
						}
					}
				}
			}
		}
		time.replaceAll(" ", "");
		return time;
	}
	// 위에 있는 인디비주얼 불러온ㄴ 메소드 사용할 것
	// hasCost 위에 정의

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