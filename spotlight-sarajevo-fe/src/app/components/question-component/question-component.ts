import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-question-component',
  imports: [],
  templateUrl: './question-component.html',
  styleUrl: './question-component.css',
  host: {
    class: 'w-full',
  },
})
export class QuestionComponent {
  @Input() isSelectedA: boolean = false;
  @Input() isSelectedB: boolean = false;
  @Input() question: string = '';
  @Output() isSelectedAChanged = new EventEmitter<boolean>();
  @Output() isSelectedBChanged = new EventEmitter<boolean>();

  onYesClick() {
    this.isSelectedA = true;
    this.isSelectedAChanged.emit(this.isSelectedA);
    this.isSelectedB = false;
    this.isSelectedBChanged.emit(this.isSelectedB);
  }

  onNoClick() {
    this.isSelectedB = true;
    this.isSelectedBChanged.emit(this.isSelectedB);
    this.isSelectedA = false;
    this.isSelectedAChanged.emit(this.isSelectedA);
  }

  yesButtonStyle() {
    return this.isSelectedA ? 'dark:bg-(--primary-400) bg-(--primary-400)' : 'dark:bg-(--secondary-300) bg-(--secondary-800)';
  }

  noButtonStyle() {
    return this.isSelectedB ? 'dark:bg-red-700 bg-red-300' : 'dark:bg-(--secondary-300) bg-(--secondary-800)';
  }
}
