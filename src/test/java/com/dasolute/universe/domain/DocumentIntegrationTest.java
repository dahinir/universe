package com.dasolute.universe.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.dasolute.universe.domain.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@RooIntegrationTest(entity = Document.class)
public class DocumentIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private DocumentDataOnDemand dod;

	@Test
    public void testCountDocuments() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        long count = com.dasolute.universe.domain.Document.countDocuments();
        org.junit.Assert.assertTrue("Counter for 'Document' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindDocument() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        java.lang.Long id = dod.getRandomDocument().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to provide an identifier", id);
        com.dasolute.universe.domain.Document obj = com.dasolute.universe.domain.Document.findDocument(id);
        org.junit.Assert.assertNotNull("Find method for 'Document' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Document' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllDocuments() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        long count = com.dasolute.universe.domain.Document.countDocuments();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Document', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.dasolute.universe.domain.Document> result = com.dasolute.universe.domain.Document.findAllDocuments();
        org.junit.Assert.assertNotNull("Find all method for 'Document' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Document' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindDocumentEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        long count = com.dasolute.universe.domain.Document.countDocuments();
        if (count > 20) count = 20;
        java.util.List<com.dasolute.universe.domain.Document> result = com.dasolute.universe.domain.Document.findDocumentEntries(0, (int)count);
        org.junit.Assert.assertNotNull("Find entries method for 'Document' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Document' returned an incorrect number of entries", count, result.size());
    }

	@Test
    @Transactional
    public void testFlush() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        java.lang.Long id = dod.getRandomDocument().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to provide an identifier", id);
        com.dasolute.universe.domain.Document obj = com.dasolute.universe.domain.Document.findDocument(id);
        org.junit.Assert.assertNotNull("Find method for 'Document' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDocument(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Document' failed to increment on flush directive", obj.getVersion() > currentVersion || !modified);
    }

	@Test
    @Transactional
    public void testMerge() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        java.lang.Long id = dod.getRandomDocument().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to provide an identifier", id);
        com.dasolute.universe.domain.Document obj = com.dasolute.universe.domain.Document.findDocument(id);
        org.junit.Assert.assertNotNull("Find method for 'Document' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDocument(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.merge();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Document' failed to increment on merge and flush directive", obj.getVersion() > currentVersion || !modified);
    }

	@Test
    @Transactional
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        com.dasolute.universe.domain.Document obj = dod.getNewTransientDocument(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Document' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Document' identifier to no longer be null", obj.getId());
    }

	@Test
    @Transactional
    public void testRemove() {
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to initialize correctly", dod.getRandomDocument());
        java.lang.Long id = dod.getRandomDocument().getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Document' failed to provide an identifier", id);
        com.dasolute.universe.domain.Document obj = com.dasolute.universe.domain.Document.findDocument(id);
        org.junit.Assert.assertNotNull("Find method for 'Document' illegally returned null for id '" + id + "'", obj);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Document' with identifier '" + id + "'", com.dasolute.universe.domain.Document.findDocument(id));
    }
}
