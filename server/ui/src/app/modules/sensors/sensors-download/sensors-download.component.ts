import {Component, Input} from '@angular/core';
import {SensorService} from "@services/sensor.service";

@Component({
  selector: 'app-sensors-download',
  templateUrl: './sensors-download.component.html',
  styleUrls: ['./sensors-download.component.scss']
})
export class SensorsDownloadComponent {

  @Input() id: number;

  constructor(private sensorService: SensorService) { }

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
    this.sensorService
      .download({sensorId: this.id, type: type})
      .subscribe(() => {
      }, console.error);

  }

}
