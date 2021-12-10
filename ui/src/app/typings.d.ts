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
  metadata: {
    id: number,
    description: string,
    name: string,
    version: string,
  },
  parameters: {
    metadata: {
      headers : string[]
      ids : string[],
      units : string[]
    },
    values : (string|number)[][]
  }
}

declare interface SensorMetadataExtended extends SensorMetadata {
  sensors: Sensor[],
  parametersMetadata: ParameterMetadata[]
}

declare interface QueryParameters {
  sensorId: number,
  parameterMetadataId?: number,
  start?: Date,
  end?: Date,
  type?: "csv" | "json"
}


