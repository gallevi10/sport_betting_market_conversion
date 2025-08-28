package com.project.converter.service;

import com.project.converter.model.InMarket;
import com.project.converter.model.InSelection;
import com.project.converter.model.OutMarket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for MarketConversionService
class MarketConversionServiceTest {

    private final MarketConversionService marketConversionService = new MarketConversionService();

    private static InSelection getInSelection(String name, double odds) {
        InSelection inSelection = new InSelection();
        inSelection.setName(name);
        inSelection.setOdds(Double.toString(odds));
        return inSelection;
    }

    @DisplayName("Test Conversion of OneXTwo Market")
    @Test
    public void testOneXTwo() {
        InMarket inMarket = new InMarket();
        inMarket.setEventId("e1");
        inMarket.setName("1x2");
        inMarket.setSelections(Arrays.asList(
            getInSelection("Team A", 1.65),
            getInSelection("draw", 3.2),
            getInSelection("Team B", 2.6)
        ));

        OutMarket outMarket = marketConversionService.convert(inMarket);

        assertAll(
            () -> assertEquals("1", outMarket.getMarketTypeId(),
                "Market type ID should be '1' for 1X2 market"),
            () -> assertEquals("e1_1", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e1_1'"),
            () -> assertEquals(3, outMarket.getSelections().size(),
                "There should be 3 selections in the output market")
        );
    }

    @DisplayName("Test Conversion of Total Market")
    @Test
    public void testTotal() {
        InMarket in = new InMarket();
        in.setEventId("e2");
        in.setName("Total");
        in.setSelections(Arrays.asList(
            getInSelection("over 2.5", 1.85),
            getInSelection("under 2.5", 1.95)
        ));

        OutMarket outMarket = marketConversionService.convert(in);

        assertAll(
            () -> assertEquals("18", outMarket.getMarketTypeId(),
                "Market type ID should be '18' for Total market"),
            () -> assertEquals("e2_18_2.5", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e2_18_2.5'"),
            () -> assertEquals(2, outMarket.getSelections().size(),
                "There should be 2 selections in the output market")
        );
    }

    @DisplayName("Test Conversion of Handicap Market")
    @Test
    public void testHandicap() {
        InMarket in = new InMarket();
        in.setEventId("e3");
        in.setName("Handicap");
        in.setSelections(Arrays.asList(
            getInSelection("Team A +1.5", 1.8),
            getInSelection("Team B -1.5", 2.0)
        ));

        OutMarket outMarket = marketConversionService.convert(in);

        assertAll(
            () -> assertEquals("16", outMarket.getMarketTypeId(),
                "Market type ID should be '16' for Handicap market"),
            () -> assertEquals("e3_16_1.5", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e3_16_1.5'"),
            () -> assertEquals(2, outMarket.getSelections().size(),
                "There should be 2 selections in the output market")
        );
    }

    @DisplayName("Test Conversion of Both Teams to Score Market")
    @Test
    public void testBtts() {
        InMarket in = new InMarket();
        in.setEventId("e4");
        in.setName("Both teams to score");
        in.setSelections(Arrays.asList(
            getInSelection("Yes", 1.7),
            getInSelection("No", 2.1)
        ));

        OutMarket outMarket = marketConversionService.convert(in);

        assertAll(
            () -> assertEquals("50", outMarket.getMarketTypeId(),
                "Market type ID should be '50' for BTTS market"),
            () -> assertEquals("e4_50", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e4_50'"),
            () -> assertEquals(2, outMarket.getSelections().size(),
                "There should be 2 selections in the output market")
        );
    }

    @DisplayName("OneXTwo Case Insensitive and Spaces")
    @Test
    public void testOneXTwoInsensitiveCaseAndSpaces() {
        InMarket inMarket = new InMarket();
        inMarket.setEventId(" e5");
        inMarket.setName("  1X2  ");
        inMarket.setSelections(Arrays.asList(
            getInSelection("  TEAM a ", 1.1),
            getInSelection(" Draw", 3.0),
            getInSelection("team B  ", 5.0)
        ));

        OutMarket outMarket = marketConversionService.convert(inMarket);
        assertAll(
            () -> assertEquals("1", outMarket.getMarketTypeId(),
                "Market type ID should be '1' for 1X2 market"),
            () -> assertEquals("e5_1", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e5_1'"),
            () -> assertEquals(3, outMarket.getSelections().size(),
                "There should be 3 selections in the output market"),
            () -> assertEquals(1, outMarket.getSelections().get(0).getSelectionTypeId(),
                "Selection type ID for Team A should be 1"),
            () -> assertEquals(2, outMarket.getSelections().get(1).getSelectionTypeId(),
                "Selection type ID for Draw should be 2"),
            () -> assertEquals(3, outMarket.getSelections().get(2).getSelectionTypeId(),
                "Selection type ID for Team B should be 3")
        );
    }

    @DisplayName("Test Support for Integer Specifier in Total Market")
    @Test
    public void testSupportForIntegerSpecifier() {
        InMarket inMarket = new InMarket();
        inMarket.setEventId("e6");
        inMarket.setName("Total");
        inMarket.setSelections(Arrays.asList(
            getInSelection("over 2", 1.8),
            getInSelection("under 2", 2.0)
        ));

        OutMarket outMarket = marketConversionService.convert(inMarket);
        assertEquals("18", outMarket.getMarketTypeId(),
            "Market type ID should be '18' for Total market");
        assertEquals("e6_18_2", outMarket.getMarketUid(),
            "Market UID should be correctly formatted. Expected 'e11_18_2'");
    }

    @DisplayName("Test Conversion of 1st Half Total Market")
    @Test
    public void testTotalFirstHalfTypeId() {
        InMarket inMarket = new InMarket();
        inMarket.setEventId("e11");
        inMarket.setName("1st half - total");
        inMarket.setSelections(Arrays.asList(
            getInSelection("over 1.5", 2.1),
            getInSelection("under 1.5", 1.7)
        ));

        OutMarket outMarket = marketConversionService.convert(inMarket);
        assertAll(
            () -> assertEquals("68", outMarket.getMarketTypeId(),
                "Market type ID should be '68' for 1st half Total market"),
            () -> assertEquals("e11_68_1.5", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e11_68_2.5'")
        );
    }

    @DisplayName("Test Handicap Market Specifier with Leading Sign")
    @Test
    public void testSpecifierWithLeadingSign() {
        InMarket inMarket = new InMarket();
        inMarket.setEventId("e12");
        inMarket.setName("Handicap");
        inMarket.setSelections(Arrays.asList(
            getInSelection("Team A -1.5", 1.9),
            getInSelection("Team B +1.5", 1.9)
        ));

        OutMarket outMarket = marketConversionService.convert(inMarket);

        assertAll(
            () -> assertEquals("16", outMarket.getMarketTypeId(),
                "Market type ID should be '16' for Handicap market"),
            () -> assertEquals("e12_16_1.5", outMarket.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e12_16_1.5'"),
            () -> assertEquals(1714, outMarket.getSelections().get(0).getSelectionTypeId(),
                "Selection type ID for Team A should be 1714"),
            () -> assertEquals(1715, outMarket.getSelections().get(1).getSelectionTypeId(),
                "Selection type ID for Team B should be 1715")
        );
    }

    @DisplayName("Test First and Second Half Handicap Markets")
    @Test
    public void testFirstAndSecondHalfHandicap() {
        InMarket inMarket1 = new InMarket("1st half - handicap", "e13",
            Arrays.asList(
                getInSelection("Team A +0.5", 1.9),
                getInSelection("Team B -0.5", 1.9)
            )
        );
        InMarket inMarket2 = new InMarket("2nd half - handicap", "e14",
            Arrays.asList(
                getInSelection("Team A +1", 2.0),
                getInSelection("Team B -1", 1.8)
            )
        );

        OutMarket outMarket1 = marketConversionService.convert(inMarket1);
        OutMarket outMarket2 = marketConversionService.convert(inMarket2);

        assertAll(
            () -> assertEquals("66", outMarket1.getMarketTypeId(),
                "Market type ID should be '66' for 1st half Handicap market"),
            () -> assertEquals("e13_66_0.5", outMarket1.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e13_66_0.5'"),
            () -> assertEquals("88", outMarket2.getMarketTypeId(),
                "Market type ID should be '88' for 2nd half Handicap market"),
            () -> assertEquals("e14_88_1", outMarket2.getMarketUid(),
                "Market UID should be correctly formatted. Expected 'e14_88_1'")
        );
    }

    @DisplayName("Exception on Unknown Total Selection Name")
    @Test
    public void exceptionOnUnknownTotalSelectionName() {
        InMarket inMarket = new InMarket("Total", "e15",
            Arrays.asList(
                getInSelection("notover 2.5", 1.8),
                getInSelection("under 2.5", 2.0)
            )
        );

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convert(inMarket),
            "Expected IllegalArgumentException for unknown total selection name");

        assertEquals("Unknown total selection name: notover 2.5", exception.getMessage(),
            "Exception message should indicate unknown total selection name");
    }

    @DisplayName("Exception on Unknown Handicap Selection Name")
    @Test
    public void exceptionOnUnknownHandicapSelectionName() {
        InMarket inMarket = new InMarket("Handicap", "e16",
            Arrays.asList(
                getInSelection("Home +1.5", 1.8),
                getInSelection("Team B -1.5", 2.0)
            )
        );
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convert(inMarket),
            "Expected IllegalArgumentException for unknown handicap selection name");

        assertEquals("Unknown handicap selection name: home +1.5", exception.getMessage(),
            "Exception message should indicate unknown handicap selection name");
    }

    @DisplayName("Exception on Blank Market Name")
    @Test
    public void exceptionOnBlankMarketName() {
        InMarket inMarket = new InMarket("   ", "Total",
            Arrays.asList(
                getInSelection("over 2.5", 1.8),
                getInSelection("under 2.5", 2.0)
            )
        );
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convert(inMarket),
            "Expected IllegalArgumentException for blank market name");
        assertEquals("Market name must not be blank", exception.getMessage(),
            "Exception message should indicate blank market name");
    }

    @DisplayName("Exception on Blank Event Id")
    @Test
    public void exceptionOnBlankEventId() {
        InMarket inMarket = new InMarket("Total", "   ",
            Arrays.asList(
                getInSelection("over 2.5", 1.8),
                getInSelection("under 2.5", 2.0)
            )
        );
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convert(inMarket),
            "Expected IllegalArgumentException for blank event id");
        assertEquals("eventId must not be blank", exception.getMessage(),
            "Exception message should indicate blank event id");
    }

    @DisplayName("Exception on Empty Selections")
    @Test
    public void exceptionOnEmptySelections() {
        InMarket inMarket = new InMarket("1x2", "e17", List.of());
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convert(inMarket),
            "Expected IllegalArgumentException for empty selections");
        assertEquals("Selections must not be null or empty", exception.getMessage(),
            "Exception message should indicate empty selections");
    }

    @DisplayName("Check if convertAll preserves order of markets")
    @Test
    public void checkIfConvertAllPreservesOrderOfMarkets() {
        InMarket inMarket1 = new InMarket("1x2", "e18",
            Arrays.asList(
                getInSelection("Team A", 1.1),
                getInSelection("draw", 2.2),
                getInSelection("Team B", 3.3)
            )
        );
        InMarket inMarket2 = new InMarket("Total", "e19",
            Arrays.asList(
                getInSelection("over 3.5", 2.1),
                getInSelection("under 3.5", 1.7)
            )
        );

        List<OutMarket> outMarkets = marketConversionService.convertAll(Arrays.asList(inMarket1, inMarket2));
        assertAll(
            () -> assertEquals(2, outMarkets.size(),
                "There should be 2 converted markets"),
            () -> assertEquals("e18_1", outMarkets.get(0).getMarketUid(),
                "First market UID should be 'e18_1'"),
            () -> assertEquals("e19_18_3.5", outMarkets.get(1).getMarketUid(),
                "Second market UID should be 'e19_18_3.5'")
        );
    }
  
}