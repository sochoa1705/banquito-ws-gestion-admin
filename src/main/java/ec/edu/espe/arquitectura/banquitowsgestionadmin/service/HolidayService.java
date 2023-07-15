package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.HolidayRepository;

@Service
public class HolidayService {
    
    private final HolidayRepository holidayRepository;
    private final GeoLocationRepository geoLocationRepository;

    public HolidayService(HolidayRepository holidayRepository, GeoLocationRepository geoLocationRepository) {
        this.holidayRepository = holidayRepository;
        this.geoLocationRepository = geoLocationRepository;
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public List<Holiday> getAllHolidaysWithLocation() {
        List<Holiday> holidays = holidayRepository.findAll();   
        return holidays;
    }
}
