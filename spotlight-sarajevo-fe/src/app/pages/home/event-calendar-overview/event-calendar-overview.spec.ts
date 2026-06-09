import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventCalendarOverview } from './event-calendar-overview';

describe('EventCalendarOverview', () => {
  let component: EventCalendarOverview;
  let fixture: ComponentFixture<EventCalendarOverview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCalendarOverview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventCalendarOverview);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
