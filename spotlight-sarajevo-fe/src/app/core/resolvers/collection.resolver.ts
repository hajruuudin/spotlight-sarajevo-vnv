import { ResolveFn } from '@angular/router';
import { CollectionItemsModel, CollectionModel } from '../../shared/models/collection.model';
import { inject } from '@angular/core';
import { CollectionService } from '../../services/collection.service';
import { map, of, switchMap, tap } from 'rxjs';
import { error } from 'console';
import { HttpErrorResponse } from '@angular/common/http';

export interface CollectionPageData {
  userCollections: CollectionModel[];
  selectedCollection: CollectionItemsModel | null;
}

export const collectionsResolver: ResolveFn<CollectionPageData> = (route, state) => {
  const collectionService = inject(CollectionService);

  return collectionService.findUserCollections().pipe(
    map((collections: CollectionModel[]) => {
      return collections ? collections.filter((c) => !c.isSystem) : [];
    }),
    switchMap((collections: CollectionModel[]) => {
      const userCollections = collections || [];

      return collectionService.findAllSpotsCollection().pipe(
        map((selected: CollectionItemsModel) => {
          if (selected?.collectionItems) {
            const uniqueItems = Array.from(
              new Map(selected.collectionItems.map((item) => [item.id, item])).values()
            );
            selected.collectionItems = uniqueItems;
          }

          return {
            userCollections,
            selectedCollection: selected ?? {
              collectionName: 'Default',
              collectionDescription: 'Default',
              collectionId: 0,
              collectionType: 'SPOT',
              collectionItems: [],
              isSystem: false,
            },
          };
        })
      );
    })
  );
};
