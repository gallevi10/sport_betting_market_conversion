package com.project.converter;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.converter.model.InMarket;
import com.project.converter.model.OutMarket;
import com.project.converter.service.MarketConversionService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

// Main application class for converting market data from input JSON to output JSON
public class ConverterApp {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("\nUsage: converter.ConverterApp <input.json> <output.json> [--pretty]");
            System.exit(1);
        }
    }

    private static void run(String[] args) throws Exception {
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException("Expected 2 or 3 args: <input.json> <output.json> [--pretty]");
        }

        String inputPath = args[0];
        String outputPath = args[1];
        boolean pretty = args.length == 3 && "--pretty".equals(args[2]);

        // validates files/paths
        File inFile = new File(inputPath);
        if (!inFile.isFile()) {
            throw new IllegalArgumentException("Input file not found: " + inputPath);
        }
        File outFile = new File(outputPath);
        if (outFile.getParentFile() != null) {
            Files.createDirectories(outFile.getParentFile().toPath());
        }

        // JSON mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        if (pretty) mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // reads input
        List<InMarket> inMarkets = mapper.readValue(inFile, new TypeReference<>() {});

        // converts markets
        MarketConversionService service = new MarketConversionService();
        List<OutMarket> outMarkets = service.convertAll(inMarkets);

        // writes the output
        mapper.writeValue(outFile, outMarkets);
    }
}
