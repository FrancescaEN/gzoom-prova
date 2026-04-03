import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { SecurityGroupsComponent } from './security-groups.component';
import { PermissionComponent } from './permission/permission.component';
import { FunctionsExcludedComponent } from './functions-excluded/functions-excluded.component';
import { securityGroupResolver } from 'app/api/resolver/security-group-resolver.service';
import { securityGroupPermissionResolver } from 'app/api/resolver/security-group-permission-resolver.service';
import { securityGroupContentResolver } from 'app/api/resolver/security-group-content-resolver.service';
import { EnabledUsersComponent } from './enabled-users/enabled-users.component';
import { userLoginSecurityGroupResolver } from 'app/api/resolver/user-login-security-group-resolver.service';
import { OverDetailsSecurityGroupsComponent } from './over-details-security-groups/over-details-security-groups.component';


const routes: Routes = [
    { path: '', component: SecurityGroupsComponent, resolve: { obss: securityGroupResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    {
        path: ':groupId', component: OverDetailsSecurityGroupsComponent, children: [
        { path: 'permission', component: PermissionComponent, resolve: { obss: securityGroupPermissionResolver }, data: { breadcrumb: 'Permission-Group' }, canDeactivate: [canDeactivateUnsavedGuard] },
        { path: 'functions-excluded', component: FunctionsExcludedComponent, resolve: { obss: securityGroupContentResolver }, data: { breadcrumb: 'SecurityGroupPermissionExcept' }, canDeactivate: [canDeactivateUnsavedGuard] },
        { path: 'enabled-users', component: EnabledUsersComponent, resolve: { obss: userLoginSecurityGroupResolver }, data: { breadcrumb: 'Enabled users' }, canDeactivate: [canDeactivateUnsavedGuard] }]
    }


];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SecurityGroupsRoutingModule { }
