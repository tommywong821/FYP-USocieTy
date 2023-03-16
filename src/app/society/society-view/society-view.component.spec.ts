import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SocietyViewComponent } from './society-view.component';

describe('SocietyViewComponent', () => {
  let component: SocietyViewComponent;
  let fixture: ComponentFixture<SocietyViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SocietyViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SocietyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
