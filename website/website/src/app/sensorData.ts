export class SensorData {

    value: number;
    date: string;
    unit: string;
    sensorName: string;
    latitude: number;
    longitude: number;
    measuredParameter: string;

    constructor(
        value: number,
        date: string,
        unit: string,
        sensorName: string,
        latitude: number,
        longitude: number,
        measuredParameter: string) {

        this.value = value;
        this.date = date;
        this.unit = unit;
        this.sensorName = sensorName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.measuredParameter = measuredParameter;

    }
}