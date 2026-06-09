import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, forkJoin, of, from } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface ImageBBResponse {
  data: {
    url: string;
    delete_url: string;
    display_url: string;
    thumb: {
      url: string;
    };
  };
  success: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ImageUploadService {
  private imageBBApiUrl = 'https://api.imgbb.com/1/upload';
  private apiKey = environment.IMAGE_BB_API;

  constructor(private http: HttpClient) {}

  /**
   * Upload a single file to ImageBB
   * @param file The file to upload
   * @returns Observable with the ImageBB response containing url and delete_url
   */
  uploadImage(file: File): Observable<ImageBBResponse> {
    return from(this.compressImage(file)).pipe(
      switchMap((base64String) => {
        const formData = new FormData();
        formData.append('key', this.apiKey);
        // Remove the data:image/...;base64, prefix and send only the base64 string
        const base64Data = base64String.split(',')[1];
        formData.append('image', base64Data);

        return this.http.post<ImageBBResponse>(this.imageBBApiUrl, formData);
      })
    );
  }

  /**
   * Upload multiple files to ImageBB
   * @param files Array of files to upload
   * @returns Observable with array of ImageBB responses
   */
  uploadMultipleImages(files: File[]): Observable<ImageBBResponse[]> {
    if (files.length === 0) {
      return of([]);
    }

    const uploadObservables = files.map((file) => this.uploadImage(file));
    return forkJoin(uploadObservables);
  }

  /**
   * Delete an image from ImageBB using its delete URL
   * @param deleteUrl The delete URL provided by ImageBB
   * @returns Observable for the delete request
   */
  deleteImage(deleteUrl: string): Observable<any> {
    if (!deleteUrl) {
      return of(null);
    }
    
    // ImageBB delete URLs are direct GET requests
    return this.http.get(deleteUrl);
  }

  /**
   * Delete multiple images from ImageBB
   * @param deleteUrls Array of delete URLs
   * @returns Observable with array of delete responses
   */
  deleteMultipleImages(deleteUrls: string[]): Observable<any[]> {
    if (deleteUrls.length === 0) {
      return of([]);
    }

    const deleteObservables = deleteUrls
      .filter((url) => url && url.trim() !== '')
      .map((url) => this.deleteImage(url));
    
    return deleteObservables.length > 0 ? forkJoin(deleteObservables) : of([]);
  }

  /**
   * Compress an image to 75% quality
   * @param file The image file to compress
   * @returns Promise resolving to compressed base64 string
   */
  private compressImage(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      
      reader.onload = (event: any) => {
        const img = new Image();
        
        img.onload = () => {
          const canvas = document.createElement('canvas');
          canvas.width = img.width;
          canvas.height = img.height;
          
          const ctx = canvas.getContext('2d');
          if (!ctx) {
            reject(new Error('Failed to get canvas context'));
            return;
          }
          
          ctx.drawImage(img, 0, 0);
          
          // Compress to 75% quality
          const compressedBase64 = canvas.toDataURL('image/jpeg', 0.75);
          resolve(compressedBase64);
        };
        
        img.onerror = () => {
          reject(new Error(`Failed to load image: ${file.name}`));
        };
        
        img.src = event.target.result;
      };
      
      reader.onerror = () => {
        reject(new Error(`Failed to read file: ${file.name}`));
      };
      
      reader.onabort = () => {
        reject(new Error(`File reading was aborted: ${file.name}`));
      };
      
      try {
        reader.readAsDataURL(file);
      } catch (error) {
        reject(error);
      }
    });
  }
}
