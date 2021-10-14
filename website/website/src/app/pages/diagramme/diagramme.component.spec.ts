import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagrammeComponent } from './diagramme.component';

describe('DiagrammeComponent', () => {
  let component: DiagrammeComponent;
  let fixture: ComponentFixture<DiagrammeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiagrammeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagrammeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
