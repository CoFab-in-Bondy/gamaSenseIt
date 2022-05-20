import {Component, HostListener, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SensorService} from "@services/sensor.service"
import "echarts/lib/chart/bar";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {ECharts} from "echarts";

@Component({
  selector: 'app-sensors-graph',
  templateUrl: './sensors-graph.component.html',
  styleUrls: ['./sensors-graph.component.scss']
})
export class SensorsGraphComponent implements OnInit {
  options: any = {};
  updateOptions: any = {};

  private routeSub: Subscription;
  private parameters: RecordParameters;
  private echarts: ECharts;
  public sensor: SensorExtended;
  public height = "100vh";

  constructor(
    private sensorService: SensorService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe((params) => {
      let id = +params["id"];
      this.sensorService.getById(id).subscribe(
        (sensor) => {
          this.sensor = sensor;
          this.afterFetchSensor();
          this.sensorService.getParametersOfId(this.sensor.id).subscribe(
            parameters=>{
              this.parameters = parameters;
              this.afterFetchParameters();
            }
          );

        },
        (err) => {
          if (err.status == 403) {
            this.router.navigate(["/sensors"]);
          } else {
            console.log("Error during sensor fetch");
            console.log(err);
          }
        }
      );
    });
  }

  afterFetchSensor(): void {
    const meta = this.sensor.metadata.parameters;
    const indexes = Array.from(Array(meta.width - 1).keys());
    const options: any = {
      title: {
        left: 'center',
        text: this.sensor.name
      },
      tooltip: {
        trigger: 'axis',
        position: function (pt: any) {
          return [pt[0] + 10, '10%'];
        },
        formatter: (params: any) => {
          params = params.sort((a: any, b: any) => a.componentIndex - b.componentIndex);
          let values = params[0].value[0] ;

          for (const i of indexes) {
            const value = params[i].value[1];
            const content = value == null? '-' : `${value} ${meta.units[i+1]}`;
            values += "<br/>";
            values += `<div style="display: inline-block" >${ params[i].marker }${ params[i].seriesName }</div>`;
            values += `<div style="margin-left: 50px; display: inline-block; float: right; text-align: right; font-weight: bold">${content}</div>`;
          }
          return values;
        },
        axisPointer: {
          animation: false
        }
      },
      legend: {
        data: meta.headers.slice(1),
        y: 30
      },
      toolbox: {
        feature: {
          dataZoom: {
            yAxisIndex: 'none'
          },
          saveAsImage: {
            name: this.sensor.name
          }
        }
      },
      axisPointer: {
        link: [
          {
            xAxisIndex: 'all'
          }
        ]
      },
      grid: [],
      dataZoom: [
        {
          show: true,
          realtime: true,
          start: 0,
          end: 100,
          xAxisIndex: indexes
        },
        {
          type: 'inside',
          realtime: true,
          start: 0,
          end: 100,
          xAxisIndex: indexes
        }
      ],
      xAxis: [],
      yAxis: [],
      series: []
    }

    const height = 65 / (meta.width - 1);
    const step = 83 / (meta.width - 1);
    for (let i of Array(meta.width).keys()) {
      if (i == 0) continue; // skip captureDate
      options.grid.push(
        {
          left: 50,
          right: 30,
          top: 11 + step * (i - 1) + '%',
          height: height + '%'
        })
      options.series.push({
        name: meta.headers[i],
        type: 'line',
        data: [],
        showSymbol: false,
        connectNulls: true,
        xAxisIndex: i - 1,
        yAxisIndex: i - 1,
        symbolSize: 8,
        unit: meta.units[i],
        barCategoryGap: '50%',
        groupPadding: 20,

      });
      options.xAxis.push({
        type: 'time',
        boundaryGap: false,
        gridIndex: i - 1
      });

      options.yAxis.push({
        scale: true,
        type: 'value',
        gridIndex: i - 1,
        boundaryGap: [0, '100%'],
        splitLine: {
          show: true
        }
      })
    }
    this.options = options;
    console.log(this.options);
    this.onResize();
  }



  afterFetchParameters(): void {
    this.updateOptions = {series: []};
    const meta = this.sensor.metadata.parameters;
    for (let i of Array(meta.width).keys()) {
      if (i == 0) continue; // skip captureDate
      const data = [];
      for (let record of this.parameters.values) {
        const date = new Date(record[0]).toString();
        data.push({name: date, value: [record[0], record[i]]})
      }
      this.updateOptions.series.push({
        data: data
      });
    }
    console.log(this.updateOptions);
  }

  @HostListener('window:resize')
  onResize() {
    const count = this.sensor.metadata.parameters.width;
    this.height = window.innerHeight > (count * 90 + 100) ? '100vh': count * 150 + "px";
  }

  onChartInit(event: any): void {
    this.echarts = event;
  }
}
