declare interface Parameter {
  readonly id: number,
  readonly value: number | string,
  readonly captureDate: number,
  readonly sensorId: number,
  readonly sensor: Sensor,
  readonly parameterMetadataId: number,
  readonly parameterMetadata: ParameterMetadata
}


declare interface ParameterMetadata {
  readonly id: number,
  readonly name: string,
  readonly unit: string,
  readonly sensorMetadataId: number,
  readonly sensorMetadata: SensorMetadata,
  readonly parameters: Parameter[],
  readonly dataType: string,
  readonly depreciatedParameter: string,
}


declare interface AuthMe {
  readonly roles: string[],
  readonly name: string,
  readonly auth: boolean
}


declare interface Sensor {
  readonly id: number,
  readonly name: string,
  readonly displayName: string,
  readonly subDisplayName: string,
  readonly longitude: number,
  readonly latitude: number,
  readonly isHidden: boolean,
  readonly hiddenMessage: string,
  readonly sensorMetadataId: number,
}


declare interface SensorMetadata {
  readonly id: number,
  readonly version: string,
  readonly name: string,
  readonly measuredDataOrder: string,
  readonly dataSeparator: string,
  readonly description: string,
  readonly sensors: Sensor[],
  readonly parametersMetadata: ParameterMetadata[]
}

type Mutable<E> = {
  -readonly [K in keyof E]: E[K]
}



declare interface SensorMetadataCyclic extends SensorMetadata{
  sensors: SensorCyclic[],
  parametersMetadata: ParameterMetadataCyclic[]
}

declare interface SensorCyclic extends Sensor {
  sensorMetadata: SensorMetadataCyclic,
}

declare interface ParameterMetadataCyclic extends ParameterMetadata {
  sensorMetadata: SensorMetadataCyclic,
}

declare interface QueryParameters {
  sensorId: number,
  parameterMetadataId?: number,
  start?: Date,
  end?: Date,
  type?: "csv" | "json"
}
