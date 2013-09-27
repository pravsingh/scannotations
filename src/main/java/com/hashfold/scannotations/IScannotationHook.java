package com.hashfold.scannotations;

public interface IScannotationHook {

	public void getClass(String className);

	public void getMethod(String methodName);

	public void getHttpMethod(String httpMethodName);

	public void getURL(String url);

	public void getResponseType(String responseType);

	public void getQueryParam(String queryParamType, String queryParamName);

	public void resourceStart();

	public void resourceEnd();

	public void init();

	public void finish();
}
