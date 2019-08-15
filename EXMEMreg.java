//Pipeline Register for EX-MEM stage.

public class EXMEMreg {
	
	//Write values of the IDEXreg
		//Control Signals
	private int writeMemRead = 0;		//Read from member
	private int writeMemWrite = 0;		//Write to memory
	private int writeMemToReg = 0;		//0 - data to write to reg is from ALU, 1 - data to write to reg is from memory
	private int writeRegWrite = 0;		//Register will get written
		//Instruction Values
	private int writeALUResult = 0;			//Calculated result from ALU Component
	private int writeSBValue = 0; 		//Value to store into memory
	private int writeWriteRegNum = 0;	//Register number that will get written to 

	
	//Read values of the IDEXreg
		//Control Signals
	private int readMemRead = 0;
	private int readMemWrite = 0;
	private int readMemToReg = 0;
	private int readRegWrite = 0;
		//Instruction Values
	private int readALUResult = 0;	
	private int readSBValue = 0;
	private int readWriteRegNum = 0;
	
	//SETTER METHODS:
	
		//Control Signals

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
	
	public void writeALUResult(int writeALUResult) {
		this.writeALUResult = writeALUResult;
	}

	public void writeSBValue(int writeSBValue) {
		this.writeSBValue = writeSBValue;
	}

	public void writeWriteRegNum(int writeWriteRegNum) {
		this.writeWriteRegNum = writeWriteRegNum;
	}
		
	
	//COPY WRITE TO READ
	public void copyWriteToRead() {
		
		readMemRead = writeMemRead;
		readMemWrite = writeMemWrite;
		readMemToReg = writeMemToReg;
		readRegWrite = writeRegWrite;
		
		readSBValue = writeSBValue;
		readWriteRegNum = writeWriteRegNum;
		readALUResult = writeALUResult;
	
	}
	
	//GETTER METHODS

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

	public int readALUResult() {
		return readALUResult;
	}

	public int readSBValue() {
		return readSBValue;
	}

	public int readWriteRegNum() {
		return readWriteRegNum;
	}


	//Print statement
	public String toString() {
		
		String rtn = "";
		
		rtn += "EX/MEM WRITE (written to by the EX stage):\n";
		rtn += "     Control Signals:\n";
		rtn += "         MemRead  = " + writeMemRead + "\n";
		rtn += "         MemWrite = " + writeMemWrite + "\n";
		rtn += "         MemToReg = " + writeMemToReg + "\n";
		rtn += "         RegWrite = " + writeRegWrite + "\n";
		rtn += String.format("%s0x%08X\n", "     ALUResult   = ", writeALUResult);
		rtn += String.format("%s0x%08X\n", "     SBValue     = ", writeSBValue); 
		rtn += "     WriteRegNum = " + writeWriteRegNum +  "\n\n";
	
		
		rtn += "EX/MEM READ (read by the MEM stage):\n";
		rtn += "     Control Signals:\n";
		rtn += "         MemRead  = " + readMemRead + "\n";
		rtn += "         MemWrite = " + readMemWrite + "\n";
		rtn += "         MemToReg = " + readMemToReg + "\n";
		rtn += "         RegWrite = " + readRegWrite + "\n";
		rtn += String.format("%s0x%08X\n", "     ALUResult   = ", readALUResult);
		rtn += String.format("%s0x%08X\n", "     SBValue     = ", readSBValue);
		rtn += "     WriteRegNum = " + readWriteRegNum +  "\n\n";

		return rtn;		
	}
}
