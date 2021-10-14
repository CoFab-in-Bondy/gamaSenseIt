import { MesuredParameter } from "./MesuredParameter";
import { version } from 'punycode';

export class SensorVersion {
  idType: number;
  name: string;
  version: string;
  dataSeparator: string;
  measuredDataOrder: string;
  description: string;
  // parameterMetadata : MesuredParameter[];

  constructor(
    idType: number,
    name: string,
    version: string,
    dataSeparator: string,
    measuredDataOrder: string,
    description: string)
  {
    this.idType = idType;
    this.version = version;
    this.name = name;
    this.dataSeparator = dataSeparator;
    this.measuredDataOrder = measuredDataOrder;
    this.description = description;
    // this.parameterMetadata = mp;
    // this.parameterMetadata.forEach(function (value) {
    //   this.parameterMetadata = this.measuredDataOrder + value.id+this.dataSeparator;
    // })
  }
}
