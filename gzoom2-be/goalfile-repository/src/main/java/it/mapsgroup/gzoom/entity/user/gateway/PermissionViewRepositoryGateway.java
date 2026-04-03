package it.mapsgroup.gzoom.entity.user.gateway;

import it.mapsgroup.gzoom.entity.goalfile.model.Context;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;

public interface PermissionViewRepositoryGateway {

    public PermissionView getPermissionView(String userLoginId, Context context);

}
