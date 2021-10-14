import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'searchSensor'
})
export class SearchPipe implements PipeTransform {
  transform(sensors: any, searchText: any): any {
    if (searchText == null) {
      return sensors;
      // setInterval(() => sensors, 3000);
    }

    // tslint:disable-next-line: only-arrow-functions
    return sensors.filter(function(item) {
      return item.name.toLowerCase().indexOf(searchText.toLowerCase()) > - 1;
    });
  }

}
