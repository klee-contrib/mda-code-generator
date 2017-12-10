package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Class representing a java annotation
 * @author Fabien Crapart
 *
 */
public class JavaAnnotation {
	/** Full annotation name */
	private final String name;

	/** Properties of annotation if needed, ex : \@Annotation(properties) */
	private List<JavaAnnotationProperty> properties = new ArrayList<>();

	/**
	 * Constructor of annotation
	 * @param nameWithoutArobase name of annotation (with package) but no @
	 */
	public JavaAnnotation(String nameWithoutArobase, JavaAnnotationProperty... properties) {
		this.name = "@" + nameWithoutArobase;
		if(properties.length > 0) {
			this.properties.addAll(Arrays.asList(properties));
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the properties (copy)
	 */
	public List<JavaAnnotationProperty> getProperties() {
		return new ArrayList<>(properties);
	}

	/**
	 * @param property the property to add
	 */
	public void addProperty(JavaAnnotationProperty property) {
		this.properties.add(property);
	}
}
