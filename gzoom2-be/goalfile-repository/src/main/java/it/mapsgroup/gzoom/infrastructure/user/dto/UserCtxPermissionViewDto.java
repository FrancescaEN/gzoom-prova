package it.mapsgroup.gzoom.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserCtxPermissionViewDto {
    private String userLoginId;
    private String partyId;
    private String organizationId;
    private String userCtx;
    private String ctxPermission;
    private Integer isOrg;
    private Integer isSup;
    private Integer isTop;
    private Integer isRole;
    private Integer isView;
    private Integer rootEnabled;
}