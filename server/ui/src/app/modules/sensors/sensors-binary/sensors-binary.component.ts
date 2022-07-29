import {Component, Input, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {BinaryService} from "@services/binary.service";
import {Observable} from "rxjs";
import {saveAs} from "file-saver";
import {ServerService} from "@services/server.service";
import {FormBuilder, FormControl} from '@angular/forms';

export enum State {
  DOWNLOAD, OPEN, WAIT,ERROR, DONE
}

@Component({
  selector: 'app-sensors-binary',
  templateUrl: './sensors-binary.component.html',
  styleUrls: ['./sensors-binary.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SensorsBinaryComponent implements OnInit {

  @Input() id: number;

  state: State = State.WAIT;
  StateEnum = State;


  humidityAndTemperatureSensor = new FormControl('DHT_SENSOR');
  pmSensor = new FormControl('NextPM_SENSOR');
  options = this._formBuilder.group({
    humidityAndTemperatureSensor: this.humidityAndTemperatureSensor,
    pmSensor: this.pmSensor
  });

  constructor(private binaryService: BinaryService, private server: ServerService, private _formBuilder: FormBuilder) { }

  ngOnInit(): void {

  }

  get(): string {
    return this.humidityAndTemperatureSensor.value || 'auto';
  }

  private warpPostToken(): Observable<string> {
    this.state = State.DOWNLOAD;
    return new Observable(o => {
      this.binaryService.postToken(this.id, {
        'TEMP_HUM_SENSOR': this.options.get("humidityAndTemperatureSensor")?.value + "",
        'PM_SENSOR': this.options.get("pmSensor")?.value + ""
      }).subscribe(
        res=>{o.next(res); if (this.state == State.DOWNLOAD) this.state = State.DONE},
          err=>{o.error(err); if (this.state == State.DOWNLOAD) this.state = State.ERROR}
      )
    });
  }

  onCopyKey() {
    if (this.state == State.DOWNLOAD) return;
    this.warpPostToken().subscribe(
      token => {
        this.copyToClipboard(token);
      },
      console.error
    );
  }

  private copyToClipboard(value: string, retry: number = 180) {
    setTimeout(async() => {
      try {
        await navigator.clipboard.writeText(value);
      } catch (err) {
        if (retry == 0) throw err;
        this.copyToClipboard(value, retry - 1);
      }
    }, 2000);
  }


  onLaunch() {
    if (this.state == State.DOWNLOAD) return;
    this.warpPostToken().subscribe(
      token => {
        this.runBinary(token)
      },
      console.error
    )
  }

  private runBinary(token: string, retry: number = 180) {
    setTimeout(async() => {
      try {
        const res = window.open("gmstlauncher:" + token, '_blank');
        if (res == null) {
          this.state = State.ERROR;
        }
      } catch (err) {
        if (retry == 0) throw err;
        this.runBinary(token, retry - 1);
      }
    }, 2000);
  }

  onProgram() {
    saveAs(this.server.getServerUrl() + "/download/gamasenseit-app.jar", "gamasenseit-app.jar")
  }

  onBinary() {
    if (this.state == State.DOWNLOAD) return;
    this.warpPostToken().subscribe(token => {
      this.binaryService.getBinaryFile(token).subscribe(()=>{}, console.error);
    })
  }

  unavailable() {
    return this.state == State.DOWNLOAD;
  }
}
