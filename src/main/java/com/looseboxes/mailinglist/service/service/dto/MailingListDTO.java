package com.looseboxes.mailinglist.service.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.looseboxes.mailinglist.service.domain.MailingList} entity.
 */
public class MailingListDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Instant timeCreated;

    @NotNull
    private Instant timeModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Instant getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(Instant timeModified) {
        this.timeModified = timeModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MailingListDTO)) {
            return false;
        }

        MailingListDTO mailingListDTO = (MailingListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mailingListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MailingListDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            ", timeModified='" + getTimeModified() + "'" +
            "}";
    }
}
