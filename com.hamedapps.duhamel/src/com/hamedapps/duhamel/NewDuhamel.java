/**
 * 
 */
package com.hamedapps.duhamel;

import java.util.List;

/**
 * @author Hamed Mohammadi
 *
 */
public class NewDuhamel {
	private List<InputForce> inputForces;
	private List<InputForce> forces;
	
	public NewDuhamel(List<InputForce> inputForces) {
		this.inputForces = inputForces;
	}
	
	private double xiwd;
	private double wd;
	private double dwsq;

	private double int1(double tau) {
		return Math.exp(xiwd*tau)*(xiwd*Math.cos(wd*tau)+wd*Math.sin(wd*tau))/dwsq;
	}
	
	private double int2(double tau) {
		return Math.exp(xiwd*tau)*(xiwd*Math.sin(wd*tau)-wd*Math.cos(wd*tau))/dwsq;
	}
	
	private double int3(double tau) {
		return tau*int2(tau)-xiwd*int2(tau)/dwsq+wd*int1(tau)/dwsq;
	}

	private double int4(double tau) {
		return tau*int1(tau)-xiwd*int1(tau)/dwsq-wd*int2(tau)/dwsq;
	}
}
