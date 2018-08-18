package com;

import org.shredzone.commons.suncalc.MoonPosition;

import java.util.Date;

public class CalcOfMoon {
    public Double attutiudeMunPosition(Date date){
        double lat = 51.5033640;
        double lng = -0.1276250;
        MoonPosition position = MoonPosition.compute()
                .on(date)       // set a date
                .at(lat, lng)   // set a location
                .execute();     // get the results
        return position.getAltitude();
    }

    public Double distanceOfMoon(Date date){
        double lat = 51.5033640;
        double lng = -0.1276250;
        MoonPosition position = MoonPosition.compute()
                .on(date)
                .at(lat, lng)
                .execute();
        return position.getAzimuth();
    }
}
