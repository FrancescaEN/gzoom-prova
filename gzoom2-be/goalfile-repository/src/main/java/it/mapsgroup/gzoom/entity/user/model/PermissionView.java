package it.mapsgroup.gzoom.entity.user.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionView {
    private String userLoginId;
    private String organizationId;
    private String partyId;
    private String userCtx;
    private String ctxPermission;
    private boolean isOrg;
    private boolean isSup;
    private boolean isTop;
    private boolean isRole;
    private boolean isView;
    private boolean rootEnabled;
}
