import { Component, OnInit, Input } from '@angular/core';
import { Observable, from } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import Chart from 'chart.js';

import { SensorVersion } from '../../SensorVersion';
import { StockCapteurArr } from '../../stock-capteur';
import { FormControl, CheckboxControlValueAccessor, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SensorVersionService } from '../sensor-version/sensor-version-service';
import { Sensor } from '../../Sensor';
import { SensorDataGraph } from '../../SensorDataGraph';
import { MapsComponent } from '../maps/maps.component';
import { SensorData } from '../../sensorData';


@Component({
  selector: 'app-diagramme',
  templateUrl: './diagramme.component.html',
  styleUrls: ['./diagramme.component.scss']
})
export class DiagrammeComponent implements OnInit {
  sensorsData: SensorData[];


  constructor(private sensorService: SensorVersionService, private fb: FormBuilder, /*private mapSensor: MapsComponent*/) {
    this.SearchCapteurForm = this.fb.group({
      name: ['', Validators.required],
    });
  }

  @Input() typeGraph = '';
  typeData = 'TEMP';

  sensorTypes: SensorVersion[];
  sensors: Sensor[];
  sensorChoosed = [];
  // sensorMap: Sensor[];
  //  sensors: SensorVersion[] = StockCapteurArr;
  sensorsCheck; myLine; dataUpdate;
  myBar; myPie;
  element; canvas;
  myControl: FormControl = new FormControl();
  filteredOptions: Observable<any>;
  searchText: any = '';
  displaySensor; j = 0; k = 0; l = 0;
  dataMesure: [68, 80, 32, 15, 50, 100, 20];
  checkValue = [];
  SearchCapteurForm: FormGroup;
  ctx: HTMLElement;
  sensorGraph: SensorDataGraph[] = [];

  selection = [];/*
  monthsLabel: ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
  daysLabel: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];*/

  // tslint:disable-next-line: only-arrow-functions
  randomScalingFactor = function () {
    return Math.round(Math.random() * 100);
  };

  // tslint:disable-next-line: member-ordering
  datasetSensor = [];

  // tslint:disable-next-line: member-ordering
  datasetSensorB = [];

  /*Chart JS */

  // tslint:disable-next-line: member-ordering
  config = {
    type: 'line',
    data: {
      labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15'],
      datasets: this.datasetSensor,
    },
    options: {
      legend: {
        display: true
      },
      responsive: true,
      maintainAspectRatio: false,
      title: {
        display: true,
        text: ' Line Chart'
      },
      tooltips: {
        mode: 'index',
        intersect: false,
      },
      hover: {
        mode: 'nearest',
        intersect: true
      },
      scales: {
        xAxes: [{
          display: true,
          scaleLabel: {
            display: true,
            labelString: '15 last measurements'
          }
        }],
        yAxes: [{
          display: true,
          scaleLabel: {
            display: true,
            labelString: 'Value'
          }
        }]
      }
    }
  };

  // tslint:disable-next-line: member-ordering
  configbar = {
    type: 'bar',
    data: {
      labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15'],
      datasets: this.datasetSensorB,
    },
    options: {
      legend: {
        display: true
      },
      responsive: true,
      maintainAspectRatio: false,
      title: {
        display: true,
        text: ' Bar Chart'
      },
      tooltips: {
        mode: 'index',
        intersect: false,
      },
      hover: {
        mode: 'nearest',
        intersect: true
      },
      scales: {
        xAxes: [{
          display: true,
          scaleLabel: {
            display: true,
            labelString: '15 Last Measurement'
          }
        }],
        yAxes: [{
          display: true,
          scaleLabel: {
            display: true,
            labelString: 'Value'
          }
        }]
      }
    }
  };

  // tslint:disable-next-line: member-ordering
  configpie = {
    type: 'pie',
    data: {
      datasets: [],
      labels: [
        'Red',
        'Orange',
        'Yellow',
        'Green',
        'Blue'
      ]
    },
    options: {
      title: {
        display: true,
        text: ' Pie Chart'
      },
      responsive: true
    }
  };



  /*typeGraphChange() {
    this.myLine.type = this.typeGraph;
    console.log(this.myLine.type);
    this.myLine.update();
  }*/


  /* Get Data to Form */

  /* addSensor (displaySensor){
     var dataMesure;
     var $red = this.randomScalingFactor()*255;
     var $green = this.randomScalingFactor()*255;
     var $blue = this.randomScalingFactor()*255;

     for( let i of displaySensor.measuredParameters){
       dataMesure.push(i.DataParameter.PRESSURE)
     }

     var dataSensor= {
       label:displaySensor.id,
       backgroundColor:"rgba("+$red+","+$green+","+$blue+",0.5)",
       borderColor:"rgba("+$red+","+$green+","+$blue+",1)",
       data:dataMesure,
       fill:false
     }
     this.sensorsCheck.push(dataSensor);
   }*/

  addSensoTest(item: SensorDataGraph) {

    const red = this.randomScalingFactor() * 255 / 100;
    const green = this.randomScalingFactor() * 255 / 100;
    const blue = this.randomScalingFactor() * 255 / 100;

    const dataSensorL = {
      label: item.name,
      backgroundColor: 'rgba(' + red + ',' + green + ',' + blue + ',0.5)',
      borderColor: 'rgba(' + red + ',' + green + ',' + blue + ',1)',
      data: item.TEMP,
      fill: false
    };
    const dataSensorB = {
      label: item.name,
      backgroundColor: 'rgba(' + red + ',' + green + ',' + blue + ',0.5)',
      borderColor: 'rgba(' + red + ',' + green + ',' + blue + ',1)',
      data: item.TEMP,
      fill: false
    };
    const dataSensorP = {
      label: item.name,
      backgroundColor: [
        'rgba(' + red + ',' + green + ',' + blue + ',0.5)',
        'rgba(' + this.randomScalingFactor() + ',' +
        this.randomScalingFactor() + ',' + this.randomScalingFactor() + ',0.5)',
        'rgba(' + this.randomScalingFactor() + ',' +
        this.randomScalingFactor() + ',' + this.randomScalingFactor() + ',0.5)',
        'rgba(' + this.randomScalingFactor() + ',' +
        this.randomScalingFactor() + ',' + this.randomScalingFactor() + ',0.5)',
        'rgba(' + this.randomScalingFactor() + ',' +
        this.randomScalingFactor() + ',' + this.randomScalingFactor() + ',0.5)',
      ],
      data: item.TEMP,
    };
    // tslint:disable-next-line: triple-equals
    if (this.typeGraph === 'line') {
      switch (this.typeData) {
        case 'PM1':
          dataSensorL.data = item.PM1;
          break;
        case 'PM2_5':
          dataSensorL.data = item.PM2_5;
          break;
        case 'PM10':
          dataSensorL.data = item.PM10;
          break;
        case 'TEMP':
          dataSensorL.data = item.TEMP;
          break;
        case 'PRESS':
          dataSensorL.data = item.PRESS;
          break;
        case 'HUM':
          dataSensorL.data = item.HUM;
          break;
        case 'CO2':
          dataSensorL.data = item.CO2;
          break;
        default:
          dataSensorL.data = item.TEMP;
          break;
      }
      this.myLine.data.datasets.push(dataSensorL);
      this.myLine.update();
    } else if (this.typeGraph === 'bar') {
      switch (this.typeData) {
        case 'PM1':
          dataSensorB.data = item.PM1;
          break;
        case 'PM2_5':
          dataSensorB.data = item.PM2_5;
          break;
        case 'PM10':
          dataSensorB.data = item.PM10;
          break;
        case 'TEMP':
          dataSensorB.data = item.TEMP;
          break;
        case 'PRESS':
          dataSensorB.data = item.PRESS;
          break;
        case 'HUM':
          dataSensorB.data = item.HUM;
          break;
        case 'CO2':
          dataSensorB.data = item.CO2;
          break;
        default:
          dataSensorB.data = item.TEMP;
          break;
      }
      this.myBar.data.datasets.push(dataSensorB);
      this.myBar.update();
    } else if (this.typeGraph === 'pie') {
      switch (this.typeData) {
        case 'PM1':
          dataSensorP.data = item.PM1;
          break;
        case 'PM2_5':
          dataSensorP.data = item.PM2_5;
          break;
        case 'PM10':
          dataSensorP.data = item.PM10;
          break;
        case 'TEMP':
          dataSensorP.data = item.TEMP;
          break;
        case 'PRESS':
          dataSensorP.data = item.PRESS;
          break;
        case 'HUM':
          dataSensorP.data = item.HUM;
          break;
        case 'CO2':
          dataSensorP.data = item.CO2;
          break;
        default:
          dataSensorP.data = item.TEMP;
          break;
      }
      this.myPie.data.datasets.push(dataSensorP);
      this.myPie.update();
    }
  }

  resetGraph() {
    if (this.typeGraph === 'line') {
      this.myLine.data.datasets = [];
      this.myLine.update();
    } else if (this.typeGraph === 'bar') {
      this.myBar.data.datasets = [];
      this.myBar.update();
    } else if (this.typeGraph === 'pie') {
      this.myPie.data.datasets = [];
      this.myPie.update();
    }

  }

  line() {
    this.typeGraph = 'line';
    this.element = document.getElementById('chart');
    while (this.element.firstChild) {
      this.element.removeChild(this.element.firstChild);
    }
    this.canvas = document.createElement('canvas');
    this.canvas.setAttribute('id', 'myChart');
    this.canvas.className = 'chart-canvas';
    this.element.appendChild(this.canvas);

    this.ctx = document.getElementById('myChart');

    this.myLine = new Chart(this.ctx, this.config);
  }
  bar() {
    this.typeGraph = 'bar';
    // this.ctx=null;
    this.element = document.getElementById('chart');
    while (this.element.firstChild) {
      this.element.removeChild(this.element.firstChild);
    }
    this.canvas = document.createElement('canvas');
    this.canvas.setAttribute('id', 'myChart');
    this.canvas.className = 'chart-canvas';
    this.element.appendChild(this.canvas);

    this.ctx = document.getElementById('myChart');

    this.myBar = new Chart(this.ctx, this.configbar);
  }
  pie() {
    this.typeGraph = 'pie';
    // this.ctx=null;
    this.element = document.getElementById('chart');
    while (this.element.firstChild) {
      this.element.removeChild(this.element.firstChild);
    }
    this.canvas = document.createElement('canvas');
    this.canvas.setAttribute('id', 'myChart');
    this.canvas.className = 'chart-canvas';
    this.element.appendChild(this.canvas);

    this.ctx = document.getElementById('myChart');
    this.myPie = new Chart(this.ctx, this.configpie);
  }

  days() {
    /*if(this.typeGraph == 'line'){
      this.myLine.data.labels =this.daysLabel ;
      this.myLine.update();
    }else if(this.typeGraph == 'bar'){
      this.myBar.data.labels=this.daysLabel;
      this.myBar.update();
    }else if (this.typeGraph == 'pie'){
      this.myPie.data.labels =this.daysLabel ;
      this.myPie.update();
    }*/
  }


  months() {/*
    if(this.typeGraph == 'line'){
      this.myLine.data.labels =this.monthsLabel ;
      this.myLine.update();
    }else if(this.typeGraph == 'bar'){
      this.myBar.data.labels=this.monthsLabel;
      this.myBar.update();
    }else if (this.typeGraph == 'pie'){
      this.myPie.data.labels =this.monthsLabel ;
      this.myPie.update();
    }*/
  }

  initValueChecked() {

    // tslint:disable-next-line: prefer-for-of
    /*for (let i = 0; i < this.sensors.length; i++) {
      console.log('element choisi ' + this.sensors[i]);
      const sensor = this.sensors[i];
      console.log('sensor ' + sensor);
      const value = { sensor, selected: false };
      console.log(value);
      this.checkValue.push(value);
      console.log('valeur ajoutee de plus' + this.checkValue);
    }*/

    // tslint:disable-next-line: forin
    for (const item in this.sensors) {
      console.log('item ' + item);
      const value = { sensor: item, selected: false };
      this.checkValue.push(item);

    }

    console.log(this.checkValue);

  }
  autoCompletInit() {
    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => typeof value === 'string' ? value : value.name),
        map(name => name ? this._filter(name) : this.sensors.slice())
      );
  }

  displayFn(user?: any): string | undefined {
    return user ? user.name : undefined;
  }

  private _filter(name: string): any {
    const filterValue = name.toLowerCase();
    return this.sensors.filter(
      option => option.name.toLowerCase().indexOf(filterValue) === 0
    );
  }

  selectedSensor(value: Sensor, check: boolean) {
    console.log('selected Sensor Action ' + check + ' ' + value.name);

    if (check) {
      this.checkValue.push(value);
    } else {

      this.onDeleteOnTable(value);
    }
  }

  onAddSensor() {
    let tabValue;
    let val: any;
    let i;
    console.log('Add Sensor');
    const value = this.myControl.value;
    tabValue = value.split(' ');
    for (const sensorItem of this.sensorTypes) {
      if (sensorItem.name === tabValue[0] && sensorItem.version === tabValue[1]) {
        val = sensorItem;
      }
    }

    for (const item of this.checkValue) {
      if (item.name === val.name && item.version === val.version) {
        i = 0;
      } else {
        i = 1;
      }
    }

    if (i === 0) {
      console.log('Valeur deja ajoutee dans le tableau');
    } else {
      this.checkValue.push(val);
    }
  }

  onChooseSensorType() {
    let val;
    let receivedValue;
    let i = 0;
    const value = this.myControl.value;
    receivedValue = value.split(' ').join(' -- ');
    console.log(receivedValue);
    console.log(this.sensors);
    for (const sensorItem of this.sensors) {
      if (sensorItem.sensorMetadataName === receivedValue) {
        val = sensorItem;
        for (const item of this.sensorChoosed) {
          if (val === item) {
            i = 1;
          }
        }
        if (i === 1) {
          console.log('Valeur deja ajoutee dans le tableau');
        } else {
          this.sensorChoosed.push(val);
        }
      }
    }
    console.log(this.sensorChoosed);
  }

  onDeleteOnTable(item) {
    let i;
    for (let l = 0; l < this.checkValue.length; l++) {
      if (item === this.checkValue[l]) {
        i = l;
      }
    }
    this.checkValue.splice(i, 1);
  }

  tracerGraph() {
    let i = 1;
    this.resetGraph();
    this.sensorGraph = [];
    for (const item of this.checkValue) {
      if (this.sensorGraph.length === 0) {
        const variable = new SensorDataGraph(item.name);
        this.sensorGraph.push(variable);
      } else {
        for (const el of this.sensorGraph) {
          if (el.name !== item.name) {
            i *= 1;
          } else {
            i *= 0;
          }
        }
        if (i === 1) {
          const variable = new SensorDataGraph(item.name);
          this.sensorGraph.push(variable);
        }
      }
    }
    for (const item of this.sensorGraph) {
      for (let i = this.sensorsData.length - 1; i > 0; i--) {
        if (item.name === this.sensorsData[i].sensorName) {
          item.addMesure(this.sensorsData[i].measuredParameter, this.sensorsData[i].value);
        }
      }
    }
    console.log(this.sensorGraph);
    for (const item of this.sensorGraph) {
      this.addSensoTest(item);
    }

  }

  updateGraph() {

  }


  ngOnInit() {
    this.sensorTypes = this.sensorService.loadSensorTypes();
    this.sensors = this.sensorService.loadSensors();
    this.sensorsData = this.sensorService.loadData();
    // console.log(this.sensorsData);
    this.autoCompletInit();
    this.line();
    // this.mapSensor.getsensorChecked();

  }


}
