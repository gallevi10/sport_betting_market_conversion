package com.project.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.converter.model.InMarket;
import com.project.converter.model.OutMarket;
import com.project.converter.service.MarketConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// End-to-end tests for the ConverterApp
class ConverterAppTest {

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }


    @DisplayName("Test Conversion of Full JSON Example")
    @Test
    public void testFullJsonExample() throws Exception {
        String input = "[{\"name\":\"1x2\",\"event_id\":\"123456\",\"selections\":["
            + "{\"name\":\"Team A\",\"odds\":1.65},"
            + "{\"name\":\"draw\",\"odds\":3.2},"
            + "{\"name\":\"Team B\",\"odds\":2.6}"
            + "]}]";

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        MarketConversionService service = new MarketConversionService();
        List<OutMarket> outMarkets = service.convertAll(inMarkets);

        OutMarket outMarket = outMarkets.get(0); // only one market in this test

        assertAll(
            () -> assertEquals("1", outMarket.getMarketTypeId(),
                "Market type ID should be '1' for 1X2 market"),
            () -> assertEquals("123456_1", outMarket.getMarketUid(),
                "Market UID should be correctly formatted"),
            () -> assertEquals(3, outMarket.getSelections().size(),
                "There should be 3 selections in the output market")
        );
    }

    @DisplayName("Test Conversion of Total Market with Decimal Specifier")
    @Test
    public void testTotalDecimalSpecifier() throws Exception {
        String input = """
        [{"name":"Total","event_id":"e2","selections":[
          {"name":"over 2.5","odds":1.85},
          {"name":"under 2.5","odds":1.95}
        ]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        OutMarket outMarket = new MarketConversionService().convertAll(inMarkets).get(0);

        assertAll(
            () -> assertEquals("18", outMarket.getMarketTypeId(),
                "Market type ID should be '18' for Total market"),
            () -> assertEquals("e2_18_2.5", outMarket.getMarketUid(),
                "Market UID should include decimal specifier 2.5")
        );
    }

    @DisplayName("Test Conversion of Total Market with Integer Specifier")
    @Test
    public void testTotalIntegerSpecifier() throws Exception {
        String input = """
        [{"name":"Total","event_id":"e3","selections":[
          {"name":"over 2","odds":1.9},
          {"name":"under 2","odds":1.9}
        ]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        OutMarket outMarket = new MarketConversionService().convertAll(inMarkets).get(0);

        assertEquals("e3_18_2", outMarket.getMarketUid(),
            "Market UID should contain '2' without trailing '.0'");
    }

    @DisplayName("Test Conversion of 1st Half Total Market")
    @Test
    public void testTotalFirstHalf() throws Exception {
        String input = """
        [{"name":"1st half - total","event_id":"e4","selections":[
          {"name":"over 1.5","odds":2.1},
          {"name":"under 1.5","odds":1.7}
        ]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        OutMarket outMarket = new MarketConversionService().convertAll(inMarkets).get(0);

        assertAll(
            () -> assertEquals("68", outMarket.getMarketTypeId(),
                "Market type ID should be '68' for 1st half Total market"),
            () -> assertEquals("e4_68_1.5", outMarket.getMarketUid(),
                "Market UID should include 1.5 as specifier")
        );
    }

    @DisplayName("Exception on Unknown Total Selection Name")
    @Test
    public void exceptionOnUnknownTotalSelection() throws Exception {
        String input = """
        [{"name":"Total","event_id":"e5","selections":[
          {"name":"notover 2.5","odds":1.8},
          {"name":"under 2.5","odds":2.0}
        ]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        MarketConversionService marketConversionService = new MarketConversionService();

        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convertAll(inMarkets),
            "Expected IllegalArgumentException for unknown total selection name");
        assertEquals("Unknown total selection name: notover 2.5", ex.getMessage(),
            "Exception message should indicate unknown total selection name");
    }

    @DisplayName("Test Conversion of Handicap Market with Absolute Specifier")
    @Test
    public void testHandicapSpecifierAbsolute() throws Exception {
        String input = """
        [{"name":"Handicap","event_id":"e6","selections":[
          {"name":"Team A -1.5","odds":1.9},
          {"name":"Team B +1.5","odds":1.9}
        ]}]
        """;

        OutMarket outMarket = new MarketConversionService()
            .convertAll(objectMapper.readValue(input, new TypeReference<>() {})).get(0);

        assertAll(
            () -> assertEquals("16", outMarket.getMarketTypeId(),
                "Market type ID should be '16' for Handicap market"),
            () -> assertEquals("e6_16_1.5", outMarket.getMarketUid(),
                "Market UID should use absolute specifier 1.5")
        );
    }

    @DisplayName("Test Conversion of First and Second Half Handicap Markets")
    @Test
    public void testHandicapHalfMarkets() throws Exception {
        String input = """
        [
          {"name":"1st half - handicap","event_id":"e7","selections":[
            {"name":"Team A +0.5","odds":1.9},
            {"name":"Team B -0.5","odds":1.9}
          ]},
          {"name":"2nd half - handicap","event_id":"e8","selections":[
            {"name":"Team A +1","odds":2.0},
            {"name":"Team B -1","odds":1.8}
          ]}
        ]
        """;

        List<OutMarket> outMarkets = new MarketConversionService()
            .convertAll(objectMapper.readValue(input, new TypeReference<>() {}));

        assertAll(
            () -> assertEquals("66", outMarkets.get(0).getMarketTypeId(),
                "Market type ID should be '66' for 1st half Handicap market"),
            () -> assertEquals("e7_66_0.5", outMarkets.get(0).getMarketUid(),
                "Market UID should include 0.5"),
            () -> assertEquals("88", outMarkets.get(1).getMarketTypeId(),
                "Market type ID should be '88' for 2nd half Handicap market"),
            () -> assertEquals("e8_88_1", outMarkets.get(1).getMarketUid(),
                "Market UID should include 1")
        );
    }

    @DisplayName("Exception on Unknown Handicap Selection Name")
    @Test
    public void exceptionOnUnknownHandicapSelection() throws Exception {
        String input = """
        [{"name":"Handicap","event_id":"e9","selections":[
          {"name":"Home +1.5","odds":1.8},
          {"name":"Team B -1.5","odds":2.0}
        ]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        MarketConversionService marketConversionService = new MarketConversionService();

        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convertAll(inMarkets),
            "Expected IllegalArgumentException for unknown handicap selection name");
        assertEquals("Unknown handicap selection name: home +1.5", ex.getMessage(),
            "Exception message should indicate unknown handicap selection name");
    }

    @DisplayName("Test Conversion of BTTS Market")
    @Test
    public void testBttsMarket() throws Exception {
        String input = """
        [{"name":"Both teams to score","event_id":"e10","selections":[
          {"name":"Yes","odds":1.7},
          {"name":"No","odds":2.1}
        ]}]
        """;

        OutMarket outMarket = new MarketConversionService()
            .convertAll(objectMapper.readValue(input, new TypeReference<>() {})).get(0);

        assertAll(
            () -> assertEquals("50", outMarket.getMarketTypeId(),
                "Market type ID should be '50' for BTTS market"),
            () -> assertEquals("e10_50", outMarket.getMarketUid(),
                "Market UID should be eventId_typeId without specifier")
        );
    }

    @DisplayName("Case Insensitive Names and Order Preservation")
    @Test
    public void testCaseInsensitiveAndOrder() throws Exception {
        String input = """
        [
          {"name":" 1X2 ","event_id":" e11 ","selections":[
            {"name":"  TEAM a ", "odds":1.1},
            {"name":" Draw",    "odds":3.0},
            {"name":"team B  ", "odds":5.0}
          ]},
          {"name":"Total","event_id":"e12","selections":[
            {"name":"over 3.5","odds":2.1},
            {"name":"under 3.5","odds":1.7}
          ]}
        ]
        """;

        List<OutMarket> outMarkets = new MarketConversionService()
            .convertAll(objectMapper.readValue(input, new TypeReference<>() {}));

        assertAll(
            () -> assertEquals("e11_1", outMarkets.get(0).getMarketUid(),
                "First market should stay first"),
            () -> assertEquals("e12_18_3.5", outMarkets.get(1).getMarketUid(),
                "Second market should stay second")
        );
    }

    @DisplayName("Exception on Blank Event Id")
    @Test
    public void exceptionOnBlankEventId() throws Exception {
        String input = """
        [{"name":"Total","event_id":"   ","selections":[
          {"name":"over 2.5","odds":1.8},
          {"name":"under 2.5","odds":2.0}
        ]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        MarketConversionService marketConversionService = new MarketConversionService();

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convertAll(inMarkets),
            "Expected IllegalArgumentException for blank eventId");

        assertEquals("eventId must not be blank", exception.getMessage(),
            "Exception message should indicate blank eventId");
    }

    @DisplayName("Exception on Blank Market Name")
    @Test
    public void exceptionOnBlankMarketName() throws Exception {
        String input = """
        [{"name":"   ","event_id":"e99","selections":[
          {"name":"Yes","odds":1.7},
          {"name":"No","odds":2.1}
        ]}]
        """;

        List<InMarket> in = objectMapper.readValue(input, new TypeReference<>() {});
        MarketConversionService svc = new MarketConversionService();

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> svc.convertAll(in),
            "Expected IllegalArgumentException for blank market name");

        assertEquals("Market name must not be blank", exception.getMessage(),
            "Exception message should indicate blank market name");

    }

    @DisplayName("Exception on Empty Selections")
    @Test
    public void exceptionOnEmptySelections() throws Exception {
        String input = """
        [{"name":"1x2","event_id":"e13","selections":[]}]
        """;

        List<InMarket> inMarkets = objectMapper.readValue(input, new TypeReference<>() {});
        MarketConversionService marketConversionService = new MarketConversionService();

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> marketConversionService.convertAll(inMarkets),
            "Expected IllegalArgumentException for empty selections");
        assertEquals("Selections must not be null or empty", exception.getMessage(),
            "Exception message should indicate empty selections");
    }

    @DisplayName("Test JSON Shape of Specifiers (Flat Object)")
    @Test
    public void testSpecifiersJsonShape() throws Exception {
        String input = """
        [{"name":"Total","event_id":"e14","selections":[
          {"name":"over 2.5","odds":1.8},
          {"name":"under 2.5","odds":2.0}
        ]}]
        """;

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        OutMarket outMarket = new MarketConversionService()
            .convertAll(objectMapper.readValue(input, new TypeReference<>() {})).get(0);

        String json = objectMapper.writeValueAsString(outMarket);

        assertAll(
            () -> assertTrue(json.contains("\"specifiers\" : {"),
                "Specifiers should serialize as an object"),
            () -> assertFalse(json.contains("\"specifiers\" : { \"specifiers\""),
                "Specifiers should not contain nested 'specifiers'")
        );
    }

}