import { Component, EventEmitter, OnInit, Output } from "@angular/core";
import { Router } from "@angular/router";
import { AccessService } from "@services/access.service";

@Component({
  selector: "app-access-list",
  templateUrl: "./access-list.component.html",
  styleUrls: ["./access-list.component.scss"],
})
export class AccessListComponent implements OnInit {
  accesses: Access[] = [];

  constructor(private accessService: AccessService, private router: Router) {}

  ngOnInit(): void {
    this.onSearch('');
  }

  onSearch(event: Event | string) {
    const query: string =
      event instanceof Event ? (<any>event.target).value || "" : event;
    this.accessService.search({ query }).subscribe((res) => {
      this.accesses = res;
    }, console.error);
  }

  getSize() {
    return window.innerHeight - 170;
  }
}
