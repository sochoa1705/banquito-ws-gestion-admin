package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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

    public List<HolidayRS> getHolidaysByName(String name){
        List <Holiday> holidayList = this.holidayRepository.findByNameLike(name);
        return this.transformToHolidaysRS(holidayList);
    }
    public List<HolidayRS> getHolidaysByType(String type) {
        List <Holiday> holidayList = this.holidayRepository.findByType(type);
        return this.transformToHolidaysRS(holidayList);
    }

    public List<HolidayRS> getHolidaysBetweenDates(Date start, Date end){
        List<Holiday> holidayList = this.holidayRepository.findByHolidayDateBetween(start, end);
        return this.transformToHolidaysRS(holidayList);
    }

    public HolidayRS obtainHoliday(String holidayUuid){
        try{
            Holiday holiday = this.holidayRepository.findByUniqueId(holidayUuid);
            if(holiday != null){
                return this.transformHolidayRS(holiday);
            }else {
                throw new RuntimeException("Holiday don't exists!");
            }
        }catch(RuntimeException ex){
            throw ex;
        }
    }

    public void createHoliday(HolidayRQ holidayRQ){
        try{
            Holiday holidayRequest = this.transformHolidayRQ(holidayRQ);
            Holiday optionalHoliday = this.holidayRepository.findByUniqueId(holidayRequest.getUniqueId());
            if(optionalHoliday == null){
                holidayRequest.setUniqueId(UUID.randomUUID().toString());
                holidayRequest.setState("ACT");
                this.holidayRepository.save(holidayRequest);
            }else{
                throw new RuntimeException("Holiday already exists!");
            }
        }catch (RuntimeException re){
            throw re;
        }
    }

    public HolidayRS updateHoliday(HolidayRQ holidayRQ){
        try{
            Holiday holiday = this.transformHolidayRQ(holidayRQ);
            Holiday updateHoliday = this.holidayRepository.findByUniqueId(holiday.getUniqueId());
            if(updateHoliday != null){
                updateHoliday.setHolidayDate(holiday.getHolidayDate());
                updateHoliday.setName(holiday.getName());
                updateHoliday.setType(holiday.getType());
                return this.transformHolidayRS(this.holidayRepository.save(updateHoliday));
            }else{
                throw new RuntimeException("Holiday aren't exists!");
            }
        }catch(RuntimeException re){
            throw re;
        }
    }

    public HolidayRS logicDeleteHoliday(String uuid){
        try{
            Holiday holiday = this.holidayRepository.findByUniqueIdAndState(uuid,"ACT");
            if(holiday != null){
                holiday.setState("INA");
                return this.transformHolidayRS(this.holidayRepository.save(holiday));
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

        if (codeCountry == null || codeCountry.isEmpty()) {
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
            HolidaysList.add(this.transformHolidayRS(holiday));
        }
        return HolidaysList;
    }

    private Holiday createWeekendHolidaywithLocation(LocalDate date, Country country, GeoLocation location){

        return Holiday.builder()
                .uniqueId(UUID.randomUUID().toString())
                .holidayDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .location(location)
                .country(country)
                .name(date.getDayOfWeek() == DayOfWeek.SATURDAY ? "SATURDAY WEEKEND" : "SUNDAY WEEKEND")
                .type(getType(country, location))
                .state("ACT").build();
    }

    private Holiday createWeekendHolidaywithoutLocation(LocalDate date, Country country){
        return Holiday.builder()
                .uniqueId(UUID.randomUUID().toString())
                .holidayDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .location(null)
                .country(country)
                .name(date.getDayOfWeek() == DayOfWeek.SATURDAY ? "SATURDAY WEEKEND" : "SUNDAY WEEKEND")
                .type("REG")
                .state("ACT").build();
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
                .uniqueId(rq.getUniqueId())
                .holidayDate(rq.getHolidayDate())
                .name(rq.getName())
                .type(rq.getType())
                .location(rq.getLocation())
                .country(rq.getCountry())
                .build();
    }

    public HolidayRS transformHolidayRS(Holiday holiday){
        return HolidayRS.builder()
                .uniqueId(holiday.getId())
                .holidayDate(holiday.getHolidayDate())
                .name(holiday.getName())
                .type(holiday.getType())
                .state(holiday.getState())
                .location(holiday.getLocation())
                .country(holiday.getCountry())
                .build();
    }

    private List<HolidayRS> transformToHolidaysRS(List<Holiday> listHoliday){
        List <HolidayRS> holidayRSList = new ArrayList<>();
        for(Holiday holiday : listHoliday){
            holidayRSList.add(this.transformHolidayRS(holiday));
        }
        return holidayRSList;
    }
}
