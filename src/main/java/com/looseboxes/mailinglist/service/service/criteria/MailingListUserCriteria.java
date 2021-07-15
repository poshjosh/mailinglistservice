package com.looseboxes.mailinglist.service.service.criteria;

import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.looseboxes.mailinglist.service.domain.MailingListUser} entity. This class is used
 * in {@link com.looseboxes.mailinglist.service.web.rest.MailingListUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mailing-list-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MailingListUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MailingListUserStatus
     */
    public static class MailingListUserStatusFilter extends Filter<MailingListUserStatus> {

        public MailingListUserStatusFilter() {}

        public MailingListUserStatusFilter(MailingListUserStatusFilter filter) {
            super(filter);
        }

        @Override
        public MailingListUserStatusFilter copy() {
            return new MailingListUserStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private StringFilter emailAddress;

    private StringFilter lastName;

    private StringFilter firstName;

    private MailingListUserStatusFilter status;

    private InstantFilter timeCreated;

    private InstantFilter timeModified;

    public MailingListUserCriteria() {}

    public MailingListUserCriteria(MailingListUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.timeCreated = other.timeCreated == null ? null : other.timeCreated.copy();
        this.timeModified = other.timeModified == null ? null : other.timeModified.copy();
    }

    @Override
    public MailingListUserCriteria copy() {
        return new MailingListUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public StringFilter emailAddress() {
        if (emailAddress == null) {
            emailAddress = new StringFilter();
        }
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public MailingListUserStatusFilter getStatus() {
        return status;
    }

    public MailingListUserStatusFilter status() {
        if (status == null) {
            status = new MailingListUserStatusFilter();
        }
        return status;
    }

    public void setStatus(MailingListUserStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getTimeCreated() {
        return timeCreated;
    }

    public InstantFilter timeCreated() {
        if (timeCreated == null) {
            timeCreated = new InstantFilter();
        }
        return timeCreated;
    }

    public void setTimeCreated(InstantFilter timeCreated) {
        this.timeCreated = timeCreated;
    }

    public InstantFilter getTimeModified() {
        return timeModified;
    }

    public InstantFilter timeModified() {
        if (timeModified == null) {
            timeModified = new InstantFilter();
        }
        return timeModified;
    }

    public void setTimeModified(InstantFilter timeModified) {
        this.timeModified = timeModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MailingListUserCriteria that = (MailingListUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(timeCreated, that.timeCreated) &&
            Objects.equals(timeModified, that.timeModified)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, emailAddress, lastName, firstName, status, timeCreated, timeModified);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MailingListUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (timeCreated != null ? "timeCreated=" + timeCreated + ", " : "") +
            (timeModified != null ? "timeModified=" + timeModified + ", " : "") +
            "}";
    }
}
