package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.HolidayRepository;

@Service
public class HolidayService {
    
    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public List<Holiday> getAllHolidays() {
        return this.holidayRepository.findAll();
    }

    public Holiday obtainHoliday(String holidayId){
        try{
            return this.holidayRepository.findById(holidayId)
                    .orElse(null);
        }catch(RuntimeException ex){
            throw ex;
        }
    }

    public Holiday createHoliday(Holiday holiday){
        try{
            Optional<Holiday> optionalHoliday = this.holidayRepository.findById(holiday.getId());
            if(!optionalHoliday.isPresent()){
                return this.holidayRepository.save(holiday);
            }else{
                throw new RuntimeException("Holiday already exists!");
            }
        }catch (RuntimeException re){
            throw re;
        }
    }
}
