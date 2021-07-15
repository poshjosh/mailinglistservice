package com.looseboxes.mailinglist.service.service.criteria;

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
 * Criteria class for the {@link com.looseboxes.mailinglist.service.domain.MailingList} entity. This class is used
 * in {@link com.looseboxes.mailinglist.service.web.rest.MailingListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mailing-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MailingListCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private InstantFilter timeCreated;

    private InstantFilter timeModified;

    public MailingListCriteria() {}

    public MailingListCriteria(MailingListCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.timeCreated = other.timeCreated == null ? null : other.timeCreated.copy();
        this.timeModified = other.timeModified == null ? null : other.timeModified.copy();
    }

    @Override
    public MailingListCriteria copy() {
        return new MailingListCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final MailingListCriteria that = (MailingListCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(timeCreated, that.timeCreated) &&
            Objects.equals(timeModified, that.timeModified)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, timeCreated, timeModified);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MailingListCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (timeCreated != null ? "timeCreated=" + timeCreated + ", " : "") +
            (timeModified != null ? "timeModified=" + timeModified + ", " : "") +
            "}";
    }
}
