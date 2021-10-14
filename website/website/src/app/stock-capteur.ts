import { SensorVersion } from './SensorVersion';
import { MesuredParameter } from './MesuredParameter';
import { DataFormat, DataParameter } from './Enums'

// export  const metatData1: MesuredParameter[] = [
//     {varName:"p1", id:1,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p2", id:1,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p3", id:1,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p4", id:1,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p5", id:1,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p6", id:1,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"}
// ];
//
// export  const metatData2: MesuredParameter[] = [
//     {varName:"p1", id:2,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p2", id:2,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p3", id:2,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p4", id:2,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p5", id:2,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"},
//     {varName:"p6", id:2,parameter : DataParameter.PRESSURE, dataFormat:DataFormat.INTEGER,unit:"cb"}
// ];

export const StockCapteur: SensorVersion = {
  name: 'paris',
  version: "V1",
  idType:1,
  measuredDataOrder:"",
  dataSeparator:':',
  description: "this is the description"
};

export const StockCapteurArr: SensorVersion[] = [
  StockCapteur,
  { name: 'vegas',
    version: "V1",
    idType:2,
    measuredDataOrder:"",
    dataSeparator:':',
    description:"the description"
  },
  { name: 'Dakar',
    version: "V3",
    idType:3,
    measuredDataOrder:"",
    dataSeparator:':',
    description:"the description"
  },
  { name: 'Sanar',
    version: "V2",
    idType:4,
    measuredDataOrder:"",
    dataSeparator:':',
    description:"the description"
  },
  { name: 'Bondy',
    version: "V1",
    idType:5,
    measuredDataOrder:"",
    dataSeparator:':',
    description:"the description"
  }
];
//{nom: 'cam1', version: 1, longitude: 23, latitude: 52},
//{nom: 'cam2', version: 1, longitude: 22, latitude: 54}
