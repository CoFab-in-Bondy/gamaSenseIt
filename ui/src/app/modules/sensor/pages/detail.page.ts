import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "app-detail",
  template: '<app-sensor-single [id]="id"></app-sensor-single>'
})
export class DetailPage implements OnInit {
  public id: number;
  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }
}
