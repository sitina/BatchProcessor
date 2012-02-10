package net.sitina.bp.test.modules;

import static org.junit.Assert.*;

import java.util.Hashtable;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.modules.CZSODatabaseStorageModule;

import org.junit.Test;

public class CZSODatabaseStorageTest {

	@Test
	public void testProcess() {
		Hub inHub = new InMemoryHub();
		Hub outHub = new InMemoryHub();
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("connectionString", "jdbc:mysql://localhost/companies");
		properties.put("user", "root");
		properties.put("password", "");
		properties.put("databaseDriver", "com.mysql.jdbc.Driver");
		
		inHub.setComplete();
		// inHub.putItem("../down/irsw/detail.jsp-prajed_id=2161381.html;63674734;STK Hořovice, spol. s r.o.;112 - Společnost s ručením omezeným;21.4.1995;-;1 - 5 zaměstnanců;Ostatní profesní, vědecké a technické činnosti;Nefinanční podniky soukromé národní;CZ0202;");
		
		// inHub.putItem("../down/irsw/detail.jsp-prajed_id=2172237.html;63783860;;421 - Zahraniční osoba;10.10.1995;-;Bez zaměstnanců;Maloobchod s textilem, oděvy a obuví ve stáncích a na trzích;Nefinanční podniky soukromé pod zahraniční kontrolou;CZ0421;");
		
		inHub.putItem("../down/irsw/detail.jsp-prajed_id=2163257.html;;Vladimír Velart;101 - Fyzická osoba podnikající dle živnostenského zákona nezapsaná v obchodním rejstříku;22.5.1995;-;Neuvedeno;Malířské a natěračské práce;Ostatní osoby samostatně výdělečně činné;CZ0805;");
		
		Module dbStorageModule = new CZSODatabaseStorageModule(inHub, outHub, new ModuleConfiguration("", 1, properties), 1);
		
		Thread t = new Thread(dbStorageModule);
		t.start();
		
		while (t.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.err.println("Interrupted exception");
			}			
		}
	}

	@Test
	public void testLoadConfiguration() {
		// fail("Not yet implemented");
	}

}
