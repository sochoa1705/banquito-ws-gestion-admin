package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class    CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CountryRS obtain(String countryCode) {
        Country country = this.countryRepository.findByCode(countryCode);
        return this.transformCountry(country);
    }

    public CountryRS obtainByName(String countryName) {
        Country country = this.countryRepository.findByName(countryName);
        return this.transformCountry(country);
    }

    public List<CountryRS> getAllCountries(){
        List<Country> countries = this.countryRepository.findAll();
        List<CountryRS> countriesList = new ArrayList<>();
        for(Country country : countries){
            countriesList.add(this.transformCountry(country));
        }
        return countriesList;
    }

    public void createCountry(CountryRQ countryRQ) {
       try{
           Country country = this.transformCountryRQ(countryRQ);
           country.setStatus("ACT");
           this.countryRepository.save(country);
       }catch (RuntimeException rte) {
           throw new RuntimeException(rte);
       }

    }

    public CountryRS updateCountry(CountryRQ countryRQ) {

        Country country = this.transformCountryRQ(countryRQ);
        try{
            Country countryUpdate = this.countryRepository.findByCode(country.getCode());
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
            Country countryLogicDelete = this.countryRepository.findByCode(id);
            if(countryLogicDelete != null) {
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
                .build();
        return countryRS;
    }
}
