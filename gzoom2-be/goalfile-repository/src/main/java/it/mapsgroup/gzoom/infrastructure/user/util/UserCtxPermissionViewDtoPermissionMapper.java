package it.mapsgroup.gzoom.infrastructure.user.util;

import it.mapsgroup.gzoom.entity.user.model.PermissionView;
import it.mapsgroup.gzoom.infrastructure.user.dto.UserCtxPermissionViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserCtxPermissionViewDtoPermissionMapper {
    @Named("integerToBoolean")
    default Boolean integerToBoolean(Integer valueInt) {
        return valueInt != 0;
    }

    @Mapping(source = "isOrg", target = "isOrg", qualifiedByName = "integerToBoolean", resultType = Boolean.class)
    @Mapping(source = "isSup", target = "isSup", qualifiedByName = "integerToBoolean", resultType = Boolean.class)
    @Mapping(source = "isTop", target = "isTop", qualifiedByName = "integerToBoolean", resultType = Boolean.class)
    @Mapping(source = "isRole", target = "isRole", qualifiedByName = "integerToBoolean", resultType = Boolean.class)
    @Mapping(source = "isView", target = "isView", qualifiedByName = "integerToBoolean", resultType = Boolean.class)
    @Mapping(target = "rootEnabled", qualifiedByName = "integerToBoolean", resultType = Boolean.class, source = "rootEnabled")
    PermissionView toPermissionView(UserCtxPermissionViewDto userCtxPermissionViewDto);
}