package net.sitina.bp.api;

import java.lang.annotation.Documented;

@Documented
public @interface ModuleParameter {

	boolean required() default false;
	
	String description();
	
}
