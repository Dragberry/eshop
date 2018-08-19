import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { AuthGuard } from './auth/auth.guard';

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
                    path: 'products',
                    loadChildren: '../products/products.module#ProductsModule',
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
