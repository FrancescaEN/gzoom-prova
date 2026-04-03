package it.mapsgroup.gzoom.user;

import it.mapsgroup.gzoom.entity.goalfile.model.Context;
import it.mapsgroup.gzoom.entity.user.gateway.PermissionViewRepositoryGateway;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;
import it.mapsgroup.gzoom.user.exception.PermissionNotFoundException;

public class PermissionUseCase {
    private final PermissionViewRepositoryGateway permissionRepositoryGateway;

    public PermissionUseCase(PermissionViewRepositoryGateway permissionRepositoryGateway) {
        this.permissionRepositoryGateway = permissionRepositoryGateway;
    }

    public PermissionView getPermission(String userLoginId, Context context) throws PermissionNotFoundException {
        PermissionView result = this.permissionRepositoryGateway.getPermissionView(userLoginId, context);
        if (result == null) {
            throw new PermissionNotFoundException("No permission for username = " + userLoginId + " and context " + context);
        }
        return result;
    }
}
