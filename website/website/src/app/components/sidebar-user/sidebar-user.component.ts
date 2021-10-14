import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

declare interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
}
export const ROUTES: RouteInfo[] = [
  { path: '/userDashboard',
    title: 'Dashboard',
    icon: 'fas fa-desktop text-primary',
    class: ''
  },
  { path: '/qrcode1',
    title: 'Scan QR codes',
    icon: 'fas fa-qrcode text-info',
    class: ''
  },
  { path: '/userMaps',
    title: "Sensor's Maps",
    icon: 'fas fa-map-marker-alt text-success',
    class: ''
  },
  { path: '/login',
    title: 'Login',
    icon: 'fas fa-user-lock text-primary',
    class: ''
  }
];

@Component({
  selector: 'app-sidebar-user',
  templateUrl: './sidebar-user.component.html',
  styleUrls: ['./sidebar-user.component.css']
})
export class SidebarUserComponent implements OnInit {

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

