import { Component, OnInit, ElementRef } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { Router } from '@angular/router';

import { ROUTES } from '../sidebar-user/sidebar-user.component';
@Component({
  selector: 'app-navbar-user',
  templateUrl: './navbar-user.component.html',
  styleUrls: ['./navbar-user.component.css']
})
export class NavbarUserComponent implements OnInit {
/*
  constructor() { }

  ngOnInit() {
  }*/
  public focus;
  public listTitles: any[];
  public location: Location;

  constructor(
    location: Location,
    private element: ElementRef,
    private router: Router)
  {
    this.location = location;
  }

  ngOnInit() {
    this.listTitles = ROUTES.filter(listTitle => listTitle);
  }

  getTitle() {
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if(titlee.charAt(0) === '#'){
        titlee = titlee.slice( 2 );
    }
    for (var item = 0; item < this.listTitles.length; item++) {
      if (this.listTitles[item].path === titlee){
          return this.listTitles[item].title;
      }
    }
    return 'User Dashboard';
  }

}
