import { Component } from '@angular/core';
import { PageHeader } from "../../../components/page-header/page-header";
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-admin-user-overview',
  imports: [PageHeader, TranslocoPipe],
  templateUrl: './admin-user-overview.html',
  styleUrl: './admin-user-overview.css'
})
export class AdminUserOverview {

}
