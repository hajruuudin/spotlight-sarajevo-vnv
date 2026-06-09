import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuideOverviewTable } from './guide-overview-table';

describe('GuideOverviewTable', () => {
  let component: GuideOverviewTable;
  let fixture: ComponentFixture<GuideOverviewTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuideOverviewTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuideOverviewTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
