package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class FlightSearch {

    // --- Class attributes (store flight search details) ---
    private String departureDate;
    private String departureAirportCode;
    private boolean emergencyRowSeating;
    private String returnDate;
    private String destinationAirportCode;
    private String seatingClass;
    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;

    // Date format setup
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    // Allowed airport codes as per the system requirements
    private static final String[] VALID_AIRPORTS = {"syd", "mel", "lax", "cdg", "del", "pvg", "doh"};

    public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating,
                                   String returnDate, String destinationAirportCode, String seatingClass,
                                   int adultPassengerCount, int childPassengerCount, int infantPassengerCount) {

        // C1: Total passengers must be between 1 and 9
        int totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;
        if (totalPassengers < 1 || totalPassengers > 9) {
            // If total passenger count is outside the allowed range, stop immediately.
            return false;
        }

        // C2: Children cannot sit in the emergency row or first class
        if (childPassengerCount > 0) {
            if (emergencyRowSeating) return false;
            if ("first".equals(seatingClass)) return false;
        }

        // C3: Infants cannot sit in the emergency row or business class
        if (infantPassengerCount > 0) {
            if (emergencyRowSeating) return false;
            if ("business".equals(seatingClass)) return false;
        }

        // C4: Up to 2 children can be seated per adult
        if (childPassengerCount > 2 * adultPassengerCount) {
            return false;
        }

        // C5: Each adult can hold only one infant
        if (infantPassengerCount > adultPassengerCount) {
            return false;
        }

        // C6 & C7: Validate both dates strictly and check leap years
        LocalDate depDate;
        LocalDate retDate;
        try {
            depDate = LocalDate.parse(departureDate, DATE_FMT);
            retDate = LocalDate.parse(returnDate, DATE_FMT);
        } catch (Exception e) {
            // If date format is wrong or the date doesn’t exist, it’s invalid
            return false;
        }

        // C6: The departure date cannot be in the past
        if (depDate.isBefore(LocalDate.now())) {
            return false;
        }

        // C8: Return date cannot be before the departure date
        if (retDate.isBefore(depDate)) {
            return false;
        }

        // C9: Seating class must be one of the allowed types
        boolean validClass = seatingClass.equals("economy") ||
                seatingClass.equals("premium economy") ||
                seatingClass.equals("business") ||
                seatingClass.equals("first");
        if (!validClass) {

            return false;
        }

        // C10: Only economy class can have emergency row seating
        if (emergencyRowSeating && !"economy".equals(seatingClass)) {

            return false;
        }

        // C11: Airport codes must be valid and cannot be the same
        boolean depValid = false;
        boolean destValid = false;

        // Check if both airport codes exist in the allowed list
        for (String code : VALID_AIRPORTS) {
            if (code.equals(departureAirportCode)) depValid = true;
            if (code.equals(destinationAirportCode)) destValid = true;
        }

        // If either code is invalid or both are the same, reject the search
        if (!depValid || !destValid) return false;
        if (departureAirportCode.equals(destinationAirportCode)) return false;

        // All validations passed — store details in the class attributes
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.emergencyRowSeating = emergencyRowSeating;
        this.returnDate = returnDate;
        this.destinationAirportCode = destinationAirportCode;
        this.seatingClass = seatingClass;
        this.adultPassengerCount = adultPassengerCount;
        this.childPassengerCount = childPassengerCount;
        this.infantPassengerCount = infantPassengerCount;


        return true;
    }

    // Getter methods
    public String getDepartureDate() { return departureDate; }
    public String getDepartureAirportCode() { return departureAirportCode; }
    public boolean isEmergencyRowSeating() { return emergencyRowSeating; }
    public String getReturnDate() { return returnDate; }
    public String getDestinationAirportCode() { return destinationAirportCode; }
    public String getSeatingClass() { return seatingClass; }
    public int getAdultPassengerCount() { return adultPassengerCount; }
    public int getChildPassengerCount() { return childPassengerCount; }
    public int getInfantPassengerCount() { return infantPassengerCount; }
}