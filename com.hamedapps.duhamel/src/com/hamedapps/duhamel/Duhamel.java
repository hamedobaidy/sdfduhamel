/**
 * 
 */
package com.hamedapps.duhamel;

import java.util.Vector;

/**
 * @author Hamed Mohammadi
 *
 */
public class Duhamel {
	private Vector<InputForce> inputForces;
	private Vector<InputForce> forces;
	private Vector<Response> responses;
	
	private double kesi;
	private double m;
	private double omega;
	private double omega_D;
	private double dt;
	
	public Duhamel( Vector<InputForce> inputForces, double kesi, double m, double omega, double omega_D) {
		responses = new Vector<>();
		this.inputForces = inputForces;
		this.kesi = kesi;
		this.m = m;
		this.omega = omega;
		this.omega_D = omega_D;
		
		forces = new Vector<>();
	}
	
	public void compute() {
		responses.clear();
		
		double i1_ti_1 = 0;
		double i2_ti_1 = 0;
		double i3_ti_1 = 0;
		double i4_ti_1 = 0;
		double aD_ti_1 = 0;
		double bD_ti_1 = 0;
		double pti_1 = 0;
		double ti_1 = 0;
		
		computeForces();
		
		for (InputForce inputForce : forces) {
			double ti = inputForce.getT();
			double pi = inputForce.getP();
			
			// Compute common factors
			double const1 = Math.pow(kesi*omega, 2)+Math.pow(omega_D, 2);
			double const2 = Math.pow(Math.E, kesi*omega*ti)/const1;
			
			// Compue I1(ti)
			double const4 = kesi*omega*Math.cos(omega_D*ti);
			double const5 = omega_D*Math.sin(omega_D)*ti;
			double i1_ti = const1 * (const4 + const5);
			
			// Compute I1
			double i1 = i1_ti - i1_ti_1;
			
			//Compute I2(ti)
			double const6 = kesi*omega*Math.sin(omega_D*ti);
			double const7 = omega_D * Math.cos(omega_D*ti);
			double i2_ti = const2 * (const6-const7);
			
			// Compute I2
			double i2 = i2_ti - i2_ti_1;
			
			// Compute I3(ti)
			double i3_ti = (ti - ((kesi*omega)/const1))*i2 + (omega_D/const1)*i1;
			
			// Compute I3
			double i3 = i3_ti - i3_ti_1;
			
			// Compute I4(ti)
			double i4_ti = (ti - ((kesi*omega)/const1))*i1 - (omega_D/const1)*i2;
			
			// Compute I4
			double i4 = i4_ti - i4_ti_1;
			
			// Compute A_D(ti)
			double dp_dt = (pi-pti_1)/(ti-ti_1);
			double aD = aD_ti_1 + (pti_1 - ( ti_1 *  dp_dt))*i1 + dp_dt *i4;
			
			// Compute B_D(ti)
			double bD = bD_ti_1 +(pti_1 - ti_1 * dp_dt) * i2 + dp_dt* i3;
			
			// Compute u(ti)
			double m1 = Math.pow(Math.E, -kesi*omega*ti)/(m*omega_D);
			double u_ti = m1*(aD*Math.sin(omega_D*ti)+bD * Math.cos(omega_D*ti));
			
			Response r = new Response(ti, u_ti);
			r.setP(pi);
			responses.add(r);
			
			i1_ti_1 = i1_ti;
			i2_ti_1 = i2_ti;
			i3_ti_1 = i3_ti;
			i4_ti_1 = i4_ti;
			aD_ti_1 = aD;
			bD_ti_1 = bD;
			pti_1 = pi;
			ti_1 = ti;
		}
	}

	/**
	 * 
	 */
	private void computeForces() {
		forces.clear();
		double t = 0;
		for (int i = 0; i < inputForces.size() -1; i++) {
			InputForce force = inputForces.get(i);
			
			int n = 0;
			if(i == inputForces.size()-2) {
				n = 10000;
				for(int j = 1; j < n; j++) {
					t = force.getT() + j * dt;
					InputForce in = new InputForce(t, 0);
					forces.add(in);
				}
			} else {
				n = (int) ((inputForces.get(i+1).getT() - force.getT())/dt);
				for(int j = 1; j < n; j++) {
					t = force.getT() + j * dt;
					InputForce in = new InputForce(t, force.getP());
					forces.add(in);
				}
			}
		}
	}

	/**
	 * @return the inputForces
	 */
	public Vector<InputForce> getInputForces() {
		return inputForces;
	}

	/**
	 * @param inputForces the inputForces to set
	 */
	public void setInputForces(Vector<InputForce> inputForces) {
		this.inputForces = inputForces;
	}

	/**
	 * @return the kesi
	 */
	public double getKesi() {
		return kesi;
	}

	/**
	 * @param kesi the kesi to set
	 */
	public void setKesi(double kesi) {
		this.kesi = kesi;
	}

	/**
	 * @return the m
	 */
	public double getM() {
		return m;
	}

	/**
	 * @param m the m to set
	 */
	public void setM(double m) {
		this.m = m;
	}

	/**
	 * @return the omega
	 */
	public double getOmega() {
		return omega;
	}

	/**
	 * @param omega the omega to set
	 */
	public void setOmega(double omega) {
		this.omega = omega;
	}

	/**
	 * @return the omega_D
	 */
	public double getOmega_D() {
		return omega_D;
	}

	/**
	 * @param omega_D the omega_D to set
	 */
	public void setOmega_D(double omega_D) {
		this.omega_D = omega_D;
	}

	public Vector<Response> getResponses() {
		return responses;
	}

	public void setResponses(Vector<Response> responses) {
		this.responses = responses;
	}

	/**
	 * @return the dt
	 */
	public double getDt() {
		return dt;
	}

	/**
	 * @param dt the dt to set
	 */
	public void setDt(double dt) {
		this.dt = dt;
	}
}
