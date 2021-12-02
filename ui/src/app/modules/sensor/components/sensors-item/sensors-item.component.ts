import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: "app-sensors-item",
  templateUrl: "./sensors-item.component.html",
  styleUrls: ["./sensors-item.component.scss"],
})
export class SensorsItemComponent implements OnInit {
  @Input() displayName: string;
  @Input() id: number;
  @Input() latitule: number;
  @Input() longitude: number;
  @Input() sensorMetadataName: string;

  constructor() {}

  ngOnInit(): void {}
}
