package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRS;
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

    public List<HolidayRS> getAllHolidays(String type) {
        List <Holiday> holidayList = this.holidayRepository.findByType(type);
        List <HolidayRS> holidayRSList = new ArrayList<>();
        for(Holiday holiday : holidayList){
            holidayRSList.add(this.responseHoliday(holiday));
        }
        return holidayRSList;
    }

    public HolidayRS obtainHoliday(String holidayId){
        try{
            Optional<Holiday> optionalHoliday = this.holidayRepository.findById(holidayId);
            if(optionalHoliday.isPresent()){
                Holiday holiday = optionalHoliday.get();
                return this.responseHoliday(holiday);
            }else {
                throw new RuntimeException("Holiday don't exists!");
            }
        }catch(RuntimeException ex){
            throw ex;
        }
    }

    public Holiday createHoliday(HolidayRQ holidayRQ){
        try{
            Holiday holidayRequest = this.transformHolidayRQ(holidayRQ);
            Optional<Holiday> optionalHoliday = this.holidayRepository.findById(holidayRequest.getId());
            if(!optionalHoliday.isPresent()){
                holidayRequest.setState("ACT");
                return this.holidayRepository.save(holidayRequest);
            }else{
                throw new RuntimeException("Holiday already exists!");
            }
        }catch (RuntimeException re){
            throw re;
        }
    }

    public HolidayRS updateHoliday(HolidayRQ holidayRQ){
        Holiday holiday = this.transformHolidayRQ(holidayRQ);
        try{
            Holiday updatelHoliday = this.holidayRepository.findById(holiday.getId()).orElse(null);
            if(updatelHoliday != null){
                updatelHoliday.setHolidayDate(holiday.getHolidayDate());
                updatelHoliday.setName(holiday.getName());
                updatelHoliday.setType(holiday.getType());
                return this.responseHoliday(this.holidayRepository.save(updatelHoliday));
            }else{
                throw new RuntimeException("Holiday aren't exists!");
            }
        }catch(RuntimeException re){
            throw re;
        }
    }

    public HolidayRS logicDeleteHoliday(String id){
        try{
            Optional<Holiday> holidayOptional = this.holidayRepository.findById(id);
            if(holidayOptional.isPresent()){
                Holiday holidayLogicDelete = holidayOptional.get();
                holidayLogicDelete.setState("INA");
                return this.responseHoliday(this.holidayRepository.save(holidayLogicDelete));
            }else{
                throw new RuntimeException("Holiday don't found!");
            }
        }catch(RuntimeException rte){
            throw rte;
        }
    }

    public List<HolidayRS> generateHolidayWeekends(int year,
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
        List<HolidayRS> HolidaysList = new ArrayList<>();
        for(Holiday holiday : this.holidayRepository.saveAll(holidayWeekend)) {
            HolidaysList.add(this.responseHoliday(holiday));
        }
        return HolidaysList;
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

    public Holiday transformHolidayRQ(HolidayRQ rq){
        return Holiday.builder()
                .id(rq.getId())
                .holidayDate(rq.getHolidayDate())
                .name(rq.getName())
                .type(rq.getType())
                .state(rq.getState())
                .version(rq.getVersion())
                .location(rq.getLocation())
                .country(rq.getCountry())
                .build();
    }

    public HolidayRS responseHoliday(Holiday holiday){
        return HolidayRS.builder()
                .id(holiday.getId())
                .holidayDate(holiday.getHolidayDate())
                .name(holiday.getName())
                .type(holiday.getType())
                .state(holiday.getState())
                .version(holiday.getVersion())
                .location(holiday.getLocation())
                .country(holiday.getCountry())
                .build();
    }
}
