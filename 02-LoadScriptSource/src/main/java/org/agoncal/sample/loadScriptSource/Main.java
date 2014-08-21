package org.agoncal.sample.loadScriptSource;

import javax.persistence.Persistence;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         https://github.com/agoncal
 *         --
 */
public class Main {

	public static void main(String[] args) {

		Persistence.generateSchema("sampleJPALoadScriptSourcePU", null);
	}
}
