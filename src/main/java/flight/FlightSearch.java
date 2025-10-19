package flight;

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
            if (emergencyRowSeating) {
                return false; // no children in emergency row
            }
            if ("first".equalsIgnoreCase(seatingClass)) {
                return false; // no children in first class
            }
        }

        // ✅ Condition 3: infants cannot be in emergency row or business class
        if (infantPassengerCount > 0) {
            if (emergencyRowSeating) {
                return false; // infants not in emergency row
            }
            if ("business".equalsIgnoreCase(seatingClass)) {
                return false; // infants not in business class
            }
        }

        // ✅ Condition 4: at most 2 children per adult
        if (childPassengerCount > 2 * adultPassengerCount) {
            return false;
        }

        // --- initialise attributes when all checks pass so far ---
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