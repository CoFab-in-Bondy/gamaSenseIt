type SafeHtml = import('@angular/platform-browser').SafeHtml;
type SafeResourceUrl = import('@angular/platform-browser').SafeResourceUrl;
type SafeScript = import('@angular/platform-browser').SafeScript;
type SafeStyle = import('@angular/platform-browser').SafeStyle;
type SafeUrl = import('@angular/platform-browser').SafeUrl;


type N = 0|1|2|3|4|5|6|7|8|9;
type DD = (
  "01" | "02" | "03" | "04" | "05" | "06" | "07" | "08" | "09" | "10" |
  "11" | "12" | "13" | "14" | "15" | "16" | "17" | "18" | "19" | "20" |
  "21" | "22" | "23" | "24" | "25" | "26" | "27" | "28" | "29" | "30" |
  "31"
);
type MM = (
  "01"| "02" | "03" | "04" | "05" | "06" | "07" | "08" | "09" | "10" | "11" | "12"
)
type YYYY = `${20|21}${N}${N}`
type DDMMYYYY = `${DD}${MM}${YYYY}`;

declare interface ErrorValue {
  title: string;
  message: string;
  status: number|null;
}

declare interface Parameter {
  id: number,
  value: number | string,
  captureDate: number,
  sensorId: number,
  // sensor: Sensor,
  parameterMetadataId: number,
  // parameterMetadata: ParameterMetadata
}

declare interface Data {
  name: string,
  value: (number|string)[]
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
  description: string,
  // sensors: Sensor[],
  parameters:  {
    headers : string[]
    ids : number[],
    units : string[],
    formats : ("INTEGER"|"DOUBLE"|"STRING"|"DATE")[],
    width : number
  }
}

type BypassSecurityOptions = {
  html: SafeHtml,
	style: SafeStyle,
	script: SafeScript,
	url: SafeUrl,
	resourceUrl: SafeResourceUrl
}

declare interface SensorExtended {
  id: number,
  name: string,
  displayName: string,
  subDisplayName: string,
  latitude: number,
  longitude: number,
  lastCaptureDate: number,
  hiddenMessage: string,
  isHidden: boolean,
  description: string,
  manageable: boolean;
  maintenanceDescription?: string
  metadata: SensorMetadata
}

declare interface Icon {
  url: string,
  width: number,
  height: number
}

declare type DTValue = string|number|Icon;
declare type DTFormatter<D> = (d: D) => DTValue[];
declare type DTLinker<D> = (d: D) => (string|number|null)[];


declare interface RecordParameters {
  values : (string|number)[][],
  total : number,
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
  start?: DDMMYYYY,
  end?: DDMMYYYY,
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

declare interface DataTableNatigateEvent {
  sort?: number
  asc?: boolean
  page?: number
  count?: number
}


type Pos = {
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
