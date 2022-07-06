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
type Roles = "ADMIN" | "USER" | "SYSTEM";
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


declare interface Sensor<Extended extends boolean> {
  id: number,
  name: string,
  indications: string,
  longitude: number,
  latitude: number,
  isHidden: boolean,
  hiddenMessage: string,
  sensorMetadataId: number,
  lastCaptureDate: string|null,
  description: string,
  maintenanceDescription: string,
  manageable: boolean,
  metadata: Extended extends true ? SensorMetadata<false> : undefined
}

declare interface User {
  firstname: string
  lastname: string
  id: number
}

type Merge<A, B> = { [K in keyof (A | B)]: K extends keyof B ? B[K] : A[K] };
type AccessMatchUser = {user: User, present: boolean, privilege: "MANAGE"|"VIEW"};
type AccessMatchSensor = {sensor: Sensor<false>, present: boolean };

type AccessMatch = AccessMatchUser | AccessMatchSensor;
type AccessSearch = AccessMatch[];
type RGB = `rgb(${number},${''|' '}${number},${''|' '}${number})`;


declare interface SensorMetadata<Extended extends boolean> {
  id: number,
  version: string,
  name: string,
  description: string,
  // sensors: Sensor[],
  parameters:  {
    headers : string[]
    ids : number[],
    units : string[],
    formats : ("LONG"|"DOUBLE"|"STRING"|"DATE")[],
    width : number
  }
  sensors: Extended extends true? Sensor<false>[]: undefined;
}

type BypassSecurityOptions = {
  html: SafeHtml,
	style: SafeStyle,
	script: SafeScript,
	url: SafeUrl,
	resourceUrl: SafeResourceUrl
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

declare interface QueryParams {
  sensorId: number,
  parameterMetadataId?: number,
  start?: DDMMYYYY,
  end?: DDMMYYYY,
  type?: "csv" | "json" | "xlsx"
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
