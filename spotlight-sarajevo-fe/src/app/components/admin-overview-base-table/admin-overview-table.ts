import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { CategoryService } from '../../services/category.service';
import { TagService } from '../../services/tag.service';
import { ImageUploadService } from '../../services/image-upload.service';
import { SpinnerService } from '../../core/services/spinner.service';
import { HotToastService } from '@ngxpert/hot-toast';

/**
 * Base Admin Table class
 * 
 * This class is the base abstract implementation of all the tables useg within the overviews for each
 * data type inside the admin panel. The table contains template implementations for all the methods and 
 * functions which are common to all objects and is extended by six different complex objects to allow them to
 * implement their own specific properties.
 * 
 * The functions provided by this table are:
 * - Tabular display (avoiding usage of general HTML table tags like thead and tbody) using dividers and columns
 * to showcase shorthand data for all data types.
 * - Pagination enabled to display data incrementally
 * - ViewMore options to provide a detailed overview of the specified object.
 * 
 * The objects which this table will be used for are:
 * - Spots, including their shorthand info as well as overviews for data, images, spot reviews...
 * - Events, including their shorthand info as well as overviews for data, images, organiser reviews...
 * - Users, including their shorthand info as well as additional, non-editable info regarding their status and contact
 * - Tourist Guides, including their shorthand info as well as information about each tourist guide section, with the option to add a new section
 * - Public transport methoids, including their shorthand info as well as the ability to add or remove stations to the system
 * - Community requests, including their shorthand info as well as the ability to reviews, remove, approve requests
 * 
 * Tables serve only as the 'view' of the data, meanwhile all the state and data requests are handeled within the page component the table is placed in
 */
@Component({
  selector: 'app-admin-overview-table',
  imports: [],
  templateUrl: './admin-overview-table.html',
  styleUrl: './admin-overview-table.css'
})
export abstract class AdminOverviewBaseTable {
  /* Column Definitions are specific to each object that is being viewed */
  @Input() columnDefinitions: string[] = []

  /* By defaul, table language is english (and as ov v1.1 that remains the only option inside the admin overview) */
  @Input() columnLang: string = 'en'

  /* Pagination rules: we require the current page, total pages and total items of the search result */
  @Input() currentPage: number = 0
  @Input() totalPages: number = 99
  @Input() totalItems: number = 999

  /* Pagination events: Once clicked these events are emitted to the overview page in question */
  @Output() onNextPage: EventEmitter<number> = new EventEmitter
  @Output() onPreviousPage: EventEmitter<number> = new EventEmitter

  /* Overview selection and Item Deletion events */
  /* NOTE: Item editind and item retrieval is handeled within the parent overview component */
  @Output() onOverviewSelect: EventEmitter<number> = new EventEmitter
  @Output() onDeleteItem: EventEmitter<number> = new EventEmitter

  // Services used in derived classes (Individual table components)
  protected fb = inject(FormBuilder);
  protected categoryService = inject(CategoryService);
  protected tagService = inject(TagService);
  protected imageUploadService = inject(ImageUploadService);
  protected spinnerService = inject(SpinnerService);
  protected toastService = inject(HotToastService);

  selectedItemId: number | null = null
  showItemOverview: boolean = false

  onDeleteItemSelected(){}
  onSaveChangeSelected(){}

  onOverviewSelected(id: number){
    if (this.selectedItemId === id) {
      this.selectedItemId = null
    } else {
      this.selectedItemId = id
      this.onOverviewSelect.emit(id)
    }
  }

  nextPage(){
    if(this.currentPage < this.totalPages - 1) {
      this.currentPage++
      this.onNextPage.emit(this.currentPage)
    }
  }

  previousPage(){
    if(this.currentPage > 0) {
      this.currentPage--
      this.onPreviousPage.emit(this.currentPage)
    }
  }
}
