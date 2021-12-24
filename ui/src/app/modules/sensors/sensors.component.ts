import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-sensors',
  templateUrl: './sensors.component.html',
  styleUrls: ['./sensors.component.scss']
})
export class SensorsComponent implements OnInit {
  id: number = NaN;
  state: "map" | "list"= "map";
  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }

  hasId() {
    return !Number.isNaN(this.id);
  }

  onMap() {
    this.state = "map";
  }

  onList() {
    this.state = "list";
  }
}
