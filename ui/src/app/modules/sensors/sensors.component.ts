import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-sensors',
  templateUrl: './sensors.component.html',
  styleUrls: ['./sensors.component.scss']
})
export class SensorsComponent implements OnInit {
  id: number = NaN;
  state: "map" | "list" = "map";
  width: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
      this.width = window.innerWidth;
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
