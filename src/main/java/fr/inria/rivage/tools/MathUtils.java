/*
 * Created on Jul 18, 2004
 */
package fr.inria.rivage.tools;

/**
 * @author Yves
 */
public class MathUtils {
	public static double deg2rad(double deg){
		return ((deg%360.0)/180.0*Math.PI);
	}
	
	public static double rad2deg(double rad){
		return (rad/Math.PI*180.0)%360.0;
	}
}
