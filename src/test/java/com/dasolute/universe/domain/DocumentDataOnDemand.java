package com.dasolute.universe.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.dasolute.universe.domain.Document;

@Configurable
@Component
@RooDataOnDemand(entity = Document.class)
public class DocumentDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<Document> data;

	public Document getNewTransientDocument(int index) {
        com.dasolute.universe.domain.Document obj = new com.dasolute.universe.domain.Document();
        obj.setContent("content_" + index);
        obj.setTitle("title_" + index);
        return obj;
    }

	public Document getSpecificDocument(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size()-1)) index = data.size() - 1;
        Document obj = data.get(index);
        return Document.findDocument(obj.getId());
    }

	public Document getRandomDocument() {
        init();
        Document obj = data.get(rnd.nextInt(data.size()));
        return Document.findDocument(obj.getId());
    }

	public boolean modifyDocument(Document obj) {
        return false;
    }

	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {
        if (data != null) {
            return;
        }
        
        data = com.dasolute.universe.domain.Document.findDocumentEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Document' illegally returned null");
        if (data.size() > 0) {
            return;
        }
        
        data = new java.util.ArrayList<com.dasolute.universe.domain.Document>();
        for (int i = 0; i < 10; i++) {
            com.dasolute.universe.domain.Document obj = getNewTransientDocument(i);
            obj.persist();
            data.add(obj);
        }
    }
}
