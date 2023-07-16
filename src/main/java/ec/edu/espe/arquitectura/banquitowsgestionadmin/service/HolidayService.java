package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoStructureRepository;
import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.HolidayRepository;

@Service
public class HolidayService {
    
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final GeoLocationRepository geoLocationRepository;
    private final GeoStructureRepository geoStructureRepository;

    public HolidayService(HolidayRepository holidayRepository,
                          CountryRepository countryRepository,
                          GeoLocationRepository geoLocationRepository,
                          GeoStructureRepository geoStructureRepository) {
        this.holidayRepository = holidayRepository;
        this.countryRepository = countryRepository;
        this.geoLocationRepository = geoLocationRepository;
        this.geoStructureRepository = geoStructureRepository;
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
                holiday.setState("ACT");
                return this.holidayRepository.save(holiday);
            }else{
                throw new RuntimeException("Holiday already exists!");
            }
        }catch (RuntimeException re){
            throw re;
        }
    }

    public Holiday updateHoliday(Holiday holiday){
        try{
            Holiday updatelHoliday = this.holidayRepository.findById(holiday.getId()).orElse(null);
            if(updatelHoliday != null){
                updatelHoliday.setHolidayDate(holiday.getHolidayDate());
                updatelHoliday.setName(holiday.getName());
                updatelHoliday.setType(holiday.getType());
                return this.holidayRepository.save(updatelHoliday);
            }else{
                throw new RuntimeException("Holiday aren't exists!");
            }
        }catch(RuntimeException re){
            throw re;
        }
    }

    public Holiday logicDeleteHoliday(String id){
        try{
            Optional<Holiday> holidayOptional = this.holidayRepository.findById(id);
            if(holidayOptional.isPresent()){
                Holiday holidayLogicDelete = holidayOptional.get();
                holidayLogicDelete.setState("INA");
                return this.holidayRepository.save(holidayLogicDelete);
            }else{
                throw new RuntimeException("Holiday don't found!");
            }
        }catch(RuntimeException rte){
            throw rte;
        }
    }

    public List<Holiday> generateHolidayeekends(int year,
                                                int month,
                                                boolean saturday,
                                                boolean sunday,
                                                String codeCountry,
                                                String idLocation){

        if (codeCountry == null) {
            throw new RuntimeException();
        }

        GeoLocation location = null;
        if(idLocation != null && !idLocation.isEmpty()){
            Optional<GeoLocation> optionalLocation = this.geoLocationRepository.findById(idLocation);
            if (optionalLocation.isPresent()) {
                location = optionalLocation.get();
            }
        }
        List<Holiday> holidayWeekend = new ArrayList<>();
        Country country = this.countryRepository.findByCode(codeCountry);
        if (country == null) {
            throw new RuntimeException();
        }
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)){
            DayOfWeek dayOfWeek = startDate.getDayOfWeek();
            if((saturday && dayOfWeek == DayOfWeek.SATURDAY) || (sunday && dayOfWeek == DayOfWeek.SUNDAY)){
                if(location != null ){
                    holidayWeekend.add(this.createWeekendHolidaywithLocation(startDate, country, location));
                }else{
                    holidayWeekend.add(this.createWeekendHolidaywithoutLocation(startDate, country));
                }
            }
            startDate = startDate.plusDays(1);
        }
        return this.holidayRepository.saveAll(holidayWeekend);
    }

    private Holiday createWeekendHolidaywithLocation(LocalDate date, Country country, GeoLocation location){

        return Holiday.builder()
                .holidayDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .location(location)
                .country(country)
                .name(date.getDayOfWeek() == DayOfWeek.SATURDAY ? "SATURDAY WEEKEND" : "SUNDAY WEEKEND")
                .type(getType(country, location))
                .state("ACT")
                .version(0).build();
    }

    private Holiday createWeekendHolidaywithoutLocation(LocalDate date, Country country){
        return Holiday.builder()
                .holidayDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .location(null)
                .country(country)
                .name(date.getDayOfWeek() == DayOfWeek.SATURDAY ? "SATURDAY WEEKEND" : "SUNDAY WEEKEND")
                .type("REG")
                .state("ACT")
                .version(0).build();
    }

    private String getType(Country country, GeoLocation location){
        GeoStructure geoStructure = this.geoStructureRepository.findByCountryAndLocationsContaining(country, location);
        if(geoStructure != null){
            if(geoStructure.getLevelCode() == 1){
                return "NAT";
            }else{
                return "REG";
            }
        }else{
            return "NOT FOUND";
        }
    }
}
