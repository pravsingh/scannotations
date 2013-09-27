package com.hashfold.scannotations;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Praveendra Singh
 * 
 */
public class AnnotatedMethodInfo {

	public String className;
	public String methodName;
	public String classAnnotationPath;
	public String methodAnnotationPath;
	public Map<String, AnnotatedParamInfo> params = new HashMap<String, AnnotatedParamInfo>();
	public String methodAnnotationProduces;
	public String httpMethod;

	public AnnotatedMethodInfo(String className, String methodName,
			String classAnnotationPath, String methodAnnotationPath,
			Map<String, AnnotatedParamInfo> params,
			String methodAnnotationProduces, String httpMethod) {

		this.className = className;
		this.methodName = methodName;
		this.classAnnotationPath = classAnnotationPath;
		this.methodAnnotationPath = methodAnnotationPath;
		this.params = params;
		this.methodAnnotationProduces = methodAnnotationProduces;
		this.httpMethod = httpMethod;

	}

	@Override
	public String toString() {

		String data = className + ":" + methodName + "\t" + httpMethod + "\t"
				+ "" + classAnnotationPath + methodAnnotationPath+"\t"+methodAnnotationProduces;

		String param = "";

		for (String key : params.keySet()) {

			if (param.length() > 0)
				param += ", ";

			AnnotatedParamInfo p = params.get(key);

			param += p.getType() + " " + p.getQueryParam();
		}

		data += "\t" + param;

		return data;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getClassAnnotationPath() {
		return classAnnotationPath;
	}

	public void setClassAnnotationPath(String classAnnotationPath) {
		this.classAnnotationPath = classAnnotationPath;
	}

	public String getMethodAnnotationPath() {
		return methodAnnotationPath;
	}

	public void setMethodAnnotationPath(String methodAnnotationPath) {
		this.methodAnnotationPath = methodAnnotationPath;
	}

	public Map<String, AnnotatedParamInfo> getParams() {
		return params;
	}

	public void setParams(Map<String, AnnotatedParamInfo> params) {
		this.params = params;
	}

	public String getMethodAnnotationProduces() {
		return methodAnnotationProduces;
	}

	public void setMethodAnnotationProduces(String methodAnnotationProduces) {
		this.methodAnnotationProduces = methodAnnotationProduces;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	
	
}
