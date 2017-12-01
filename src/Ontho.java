import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;




public class Ontho {

	public static void uploadRDF(File rdf, String serviceURI)
			throws IOException {

		// parse the file
		Model m = ModelFactory.createDefaultModel();
		try (FileInputStream in = new FileInputStream(rdf)) {
			m.read(in, null, "RDF/XML");
		}

		// upload the resulting model
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(m);
	}

	public static void execSelectAndPrint(String serviceURI, String query) {
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}

	public static void execSelectAndProcess(String serviceURI, String query) {
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			// assumes that you have an "?x" in your query
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//uploadRDF(new File("test.rdf"), );
		String nameFile = "personne2.owl";
		String serivceUri = "http://localhost:3030/ds";
		
		String queryEcole = "SELECT ?Ecole " +
				" WHERE {?x  <http://www.semanticweb.org/nobby/ontologies/2017/10/untitled-ontology-11#nom_ecole>  ?Ecole}";
		
		String queryEntreprise = "SELECT ?Entreprise " +
				" WHERE {?x  <http://www.semanticweb.org/nobby/ontologies/2017/10/untitled-ontology-11#nom_entreprise>  ?Entreprise}";
		
		String queryFemme = "SELECT ?Femme " +
				" WHERE {?x  <http://www.semanticweb.org/nobby/ontologies/2017/10/untitled-ontology-11#nom>  ?Femme}";
		
		//uploadRDF(new File(nameFile), serivceUri);
		execSelectAndPrint(serivceUri, queryEcole);
		
		//execSelectAndProcess(serivceUri, query);
	}
}
