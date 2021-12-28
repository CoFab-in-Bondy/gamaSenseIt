import { SensorMetadataService } from "../../services/sensorMetadata.service";
import {
  Component,
  OnDestroy,
  OnInit,
  Output,
  EventEmitter,
  ElementRef,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
} from "@angular/core";
import { Subscription } from "rxjs";
import { HumanService } from "../../services/human.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-sensor-list",
  templateUrl: "./sensor-list.component.html",
  styleUrls: ["./sensor-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SensorListComponent implements OnInit, OnDestroy {
  private sensorsSubscription: Subscription;
  sensors: any[][] = [];
  links: string[][] = [];

  @Output() selected = new EventEmitter<number>();

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private router: Router,
    private ref: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.sensorsSubscription = this.sensorMetadataService
      .observeAll()
      .subscribe(
        (smds) => {
          this.sensors = [];
          this.links = [];
          for (let smd of smds) {
            for (let s of smd.sensors) {
              this.links.push(["/sensors", "" + s.id]);
              this.sensors.push([
                smd.version,
                smd.name,
                s.name,
                this.humanService.coordsToHumain(s.latitude, s.longitude),
                this.state(s),
              ]);
            }
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

  state(s: Sensor): string {
    let now = new Date().getTime();
    let date = new Date(s.lastCaptureDate).getTime();
    if (date > now - 60 * 60 * 1000) {
      return "Moins d'un heure";
    } else if (date > now - 24 * 60 * 60 * 1000) {
      return "Moins d'un jour";
    } else {
      return "Plus d'un jour";
    }
  }
}
