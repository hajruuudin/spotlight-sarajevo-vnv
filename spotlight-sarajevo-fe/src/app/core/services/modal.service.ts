import { ComponentRef, Injectable, Type, ViewContainerRef } from '@angular/core';

/**
 * Custom modal service for dynamically creating and managing modal components.
 * Provides methods to open modals and pass data to them.
 * Ensures proper cleanup and restores body scroll on modal close.
 *
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private container!: ViewContainerRef;

  registerContainer(container: ViewContainerRef) {
    this.container = container;
  }

  open(component: Type<any>, data: any = {}): ComponentRef<any> {
    document.body.style.overflow = 'hidden';
    const compRef = this.container.createComponent(component);

    Object.assign(compRef.instance, data);

    compRef.instance.close = () => {
      document.body.style.overflow = '';
      compRef.destroy();
    };

    return compRef;
  }

  openAsync<T>(component: Type<any>, data: any = {}): Promise<T> {
    document.body.style.overflow = 'hidden';
    return new Promise<T>((resolve) => {
      const compRef = this.container.createComponent(component);

      Object.assign(compRef.instance, data);

      compRef.instance.close = (result: T) => {
        document.body.style.overflow = '';
        resolve(result);
        compRef.destroy();
      };
    });
  }
}
