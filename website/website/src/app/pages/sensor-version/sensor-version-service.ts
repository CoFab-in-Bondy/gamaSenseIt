// list des sensors available for read and update
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_URLS } from '../../config/api.url.config';
import { MesuredParameter } from '../../MesuredParameter';
import { Sensor } from '../../Sensor';
import { SensorVersion } from '../../SensorVersion';
import { SensorData } from '../../sensorData';

@Injectable()
export class SensorVersionService {

  constructor(private http: HttpClient) { }

  getData(): Observable<any> {
      return this.http.get(API_URLS.SENSOR_DATA);
  }

  loadData(): SensorData[] {
    let sensorData: SensorData[] = [];
    this.getData().subscribe(
      (data: SensorData[]) => {
         for (const item of data ) {
          sensorData.push(JSON.parse(JSON.stringify(item)));
        }
      },
      error => {
        console.log('error was occured in load Data');
      }
    );
    return sensorData;
  }

  getSensorById(sensorId): Observable<any> {
    let link = API_URLS.SENSOR;
    link += "?sensorId=" + sensorId;
    return this.http.get(link);
  }

  loadSensorId(sensorId: number): Sensor[] {
    let sensor: Sensor[] = [];
    this.getSensorById(sensorId).subscribe(
      (data: Sensor) => {
        sensor.push(data);
      },
      error => {
        console.log('error was occured in loadSensorId');
      }
    );
    return sensor;
  }

  getSensors(): Observable<any> {
      return this.http.get(API_URLS.SENSORS);
  }

  loadSensors(): Sensor[] {
    let sensors: Sensor[] = [];
    this.getSensors().subscribe(
      (data: Sensor[]) => {
        for (let sensor of data) {
          sensors.push(JSON.parse(JSON.stringify(sensor)));
        }
      },
      error => {
        console.log('error was occured in loadSensors');
      }
    );
    return sensors;
  }

  getSensorsNames(): Observable<any> {
      return this.http.get(API_URLS.SENSORS_NAMES);
  }

  getSensorTypeNames(): Observable<any> {
      return this.http.get(API_URLS.SENSOR_META_DATA_FULLNAMES);
  }

  getSensorTypes(): Observable<any> {
      return this.http.get(API_URLS.SENSOR_META_DATA);
  }

  loadSensorTypes(): SensorVersion[] {
    let sensorTypes: SensorVersion[] = [];
    this.getSensorTypes().subscribe(
      (data: SensorVersion[]) => {
        for (let sensor of data) {
          sensorTypes.push(JSON.parse(JSON.stringify(sensor)));
        }
      },
      error => {
        console.log('error was occured in loadSensorTypes');
      }
    );
    return sensorTypes;
  }

  getSensorMetadataParameters(metadataId: number): Observable<any> {
    let link = API_URLS.META_DATA_SENSOR_META_DATA_ID;
    link += "?metadataId=" + metadataId;
    return this.http.get(link);
  }

  loadSensorMetadataParameters(metadataId: number): MesuredParameter[] {
    let metadata: MesuredParameter[] = [];
    this.getSensorMetadataParameters(metadataId).subscribe(
      (data: MesuredParameter[]) => {
        for (let parameter of data) {
          metadata.push(parameter);
        }
      },
      error => {
        console.log('error was occured in loadSensorMetadataParameters');
      }
    );
    return metadata;
  }

  getMetadata(): Observable<any> {
    return this.http.get(API_URLS.META_DATA);
  }

  loadMetadata(): MesuredParameter[] {
    let metadata: MesuredParameter[] = [];
    this.getMetadata().subscribe(
      (data: MesuredParameter[]) => {
        for(let parameter of data) {
          metadata.push(JSON.parse(JSON.stringify(parameter)));
        }
      },
      error => {
        console.log('error was occured in loadMetadata');
      }
    );
    return metadata;
  }

  getMetadataByParameterId(parameterId: number): Observable<any> {
    let link = API_URLS.META_DATA_ID;
    link += "?parameterId=" + parameterId;
    return this.http.get(link);
  }

  loadMetadataByParameterId(parameterId: number): MesuredParameter[] {
    let metadata: MesuredParameter[] = [];
    this.getMetadataByParameterId(parameterId).subscribe(
      (data: MesuredParameter) => {
        metadata.push(data)
      },
      error => {
        console.log('error was occured in loadMetadataId');
      }
    );
    return metadata;
  }

  // TODO
  getSensorsBetween(): Observable<any> {
      return undefined;
  }

  // TODO
  getSince(): Observable<any> {
    return undefined;
  }

  getDefaultDataSeparator(): Observable<any> {
    return this.http.get(API_URLS.DEFAULT_DATA_SEPARATOR,
      {responseType: 'text'});
  }

  initSensor(): Sensor {
    let displaySensor: Sensor = new Sensor(0,'', '','',0,0,0,'');
    return displaySensor;
  }

  initSensorType(): SensorVersion {
    let displaySensorType: SensorVersion = new SensorVersion(0, '', '', '', '','');
    return displaySensorType;
  }

  // TODO

  // updateSensor(s: Sensor): Observable<any> {
  //   var link = API_URLS.UPDATE_SENSOR;
  //   link += '?sensorId=' s.idSensor;
  //   console.log('TEST : updataSensor :',link);
  //   return this.http.put(link,null);
  // }

  deleteSensor(idSensor: number): void { // Observable<any>
    console.log('TEST : deleteSensor');
    // return this.http.delete(url).subscribe();
  }

  // updateSensorType(sm: SensorVersion): Observable<any> {
  //   var link = API_URLS.UPDATE_SENSOR_METADATA;
  //   link += '?metadataId=' s.idType;
  //   console.log('TEST : updataSensorMetadata :',link);
  //   return this.http.put(link,null);
  // }

  deleteSensorMetadata(idType: number): void { // Observable<any>
    console.log('TEST : deleteSensorMetadata');
    // return this.http.delete(url).subscribe();
  }

}
