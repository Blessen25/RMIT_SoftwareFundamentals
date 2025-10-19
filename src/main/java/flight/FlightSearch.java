package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class FlightSearch {
    private String  departureDate;
    private String  departureAirportCode;
    private boolean emergencyRowSeating;
    private String  returnDate;
    private String  destinationAirportCode;
    private String  seatingClass;
    private int     adultPassengerCount;
    private int     childPassengerCount;
    private int     infantPassengerCount;

    // Strict date format validation (handles leap years, correct day/month combinations)
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public boolean runFlightSearch(String departureDate,    String departureAirportCode,   boolean emergencyRowSeating,
                                   String returnDate,       String destinationAirportCode, String seatingClass,
                                   int adultPassengerCount, int childPassengerCount,       int infantPassengerCount) {

        // ✅ Condition 1: total passengers must be between 1 and 9
        int totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;
        if (totalPassengers < 1 || totalPassengers > 9) {
            return false;
        }

        // ✅ Condition 2: children cannot be in emergency row or first class
        if (childPassengerCount > 0) {
            if (emergencyRowSeating) return false;
            if ("first".equals(seatingClass)) return false;
        }

        // ✅ Condition 3: infants cannot be in emergency row or business class
        if (infantPassengerCount > 0) {
            if (emergencyRowSeating) return false;
            if ("business".equals(seatingClass)) return false;
        }

        // ✅ Condition 4: at most 2 children per adult
        if (childPassengerCount > 2 * adultPassengerCount) {
            return false;
        }

        // ✅ Condition 5: at most 1 infant per adult
        if (infantPassengerCount > adultPassengerCount) {
            return false;
        }

        // ✅ Conditions 6 + 7: strict format & leap-year validation
        final LocalDate depDate;
        final LocalDate retDate;
        try {
            depDate = LocalDate.parse(departureDate, DATE_FMT);
            retDate = LocalDate.parse(returnDate, DATE_FMT);
        } catch (Exception e) {
            return false;
        }

        // ✅ Condition 6: departure cannot be in the past (today allowed)
        LocalDate today = LocalDate.now();
        if (depDate.isBefore(today)) {
            return false;
        }

        // ✅ Condition 8: return date cannot be before departure
        if (retDate.isBefore(depDate)) {
            return false;
        }

        // ✅ Condition 9: seating class must be one of the allowed values
        boolean classOk =
                "economy".equals(seatingClass) ||
                        "premium economy".equals(seatingClass) ||
                        "business".equals(seatingClass) ||
                        "first".equals(seatingClass);
        if (!classOk) {
            return false;
        }

        // ✅ Condition 10: only economy can have emergency row seating
        if (emergencyRowSeating && !"economy".equals(seatingClass)) {
            return false;
        }

        // --- initialise attributes when all checks pass ---
        this.departureDate          = departureDate;
        this.departureAirportCode   = departureAirportCode;
        this.emergencyRowSeating    = emergencyRowSeating;
        this.returnDate             = returnDate;
        this.destinationAirportCode = destinationAirportCode;
        this.seatingClass           = seatingClass;
        this.adultPassengerCount    = adultPassengerCount;
        this.childPassengerCount    = childPassengerCount;
        this.infantPassengerCount   = infantPassengerCount;

        return true;
    }
}