package com.looseboxes.mailinglist.service.service.dto;

import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.looseboxes.mailinglist.service.domain.UserMailingList} entity.
 */
public class UserMailingListDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private MailingListUserStatus status;

    @NotNull
    private Instant timeCreated;

    @NotNull
    private Instant timeModified;

    private MailingListUserDTO user;

    private MailingListDTO mailingList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailingListUserStatus getStatus() {
        return status;
    }

    public void setStatus(MailingListUserStatus status) {
        this.status = status;
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

    public MailingListUserDTO getUser() {
        return user;
    }

    public void setUser(MailingListUserDTO user) {
        this.user = user;
    }

    public MailingListDTO getMailingList() {
        return mailingList;
    }

    public void setMailingList(MailingListDTO mailingList) {
        this.mailingList = mailingList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMailingListDTO)) {
            return false;
        }

        UserMailingListDTO userMailingListDTO = (UserMailingListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userMailingListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMailingListDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            ", timeModified='" + getTimeModified() + "'" +
            ", user=" + getUser() +
            ", mailingList=" + getMailingList() +
            "}";
    }
}
