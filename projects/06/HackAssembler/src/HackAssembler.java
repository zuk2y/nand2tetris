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
            Parser parser = new Parser(inputFile);
            
            Code code = new Code();

            List<String> commands = new ArrayList<>();

            while (parser.hasMoreCommands()) {
                
                parser.advance();
                Parser.CommandType commandType = parser.commandType();
                StringBuilder commandBuilder = new StringBuilder();

                switch (commandType) {
                    case A_COMMAND:
                        commandBuilder.append("0");
                        commandBuilder.append(to15DigitsBinaryString(parser.symbol()));
                        break;
                    case C_COMMAND:
                        commandBuilder.append("111");
                        commandBuilder.append(code.comp(parser.comp()));
                        commandBuilder.append(code.dest(parser.dest()));
                        commandBuilder.append(code.jump(parser.jump()));
                        break;
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
