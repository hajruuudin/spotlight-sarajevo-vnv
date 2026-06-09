import { Component, input, Input } from '@angular/core';
import { BnwDateIcon } from "../../resources/icons/bnw-date-icon/bnw-date-icon";
import { BnwEventCancelIcon } from "../../resources/icons/bnw-event-cancel-icon/bnw-event-cancel-icon";
import { BnwEventLangIcon } from "../../resources/icons/bnw-event-lang-icon/bnw-event-lang-icon";
import { BnwEventPriceIcon } from "../../resources/icons/bnw-event-price-icon/bnw-event-price-icon";
import { BnwEventResIcon } from "../../resources/icons/bnw-event-res-icon/bnw-event-res-icon";
import { BnwEventAgeIcon } from "../../resources/icons/bnw-event-age-icon/bnw-event-age-icon";
import { TranslocoPipe } from '@ngneat/transloco';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-event-info-card',
  imports: [BnwDateIcon, BnwEventCancelIcon, BnwEventLangIcon, BnwEventPriceIcon, BnwEventResIcon, BnwEventAgeIcon, TranslocoPipe, DatePipe],
  templateUrl: './event-info-card.html',
  styleUrl: './event-info-card.css',
  host: {
    class: "w-auto h-auto dark:bg-(--primary-200) dark:hover:bg-(--background-300) bg-white flex flex-col justify-center items-center rounded-2xl p-3 font-semibold text-base md:text-xl"
  }
})
export class EventInfoCard {
  @Input() iconType: string = 'DATE'
  @Input() iconTheme: string = 'DARK'
  @Input() iconSize: string = 'w-12 h-12'
  //Individual inputs for showing information
  @Input() startDate: string = 'No start Date'
  @Input() endDate: string = 'No end Date'
  @Input() reservation: boolean = false
  @Input() cancelRefund: boolean = false
  @Input() ageRequirement: number = 0
  @Input() language: string = 'Bosnian'
  @Input() entryPrice: number = 10
}
