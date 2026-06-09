package com.spotlightsarajevo.modules.collection.api;

import com.spotlightsarajevo.common.utils.CollectionItemsObject;
import com.spotlightsarajevo.modules.collection.api.dto.*;
import com.spotlightsarajevo.modules.collection.service.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "collection", description = "Collection API Routes")
@AllArgsConstructor
@RequestMapping(value = "/collection")
public class CollectionRESTController {
    CollectionService collectionService;

    @GetMapping(value = "/all")
    @Operation(description = "Get a users collections from the database, only the references for the objects")
    public ResponseEntity<List<CollectionModel>> findUserCollections(
            Principal principal
    ){
        return collectionService.findUserCollections(principal);
    }

    @PostMapping
    @Operation(description = "Create a new collection for the user, either spot or event")
    public ResponseEntity<CollectionModel> addCollection(
            @RequestBody CollectionCreateModel request,
            Principal principal
    ){
        System.out.println(request);
        return collectionService.addCollection(request, principal);
    }

    @PutMapping
    @Operation(description = "Update the name or the description of a collection")
    public ResponseEntity<CollectionModel> updateCollection(
            @RequestBody CollectionUpdateModel request,
            Principal principal
            ){
        return collectionService.updateCollection(request, principal);
    }

    @DeleteMapping(value = "/delete/{collectionId}")
    @Operation(description = "Delete a collection from the database")
    public ResponseEntity<CollectionModel> deleteCollection(
            @PathVariable Integer collectionId,
            Principal principal
    ){
        return collectionService.deleteCollection(collectionId, principal);
    }

    @GetMapping(value = "/{collectionId}/items")
    @Operation(description = "Find items for a specific collection")
    public ResponseEntity<CollectionItemsModel> findCollectionItems(
            @PathVariable Integer collectionId,
            Principal principal
    ){
        return collectionService.findCollectionItems(collectionId, principal);
    }

    @GetMapping(value = "/all-spots")
    @Operation(description = "Find all spots that were added to some collection")
    public ResponseEntity<CollectionItemsModel> findAllSpotsCollection(
            Principal principal
    ){
        return collectionService.findAllSpotsCollection(principal);
    }

    @GetMapping(value = "/all-events")
    @Operation(description = "FInd all events that were added to some collection")
    public ResponseEntity<CollectionItemsModel> findAllEventsCollection(
            Principal principal
    ){
        return collectionService.findAllEventsCollection(principal);
    }

    @PostMapping(value = "/add-item")
    @Operation(description = "Add an item to a specified collection")
    public ResponseEntity<CollectionItemsModel> addItemToCollection(
            @RequestBody CollectionAddItemModel request,
            Principal principal
    ){
        return collectionService.addItemToCollection(request, principal);
    }

    @DeleteMapping(value = "/remove-item")
    @Operation(description = "Remove an item from a collection")
    public ResponseEntity<CollectionItemsModel> removeItemFromCollection(
            @RequestParam Integer collectionId,
            @RequestParam Integer itemId,
            @RequestParam String collectionType,
            @RequestParam Boolean isSystem,
            Principal principal
    ){
        return collectionService.removeItemFromCollection(collectionId, itemId, collectionType, isSystem, principal);
    }

    @GetMapping(value = "/find-spot-in-collections")
    @Operation(description = "Find the collection ids in which a spot is located in")
    public ResponseEntity<SpotInCollectionModel> findSpotInCollections(
            @RequestParam Integer spotId,
            Principal principal
    ){
        return collectionService.findSpotInCollections(spotId, principal);
    }

    @GetMapping(value = "/find-event-in-collections")
    @Operation(description = "Find the collection ids in which an event is located in")
    public ResponseEntity<EventInCollectionModel> findEventInCollections(
            @RequestParam Integer eventId,
            Principal principal
    ){
        return collectionService.findEventInCollections(eventId, principal);
    }

    @PostMapping(value = "/add-items-bulk")
    @Operation(description = "Add one object to multiple collections at once")
    public ResponseEntity<Map<String, Object>> addItemToCollectionsBulk(
        @RequestBody AddItemToCollectionsModel request,
        Principal principal
    ){
        return collectionService.addItemToCollectionsBulk(request, principal);
    };

    @DeleteMapping(value = "/remove-items-bulk")
    @Operation(description = "Remove one object from multiple collections at once")
    public ResponseEntity<Map<String, Object>> removeItemFromCollectionsBulk(
            @RequestParam List<Integer> collectionIds,
            @RequestParam Integer objectId,
            @RequestParam String objectType,
            @RequestParam Boolean isSystem,
            Principal principal
    ){
        return collectionService.removeItemsFromCollectionsBulk(collectionIds, objectType, objectId, isSystem, principal);
    }

    @GetMapping(value = "/exists")
    @Operation(description = "Check if an object is saved to a users system collections")
    public boolean existsInSystemCollections(
            @RequestParam Integer objectId,
            @RequestParam String objectType,
            Principal principal
    ){
        return collectionService.isItemSavedInSystemCollection(objectId, objectType, principal);
    }
}
