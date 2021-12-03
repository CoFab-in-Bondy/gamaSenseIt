import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  collapsed = true;
  isAuth = false;

  constructor(private auth: AuthService) { }

  async ngOnInit(): Promise<void> {
    this.isAuth = await this.auth.isAuth();
  }
  
  toggleCollapsed(): void {
    this.collapsed = !this.collapsed;
  }

}
