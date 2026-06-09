import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransportIcon } from './transport-icon';

describe('TransportIcon', () => {
  let component: TransportIcon;
  let fixture: ComponentFixture<TransportIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransportIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransportIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
