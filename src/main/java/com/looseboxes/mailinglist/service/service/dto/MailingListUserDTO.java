package com.looseboxes.mailinglist.service.service.dto;

import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.looseboxes.mailinglist.service.domain.MailingListUser} entity.
 */
public class MailingListUserDTO implements Serializable {

    private Long id;

    @Size(max = 64)
    private String username;

    @Size(max = 191)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String emailAddress;

    @Size(max = 64)
    private String lastName;

    @Size(max = 64)
    private String firstName;

    @NotNull
    private MailingListUserStatus status;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MailingListUserDTO)) {
            return false;
        }

        MailingListUserDTO mailingListUserDTO = (MailingListUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mailingListUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MailingListUserDTO{" +
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
