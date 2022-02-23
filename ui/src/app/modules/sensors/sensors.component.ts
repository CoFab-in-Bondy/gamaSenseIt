import { ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SensorMapComponent } from 'src/app/shared/components/sensor-map/sensor-map.component';

@Component({
  selector: 'app-sensors',
  templateUrl: './sensors.component.html',
  styleUrls: ['./sensors.component.scss']
})
export class SensorsComponent implements OnInit {
  id: number = NaN;
  state: "map" | "list" = "map";
  width: number;

  constructor(private route: ActivatedRoute, private d: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }

  @ViewChild(SensorMapComponent) map: SensorMapComponent;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
      this.width = window.innerWidth;
  }

  hasId() {
    return !Number.isNaN(this.id);
  }

  onMap() {
    this.state = "map";

    // map crash if use list than map
    this.d.detectChanges();
  }

  onList() {
    this.state = "list";
  }
}
