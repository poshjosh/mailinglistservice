package com.looseboxes.mailinglist.service.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MailingList.
 */
@Entity
@Table(name = "mailing_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MailingList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "time_created", nullable = false)
    private Instant timeCreated;

    @NotNull
    @Column(name = "time_modified", nullable = false)
    private Instant timeModified;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailingList id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public MailingList name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MailingList description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTimeCreated() {
        return this.timeCreated;
    }

    public MailingList timeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Instant getTimeModified() {
        return this.timeModified;
    }

    public MailingList timeModified(Instant timeModified) {
        this.timeModified = timeModified;
        return this;
    }

    public void setTimeModified(Instant timeModified) {
        this.timeModified = timeModified;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MailingList)) {
            return false;
        }
        return id != null && id.equals(((MailingList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MailingList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            ", timeModified='" + getTimeModified() + "'" +
            "}";
    }
}
