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
            // invalid → do not initialise, just return false
            return false;
        }

        // --- initialise attributes when valid ---
        this.departureDate          = departureDate;
        this.departureAirportCode   = departureAirportCode;
        this.emergencyRowSeating    = emergencyRowSeating;
        this.returnDate             = returnDate;
        this.destinationAirportCode = destinationAirportCode;
        this.seatingClass           = seatingClass;
        this.adultPassengerCount    = adultPassengerCount;
        this.childPassengerCount    = childPassengerCount;
        this.infantPassengerCount   = infantPassengerCount;

        // valid so far
        return true;
    }
}