import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: "app-sensor-item",
  templateUrl: "./sensor-item.component.html",
  styleUrls: ["./sensor-item.component.scss"],
})
export class SensorItemComponent implements OnInit {
  @Input() displayName: string;
  @Input() id: number;
  @Input() latitule: number;
  @Input() longitude: number;
  @Input() sensorMetadataName: string;

  constructor() {}

  ngOnInit(): void {}
}
