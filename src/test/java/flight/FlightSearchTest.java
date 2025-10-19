package flight;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Cases for FlightSearch (Assessment 4)
 * Covers Conditions 1–11 and one valid case with four combinations.
 * Each test uses boundary values and verifies return value and class state.
 */
public class FlightSearchTest {

    private FlightSearch fs;

    private static final String D_OK = "10/12/2025";
    private static final String R_OK = "25/12/2025";
    private static final String D_LEAP = "29/02/2028";
    private static final String R_LEAP = "10/03/2028";

    @BeforeEach
    void setUp() {
        fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("05/12/2025", "mel", false,
                        "20/12/2025", "pvg", "economy", 1, 0, 0),
                "Setup failed: baseline valid data invalid");
    }

    // Helper methods
    private void assertStateUnchanged() {
        assertEquals("05/12/2025", fs.getDepartureDate());
        assertEquals("mel", fs.getDepartureAirportCode());
        assertEquals(false, fs.isEmergencyRowSeating());
        assertEquals("20/12/2025", fs.getReturnDate());
        assertEquals("pvg", fs.getDestinationAirportCode());
        assertEquals("economy", fs.getSeatingClass());
        assertEquals(1, fs.getAdultPassengerCount());
        assertEquals(0, fs.getChildPassengerCount());
        assertEquals(0, fs.getInfantPassengerCount());
    }

    // -----------------------------------------------------------------------
    // Condition 1: Total passengers must be between 1 and 9
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C1: total passengers must be between 1 and 9")
    void testCondition1_TotalPassengerBounds() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 0, 0, 0));
        assertStateUnchanged();

        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 9, 1, 0));
        assertStateUnchanged();
    }

    // -----------------------------------------------------------------------
    // Condition 2: Children not allowed in emergency row or first class
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C2: children cannot be in emergency row or first class")
    void testCondition2_ChildrenRestrictions() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", true, R_OK, "pvg", "economy", 1, 1, 0));
        assertStateUnchanged();

        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "first", 1, 1, 0));
        assertStateUnchanged();
    }

    // -----------------------------------------------------------------------
    // Condition 3: Infants not allowed in emergency row or business class
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C3: infants cannot be in emergency row or business class")
    void testCondition3_InfantRestrictions() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", true, R_OK, "pvg", "economy", 1, 0, 1));
        assertStateUnchanged();

        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "business", 1, 0, 1));
        assertStateUnchanged();
    }

    // -----------------------------------------------------------------------
    // Condition 4: At most 2 children per adult
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C4: at most two children per adult")
    void testCondition4_ChildrenPerAdultLimit() {
        assertTrue(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 2, 4, 0));
        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 2, 5, 0));
    }

    // -----------------------------------------------------------------------
    // Condition 5: At most 1 infant per adult
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C5: at most one infant per adult")
    void testCondition5_InfantPerAdultLimit() {
        assertTrue(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 2, 0, 2));
        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 2, 0, 3));
    }

    // -----------------------------------------------------------------------
    // Condition 6: Departure date cannot be in the past
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C6: departure date cannot be in the past")
    void testCondition6_DepartureDateNotPast() {
        assertFalse(fs.runFlightSearch("01/01/2020", "mel", false, R_OK, "pvg", "economy", 1, 0, 0));
        assertTrue(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "economy", 1, 0, 0));
    }

    // -----------------------------------------------------------------------
    // Condition 7: Strict date format and valid calendar combinations
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C7: strict date format and leap year validation")
    void testCondition7_StrictDateValidation() {
        assertFalse(fs.runFlightSearch("31/04/2026", "mel", false, R_OK, "pvg", "economy", 1, 0, 0));
        assertFalse(fs.runFlightSearch("29/02/2026", "mel", false, "05/03/2026", "pvg", "economy", 1, 0, 0));
    }

    // -----------------------------------------------------------------------
    // Condition 8: Return date cannot be before departure
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C8: return date cannot be before departure date")
    void testCondition8_ReturnDateAfterDeparture() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", false, "01/12/2025", "pvg", "economy", 1, 0, 0));
        assertTrue(fs.runFlightSearch(D_OK, "mel", false, D_OK, "pvg", "economy", 1, 0, 0));
    }

    // -----------------------------------------------------------------------
    // Condition 9: Seating class must be valid
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C9: seating class must be valid (economy, premium economy, business, first)")
    void testCondition9_ValidSeatingClass() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "ultra", 1, 0, 0));
        assertTrue(fs.runFlightSearch(D_OK, "mel", false, R_OK, "pvg", "premium economy", 1, 0, 0));
    }

    // -----------------------------------------------------------------------
    // Condition 10: Only economy can have emergency row
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C10: only economy class can have emergency row seating")
    void testCondition10_EmergencyRowEconomyOnly() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", true, R_OK, "pvg", "business", 1, 0, 0));
        assertTrue(fs.runFlightSearch(D_OK, "mel", true, R_OK, "pvg", "economy", 1, 0, 0));
    }

    // -----------------------------------------------------------------------
    // Condition 11: Airport codes must be valid and different
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("C11: airports must be valid and not the same")
    void testCondition11_AirportCodesValidation() {
        assertFalse(fs.runFlightSearch(D_OK, "mel", false, R_OK, "mel", "economy", 1, 0, 0));
        assertFalse(fs.runFlightSearch(D_OK, "xxx", false, R_OK, "pvg", "economy", 1, 0, 0));
        assertTrue(fs.runFlightSearch(D_OK, "syd", false, R_OK, "cdg", "economy", 2, 0, 0));
    }

    // -----------------------------------------------------------------------
    // Valid Case: All inputs correct (4 combinations)
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Valid Case: All inputs meet every condition (4 combos)")
    void testValidCase_AllInputsCorrect() {
        assertTrue(fs.runFlightSearch("10/12/2025", "syd", false, "25/12/2025", "cdg", "economy", 3, 2, 1));
        assertTrue(fs.runFlightSearch("05/12/2025", "mel", false, "20/12/2025", "doh", "premium economy", 2, 0, 0));
        assertTrue(fs.runFlightSearch("05/12/2025", "del", false, "20/12/2025", "lax", "business", 2, 2, 0));
        assertTrue(fs.runFlightSearch(D_LEAP, "pvg", true, R_LEAP, "syd", "economy", 2, 0, 0));
    }
}