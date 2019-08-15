//Pipeline Register for MEM-WB stage.

public class MEMWBreg {
	
	//Write values of the IDEXreg
		//Control Signals
	private int writeMemToReg = 0;		//0 - data to write to reg is from ALU, 1 - data to write to reg is from memory
	private int writeRegWrite = 0;		//Register will get written
		//Instruction Values
	private int writeALUResult = 0;			//Calculated result from ALU Component
	private int writeLBDataValue = 0; 		//Value to store into memory
	private int writeWriteRegNum = 0;	//Register number that will get written to 

	
	//Read values of the IDEXreg
		//Control Signals
	private int readMemToReg = 0;
	private int readRegWrite = 0;
		//Instruction Values
	private int readALUResult = 0;	
	private int readLBDataVlaue = 0;
	private int readWriteRegNum = 0;
	
	//SETTER METHODS:
	
		//Control Signals

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

	public void writeLBDataValue(int writeLBDataValue) {
		this.writeLBDataValue = writeLBDataValue;
	}

	public void writeWriteRegNum(int writeWriteRegNum) {
		this.writeWriteRegNum = writeWriteRegNum;
	}
		
	
	//COPY WRITE TO READ
	public void copyWriteToRead() {

		readMemToReg = writeMemToReg;
		readRegWrite = writeRegWrite;
		
		readLBDataVlaue = writeLBDataValue;
		readWriteRegNum = writeWriteRegNum;
		readALUResult = writeALUResult;
	
	}
	
	//GETTER METHODS

	public int readMemToReg() {
		return readMemToReg;
	}

	public int readRegWrite() {
		return readRegWrite;
	}

	public int readALUResult() {
		return readALUResult;
	}

	public int readLBDataValue() {
		return readLBDataVlaue;
	}

	public int readWriteRegNum() {
		return readWriteRegNum;
	}


	//Print statement
	public String toString() {
		
		String rtn = "";
		
		rtn += "MEM/WB WRITE (written to by the MEM stage):\n";
		rtn += "     Control Signals:\n";
		rtn += "         MemToReg = " + writeMemToReg + "\n";
		rtn += "         RegWrite = " + writeRegWrite + "\n";
		rtn += String.format("%s0x%08X\n", "     ALUResult   = ", writeALUResult);
		rtn += String.format("%s0x%08X\n", "     LBDataValue = ", writeLBDataValue);
		rtn += "     WriteRegNum = " + writeWriteRegNum +  "\n\n";
	
		
		rtn += "MEM/WB READ (read by the WB stage):\n";
		rtn += "     Control Signals:\n";
		rtn += "         MemToReg = " + readMemToReg + "\n";
		rtn += "         RegWrite = " + readRegWrite + "\n";
		rtn += String.format("%s0x%08X\n", "     ALUResult   = ", readALUResult);
		rtn += String.format("%s0x%08X\n", "     LBDataValue = ", readLBDataVlaue);
		rtn += "     WriteRegNum = " + readWriteRegNum +  "\n\n";

		return rtn;		
	}
}
