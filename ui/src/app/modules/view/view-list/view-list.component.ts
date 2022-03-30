import { SensorMetadataService } from "@services/sensorMetadata.service";
import {
  Component,
  OnDestroy,
  OnInit,
  Output,
  EventEmitter,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
} from "@angular/core";
import { HumanService } from "@services/human.service";

@Component({
  selector: "app-view-list",
  templateUrl: "./view-list.component.html",
  styleUrls: ["./view-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ViewListComponent implements OnInit, OnDestroy {
  sensors: [Sensor, SensorMetadataExtended][] = [];
  links: (number | string)[][] = [];
  GREEN_ICON: Icon = {"url": "assets/lights/light-green.svg", width: 24, height: 24};
  ORANGE_ICON: Icon = {"url": "assets/lights/light-orange.svg", width: 24, height: 24};
  RED_ICON: Icon = {"url": "assets/lights/light-red.svg", width: 24, height: 24};

  @Output() select = new EventEmitter<number>();

  linker: DTLinker<[Sensor, SensorMetadataExtended]> = ([s, smd]) => ["/view", s.id];
  formater: DTFormatter<[Sensor, SensorMetadataExtended]> = ([s, smd]) => [
      s.displayName,
      `${smd.name} (${smd.version})`,
      this.humanService.coordsToHumain(s.latitude, s.longitude),
      this.state(s),
  ];

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private ref: ChangeDetectorRef
  ) {

  }

  ngOnInit(): void {
    this.sensorMetadataService.getAll().subscribe(
        (smds) => {
          this.sensors = [];
          for (const smd of smds)
            for (const s of smd.sensors)
            this.sensors.push([s, smd]);
          this.sensors.sort(([s1, smd1], [s2, smd2]) => {
            const d1 = Date.parse((<any>s1).lastCaptureDate), d2 = Date.parse(<any>(s2).lastCaptureDate);
            return isNaN(d2)
              ? (isNaN(d1)? 0: -1)
              : (isNaN(d1)? 1: d2 - d1)
          });
          this.ref.detectChanges();
        },
        (err) => console.error(err)
      );
  }

  ngOnDestroy(): void {

  }

  state(s: Sensor): Icon {
    let now = new Date().getTime();
    if (s.lastCaptureDate == null)
      return this.RED_ICON;
    else {
      let date = new Date(s.lastCaptureDate).getTime();
      if (date > now - 60 * 60 * 1000) {
        return this.GREEN_ICON;
      } else if (date > now - 24 * 60 * 60 * 1000) {
        return this.ORANGE_ICON;
      } else {
        return this.RED_ICON
      }
    }
  }

  value(i: Icon) {
    switch (i) {
      case this.GREEN_ICON: return 0;
      case this.ORANGE_ICON: return 1
      case this.RED_ICON: return 2;
      default: return 3;
    }
  }
}
