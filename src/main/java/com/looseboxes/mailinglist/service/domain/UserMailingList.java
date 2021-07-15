package com.looseboxes.mailinglist.service.domain;

import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserMailingList.
 */
@Entity
@Table(name = "user_mailing_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserMailingList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MailingListUserStatus status;

    @NotNull
    @Column(name = "time_created", nullable = false)
    private Instant timeCreated;

    @NotNull
    @Column(name = "time_modified", nullable = false)
    private Instant timeModified;

    @ManyToOne(optional = false)
    @NotNull
    private MailingListUser user;

    @ManyToOne(optional = false)
    @NotNull
    private MailingList mailingList;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMailingList id(Long id) {
        this.id = id;
        return this;
    }

    public MailingListUserStatus getStatus() {
        return this.status;
    }

    public UserMailingList status(MailingListUserStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(MailingListUserStatus status) {
        this.status = status;
    }

    public Instant getTimeCreated() {
        return this.timeCreated;
    }

    public UserMailingList timeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Instant getTimeModified() {
        return this.timeModified;
    }

    public UserMailingList timeModified(Instant timeModified) {
        this.timeModified = timeModified;
        return this;
    }

    public void setTimeModified(Instant timeModified) {
        this.timeModified = timeModified;
    }

    public MailingListUser getUser() {
        return this.user;
    }

    public UserMailingList user(MailingListUser mailingListUser) {
        this.setUser(mailingListUser);
        return this;
    }

    public void setUser(MailingListUser mailingListUser) {
        this.user = mailingListUser;
    }

    public MailingList getMailingList() {
        return this.mailingList;
    }

    public UserMailingList mailingList(MailingList mailingList) {
        this.setMailingList(mailingList);
        return this;
    }

    public void setMailingList(MailingList mailingList) {
        this.mailingList = mailingList;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMailingList)) {
            return false;
        }
        return id != null && id.equals(((UserMailingList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMailingList{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            ", timeModified='" + getTimeModified() + "'" +
            "}";
    }
}
