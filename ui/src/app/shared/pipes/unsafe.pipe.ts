import { Pipe, PipeTransform, SecurityContext } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';



@Pipe({
  name: 'unsafe'
})
export class UnsafePipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) {}


  transform<K extends keyof BypassSecurityOptions>(value: BypassSecurityOptions[K], type: K): string | null {
    if (value == undefined || value == null) return null;
    switch (type) {
      case 'html': return this.sanitizer.sanitize(SecurityContext.HTML, value);
      case 'style': return this.sanitizer.sanitize(SecurityContext.STYLE, value);
      case 'script': return this.sanitizer.sanitize(SecurityContext.SCRIPT, value);
      case 'url': return this.sanitizer.sanitize(SecurityContext.URL, value);
      case 'resourceUrl': return this.sanitizer.sanitize(SecurityContext.RESOURCE_URL,value);
      default: throw new Error(`Invalid safe type specified: ${type}`);
    }
  }

}
