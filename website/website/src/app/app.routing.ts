import { NgModule } from '@angular/core';
import { CommonModule, } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { PMLayoutComponent } from './layouts/pm-layout/pm-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';

const routes: Routes = [
  {
    path: 'qameleo',
    component: PMLayoutComponent,
    children: [
      {
        path: 'qameleo',
        loadChildren: './layouts/pm-layout/pm-layout.module'
      }
    ]
  },
  {
    path: 'qameleo/:id',
    component: PMLayoutComponent,
    children: [
      {
        path: 'qameleo/:id',
        loadChildren: './layouts/pm-layout/pm-layout.module'
      }
    ]
  },

  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  }/*
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  }*/
  ,
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: './layouts/admin-layout/admin-layout.module#AdminLayoutModule'
      }
    ]
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: './layouts/auth-layout/auth-layout.module#AuthLayoutModule'
      }
    ]
  },
  {
    path: '',
    component: UserLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: './layouts/user-layout/user-layout.module#UserLayoutModule'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  },

  /*{
    path: 'user',
    redirectTo: 'user'
  }*/

];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
  ],
})
export class AppRoutingModule { }
