package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.*;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankEntityService {

    private final BankEntityRepository bankEntityRepository;
    private final GeoLocationRepository geoLocationRepository;
    private final BranchRepository branchRepository;
    private final GeoStructureRepository geoStructureRepository;
    private final CountryRepository countryRepository;

    public BankEntityService(BankEntityRepository bankEntityRepository,
                             GeoLocationRepository geoLocationRepository,
                             BranchRepository branchRepository,
                             GeoStructureRepository geoStructureRepository,
                             CountryRepository countryRepository){

        this.bankEntityRepository = bankEntityRepository;
        this.geoLocationRepository = geoLocationRepository;
        this.branchRepository = branchRepository;
        this.geoStructureRepository = geoStructureRepository;
        this.countryRepository = countryRepository;
    }

    public String getGeoLocationNameByBranchUniqueKey(String bankEntityId, String branchUniqueKey) {
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    String locationId = branch.getLocationId();
                    GeoLocation geoLocation = geoLocationRepository.findById(locationId).orElse(null);
                    if (geoLocation != null) {
                        return geoLocation.getName();
                    } else {
                        return "GeoLocalización desconocida";
                    }
                }
            }
        }
        return null;
    }

    public CountryAndBranchCode getCountryAndBranchCodeByBranchUniqueKey(String branchUniqueKey) {
        BankEntity bankEntity = bankEntityRepository.findById("64b1892b9c2c3b03c33a736f").orElse(null);
        if (bankEntity != null) {
            List<Branch> branchList = bankEntity.getBranches();
            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    String locationId = branch.getLocationId();
                    GeoLocation geoLocation = geoLocationRepository.findById(locationId).orElse(null);
                    if (geoLocation != null) {
                        GeoStructure geoStructure = geoStructureRepository.findByLocations_Id(locationId);
                        if (geoStructure != null && geoStructure.getCountry() != null) {
                            String countryCode = geoStructure.getCountry().getCode();
                            String countryCodeInitials = countryCode.substring(0, 2); // Obtener las dos primeras letras
                            String branchCodeNumber = branch.getCodeNumber();
                            return new CountryAndBranchCode(countryCodeInitials, branchCodeNumber);
                        } else {
                            return new CountryAndBranchCode("País desconocido", "");
                        }
                    } else {
                        return new CountryAndBranchCode("GeoLocalización desconocida", "");
                    }
                }
            }
        }
        return null;
    }




    private static String generateRandomLetters(int count) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomLetters = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(letters.length());
            randomLetters.append(letters.charAt(randomIndex));
        }

        return randomLetters.toString();
    }

    // Generador de números aleatorios
    private static String generateRandomNumbers(int count) {
        StringBuilder randomNumbers = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.append(randomNumber);
        }

        return randomNumbers.toString();
    }

    // Generador de código SWIFT
    private String generateSWIFTCode() {
        String base = "BQUIEC";
        String randomLetters = generateRandomLetters(2);
        String randomNumbers = generateRandomNumbers(2);

        return base + randomLetters + randomNumbers;
    }

    public BankEntity obtain(String id, String internationalCode){
        return this.bankEntityRepository.findBankEntityByIdAndInternationalCode(id, internationalCode);
    }

    public List<BranchRS> listAllBranchesByEntity(String bankEntityId){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            List<BranchRS> branchRSList = new ArrayList<>();
            for(Branch branch : branchList){
                branchRSList.add(this.transformBranch(branch));
            }
            return branchRSList;
        }
        return null;
    }

    public BranchRS BranchByCode(String bankEntityId, String branchUniqueKey){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    return this.transformBranch(branch);
                }
            }
        }
        return null;
    }

    public BankEntity addBranch(String bankEntityId, BranchRQ branchRQ){
        try{
            Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
            if (optionalBankEntity.isPresent()) {
                BankEntity bankEntity = optionalBankEntity.get();
                List<Branch> branchList = bankEntity.getBranches();
                Branch newBranch = this.transformBranchRQ(branchRQ);
                newBranch.setState("ACT");
                newBranch.setUniqueKey(UUID.randomUUID().toString());
                String swiftCode = generateSWIFTCode();
                newBranch.setCode(swiftCode); // Establecer el código SWIFT generado
                newBranch.setCreationDate(Date.from(new Date().toInstant()));
                branchList.add(newBranch);
                bankEntity.setBranches(branchList);
                return bankEntityRepository.save(bankEntity);
            }
            return null;
        }catch (RuntimeException rte){
            throw new RuntimeException("La Sucursal no pudo ser creada: ", rte);
        }
    }

    public BankEntity updateBranch(String bankEntityId, String branchUniqueKey, BranchRQ branchRQ){
        try {
            Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
            if (optionalBankEntity.isPresent()) {
                BankEntity bankEntity = optionalBankEntity.get();
                List<Branch> branchList = bankEntity.getBranches();

                boolean branchUpdated = false;

                for (Branch branch : branchList) {
                    if (branch.getUniqueKey().equals(branchUniqueKey)) {
                        branch.setName(branchRQ.getName());
                        branch.setEmailAddress(branchRQ.getEmailAddress());
                        branch.setPhoneNumber(branchRQ.getPhoneNumber());
                        branchUpdated = true;
                        break;
                    }
                }

                if (!branchUpdated) {
                    Branch newBranch = this.transformBranchRQ(branchRQ);
                    branchList.add(newBranch);
                }

                bankEntity.setBranches(branchList);
                return bankEntityRepository.save(bankEntity);
            }
            return null;
        } catch (RuntimeException rte) {
            throw new RuntimeException("La Sucursal no pudo ser creada: ", rte);
        }
    }

    public BranchRS deleteBranch(String bankEntityId, String branchUniqueKey){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        BranchRS rs = null;
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    branch.setState("INA");
                    rs = this.transformBranch(branch);
                }
            }
            bankEntity.setBranches(branchList);
            this.bankEntityRepository.save(bankEntity);
            return rs;
        }
        return null;
    }

    public BranchRS transformBranch(Branch branch){
        BranchRS branchRS = BranchRS
                .builder()
                .locationId(branch.getLocationId())
                .code(branch.getCode())
                .name(branch.getName())
                .uniqueKey(branch.getUniqueKey())
                .state(branch.getState())
                .creationDate(branch.getCreationDate())
                .emailAddress(branch.getEmailAddress())
                .phoneNumber(branch.getPhoneNumber())
                .line1(branch.getLine1())
                .line2(branch.getLine2())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .codeNumber(branch.getCodeNumber())
                .build();
            return branchRS;
    }


    public Branch transformBranchRQ(BranchRQ rq){
        Branch branch = Branch
                .builder()
                .locationId(rq.getLocationId())
                .code(rq.getCode())
                .name(rq.getName())
                .emailAddress(rq.getEmailAddress())
                .phoneNumber(rq.getPhoneNumber())
                .line1(rq.getLine1())
                .line2(rq.getLine2())
                .latitude(rq.getLatitude())
                .longitude(rq.getLongitude())
                .build();
        return branch;
    }

}
