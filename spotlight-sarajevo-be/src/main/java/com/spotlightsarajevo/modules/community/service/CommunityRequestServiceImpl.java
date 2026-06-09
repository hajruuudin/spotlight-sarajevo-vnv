package com.spotlightsarajevo.modules.community.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.FilterOptions;
import com.spotlightsarajevo.common.enums.RequestStatus;
import com.spotlightsarajevo.common.exceptions.CommunityRequestExceptions;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.community.api.dto.*;
import com.spotlightsarajevo.modules.community.domain.CommunityRequestDAO;
import com.spotlightsarajevo.modules.community.domain.CommunityRequestDataDAO;
import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestDataEntity;
import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestEntity;
import com.spotlightsarajevo.modules.community.mapper.CommunityRequestMapper;
import com.spotlightsarajevo.modules.community.utils.CommunityRequestSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CommunityRequestServiceImpl implements CommunityRequestService {
    private final UserDAO userDAO;
    private final CommunityRequestDAO communityRequestDAO;
    private final CommunityRequestDataDAO communityRequestDataDAO;
    private final CommunityRequestMapper communityRequestMapper;

    @Override
    public ResponseEntity<List<UserCommunityRequestModel>> getUserCommunityRequests(Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        List<CommunityRequestEntity> entities = communityRequestDAO.findByUserId(user.get().getId());
        List<UserCommunityRequestModel> models = communityRequestMapper.entitiesToUserDtos(entities);

        // Set username for each model
        models.forEach(model -> model.setUsername(user.get().getUsername()));

        return ResponseEntity.ok(models);
    }

    @Override
    @Transactional
    public ResponseEntity<CommunityRequestModel> createCommunityRequest(CommunityRequestCreateModel request, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        CommunityRequestEntity entity = communityRequestMapper.createDtoToEntity(request);
        entity.setUserId(user.get().getId());
        entity.setStatus(RequestStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());

        CommunityRequestEntity savedEntity = communityRequestDAO.save(entity);

        // If request has a body (for ADD type requests), save it to the data table
        if (request.getRequestBody() != null) {
            CommunityRequestDataEntity dataEntity = new CommunityRequestDataEntity();
            dataEntity.setCommunityRequestId(savedEntity.getId());
            dataEntity.setPendingInfo(request.getRequestBody());
            communityRequestDataDAO.save(dataEntity);
        }

        return ResponseEntity.ok(communityRequestMapper.entityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<List<CommunityRequestModel>> getAllCommunityRequests(FilterOptions filterOption, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (!user.get().getIsAdmin()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        // Get specification based on filter option
        var specification = CommunityRequestSpecification.getByFilterOption(filterOption);

        // Fetch entities using specification (if specification is null, all entities are returned)
        List<CommunityRequestEntity> entities = specification == null
            ? communityRequestDAO.findAll()
            : communityRequestDAO.findAll(specification);

        List<CommunityRequestModel> models = communityRequestMapper.entitiesToDtos(entities);

        return ResponseEntity.ok(models);
    }

    @Override
    public ResponseEntity<CommunityRequestDataModel> getCommunityRequestById(Integer requestId, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (!user.get().getIsAdmin()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Optional<CommunityRequestDataEntity> dataEntity = communityRequestDataDAO.findByCommunityRequestId(requestId);
        if (dataEntity.isEmpty()) throw new CommunityRequestExceptions.RequestNotFound(ExceptionCodes.CommunityRequestExceptionCodes.CR_REQUEST_NOT_FOUND);

        return ResponseEntity.ok(communityRequestMapper.dataEntityToDto(dataEntity.get()));
    }

    @Override
    public ResponseEntity<CommunityRequestOverviewModel> getCommunityRequestOverview(Integer requestId, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (!user.get().getIsAdmin()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Optional<CommunityRequestEntity> requestEntity = communityRequestDAO.findById(requestId);
        if (requestEntity.isEmpty()) throw new CommunityRequestExceptions.RequestNotFound(ExceptionCodes.CommunityRequestExceptionCodes.CR_REQUEST_NOT_FOUND);

        Optional<CommunityRequestDataEntity> dataEntity = communityRequestDataDAO.findByCommunityRequestId(requestId);
        if (dataEntity.isEmpty()) throw new CommunityRequestExceptions.RequestNotFound(ExceptionCodes.CommunityRequestExceptionCodes.CR_REQUEST_NOT_FOUND);

        CommunityRequestEntity entity = requestEntity.get();
        CommunityRequestDataEntity data = dataEntity.get();

        CommunityRequestOverviewModel  model = communityRequestMapper.entityToOverviewDto(entity);
        model.setPendingInfo(data.getPendingInfo());

        // Get username from user entity
        Optional<UserEntity> requestUser = userDAO.findById(entity.getUserId());
        if (requestUser.isPresent()) {
            model.setUsername(requestUser.get().getUsername());
        }

        return ResponseEntity.ok(model);
    }

    @Override
    @Transactional
    public ResponseEntity<CommunityRequestModel> updateCommunityRequestStatus(CommunityRequestStatusUpdate request, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (!user.get().getIsAdmin()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Optional<CommunityRequestEntity> entityOpt = communityRequestDAO.findById(request.getRequestId());
        if (entityOpt.isEmpty()) throw new CommunityRequestExceptions.RequestNotFound(ExceptionCodes.CommunityRequestExceptionCodes.CR_REQUEST_NOT_FOUND);

        CommunityRequestEntity entity = entityOpt.get();
        entity.setStatus(RequestStatus.valueOf(request.getStatus()));

        CommunityRequestEntity updatedEntity = communityRequestDAO.save(entity);

        return ResponseEntity.ok(communityRequestMapper.entityToDto(updatedEntity));
    }

    @Override
    @Transactional
    public ResponseEntity<CommunityRequestModel> deleteCommunityRequest(Integer requestId, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (!user.get().getIsAdmin()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Optional<CommunityRequestEntity> entityOpt = communityRequestDAO.findById(requestId);
        if (entityOpt.isEmpty()) throw new CommunityRequestExceptions.RequestNotFound(ExceptionCodes.CommunityRequestExceptionCodes.CR_REQUEST_NOT_FOUND);

        CommunityRequestEntity entity = entityOpt.get();
        CommunityRequestModel model = communityRequestMapper.entityToDto(entity);

        // Delete associated data if exists
        Optional<CommunityRequestDataEntity> dataEntity = communityRequestDataDAO.findByCommunityRequestId(requestId);
        dataEntity.ifPresent(communityRequestDataDAO::delete);

        communityRequestDAO.delete(entity);

        return ResponseEntity.ok(model);
    }

    @Override
    public ResponseEntity<List<CommunityRequestModel>> getRecentlyAddedCommunityRequests(Integer limit, Principal principal) {
        if (principal == null) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (limit == null || limit <= 0) limit = 10;

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        if (user.isEmpty()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);
        if (!user.get().getIsAdmin()) throw new CommunityRequestExceptions.UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes.CR_UNAUTHORIZED);

        Pageable pageable = PageRequest.of(0, limit);
        List<CommunityRequestEntity> entities = communityRequestDAO.findAll(
            CommunityRequestSpecification.orderByRecentlyAdded(),
            pageable
        ).getContent();
        List<CommunityRequestModel> models = communityRequestMapper.entitiesToDtos(entities);

        return ResponseEntity.status(200).body(models);
    }
}
