package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.HolidayRepository;

@Service
public class HolidayService {
    
    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
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
        return Holiday.builder().holidayDate(rq.getHolidayDate())
                .locationId(rq.getLocationId())
                .countryId(rq.getCountryId())
                .name(rq.getName())
                .type(rq.getType())
                .build();
        
    }
}
