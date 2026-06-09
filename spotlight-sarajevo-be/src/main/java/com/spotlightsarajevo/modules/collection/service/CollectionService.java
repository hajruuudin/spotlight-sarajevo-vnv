package com.spotlightsarajevo.modules.collection.service;

import com.spotlightsarajevo.modules.collection.api.dto.*;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface CollectionService {
    /**
     * Find all the users collection from the database. Includes default collections created and any custom collections
     * created if the user is premium.
     * * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing a List of CollectionModel of user collections.
     */
    ResponseEntity<List<CollectionModel>> findUserCollections(Principal principal);

    /**
     * Create a new collection for the authenticated user based on the provided data.
     * The collection is created with the status as active.
     * @param request The data model containing details for the new collection (e.g., title, description, type).
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing the newly created CollectionModel.
     */
    ResponseEntity<CollectionModel> addCollection(CollectionCreateModel request, Principal principal);

    /**
     * Update the details of an existing user collection.
     * @param request The data model containing the ID and updated details (e.g., name, description) for the collection.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing the updated CollectionModel.
     */
    ResponseEntity<CollectionModel> updateCollection(CollectionUpdateModel request, Principal principal);

    /**
     * Delete a user collection by its unique ID.
     * @param collectionId The unique identifier of the collection to be deleted.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity indicating success or failure of the deletion.
     */
    ResponseEntity<CollectionModel> deleteCollection(Integer collectionId, Principal principal);

    /**
     * Retrieve all items (e.g., spots or events) associated with a specific user collection.
     * @param collectionId The unique identifier of the collection whose items are to be retrieved.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing the CollectionItemsModel list.
     */
    ResponseEntity<CollectionItemsModel> findCollectionItems(Integer collectionId, Principal principal);

    ResponseEntity<CollectionItemsModel> findAllSpotsCollection(Principal principal);

    ResponseEntity<CollectionItemsModel> findAllEventsCollection(Principal principal);

    /**
     * Add a new item (e.g., a spot or event ID) to an existing user collection.
     * @param request The data model containing the collection ID, item ID, and item type to be added.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing the updated CollectionItemsModel list.
     */
    ResponseEntity<CollectionItemsModel> addItemToCollection(CollectionAddItemModel request, Principal principal);

    /**
     * Remove a specific item from an existing user collection.
     * @param collectionId The unique identifier of the collection from which the item should be removed.
     * @param itemId The unique identifier of the item to be removed.
     * @param collectionType The type of objects in the collection.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing the updated CollectionItemsModel list.
     */
    ResponseEntity<CollectionItemsModel> removeItemFromCollection(Integer collectionId, Integer itemId, String collectionType, Boolean isSystem, Principal principal);

    /**
     * Check if a specific spot is present in any of the user's collections.
     * @param spotId The unique identifier of the item to check.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing a CollectionItemsObject indicating presence in collections.
     */
    ResponseEntity<SpotInCollectionModel> findSpotInCollections(Integer spotId, Principal principal);

    /**
     * Check if a specific event is present in any of the user's collections.
     * @param eventId The unique identifier of the event to check.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing a CollectionItemsObject indicating presence in collections.
     */
    ResponseEntity<EventInCollectionModel> findEventInCollections(Integer eventId, Principal principal);

    /**
     * Add multiple items to multiple collections in bulk.
     * @param request The data model containing the list of item IDs, object type, and collection IDs.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing a map with the results of the bulk addition.
     */
    ResponseEntity<Map<String, Object>> addItemToCollectionsBulk(AddItemToCollectionsModel request, Principal principal);

    /**
     * Remove multiple items from multiple collections in bulk.
     * @param ids The list of item IDs to be removed.
     * @param objectType The type of the items (e.g., "spot" or "event").
     * @param objectId The unique identifier of the item type.
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return A ResponseEntity containing a map with the results of the bulk removal.
     */
    ResponseEntity<Map<String, Object>> removeItemsFromCollectionsBulk(List<Integer> ids, String objectType, Integer objectId, Boolean isSystem, Principal principal);

    /**
     * Check if a specific item is saved in the system collection for the authenticated user.
     * @param objectId The unique identifier of the item to check.
     * @param objectType The type of the item (e.g., "spot" or "event").
     * @param principal Spring Security principal object sent over the request. Used for authentication & authorisation.
     * @return true if the item is saved in the system collection, false otherwise.
     */
    boolean isItemSavedInSystemCollection(Integer objectId, String objectType, Principal principal);
}
