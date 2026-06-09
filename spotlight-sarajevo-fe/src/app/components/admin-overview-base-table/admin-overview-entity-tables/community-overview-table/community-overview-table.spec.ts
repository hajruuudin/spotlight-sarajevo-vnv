import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunityOverviewTable } from './community-overview-table';

describe('CommunityOverviewTable', () => {
  let component: CommunityOverviewTable;
  let fixture: ComponentFixture<CommunityOverviewTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommunityOverviewTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommunityOverviewTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
