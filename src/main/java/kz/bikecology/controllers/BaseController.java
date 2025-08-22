package kz.bikecology.controllers;

import kz.bikecology.data.dao.facility.FacilityDAO;
import kz.bikecology.data.dao.facilityFuels.FacilityFuelsDAO;
import kz.bikecology.data.dao.record.RecordDAO;

import java.time.LocalDate;
import java.time.Year;

public class BaseController {
    public final FacilityDAO facilityDAO = new FacilityDAO();
    public final RecordDAO recordDAO = new RecordDAO();
    public final FacilityFuelsDAO facilityFuelsDAO = new FacilityFuelsDAO();

    static public int getCurrentQuarter() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        return (month - 1) / 3 + 1;
    }

    static public int getCurrentYear() {
        return Year.now().getValue();
    }
}
