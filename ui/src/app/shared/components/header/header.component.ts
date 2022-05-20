import {ChangeDetectorRef, Component, HostListener, ViewChild} from '@angular/core';
import {AuthService} from '@services/auth.service';
import {Router} from '@angular/router';
import {MD} from "../../../constantes";
import {StateService} from "@services/state.service";
import {ServerService} from "@services/server.service";

function isLargeScreen() {
  return window.innerWidth >= MD;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  constructor(
    public auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    public state: StateService,
    private server: ServerService
  ) { }

  @ViewChild("mat-drawer-content", {static: true})
  private nav: any;

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.cdr.detectChanges();
    this.state.refreshMargin();
  }

  onHome() {
    document.location.href = this.server.getServerUrl();
  }
}
