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
  lastCaptureDate: string|null
}

declare interface User {
  firstname: string
  lastname: string
  id: number
}

type Merge<A, B> = { [K in keyof (A | B)]: K extends keyof B ? B[K] : A[K] };
type AccessMatchUser = {user: User, present: boolean, privilege: "MANAGE"|"VIEW"};
type AccessMatchSensor = {sensor: Sensor, present: boolean };

type AccessMatch = AccessMatchUser | AccessMatchSensor;
type AccessSearch = AccessMatch[];
type RGB = `rgb(${number},${''|' '}${number},${''|' '}${number})`;


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

declare interface Access {
  createdAt: string
  id: number
  name: string
  privilege: "OWNER"|"MAINTENANCE"|"SOCIAL"
  updatedAt: string
  sizeSensors?: number
  sizeUsers?: number
}
