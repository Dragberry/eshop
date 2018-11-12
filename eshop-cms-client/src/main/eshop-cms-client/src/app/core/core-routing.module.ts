import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { AuthGuard } from './auth/auth.guard';
import { DashboardComponent } from './main/dashboard/dashboard.component';

@NgModule({
    imports: [RouterModule.forRoot([
        {
            path: 'login',
            component: LoginComponent
        },
        {
            path: '',
            component: MainComponent,
            canActivate: [AuthGuard],
            children: [
                {
                  path: '',
                  component: DashboardComponent
                },
                {
                    path: 'catalog',
                    loadChildren: '../catalog/catalog.module#CatalogModule',
                    canActivate: [AuthGuard]
                },
                {
                    path: 'orders',
                    loadChildren: '../orders/orders.module#OrdersModule',
                    canActivate: [AuthGuard]
                }
            ]
        },
        {
            path: '**',
            redirectTo: ''
        }
    ])],
    exports: [RouterModule]
})
export class CoreRoutingModule { }
