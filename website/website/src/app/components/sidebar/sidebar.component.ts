import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

declare interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
}
export const ROUTES: RouteInfo[] = [
  { path: '/dashboard',
    title: 'Dashboard',
    icon: 'fas fa-desktop text-primary',
    class: ''
  },
  { path: '/qrcode',
    title: 'Scan QR codes',
    icon: 'fas fa-qrcode text-info',
    class: ''
  },
  { path: '/maps',
    title: "Sensor's Maps",
    icon:'fas fa-map-marker-alt text-success',
    class: ''
  },
  { path: '/user-profile',
    title: 'User profile',
    icon:'fas fa-user-alt text-dark',
    class: ''
  },/*
  { path: '/login',
    title: 'Login',
    icon:'fas fa-user-lock text-primary',
    class: ''
  },*/
  { path: '/register',
    title: 'Register',
    icon:'fas fa-user-plus text-danger',
    class: ''
  }
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  public menuItems: any[];
  public isCollapsed = true;

  constructor(private router: Router) { }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
    this.router.events.subscribe(
      (event) => {
        this.isCollapsed = true;
      }
    );
  }
}
