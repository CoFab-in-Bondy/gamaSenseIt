import {DataFormat} from './Enums';

export class MesuredParameter {
  id: number;
  varName: string;
  unit: string;
  dataFormat: DataFormat;
  parameter: string;
  sensorMetadata: number
  icon: string;

  constructor(
    id: number,
    varName: string,
    unit : string,
    dataFormat: DataFormat,
    parameter: string,
    sensorMetadata: number,
    icon: string)
  {
    this.id = id;
    this.varName = varName;
    this.unit = unit;
    this.dataFormat = dataFormat;
    this.parameter = parameter;
    this.sensorMetadata = sensorMetadata;
    this.icon = icon;
  }
}
