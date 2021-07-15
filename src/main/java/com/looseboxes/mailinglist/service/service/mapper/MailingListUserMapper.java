package com.looseboxes.mailinglist.service.service.mapper;

import com.looseboxes.mailinglist.service.domain.*;
import com.looseboxes.mailinglist.service.service.dto.MailingListUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MailingListUser} and its DTO {@link MailingListUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MailingListUserMapper extends EntityMapper<MailingListUserDTO, MailingListUser> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MailingListUserDTO toDtoId(MailingListUser mailingListUser);
}
