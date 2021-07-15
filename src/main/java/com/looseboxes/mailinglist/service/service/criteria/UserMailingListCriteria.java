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
 * Criteria class for the {@link com.looseboxes.mailinglist.service.domain.UserMailingList} entity. This class is used
 * in {@link com.looseboxes.mailinglist.service.web.rest.UserMailingListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-mailing-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserMailingListCriteria implements Serializable, Criteria {

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

    private MailingListUserStatusFilter status;

    private InstantFilter timeCreated;

    private InstantFilter timeModified;

    private LongFilter userId;

    private LongFilter mailingListId;

    public UserMailingListCriteria() {}

    public UserMailingListCriteria(UserMailingListCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.timeCreated = other.timeCreated == null ? null : other.timeCreated.copy();
        this.timeModified = other.timeModified == null ? null : other.timeModified.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.mailingListId = other.mailingListId == null ? null : other.mailingListId.copy();
    }

    @Override
    public UserMailingListCriteria copy() {
        return new UserMailingListCriteria(this);
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getMailingListId() {
        return mailingListId;
    }

    public LongFilter mailingListId() {
        if (mailingListId == null) {
            mailingListId = new LongFilter();
        }
        return mailingListId;
    }

    public void setMailingListId(LongFilter mailingListId) {
        this.mailingListId = mailingListId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserMailingListCriteria that = (UserMailingListCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(timeCreated, that.timeCreated) &&
            Objects.equals(timeModified, that.timeModified) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(mailingListId, that.mailingListId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, timeCreated, timeModified, userId, mailingListId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMailingListCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (timeCreated != null ? "timeCreated=" + timeCreated + ", " : "") +
            (timeModified != null ? "timeModified=" + timeModified + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (mailingListId != null ? "mailingListId=" + mailingListId + ", " : "") +
            "}";
    }
}
