import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import {
  AddCollectionItemsModel,
  CollectionAddItemModel,
  CollectionCreateModel,
  CollectionItemsModel,
  CollectionModel,
  CollectionUpdateModel,
  EventInCollectionModel,
  SpotInCollectionsModel,
} from '../shared/models/collection.model';

/**
 * CollectionService handles all available backend endpoints related to user collections.
 * This includes (not limited to) basic CRUD operations for collections and their items
 * - GET methods to retrieve user collections and their items
 * - POST methods to create collections and add items
 * - PUT methods to update collections
 * - DELETE methods to delete collections and remove items
 *
 * Models and entities incorporated in the methods: CollectionModel, CollectionItemsModel
 *
 * All HTTP requests include credentials for cookie/session management.
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class CollectionService {
  private apiUrl = environment.API_URL;

  constructor(private http: HttpClient) {}

  /* ============================================================= */
  /* =================== COLLECTION MANAGEMENT =================== */
  /* ============================================================= */

  /**
   * Method to retrieve all collections belonging to the current user.
   * @returns An array of CollectionModel objects representing the user's collections
   */
  findUserCollections() {
    return this.http.get<CollectionModel[]>(this.apiUrl + `/collection/all`, {
      withCredentials: true,
    });
  }

  /**
   * Method to add a new collection for the current user.
   * @param collectionCreate The collection creation data
   * @returns The created CollectionModel object
   */
  addCollection(collectionCreate: CollectionCreateModel) {
    return this.http.post<CollectionModel>(this.apiUrl + `/collection`, collectionCreate, {
      withCredentials: true,
    });
  }

  /**
   * Method to update an existing collection for the current user.
   * @param collectionUpdate The collection update data
   * @returns The updated CollectionModel object
   */
  updateCollection(collectionUpdate: CollectionUpdateModel) {
    return this.http.put<CollectionModel>(this.apiUrl + `/collection`, collectionUpdate, {
      withCredentials: true,
    });
  }

  /**
   * Method to delete an existing collection for the current user.
   * @param collectionId The ID of the collection to be deleted
   * @returns The deleted CollectionModel object
   */
  deleteCollection(collectionId: number) {
    return this.http.delete<CollectionModel>(this.apiUrl + `/collection/delete/${collectionId}`, {
      withCredentials: true,
    });
  }

  /* ============================================================= */
  /* ====================== ITEM MANAGEMENT ====================== */
  /* ============================================================= */

  /**
   * Method to retrieve all items within a specific collection.
   * @param collectionId The ID of the collection whose items are being retrieved
   * @returns A CollectionItemsModel object representing the items in the collection
   */
  findCollectionItems(collectionId: number) {
    return this.http.get<CollectionItemsModel>(this.apiUrl + `/collection/${collectionId}/items`, {
      withCredentials: true,
    });
  }

  /**
   * Method to retrieve all spots in the user's collections.
   * @returns A CollectionItemsModel object representing all spots in the user's collections
   */
  findAllSpotsCollection() {
    return this.http.get<CollectionItemsModel>(this.apiUrl + `/collection/all-spots`, {
      withCredentials: true,
    });
  }

  /**
   * Method to retrieve all events in the user's collections.
   * @returns A CollectionItemsModel object representing all events in the user's collections
   */
  findAllEventsCollection() {
    return this.http.get<CollectionItemsModel>(this.apiUrl + `/collection/all-events`, {
      withCredentials: true,
    });
  }

  /**
   * Method to add an item to a specific collection.
   * @param itemAdd The item addition data
   * @returns The updated CollectionItemsModel object representing the items in the collection
   */
  addItemToCollection(itemAdd: CollectionAddItemModel) {
    return this.http.post<CollectionItemsModel>(this.apiUrl + `/collection/add-item`, itemAdd, {
      withCredentials: true,
    });
  }

  /**
   * Method to remove an item from a specific collection.
   * @param collectionId The ID of the collection from which the item is being removed
   * @param itemId The ID of the item to be removed
   * @param collectionType The type of the collection (e.g., "spot" or "event")
   * @param isSystem Boolean indicating if the collection is a system collection
   * @returns The updated CollectionItemsModel object representing the items in the collection
   */
  removeItemFromCollection(
    collectionId: number,
    itemId: number,
    collectionType: string,
    isSystem: boolean,
  ) {
    return this.http.delete<CollectionItemsModel>(
      this.apiUrl +
        `/collection/remove-item?collectionId=${collectionId}&itemId=${itemId}&collectionType=${collectionType}&isSystem=${isSystem}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to find if a specific spot is present in any of the user's collections.
   * @param spotId The ID of the spot being checked
   * @returns A SpotInCollectionsModel object representing the collections containing the spot
   */
  findSpotInCollections(spotId: number) {
    return this.http.get<SpotInCollectionsModel>(
      this.apiUrl + `/collection/find-spot-in-collections?spotId=${spotId}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to find if a specific event is present in any of the user's collections.
   * @param eventId The ID of the event being checked
   * @returns An EventInCollectionModel object representing the collections containing the event
   */
  findEventInCollections(eventId: number) {
    return this.http.get<EventInCollectionModel>(
      this.apiUrl + `/collection/find-event-in-collections?eventId=${eventId}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to add multiple items to multiple collections in bulk.
   * @param request The bulk addition data
   * @returns A response indicating the result of the bulk addition
   */
  addItemToCollectionBulk(request: AddCollectionItemsModel) {
    return this.http.post(this.apiUrl + `/collection/add-items-bulk`, request, {
      withCredentials: true,
    });
  }

  /**
   * Method to remove an item from multiple collections in bulk.
   * @param collectionIds The IDs of the collections from which the item is being removed
   * @param objectId The ID of the item to be removed
   * @param objectType The type of the item (e.g., "spot" or "event")
   * @returns A response indicating the result of the bulk removal
   */
  removeItemFromCollectionBulk(collectionIds: number[], objectId: number, objectType: string) {
    const idsParam = collectionIds.join(',');

    return this.http.delete(this.apiUrl + `/collection/remove-items-bulk`, {
      params: {
        collectionIds: idsParam,
        objectId: objectId,
        objectType: objectType,
        isSystem: false,
      },
      withCredentials: true,
    });
  }

  /**
   * Method to check if a specific object is present in any of the user's collections.
   * @param objectId The ID of the object being checked
   * @param objectType The type of the object (e.g., "spot" or "event")
   * @returns A boolean indicating whether the object is present in any collection
   */
  checkIfPresentInCollection(objectId: number, objectType: string) {
    return this.http.get<boolean>(
      this.apiUrl + `/collection/exists?objectId=${objectId}&objectType=${objectType}`,
      {
        withCredentials: true,
      },
    );
  }
}
