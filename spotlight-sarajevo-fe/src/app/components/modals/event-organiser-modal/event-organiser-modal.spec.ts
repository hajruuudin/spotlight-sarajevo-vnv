import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventOrganiserModal } from './event-organiser-modal';

describe('EventOrganiserModal', () => {
  let component: EventOrganiserModal;
  let fixture: ComponentFixture<EventOrganiserModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventOrganiserModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventOrganiserModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
