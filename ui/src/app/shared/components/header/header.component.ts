import { Component } from '@angular/core';
import { AuthService } from '@services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  collapsed = true;

  constructor(public auth: AuthService, private router: Router) { }

  toggleCollapsed(): void {
    this.collapsed = !this.collapsed;
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
