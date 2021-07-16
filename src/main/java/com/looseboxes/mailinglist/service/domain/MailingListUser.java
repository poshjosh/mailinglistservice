package com.looseboxes.mailinglist.service.domain;

import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MailingListUser.
 */
@Entity
@Table(name = "mailing_list_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MailingListUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 64)
    @Column(name = "username", length = 64)
    private String username;

    @Size(max = 191)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email_address", length = 191, unique = true)
    private String emailAddress;

    @Size(max = 64)
    @Column(name = "last_name", length = 64)
    private String lastName;

    @Size(max = 64)
    @Column(name = "first_name", length = 64)
    private String firstName;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailingListUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public MailingListUser username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public MailingListUser emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLastName() {
        return this.lastName;
    }

    public MailingListUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public MailingListUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public MailingListUserStatus getStatus() {
        return this.status;
    }

    public MailingListUser status(MailingListUserStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(MailingListUserStatus status) {
        this.status = status;
    }

    public Instant getTimeCreated() {
        return this.timeCreated;
    }

    public MailingListUser timeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Instant getTimeModified() {
        return this.timeModified;
    }

    public MailingListUser timeModified(Instant timeModified) {
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
        if (!(o instanceof MailingListUser)) {
            return false;
        }
        return id != null && id.equals(((MailingListUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MailingListUser{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", status='" + getStatus() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            ", timeModified='" + getTimeModified() + "'" +
            "}";
    }
}
