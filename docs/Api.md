
## API

```
GET /public/parameters?sensorId=<ID>         | List<SensorData>
        [&parameterMetadataId=<ID>]
        [&start=MMddyyyy]
        [&end=MMddyyyy]
        [&type=json]

GET /public/sensors                          | List<Sensor>
GET /public/sensors/<ID>                     | Sensor
GET /public/sensors/names                    | List<Sensor.name>

GET /public/sensors/metadata                 | List<SensorMetadata>
GET /public/sensors/metadata/names           | List<name + " -- " + version>
GET /public/sensors/metadata/<ID>/parameters | List<ParameterMetadata>

GET /public/parameters/metadata              | List<ParameterMetadata>
GET /public/parameters/metadata/<ID>         | ParameterMetadata

GET /public/server/date                      | Current time in EPOCH
GET /public/server/separator                 | DEFAULT_DATA_SEPARATOR

POST /private/sensors                        | add Sensor
PATCH /private/sensors                       | update Sensor
POST /private/sensors/metadata               | add SensorMetadata
POST /private/parameters/metadata            | add ParameterMetadata
```
