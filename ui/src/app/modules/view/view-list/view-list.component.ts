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
import { Subscription } from "rxjs";
import { HumanService } from "@services/human.service";
import { Router } from "@angular/router";
import { Icon } from "@models/icon.model";

@Component({
  selector: "app-view-list",
  templateUrl: "./view-list.component.html",
  styleUrls: ["./view-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ViewListComponent implements OnInit, OnDestroy {
  private sensorsSubscription: Subscription;
  sensors: any = [];
  links: (number | string)[][] = [];
  GREEN_ICON = new Icon("assets/lights/light-green.svg", 24, 24);
  ORANGE_ICON = new Icon("assets/lights/light-orange.svg", 24, 24);
  RED_ICON = new Icon("assets/lights/light-red.svg", 24, 24);

  @Output() selected = new EventEmitter<number>();

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private router: Router,
    private ref: ChangeDetectorRef
  ) {


  }

  ngOnInit(): void {
    this.sensorsSubscription = this.sensorMetadataService
      .observeAll()
      .subscribe(
        (smds) => {
          const sensors: [Sensor, SensorMetadataExtended][] = [];
          for (const smd of smds)
            for (const s of smd.sensors)
              sensors.push([s, smd]);
          sensors.sort(([s1, smd1], [s2, smd2]) => {
            const d1 = Date.parse((<any>s1).lastCaptureDate), d2 = Date.parse(<any>(s2).lastCaptureDate);
            return isNaN(d2)
              ? (isNaN(d1)? 0: -1)
              : (isNaN(d1)? 1: d2 - d1)
          });
          this.sensors = [];
          this.links = [];
          for (const [s, smd] of sensors) {
            this.links.push(["/view", s.id]);
            this.sensors.push([
              s.displayName,
              `${smd.name} (${smd.version})`,
              this.humanService.coordsToHumain(s.latitude, s.longitude),
              this.state(s),
            ]);
          }
          this.ref.detectChanges();
        },
        (err) => console.error(err)
      );
    this.sensorMetadataService.lazyLoad();
  }

  ngOnDestroy(): void {
    this.sensorsSubscription?.unsubscribe();
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
