package com.hashfold.scannotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.HttpMethod;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;
import javassist.compiler.ast.Pair;

import org.reflections.scanners.MethodAnnotationsScanner;

/**
 * 
 * @author Praveendra Singh
 * 
 */
@SuppressWarnings("unused")
// scans for method's annotations
public class MYMethodAnnotationsScanner extends MethodAnnotationsScanner {
	String packageName;

	Map<String, AnnotatedMethodInfo> methodMap = new HashMap<String, AnnotatedMethodInfo>();

	public MYMethodAnnotationsScanner(String packageName) {
		this.packageName = packageName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void scan(Object cls) {

		ClassFile clsFile = (ClassFile) cls;

		AttributeInfo aInfo = clsFile
				.getAttribute(AnnotationsAttribute.visibleTag);

		Annotation[] annos0 = null;
		AnnotationsAttribute attrClass = null;

		if (aInfo instanceof AnnotationsAttribute) {
			attrClass = (AnnotationsAttribute) aInfo;
			annos0 = attrClass.getAnnotations();
		}

		String classAnnotationPath = "";
		if (annos0 != null) {

			Map<String, String> annoParams = fetchAttributeInfo(annos0,
					Scannotations.type+".", "");

			classAnnotationPath = annoParams.get("Path");
		}

		String className = clsFile.getName();

		// process only given package name
		if (!className.startsWith(packageName))
			return;

		className = className.replaceAll(packageName + ".", "");

		// if (className.indexOf("Test") != -1)
		// return;

		System.out.println("----------" + className + "------------");

		for (Object method : getMetadataAdapter().getMethods(cls)) {

			MethodInfo minfo = (MethodInfo) method;

			String methodName = minfo.getName();

			// System.out.println("\t.............." + methodName
			// + "..............");

			for (String methodAnnotation : (List<String>) getMetadataAdapter()
					.getMethodAnnotationNames(method)) {

				if (acceptResult(methodAnnotation)) {

					List<AttributeInfo> allAttr = minfo.getAttributes();

					Map<String, AnnotatedParamInfo> params = new HashMap<String, AnnotatedParamInfo>();

					for (AttributeInfo attrib : allAttr) {

						// System.out
						// .println("....................Processing Attribute....................");

						if (attrib instanceof ParameterAnnotationsAttribute) {
							Annotation[][] annos = ((ParameterAnnotationsAttribute) attrib)
									.getAnnotations();
							int index = 0;
							for (Annotation[] anno : annos) {

								Map<String, String> annoParams1 = fetchAttributeInfo(
										anno, Scannotations.type+".", "\t\t");

								String queryParam = annoParams1
										.get("QueryParam");

								AnnotatedParamInfo param = params.get(index
										+ "");

								if (param == null)
									param = new AnnotatedParamInfo();

								param.setQueryParam(queryParam);

								params.put(index + "", param);

								// System.out.println("\t\t\tqueryParam[" +
								// index
								// + "]:" + queryParam);

								index++;
							}
						}
						if (attrib instanceof CodeAttribute) {
							List<AttributeInfo> cas = ((CodeAttribute) attrib)
									.getAttributes();

							for (AttributeInfo ca : cas) {

								if (ca instanceof LocalVariableAttribute) {

									LocalVariableAttribute lca = ((LocalVariableAttribute) ca);
									int index = 0;
									for (int i = 0; i < lca.tableLength(); i++) {

										if (lca.variableName(i).compareTo(
												"this") == 0) {
											continue;
										}

										// System.out.println("\t\t\tTypeVar["
										// + index + "]:["
										// + lca.descriptor(i) + "]\t["
										// + lca.variableName(i) + "]");

										AnnotatedParamInfo param = params
												.get(index + "");

										if (param == null)
											param = new AnnotatedParamInfo();

										param.setType(lca.descriptor(i));
										param.setName(lca.variableName(i));

										params.put(index + "", param);

										index++;
									}
								}

							}
						}

					}

					if (params != null) {
						for (String param : params.keySet()) {

							// System.out.println("PARAM:" + param + "=>"
							// + params.get(param));
						}
					}

					AnnotationsAttribute attr = (AnnotationsAttribute) minfo
							.getAttribute(AnnotationsAttribute.visibleTag);

					Map<String, String> annoParams2 = fetchAttributeInfo(
							attr.getAnnotations(), Scannotations.type+".", "\t");

					String methodAnnotationPath = annoParams2.get("Path");
					String methodAnnotationProduces = annoParams2
							.get("Produces");

					if (methodAnnotationProduces == null)
						methodAnnotationProduces = "{}";
					else {
						methodAnnotationProduces = methodAnnotationProduces
								.replaceAll(",", "");
					}

					String httpMethod = "";

					if (httpMethod == null || httpMethod.length() <= 0) {
						httpMethod = annoParams2.get(HttpMethod.DELETE);
					}
					if (httpMethod == null || httpMethod.length() <= 0) {
						httpMethod = annoParams2.get(HttpMethod.GET);
					}
					if (httpMethod == null || httpMethod.length() <= 0) {
						httpMethod = annoParams2.get(HttpMethod.POST);
					}
					if (httpMethod == null || httpMethod.length() <= 0) {
						httpMethod = annoParams2.get(HttpMethod.PUT);
					}
					if (httpMethod == null || httpMethod.length() <= 0) {
						httpMethod = annoParams2.get(HttpMethod.HEAD);
					}
					if (httpMethod == null || httpMethod.length() <= 0) {
						httpMethod = annoParams2.get(HttpMethod.OPTIONS);
					}

					// System.out
					// .println(className + ":" + methodName + "()" + "\t"
					// + classAnnotationPath
					// + methodAnnotationPath);

					methodMap.put(className + ":" + methodName,
							new AnnotatedMethodInfo(className, methodName,
									classAnnotationPath, methodAnnotationPath,
									params, methodAnnotationProduces,
									httpMethod));

				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> fetchAttributeInfo(Annotation[] annos,
			String type, String gap) {

		if (annos == null)
			return null;

		if (annos == null)
			return null;

		Map<String, String> params = new HashMap<String, String>();

		String[] types = type.split(",");

		String value = "";

		for (Annotation anno : annos) {

			boolean found = false;
			String name = "";
			for (String tp : types) {
				if (anno.getTypeName().startsWith(tp)) {
					found = true;

					int i = anno.getTypeName().indexOf(tp);
					name = anno.getTypeName().substring(i + tp.length());
				}
			}

			if (!found)
				continue;

			Set<Pair> anno1 = anno.getMemberNames();
			if (anno1 == null) {
				params.put(name, name);
			} else {

				for (Object p : anno1.toArray()) {

					MemberValue v = anno.getMemberValue("value");

					if (value.length() > 0)
						value = ",";

					value = value + v.toString();

					if (value != null)
						value = value.replaceAll("\"", "");

					params.put(name, value);

				}

			}

		}
		return params;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void dumpMethodMap() {

		System.out
				.println("====================================================================================");

		System.out
				.println("Class:Method\tHttpMethod\tURL\tResponseType\t<[Type queryParam],>");

		List sorted = new ArrayList(methodMap.keySet());
		Collections.sort(sorted);

		for (int i = 0; i < sorted.size(); i++) {

			String key = (String) sorted.get(i);

			AnnotatedMethodInfo ami = methodMap.get(key);

			if ((ami != null) && (ami.classAnnotationPath != null))
				System.out.println(methodMap.get(key));
		}

		System.out
				.println("====================================================================================");

	}
}
