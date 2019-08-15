//Pipeline Register for IF-ID stage.
//	Stores values in the "write" attributes, and copies them over to the "read" attributes after function call.


public class IFIDreg {
	
	private int writeInstructionValue = 0; //Instruction value on write side
	private String decodeWriteInstructionValue = ""; //decoded MIPS instruction
	private int readInstructionValue = 0; //Instruction value on read side
	
	//SETTER METHOD 
	public void writeInstruction(int instruct) {
		writeInstructionValue = instruct;
		decodeWriteInstructionValue = MIPSdecode(instruct);
	}
	
	//GETTER METHOD
	public int readInstruction() {
		return readInstructionValue;
	}
	
	public void copyWriteToRead() {
		readInstructionValue = writeInstructionValue;
	}

	
	//Takes in MIPS instruction and translate to human readable form. 
	//This is copied and slightly altered from project 1, which was only designed to take in console input
	public String MIPSdecode(int instruction) {
		
		//Format components
		int opco  = 0xFC000000; //0b 1111 1100 0000 0000 0000 0000 0000 0000;
		int reg1s = 0x03E00000; //0b 0000 0011 1110 0000 0000 0000 0000 0000;
		int reg2  = 0x001F0000; //0b 0000 0000 0001 1111 0000 0000 0000 0000;
		int reg3d = 0x0000F800; //0b 0000 0000 0000 0000 1111 1000 0000 0000;
		int func  = 0x0000003F; //0b 0000 0000 0000 0000 0000 0000 0011 1111;
		int cons  = 0x0000FFFF; //0b 0000 0000 0000 0000 1111 1111 1111 1111;
		
		//Format (R vs I): If I-format, tells you the instruction
		int tform = (instruction & opco) >>> 26;
		
		//Registers of test instruction
		int treg1s; 
		int treg2;
		int treg3d; //Will not have a value for the I-format
		
		int tfunct; //R-format FUNCTION
		short tcons; //I-format OFFSET/CONSTANT - 16 bits since it can be signed
		
		String code = "";
		
		if ((tform) == 0) { //R-format
			
			treg1s = (instruction & reg1s) >>> 21; //source
			treg2 = (instruction & reg2) >>> 16; //source
			treg3d = (instruction & reg3d) >>> 11; //destination
			
			tfunct = instruction & func; //Switch based on function call
			
			switch (tfunct){
				case 0x20:
					code += "add";
					break;
					
				case 0x22:
					code += "sub";
					break;
				
				case 0x24:
					code += "and";
					break;
					
				case 0x25:
					code += "or ";
					break;
					
				case 0x2A:
					code += "slt";
					break;
				
				default:
					code += "Unknown command";	
			}
			
			//Printing out dest, src, src - instruction already printed
			
			code += " $" + treg3d + ", $" + treg1s + ", $" + treg2;
		} 
		
		
		else { //I-format
			
			treg1s = (instruction & reg1s) >>> 21; //source (the one after the number)
			treg2 = (instruction & reg2) >>> 16; //source or destination
			tcons = (short) ((instruction & cons));
			
			//tform determines what instruction
			
			//Load
			if (tform == 0x20) {
				//Printing out instruc src/dest , const(src)
				code += "lw  $" + treg2 + ", " + tcons + "($" + treg1s + ")";
			}
			
			//Store
			else if (tform == 0x28) {
				//Printing out instruc src/dest , const(src)
				code += "sw  $" + treg2 + ", " + tcons + "($" + treg1s + ")";
			}
		
		}

		return code;
	}
	
	public String toString() {
		
		String rtn = "";
		
		rtn += "IF/ID WRITE (written to by the IF stage):\n";
		rtn += String.format("%s0x%08X", "     Inst = ", writeInstructionValue);
		rtn += "  [" + decodeWriteInstructionValue + "]\n\n";
		
		rtn += "IF/ID READ (read by the ID stage):\n";
		rtn += String.format("%s0x%08X\n\n", "     Inst = ", readInstructionValue);

		
		return rtn;
		
	}
}
