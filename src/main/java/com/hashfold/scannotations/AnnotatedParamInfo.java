package com.hashfold.scannotations;

/**
 * 
 * @author Praveendra Singh
 * 
 */
public class AnnotatedParamInfo {
	private String queryParam;
	private String type;
	private String name;

	@Override
	public String toString() {
		// return queryParam + "@" + type + " " + name;

		return queryParam + "@" + type;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {

		queryParam = queryParam.trim();
		this.queryParam = queryParam;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {

		int s = type.lastIndexOf('/');

		if (s != -1) {
			type = type.substring(s + 1);

			int e = type.indexOf(';');
			if (e != -1) {
				type = type.substring(0, e);
			}
		}

		type = type.trim();

		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name.trim();
		this.name = name;
	}

}
