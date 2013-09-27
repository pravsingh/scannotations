package com.hashfold.scannotations;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * 
 * @author Praveendra Singh
 * 
 *         fetches the list of classes with annotations from a given package
 *         name and then prints all the methods with annotations with parameter
 *         types.
 * 
 *         The main purpose of this class is to generate the REST API details
 *         for the clients
 * 
 */
public class Scannotations {

	//packages annotations to look for. comma separated package names
	public static String type = "javax.ws.rs";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Scannotations().generateWADLFile();

	}

	public void generateWADLFile() {
		String packageName = "com.hashfold.scannotations.sample";

		IScannotationHook hook = new ScannotationHook();

		MYMethodAnnotationsScanner scanner = new MYMethodAnnotationsScanner(
				packageName, hook);

		@SuppressWarnings("unused")
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(packageName)).setScanners(
						scanner));

		scanner.dumpMethodMap();

	}

}
