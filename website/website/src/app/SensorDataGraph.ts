import { string } from "prop-types";

export class SensorDataGraph {
    name: string;
    PM10: any[];
    PM2_5: any[];
    PM1: any[];
    CO2: any[];
    TEMP: any[];
    HUM: any[];
    PRESS: any[];

    constructor(nom: string ) {
        this.name = nom;
        this.PM1 = [];
        this.PM10 = [];
        this.PM2_5 = [];
        this.CO2 = [] ;
        this.HUM = [];
        this.PRESS = [];
        this.TEMP = [];

    }

    addMesure(type: string, val: number) {
        switch (type) {
            case 'TEMPERATURE':
                this.TEMP.push(val);
                break;
            case 'PM1':
                this.PM1.push(val);
                break;
            case 'PM2_5':
                this.PM2_5.push(val);
                break;
            case 'PM10':
                this.PM10.push(val);
                break;
            case 'PRESSION':
                this.PRESS.push(val);
                break;
            case 'HUMIDITY':
                this.HUM.push(val);
                break;
            case 'CO2':
                this.CO2.push(val);
                break;
            default:
                console.log('param non prise en compte');
                break;
        }
    }
}