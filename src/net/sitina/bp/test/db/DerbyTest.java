package net.sitina.bp.test.db;

import junit.framework.TestCase;

import org.junit.Test;

import net.sitina.bp.core.db.DerbyDriver;


public class DerbyTest extends TestCase {

	@Test
	public void testDerbyConnection() {
		DerbyDriver dd = new DerbyDriver();
		dd.connect();
	}
	
}
