import { Component, Input } from '@angular/core';
import { ButtonPrimary } from "../button-primary/button-primary";
import { NotFoundComponent } from "../not-found-component/not-found-component";

@Component({
  selector: 'app-image-carousel',
  imports: [ButtonPrimary, NotFoundComponent],
  templateUrl: './image-carousel.html',
  styleUrl: './image-carousel.css'
})
export class ImageCarousel {
  @Input() images: string[] = [];

  fullscreenIndex: number | null = null;

  loadImageFullscreenPreview(index: number) {
    this.fullscreenIndex = index;
    document.body.style.overflow = "hidden";
  }

  exitImageFullscreenPreview() {
    this.fullscreenIndex = null;
    document.body.style.overflow = "";
  }

  nextImage() {
    if (this.fullscreenIndex === null) return;
    this.fullscreenIndex =
      (this.fullscreenIndex + 1) % this.images.length;
  }

  previousImage() {
    if (this.fullscreenIndex === null) return;
    this.fullscreenIndex =
      (this.fullscreenIndex - 1 + this.images.length) % this.images.length;
  }
}
