import { Component } from '@angular/core';
import { PageHeader } from "../../../components/page-header/page-header";
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-admin-transport-overview',
  imports: [PageHeader, TranslocoPipe],
  templateUrl: './admin-transport-overview.html',
  styleUrl: './admin-transport-overview.css'
})
export class AdminTransportOverview {

}
