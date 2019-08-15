//Pipeline Register for ID-EX stage.

public class IDEXreg {
	
	//Write values of the IDEXreg
		//Control Signals
	private int writeRegDst = 0;	//0 - Register to write is determined by bit 20-16; 1 - Determined by bit 15-11;
	private int writeALUSrc = 0;	//0 - Register value, 1 - Offset value
	private int writeALUOp = 0;		//Operation for ALU to perform. R-format = function bits. Load = add, store = add
	private int writeMemRead = 0;	//Read from member
	private int writeMemWrite = 0;	//Write to memory
	private int writeMemToReg = 0;	//0 - data to write to reg is from ALU, 1 - data to write to reg is from memory
	private int writeRegWrite = 0;	//Register will get written
		//Instruction Values
	private int writeReadReg1Value = 0;	//Data value in register 1
	private int writeReadReg2Value = 0; //Data value in register 2
	private int writeSEOffset = 0;		//Converted offset from 16-bit to 32-bit
	private int writeWriteReg_20_16 = 0;//Register destination for Load
	private int writeWriteReg_15_11 = 0;//Register destination for R-Format
	private int writeFunction = 0;		//Function value for R-Format

	
	//Read values of the IDEXreg
		//Control Signals
	private int readRegDst = 0;
	private int readALUSrc = 0;
	private int readALUOp = 0;
	private int readMemRead = 0;
	private int readMemWrite = 0;
	private int readMemToReg = 0;
	private int readRegWrite = 0;
		//Instruction Values
	private int readReadReg1Value = 0;
	private int readReadReg2Value = 0;
	private int readSEOffset = 0;
	private int readWriteReg_20_16 = 0;
	private int readWriteReg_15_11 = 0;
	private int readFunction = 0;
	
	
	//SETTER METHODS:
	
		//Control Signals
	public void writeRegDst(int writeRegDst) {
		this.writeRegDst = writeRegDst;
	}

	public void writeALUSrc(int writeALUSrc) {
		this.writeALUSrc = writeALUSrc;
	}

	public void writeALUOp(int writeALUOp) {
		this.writeALUOp = writeALUOp;
	}

	public void writeMemRead(int writeMemRead) {
		this.writeMemRead = writeMemRead;
	}

	public void writeMemWrite(int writeMemWrite) {
		this.writeMemWrite = writeMemWrite;
	}

	public void writeMemToReg(int writeMemToReg) {
		this.writeMemToReg = writeMemToReg;
	}
	
	public void writeRegWrite(int writeRegWrite) {
		this.writeRegWrite = writeRegWrite;
	}
	
		//Register Values

	public void writeReadReg1Value(int writeReadReg1Value) {
		this.writeReadReg1Value = writeReadReg1Value;
	}

	public void writeReadReg2Value(int writeReadReg2Value) {
		this.writeReadReg2Value = writeReadReg2Value;
	}

	public void writeSEOffset(int writeSEOffset) {
		this.writeSEOffset = writeSEOffset;
	}

	public void writeWriteReg_20_16(int writeWriteReg_20_16) {
		this.writeWriteReg_20_16 = writeWriteReg_20_16;
	}

	public void writeWriteReg_15_11(int writeWriteReg_15_11) {
		this.writeWriteReg_15_11 = writeWriteReg_15_11;
	}

	public void writeFunction(int writeFunction) {
		this.writeFunction = writeFunction;
	}
	
	
	//COPY WRITE TO READ
	public void copyWriteToRead() {
		
		readRegDst = writeRegDst;
		readALUSrc = writeALUSrc;
		readALUOp = writeALUOp;
		readMemRead = writeMemRead;
		readMemWrite = writeMemWrite;
		readMemToReg = writeMemToReg;
		readRegWrite = writeRegWrite;
		
		readReadReg1Value = writeReadReg1Value;
		readReadReg2Value = writeReadReg2Value;
		readSEOffset = writeSEOffset;
		readWriteReg_20_16 = writeWriteReg_20_16;
		readWriteReg_15_11 = writeWriteReg_15_11;
		readFunction = writeFunction;
		
	}
	
	//GETTER METHODS

	public int readRegDst() {
		return readRegDst;
	}

	public int readALUSrc() {
		return readALUSrc;
	}

	public int readALUOp() {
		return readALUOp;
	}

	public int readMemRead() {
		return readMemRead;
	}

	public int readMemWrite() {
		return readMemWrite;
	}

	public int readMemToReg() {
		return readMemToReg;
	}

	public int readRegWrite() {
		return readRegWrite;
	}

	public int readReadReg1Value() {
		return readReadReg1Value;
	}

	public int readReadReg2Value() {
		return readReadReg2Value;
	}

	public int readSEOffset() {
		return readSEOffset;
	}

	public int readWriteReg_20_16() {
		return readWriteReg_20_16;
	}

	public int readWriteReg_15_11() {
		return readWriteReg_15_11;
	}

	public int readFunction() {
		return readFunction;
	}

	//Print statement
	public String toString() {
		
		String rtn = "";
		
		rtn += "ID/EX WRITE (written to by the ID stage):\n";
		rtn += "     Control Signals:\n";
		rtn += "         RegDst   = " + writeRegDst + "\n";
		rtn += "         ALUSrc   = " + writeALUSrc + "\n";
		rtn += "         ALUOp    = 0b" + Integer.toBinaryString(writeALUOp) + "\n";
		rtn += "         MemRead  = " + writeMemRead + "\n";
		rtn += "         MemWrite = " + writeMemWrite + "\n";
		rtn += "         MemToReg = " + writeMemToReg + "\n";
		rtn += "         RegWrite = " + writeRegWrite + "\n";
		rtn += String.format("%s0x%08X\n", "     ReadReg1Value  = ", writeReadReg1Value);
		rtn += String.format("%s0x%08X\n", "     ReadReg2Value  = ", writeReadReg2Value);
		rtn += String.format("%s0x%08X\n", "     SE Offset 	    = ", writeSEOffset);
		rtn += "     WriteReg_20_16 = " + writeWriteReg_20_16 +  "\n";
		rtn += "     WriteReg_15_11 = " + writeWriteReg_15_11 +  "\n";
		rtn += String.format("%s0x%02X\n\n", "     Function       = ", writeFunction);
	

		
		rtn += "ID/EX READ (read by the EX stage):\n";
		rtn += "     Control Signals:\n";
		rtn += "         RegDst   = " + readRegDst + "\n";
		rtn += "         ALUSrc   = " + readALUSrc + "\n";
		rtn += "         ALUOp    = 0b" + Integer.toBinaryString(readALUOp) + "\n";
		rtn += "         MemRead  = " + readMemRead + "\n";
		rtn += "         MemWrite = " + readMemWrite + "\n";
		rtn += "         MemToReg = " + readMemToReg + "\n";
		rtn += "         RegWrite = " + readRegWrite + "\n";
		rtn += String.format("%s0x%08X\n", "     ReadReg1Value  = ", readReadReg1Value);
		rtn += String.format("%s0x%08X\n", "     ReadReg2Value  = ", readReadReg2Value);
		rtn += String.format("%s0x%08X\n", "     SE Offset 	    = ", readSEOffset);
		rtn += "     WriteReg_20_16 = " + readWriteReg_20_16 + "\n";
		rtn += "     WriteReg_15_11 = " + readWriteReg_15_11 + "\n";
		rtn += String.format("%s0x%02X\n\n", "     Function       = ", readFunction);


		return rtn;		
	}

}
