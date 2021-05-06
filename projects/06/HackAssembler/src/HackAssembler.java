import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HackAssembler {

    public static void main(String[] args) {

        try {

            Path inputFile = Paths.get(args[0]);

            Parser parserFirstPath = new Parser(inputFile);
            SymbolTable symbolTable = createInitializedSymbolTable();
            int romAddressCount = 0;

            while (parserFirstPath.hasMoreCommands()) {

                parserFirstPath.advance();
                Parser.CommandType commandType = parserFirstPath.commandType();

                switch (commandType) {
                    case A_COMMAND:
                    case C_COMMAND:
                        romAddressCount++;
                        break;
                    case L_COMMAND:
                        symbolTable.addEntry(parserFirstPath.symbol(), romAddressCount);
                        break;
                    default:
                        throw new IllegalArgumentException("command format is invalid");
                }
            }

            Parser parserSecondPath = new Parser(inputFile);
            Code code = new Code();
            List<String> commands = new ArrayList<>();
            int ramAddressCount = 16;

            while (parserSecondPath.hasMoreCommands()) {
                
                parserSecondPath.advance();
                Parser.CommandType commandType = parserSecondPath.commandType();
                StringBuilder commandBuilder = new StringBuilder();

                switch (commandType) {
                    case A_COMMAND:
                        boolean isDigit = true;
                        String symbol = parserSecondPath.symbol();
                        for (int i = 0; i < symbol.length(); i++) {
                            isDigit = Character.isDigit(symbol.charAt(i));
                            if (!isDigit) {
                                break;
                            }
                        }
                        String convertedSymbol = new String();
                        if (isDigit) {
                            convertedSymbol = symbol;
                        } else {
                            if (!symbolTable.contains(symbol)) {
                                symbolTable.addEntry(symbol, ramAddressCount);
                                ramAddressCount++;
                            }
                            convertedSymbol = String.valueOf(symbolTable.getAddress(symbol));
                        }
                        commandBuilder.append("0");
                        commandBuilder.append(to15DigitsBinaryString(convertedSymbol));
                        break;
                    case C_COMMAND:
                        commandBuilder.append("111");
                        commandBuilder.append(code.comp(parserSecondPath.comp()));
                        commandBuilder.append(code.dest(parserSecondPath.dest()));
                        commandBuilder.append(code.jump(parserSecondPath.jump()));
                        break;
                    case L_COMMAND:
                        continue;
                    default:
                        throw new IllegalArgumentException("command format is invalid");
                }

                String command = commandBuilder.toString();
                commands.add(command);
            }

            String inputFilaName = inputFile.getFileName().toString();
            String outputFileName = inputFilaName.substring(0, inputFilaName.lastIndexOf('.')) + ".hack";
            if (!Files.exists(Paths.get(outputFileName))) {
                Files.createFile(Paths.get(outputFileName));
              }
            Path outputFile = Paths.get(outputFileName);
            
            Files.write(outputFile, commands);
      
          } catch (IOException e) {
              e.printStackTrace();
          }
    }

    private static SymbolTable createInitializedSymbolTable(){
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addEntry("SP", 0);
        symbolTable.addEntry("LCL", 1);
        symbolTable.addEntry("ARG", 2);
        symbolTable.addEntry("THIS", 3);
        symbolTable.addEntry("THAT", 4);
        symbolTable.addEntry("R0", 0);
        symbolTable.addEntry("R1", 1);
        symbolTable.addEntry("R2", 2);
        symbolTable.addEntry("R3", 3);
        symbolTable.addEntry("R4", 4);
        symbolTable.addEntry("R5", 5);
        symbolTable.addEntry("R6", 6);
        symbolTable.addEntry("R7", 7);
        symbolTable.addEntry("R8", 8);
        symbolTable.addEntry("R9", 9);
        symbolTable.addEntry("R10", 10);
        symbolTable.addEntry("R11", 11);
        symbolTable.addEntry("R12", 12);
        symbolTable.addEntry("R13", 13);
        symbolTable.addEntry("R14", 14);
        symbolTable.addEntry("R15", 15);
        symbolTable.addEntry("SCREEN", 16384);
        symbolTable.addEntry("KBD", 24576);
        return symbolTable;
    }

    private static String to15DigitsBinaryString(String decimalString){
        String binary = Integer.toBinaryString(Integer.parseInt(decimalString));
        StringBuilder result15DigitsBinary = new StringBuilder();
        for (int i = 0; i < 15 - binary.length(); i++) {
            result15DigitsBinary.append("0");    
        }
        result15DigitsBinary.append(binary);
        return result15DigitsBinary.toString();
    }
}
