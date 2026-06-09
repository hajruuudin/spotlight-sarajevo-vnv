import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransportOverviewTable } from './transport-overview-table';

describe('TransportOverviewTable', () => {
  let component: TransportOverviewTable;
  let fixture: ComponentFixture<TransportOverviewTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransportOverviewTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransportOverviewTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
