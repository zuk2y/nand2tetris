import java.util.HashMap;
import java.util.Map;

public class Code {

    private Map<String, String> destTable;

    private Map<String, String> compTable;

    private Map<String, String> jumpTable;

    public Code(){
        destTable = new HashMap<String, String>();

        destTable.put("null", "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");

        compTable = new HashMap<String, String>();

        compTable.put("0", "0101010");
        compTable.put("1", "0111111");
        compTable.put("-1", "0111010");
        compTable.put("D", "0001100");
        compTable.put("A", "0110000");
        compTable.put("!D", "0001101");
        compTable.put("!A", "0110001");
        compTable.put("-D", "0001111");
        compTable.put("-A", "0110011");
        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("D+A", "0000010");
        compTable.put("D-A", "0010011");
        compTable.put("A-D", "0000111");
        compTable.put("D&A", "0000000");
        compTable.put("D|A", "0010101");
        compTable.put("M", "1110000");
        compTable.put("!M", "1110001");
        compTable.put("-M", "1110011");
        compTable.put("M+1", "1110111");
        compTable.put("M-1", "1110010");
        compTable.put("D+M", "1000010");
        compTable.put("D-M", "1010011");
        compTable.put("M-D", "1000111");
        compTable.put("D&M", "1000000");
        compTable.put("D|M", "1010101");

        jumpTable = new HashMap<String, String>();

        jumpTable.put("null", "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");
    }

    public String dest(String mnemonic){
        binatyCode = destTable.get(mnemonic);
        if (binaryCode = null) {
            throw new IllegalArgumentException("unknown mnemonic: [" + mnemonic + "]");
        } else {
            return binartyCode;
        }
    }

    public String comp(String mnemonic){
        binatyCode = compTable.get(mnemonic);
        if (binaryCode = null) {
            throw new IllegalArgumentException("unknown mnemonic: [" + mnemonic + "]");
        } else {
            return binartyCode;
        }
    }

    public String jump(String mnemonic){
        binatyCode = jumpTable.get(mnemonic);
        if (binaryCode = null) {
            throw new IllegalArgumentException("unknown mnemonic: [" + mnemonic + "]");
        } else {
            return binartyCode;
        }
    }
}
