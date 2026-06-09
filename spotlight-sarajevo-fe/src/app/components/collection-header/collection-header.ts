import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-collection-header',
  imports: [],
  templateUrl: './collection-header.html',
  styleUrl: './collection-header.css',
  host: {
    class: 'w-full h-auto flex flex-col justify-center items-center space-y-1'
  }
})
export class CollectionHeader {
  @Input() collectionHeader: string = 'DEFAULT_HEADER'
  @Input() collectionDescription: string = "DEFAULT_DESC"
}
