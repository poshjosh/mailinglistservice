package com.looseboxes.mailinglist.service.service.mapper;

import com.looseboxes.mailinglist.service.domain.*;
import com.looseboxes.mailinglist.service.service.dto.UserMailingListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserMailingList} and its DTO {@link UserMailingListDTO}.
 */
@Mapper(componentModel = "spring", uses = { MailingListUserMapper.class, MailingListMapper.class })
public interface UserMailingListMapper extends EntityMapper<UserMailingListDTO, UserMailingList> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "mailingList", source = "mailingList", qualifiedByName = "id")
    UserMailingListDTO toDto(UserMailingList s);
}
