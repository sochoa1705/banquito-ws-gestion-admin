package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.HolidayRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;

@Service
public class HolidayService {
    
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final GeoLocationRepository geoLocationRepository;

    
    public HolidayService(HolidayRepository holidayRepository,
            ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository countryRepository,
            ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository geoLocationRepository) {
        this.holidayRepository = holidayRepository;
        this.countryRepository = countryRepository;
        this.geoLocationRepository = geoLocationRepository;
    }

    public void createHoliday(HolidayRQ holidayRQ){
        Holiday holiday = this.transform(holidayRQ);
        this.holidayRepository.save(holiday);
    }

    public List<Holiday> listByName(String name){
        return this.holidayRepository.findByNameLike(name);
    }

    public List<Holiday> listByType(String type){
        return this.holidayRepository.findByType(type);
    }

    private Holiday transform(HolidayRQ rq){
        GeoLocation location = geoLocationRepository.findById(rq.getLocation()).orElse(null);
        Country country = countryRepository.findById(rq.getLocation()).orElse(null);
        return Holiday.builder().holidayDate(rq.getHolidayDate())
                .location(location)
                .country(country)
                .name(rq.getName())
                .type(rq.getType())
                .build();
        
    }
}
