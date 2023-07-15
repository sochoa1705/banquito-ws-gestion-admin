package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CountryRS obtain(String phoneCode) {
        Country country = this.countryRepository.findByPhoneCode(phoneCode);
        return this.transformCountry(country);
    }

    public void createCountry(CountryRQ countryRQ) {
        Country country = this.transformCountryRQ(countryRQ);
        country.setStatus("ACT");
        this.countryRepository.save(country);
    }

    public CountryRS updateCountry(CountryRQ countryRQ) {

        Country country = this.transformCountryRQ(countryRQ);
        try{
            Country countryUpdate = this.countryRepository.findFirstByName(country.getName());
            if(countryUpdate == null) {
                throw new RuntimeException("Country "+country.getName()+" not found");
            }else {
                countryUpdate.setCode(country.getCode());
                countryUpdate.setName(country.getName());
                countryUpdate.setPhoneCode(country.getPhoneCode());
                this.countryRepository.save(countryUpdate);
                return this.transformCountry(countryUpdate);
            }
        }catch (RuntimeException rte)
        {
            throw new RuntimeException(rte);
        }
    }

    public CountryRS logicDelete(String id){
        try {
            Optional<Country> countryOptional = this.countryRepository.findById(id);
            if(countryOptional.isPresent()) {
                Country countryLogicDelete = countryOptional.get();
                countryLogicDelete.setStatus("INA");
                this.countryRepository.save(countryLogicDelete);
                return this.transformCountry(countryLogicDelete);
            }else {
                throw new RuntimeException("Country not found: " + id);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("Country not found and not can't be deleted: " + id, rte);
        }
    }

    public Country transformCountryRQ(CountryRQ rq){
        Country country = Country
                .builder()
                .code(rq.getCode())
                .name(rq.getName())
                .phoneCode(rq.getPhoneCode())
                .build();
        return country;
    }

    public CountryRS transformCountry(Country country){
        CountryRS countryRS = CountryRS
                .builder()
                .code(country.getCode())
                .name(country.getName())
                .phoneCode(country.getPhoneCode())
                .status(country.getStatus())
                .build();
        return countryRS;
    }
}
