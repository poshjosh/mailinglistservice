package com.looseboxes.mailinglist.service.service.mapper;

import com.looseboxes.mailinglist.service.domain.*;
import com.looseboxes.mailinglist.service.service.dto.MailingListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MailingList} and its DTO {@link MailingListDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MailingListMapper extends EntityMapper<MailingListDTO, MailingList> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MailingListDTO toDtoId(MailingList mailingList);
}
