package com.dasolute.universe.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
// @RooJavaBean
// @RooToString
// @RooEntity
public class Document {
    @NotNull
    @Size(max = 60)
    private String title;

    @NotNull
    @Size(max = 3000)
    private String content;

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

	public String getContent() {
        return this.content;
    }

	public void setContent(String content) {
        this.content = content;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Title: ").append(getTitle()).append(", ");
        sb.append("Content: ").append(getContent());
        return sb.toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Document attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public Document merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Document merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
		System.out.println("Document.entityManger called");
        EntityManager em = new Document().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDocuments() {
        return ((Number) entityManager().createQuery("select count(o) from Document o").getSingleResult()).longValue();
    }

	@SuppressWarnings("unchecked")
    public static List<Document> findAllDocuments() {
        return entityManager().createQuery("select o from Document o").getResultList();
    }

	public static Document findDocument(Long id) {
        if (id == null) return null;
        System.out.println("Document.findDocument called");
        return entityManager().find(Document.class, id);
    }

	@SuppressWarnings("unchecked")
    public static List<Document> findDocumentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Document o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
