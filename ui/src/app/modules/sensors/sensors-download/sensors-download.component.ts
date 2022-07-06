import {Component, Input, OnInit} from '@angular/core';
import {SensorService} from "@services/sensor.service";

@Component({
  selector: 'app-sensors-download',
  templateUrl: './sensors-download.component.html',
  styleUrls: ['./sensors-download.component.scss']
})
export class SensorsDownloadComponent implements OnInit {

  @Input() id: number | undefined;
  @Input() editable: boolean;

  constructor(private sensorService: SensorService) { }

  ngOnInit(): void {

  }

  /**
   * Run download for csv file.
   */
  onDownloadCSV(): void {
    this.onDownload("csv");
  }

  /**
   * Run download for csv file.
   */
  onDownloadXSLX(): void {
    this.onDownload("xlsx");
  }

  /**
   * Run download for json file.
   */
  onDownloadJSON(): void {
    this.onDownload("json");
  }

  onDownload(type: QueryParams["type"]): void {
    if (this.id == undefined) return;
    this.sensorService
      .download({sensorId: this.id, type: type})
      .subscribe(() => {
      }, console.error);

  }

  /**
   * Run download for binary file.
   */
  onDownloadBinary(): void {
    if (this.id == undefined) return;
    this.sensorService
      .binary(this.id)
      .subscribe(() => {
      }, console.error);
  }

}
