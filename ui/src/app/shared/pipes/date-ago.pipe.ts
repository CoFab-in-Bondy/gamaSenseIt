import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "dateAgo",
  pure: true,
})
export class DateAgoPipe implements PipeTransform {
  transform(value: string|number|Date|null): string {
    if (value) {
      const seconds = Math.floor((+new Date() - +new Date(value)) / 1000);
      if (seconds < 3)
        // less than 30 seconds ago will show as 'Just now'
        return "Juste à l'instant";
      const intervals: any = {
        an: 31536000,
        mois: 2592000,
        semaine: 604800,
        jour: 86400,
        heure: 3600,
        minute: 60,
        seconde: 1,
      };
      let counter: number;
      for (const part in intervals) {
        counter = Math.floor(seconds / intervals[part]);
        if (counter > 0) {
          const s = counter == 1 || part == 'mois' ? '' : 's';
          return `Il y a ${counter} ${part}${s}`;
        }
      }
    }
    return "Jamais"
  }
}
