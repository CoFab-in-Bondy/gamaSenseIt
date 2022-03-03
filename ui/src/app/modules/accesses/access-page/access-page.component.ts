import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-access-page',
  templateUrl: './access-page.component.html',
  styleUrls: ['./access-page.component.scss']
})
export class AccessPageComponent implements OnInit {

  routerActive = false;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  onActivate(event : any) {
    this.routerActive = true;
  }

  onDeactivate(event : any) {
    this.routerActive = false;
  }

}
