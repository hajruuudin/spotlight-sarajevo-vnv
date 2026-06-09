import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventsIcon } from './events-icon';

describe('EventsIcon', () => {
  let component: EventsIcon;
  let fixture: ComponentFixture<EventsIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventsIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventsIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
