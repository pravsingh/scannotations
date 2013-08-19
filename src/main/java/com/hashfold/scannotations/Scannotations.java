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
	@SuppressWarnings({ "unused" })
	public static void main(String[] args) {

		String packageName = "com.hashfold.scannotations.sample";

		MYMethodAnnotationsScanner scanner = new MYMethodAnnotationsScanner(
				packageName);

		ConfigurationBuilder builder = new ConfigurationBuilder();

		Reflections reflections = new Reflections(builder.setUrls(
				ClasspathHelper.forPackage(packageName)).setScanners(scanner));

		scanner.dumpMethodMap();

	}

}
