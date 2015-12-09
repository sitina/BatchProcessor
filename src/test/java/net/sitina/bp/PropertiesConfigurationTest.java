package net.sitina.bp;

import java.util.List;

import junit.framework.TestCase;

import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.PropertiesConfiguration;

import org.junit.Test;


public class PropertiesConfigurationTest extends TestCase {

    @Test
    public void testPropertiesConfigurationLoad() {
        PropertiesConfiguration pc = new PropertiesConfiguration();
        List<ModuleConfiguration> moduleConfigurations = pc.getModuleConfigurations();
        assertNotNull(moduleConfigurations);
        assertTrue(moduleConfigurations.size() > 0);

        for (ModuleConfiguration mc : moduleConfigurations) {
            System.out.println(mc.toString());
        }
    }

}
