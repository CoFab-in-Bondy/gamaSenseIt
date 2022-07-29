import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {SensorService} from "@services/sensor.service";
import {SensorMetadataService} from "@services/sensorMetadata.service";
import {HumanService} from "@services/human.service";
import {ActivatedRoute, Event, Router} from "@angular/router";
import {AuthService} from "@services/auth.service";
import {Subscription} from "rxjs";
import {SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-sensor-image',
  templateUrl: './sensors-image.component.html',
  styleUrls: ['./sensors-image.component.scss']
})
export class SensorsImageComponent implements OnInit {

  private routeSub: Subscription;
  public size: number = 0;
  public urlData: SafeUrl;


  @ViewChild("innerBox")
  private innerBox: ElementRef;

  constructor(
    private formBuilder: FormBuilder,
    private sensorService: SensorService,
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private route: ActivatedRoute,
    public auth: AuthService,
  ) { }

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe((params) => {
      let id = +params["id"];
      this.sensorService.getImage(id).subscribe(url=>{
        this.urlData = url;
        this.onResize();
      }, console.error);
    });
  }

  onResize() {
    const box = this.innerBox?.nativeElement;
    const width = box?.offsetWidth || 0;
    const height = box?.offsetHeight || 0;
    this.size = width < height? width: height;
  }
}
