declare interface Parameter {
  id: number,
  value: number | string,
  captureDate: number,
  sensorId: number,
  // sensor: Sensor,
  parameterMetadataId: number,
  // parameterMetadata: ParameterMetadata
}


declare interface ParameterMetadata {
  id: number,
  name: string,
  unit: string,
  sensorMetadataId: number,
  // sensorMetadata: SensorMetadata,
  // parameters: Parameter[],
  dataType: string,
  depreciatedParameter: string,
}

declare interface PartialSensor {
  sensorMetadataId: number,
  name: string,
  displayName: string,
  subDisplayName: string,
  longitude: number,
  latitude: number,
  hiddenMessage?: string,
  isHidden?: boolean
}


declare interface AuthMe {
  roles: string[],
  name: string,
  auth: boolean
}


declare interface Sensor {
  id: number,
  name: string,
  displayName: string,
  subDisplayName: string,
  longitude: number,
  latitude: number,
  isHidden: boolean,
  hiddenMessage: string,
  sensorMetadataId: number,
  lastCaptureDate: number
}


declare interface SensorMetadata {
  id: number,
  version: string,
  name: string,
  measuredDataOrder: string,
  dataSeparator: string,
  description: string,
  // sensors: Sensor[],
  // parametersMetadata: ParameterMetadata[]
}

declare interface SensorExtended {
  id: number,
  name: string,
  displayName: string,
  subDisplayName: string,
  latitude: number,
  longitude: number,
  lastCaptureDate: number,
  metadata: {
    id: number,
    description: string,
    name: string,
    version: string,
  },
  parameters: {
    metadata: {
      headers : string[]
      ids : number[],
      units : string[],
      formats : ("INTEGER"|"DOUBLE"|"STRING"|"DATE")[],
      width : number
    },
    values : (string|number)[][],
    total : number,
  }
}

declare interface SensorMetadataExtended extends SensorMetadata {
  sensors: Sensor[],
  parametersMetadata: ParameterMetadata[]
}


declare interface Params {
  [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
}

declare interface QueryParams {
  sensorId: number,
  parameterMetadataId?: number,
  start?: Date,
  end?: Date,
  type?: "csv" | "json"
}

declare interface ParamsOption {
  start?: Date
  end?: Date
  sort?: number
  asc?: boolean
  page?: number
  count?: number
}

declare interface DataTableEvent {
  sort?: number
  asc?: boolean
  page?: number
  count?: number
}

declare interface Pos {
  lat: number,
  lng: number
}
