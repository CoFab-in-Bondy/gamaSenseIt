export const enum DataParameter {
  TEMPERATURE = 0,
  CO2 = 1,
  PM10 = 2,
  PM2_5 = 3,
  PM1 = 4,
  PRESSURE = 5,
  HUMIDITY = 6
};

export enum DataFormat {
  INTEGER = 0,
  DOUBLE = 1,
  STRING = 2
};

export function dataFormatToString(val:DataParameter): string {
  switch (val) {
    case 0: {return 'INTEGER'}
    case 1: {return 'DOUBLE'}
    case 2: {return 'STRING'}
  }
}
