package translation.ontology;

import static org.semanticweb.owlapi.vocab.OWLFacet.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
//import org.junit.Ignore;
//import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomCollection;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;

import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;

import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;

import org.semanticweb.owlapi.model.OWLDataRange;

import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;

import org.semanticweb.owlapi.model.OWLDataUnionOf;

import org.semanticweb.owlapi.model.OWLDatatype;

import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;

import org.semanticweb.owlapi.model.OWLDatatypeRestriction;

import org.semanticweb.owlapi.model.OWLDeclarationAxiom;

import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;

import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;

import org.semanticweb.owlapi.model.OWLEntity;

import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLFacetRestriction;

import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;

import org.semanticweb.owlapi.model.OWLIndividual;

import org.semanticweb.owlapi.model.OWLLiteral;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;

import org.semanticweb.owlapi.model.OWLObjectExactCardinality;

import org.semanticweb.owlapi.model.OWLObjectHasValue;

import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;

import org.semanticweb.owlapi.model.OWLObjectOneOf;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import org.semanticweb.owlapi.model.OWLOntology;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import org.semanticweb.owlapi.model.OWLOntologyFormat;

import org.semanticweb.owlapi.model.OWLOntologyID;

import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;

import org.semanticweb.owlapi.model.OWLOntologyManager;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import org.semanticweb.owlapi.model.PrefixManager;

import org.semanticweb.owlapi.model.SWRLAtom;

import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;

import org.semanticweb.owlapi.model.SWRLRule;

import org.semanticweb.owlapi.model.SWRLVariable;

import org.semanticweb.owlapi.model.SetOntologyID;

import org.semanticweb.owlapi.reasoner.BufferingMode;

import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;

import org.semanticweb.owlapi.reasoner.InferenceType;

import org.semanticweb.owlapi.reasoner.Node;

import org.semanticweb.owlapi.reasoner.NodeSet;

import org.semanticweb.owlapi.reasoner.OWLReasoner;

import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;

import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;

import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import org.semanticweb.owlapi.util.AutoIRIMapper;

import org.semanticweb.owlapi.util.DefaultPrefixManager;

import org.semanticweb.owlapi.util.InferredAxiomGenerator;

import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

//import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import org.semanticweb.owlapi.util.OWLEntityRemover;

import org.semanticweb.owlapi.util.OWLOntologyMerger;

import org.semanticweb.owlapi.util.OWLOntologyWalker;

import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.PriorityCollection;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import org.semanticweb.owlapi.vocab.OWL2Datatype;

import org.semanticweb.owlapi.vocab.OWLFacet;

import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

public class index {

	public static void main(String[] args) throws OWLException {

		OntologyManager.startOntologyManager();
		OntologyManager.createOWLReasoner();
		OntologyManager.createDLQueryEngine();
		OntologyManager.showOntology();		//show ontology path
		OntologyManager.showClasses();		//show ontology classes
		OntologyManager.showSubclasses();	//show all of subclasses with superclasses
		OntologyManager.printIndividualsByclass("http://www.semanticweb.org/kimkimin/ontologies/2019/6/untitled-ontology-12#Sensor");
		
		System.out.println("\n" + OntologyManager.getOntoobjProperty("captures"));		//object property
		System.out.println("\n" + OntologyManager.getOntoDataProperty("captures"));		//data property
		
		OntologyManager.findSensor();
	}
}
