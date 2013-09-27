package com.hashfold.scannotations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScannotationHook implements IScannotationHook {

	BufferedWriter bw;

	String className;
	String methodName;
	String httpMethodName;
	String url;
	String responseType;
	List<String> params = new ArrayList<String>(); // queryParamType:queryParamName

	@Override
	public void resourceStart() {
		className = "";
		methodName = "";
		httpMethodName = "";
		url = "";
		responseType = "";
		params.clear();

	}

	@Override
	public void resourceEnd() {

		String resource = "<resource path=\"" + url + "\">"
				+ " <method name=\"" + httpMethodName + "\" id=\"" + methodName
				+ "\">" + "    <request>";

		for (String data : params) {
			String[] keys = data.split(":");

			resource += "<param name=\"" + keys[1] + "\" type=\"xsd:"
					+ keys[0].toLowerCase()
					+ "\" style=\"query\" required=\"true\"/>";
		}

		resource += "    </request>" + "    <response status=\"200\">"
				+ "      <representation mediaType=\"" + responseType + "\""
				+ "        element=\"lc:ResultSet\"/>" + "    </response>"
				+ "    <response status=\"400\">"
				+ "      <representation mediaType=\"" + responseType + "\""
				+ "        element=\"lca:Error\"/>" + "    </response>"
				+ " </method>" + "</resource>";

		try {
			bw.write(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void getClass(String className) {
		this.className = className;

	}

	@Override
	public void getMethod(String methodName) {
		this.methodName = methodName;

	}

	@Override
	public void getHttpMethod(String httpMethodName) {
		this.httpMethodName = httpMethodName;

	}

	@Override
	public void getURL(String url) {
		this.url = url;

	}

	@Override
	public void getResponseType(String responseType) {
		this.responseType = responseType;

	}

	@Override
	public void getQueryParam(String queryParamType, String queryParamName) {
		params.add(queryParamType + ":" + queryParamName);

	}

	@Override
	public void init() {

		try {
			File file = new File("./wadl.xml");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String head = "<?xml version=\"1.0\"?>"
				+ "<application xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xsi:schemaLocation=\"http://wadl.dev.java.net/2009/02 wadl.xsd\""
				+ " xmlns:tns=\"urn:lendingclub:lc\""
				+ "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
				+ "    xmlns:lc=\"urn:lendingclub:lc\""
				+ "    xmlns:lca=\"urn:lendingclub:api\""
				+ "    xmlns=\"http://wadl.dev.java.net/2009/02\">"
				+ "    <grammars>" + "      <include"
				+ "         href=\"NewsSearchResponse.xsd\"/>"
				+ "      <include" + "        href=\"Error.xsd\"/>"
				+ "    </grammars>"
				+ "    <resources base=\"http://example.com\">";

		try {
			bw.write(head);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void finish() {

		String footer = "  </resources>" + " </application>";

		try {
			bw.write(footer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
