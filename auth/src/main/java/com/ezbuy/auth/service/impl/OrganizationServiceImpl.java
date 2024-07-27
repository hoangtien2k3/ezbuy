//package com.ezbuy.auth.service.impl;
//
//import com.ezbuy.auth.constants.AuthConstants;
//import com.ezbuy.auth.model.dto.request.CreateOrganizationRequest;
//import com.ezbuy.auth.model.dto.request.CreateOrganizationUnitRequest;
//import com.ezbuy.auth.model.postgresql.Individual;
//import com.ezbuy.auth.model.postgresql.OrganizationUnit;
//import com.ezbuy.auth.service.OrganizationService;
//import com.ezbuy.framework.model.response.DataResponse;
//import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//import com.ezbuy.auth.model.dto.request.SyncRequestDTO;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class OrganizationServiceImpl implements OrganizationService {
//
//    @Override
//    @Transactional
//    public Mono<DataResponse<OrganizationUnit>> createOrganizationUnit(CreateOrganizationUnitRequest request, String organizationId) {
//        return createOrganizationUnitGen(request, organizationId).flatMap(response -> {
//            if (DataUtil.isNullOrEmpty(response.getErrorCode())) {
//                // push event to sync data
//                var syncCommandRequest =
//                        SyncRequestDTO.builder().orgId(organizationId)
//                                .action(SyncData.Action.INSERT.toString())
//                                .serviceType(SyncData.SyncServiceType.ALL_BY_ORG.toString())
//                                .objectType(AuthConstants.DataSyncType.ORGANIZATION_UNIT_TYPE)
//                                .syncType(SyncData.SyncObjectType.EVENT.toString())
//                                .ids(List.of(response.getData().getId())).build();
//                return syncMessageService.requestAndAsyncPushMessage(syncCommandRequest)
//                        .flatMap(rs -> Mono.just(response));
//            }
//            return Mono.just(response);
//        });
//    }
//
//    @Override
//    public Mono<Optional<Organization>> createOrganization(CreateOrganizationRequest request, String createUser, String individualId, boolean isTrustedIdentify) {
//        var posIdForRep = positionsRepository.getIdByCode(AuthConstants.PositionCode.REPRESENTATIVE);
//        var posIdForOwner = positionsRepository.getIdByCode(AuthConstants.PositionCode.OWNER);
//        var getUnitTypeIdByCode = unitTypeRepository.getUnitTypeIdByCode(AuthConstants.UnitTypeCode.DEPARTMENT);
//
//        return Mono.zip(posIdForRep, posIdForOwner, getUnitTypeIdByCode)
//                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "position.not.exist")))
//                .flatMap(posId -> {
//                    log.info("posIdForRep :{}, posIdForOwner: {}", posId.getT1(), posId.getT2());
//                    Organization organization = initOrg(request, createUser);
//                    String organizationId = organization.getId();
//
//                    OrganizationUnit organizationUnit = initOrgUnit(request, organizationId, createUser, posId.getT3());
//                    String organizationUnitId = organizationUnit.getId();
//
//                    List<TenantIdentify> tenantIdentifiesForOrg = initTenants(request, organizationId, createUser, isTrustedIdentify);
//
//                    // save representative
//                    String represensiveId = String.valueOf(UUID.randomUUID());
//                    Individual represensiveDTO = request.getRepresentative();
//                    Individual represensive = new Individual(represensiveDTO);
//                    represensive.setNew(true);
//                    represensive.setId(represensiveId);
//                    represensive.setStatus(1);
//                    represensive.setCreateAt(LocalDateTime.now());
//                    represensive.setCreateBy(createUser);
//
//                    // save positionUnit for representative
//                    IndividualUnitPosition individualUnitPosition = new IndividualUnitPosition();
//                    individualUnitPosition.setId(represensiveId);
//                    individualUnitPosition.setState(1);
//                    individualUnitPosition.setStatus(1);
//                    individualUnitPosition.setIndividualId(represensiveId);
//                    individualUnitPosition.setOrganizationUnitId(organizationUnitId);
//                    individualUnitPosition.setOrganizationId(organizationId);
//                    individualUnitPosition.setPositionId(posId.getT1());
//                    individualUnitPosition.setCreateAt(LocalDateTime.now());
//                    individualUnitPosition.setCreateBy(createUser);
//
//                    List<TenantIdentify> tenantIdentifiesForRep = request.getRepresentative().getIdentifies();
//                    if (tenantIdentifiesForRep != null && tenantIdentifiesForRep.size() > 0) {
//                        for (int i = 0; i < tenantIdentifiesForRep.size(); i++) {
//                            TenantIdentify tenantIdentify = tenantIdentifiesForRep.get(i);
//                            tenantIdentify.setId(String.valueOf(UUID.randomUUID()));
//                            tenantIdentify.setTenantId(represensiveId);
//                            tenantIdentify.setType(AuthConstants.TenantType.INDIVIDUAL);
//                            tenantIdentify.setStatus(1);
//                            tenantIdentify.setCreateAt(LocalDateTime.now());
//                            tenantIdentify.setCreateBy(createUser);
//                            tenantIdentify.setNew(true);
//                            if (isTrustedIdentify) {
//                                tenantIdentify.setTrustStatus(Constants.Activation.ACTIVE);
//                            }
//                            tenantIdentifiesForRep.set(i, tenantIdentify);
//                        }
//                        tenantIdentifiesForOrg.addAll(tenantIdentifiesForRep);
//                    }
//
//                    // save owner
//                    IndividualUnitPosition ownerUnitPos = new IndividualUnitPosition();
//                    ownerUnitPos.setId(String.valueOf(UUID.randomUUID()));
//                    ownerUnitPos.setState(1);
//                    ownerUnitPos.setStatus(1);
//                    ownerUnitPos.setIndividualId(individualId);
//                    ownerUnitPos.setOrganizationId(organizationId);
//                    ownerUnitPos.setOrganizationUnitId(organizationUnitId);
//                    ownerUnitPos.setPositionId(posId.getT2());
//                    ownerUnitPos.setCreateAt(LocalDateTime.now());
//                    ownerUnitPos.setCreateBy(createUser);
//
//                    var saveOrganization = organizationRepo.save(organization);
//                    var saveOrganizationUnit = organizationUnitRepo.save(organizationUnit);
//                    var saveRepresentative = individualRepository.save(represensive);
//                    var saveTenantIdentifyForOrg = tenantIdentifyRepo.saveAll(tenantIdentifiesForOrg).collectList();
////                    var saveTenantIdentifyForRep = tenantIdentifyRepo.saveAll(tenantIdentifiesForRep);
//                    var saveIndividualUnitPosForRep = unitPositionRepo.save(individualUnitPosition);
//                    var saveOwner = unitPositionRepo.save(ownerUnitPos);
//
//                    return Mono.zip(saveOrganization, saveOrganizationUnit, saveTenantIdentifyForOrg, saveRepresentative)
//                            .flatMap(rs -> Mono.zip(saveIndividualUnitPosForRep, saveOwner))
//                            .flatMap(rs1 -> Mono.just(Optional.ofNullable(organization)));
//                });
//    }
//}
