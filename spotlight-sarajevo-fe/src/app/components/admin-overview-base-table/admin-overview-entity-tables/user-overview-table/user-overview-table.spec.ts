import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserOverviewTable } from './user-overview-table';

describe('UserOverviewTable', () => {
  let component: UserOverviewTable;
  let fixture: ComponentFixture<UserOverviewTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserOverviewTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserOverviewTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
