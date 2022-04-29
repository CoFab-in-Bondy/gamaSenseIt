import {
  Directive,
  Output,
  Input,
  EventEmitter,
  HostBinding,
  HostListener
} from '@angular/core';

@Directive({
  selector: '[appDnd]'
})
export class DndDirective {
  @HostBinding('class.fileover') fileOver: boolean;
  @HostBinding('class.filenear') fileNear: boolean;
  @Output() fileDropped = new EventEmitter<any>();


  // Dragover listener
  @HostListener('window:dragover', ['$event'])
  onDragOverWindow(event: any) {
    event.preventDefault();
    event.stopPropagation();
    this.fileNear = true;
  }

  // Dragleave listener
  @HostListener('window:dragleave', ['$event'])
  public onDragLeaveWindow(event: any) {
    event.preventDefault();
    event.stopPropagation();
    this.fileNear = false;
  }

  // Dragover listener
  @HostListener('drop', ['$event'])
  onDropWindow(event: any) {
    event.preventDefault();
    event.stopPropagation();
    this.fileNear = false;
  }


  // Dragover listener
  @HostListener('dragover', ['$event'])
  onDragOver(event: any) {
    this.fileOver = true;
  }

  // Dragleave listener
  @HostListener('dragleave', ['$event'])
  public onDragLeave(event: any) {
    this.fileOver = false;
  }

  // Drop listener
  @HostListener('drop', ['$event'])
  public onDrop(event: any) {
    event.preventDefault();
    event.stopPropagation();
    this.fileOver = false;
    this.fileNear = false;
    let files = event.dataTransfer.files;
    if (files.length > 0) {
      this.fileDropped.emit(files);
    }
  }
}
