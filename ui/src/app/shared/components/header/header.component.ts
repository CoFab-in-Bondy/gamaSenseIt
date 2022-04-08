import {ChangeDetectorRef, Component, HostListener, ViewChild} from '@angular/core';
import { AuthService } from '@services/auth.service';
import { Router } from '@angular/router';
import {MD} from "../../../constantes";
import {MatDrawer} from "@angular/material/sidenav";

function isLargeScreen() {
  return window.innerWidth >= MD;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  private openned: boolean = isLargeScreen();

  @ViewChild(MatDrawer)
  private nav: MatDrawer;

  constructor(public auth: AuthService, private router: Router, private cdr: ChangeDetectorRef) { }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.cdr.detectChanges();
  }

  isOpen() {
    return this.openned && isLargeScreen();
  }

  swapOpen() {
    if (isLargeScreen()) {
      this.openned = !this.openned;
    }
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
