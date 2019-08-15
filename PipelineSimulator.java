/* Name: Alina Chaiyasarikul
 * Date: July 30th, 2019
 * 
 * Pipeline Simulator:
 * This is a software simulation of a pipelined data path 
 * This program uses an array of data to act as the memory, and another to act as the registers.
 * 
 * This simulation does not handle hazards and do not consider branches.
 * 	Values omitted: IncrPC, Branch, CalcBTA, Zero
 * This simulation stores and loads bytes instead of words (sb/lb).
 * 
 * Although in reality there is one pipeline register that contains one instance of each attribute and gets written to at the end 
 * of the clock cycle, our simulation has 2 instances of each attribute, writes as needed, and gets copied at the end of clock 
 * cycle and read at the beginning.
 */

public class PipelineSimulator {

	public static void main(String[] args) {
		
		PipelineSimulator sim = new PipelineSimulator();
		sim.run();
		
	}
	
	public void run() {
			
		//Instantiating and initializing the main memory
		int[] Main_mem = new int[1024];
		
		int memVal = 0;
		
		for (int i = 0; i < Main_mem.length; i++) {
			Main_mem[i] = memVal;
			memVal++;
			if (memVal > 255) {
				memVal = 0;
			}
		}
		
		//Instantiating and initializing the register
		int[] Regs = new int[32];
		
		Regs[0] = 0;
		
		int regVal = 0x100;
		
		for (int i = 1; i < Regs.length; i++) {
			Regs[i] = regVal+i;
		}
		
		
		//Pipeline Registers
		IFIDreg IFID = new IFIDreg();
		IDEXreg IDEX = new IDEXreg();
		EXMEMreg EXMEM = new EXMEMreg();
		MEMWBreg MEMWB = new MEMWBreg();
		
		int[] instruction_cache = {0xA1020000, 0x810AFFFC, 0x00831820, 0x01263820, 0x01224820, 0x81180000, 0x81510010, 0x00624022,
		              0x00000000, 0x00000000, 0x00000000, 0x00000000};
		
		//A different set of test values
		//int[] instruction_cache = {0x00A63820, 0x8D0F0004, 0xAD09FFFC, 0x00625022, 0x10C8FFFB, 0x00000000, 0x00000000, 0x00000000, 0x00000000};
		
		for (int i = 0; i < instruction_cache.length; i++) {
			System.out.println("///////////////////");
			System.out.println("// Clock Cycle " + (i+1) + " //");
			System.out.println("///////////////////");
			
			IF_stage(instruction_cache[i], IFID);
			ID_stage(IFID, IDEX, Regs);
			EX_stage(IDEX, EXMEM);
			MEM_stage(EXMEM, MEMWB, Main_mem);
			WB_stage(MEMWB, Regs);
			
			//To print out everything before pipeline register data is copied from the write to the read side.
			System.out.println("\nBefore we copy the write side of pipeline registers to the read side");
			System.out.println("--------------------------------------------------------------------");
			Print_out_everything(IFID, IDEX, EXMEM, MEMWB, Regs, Main_mem);
			Copy_write_to_read(IFID, IDEX, EXMEM, MEMWB);
			
			//To print out everything after pipeline register data is copied from the write to the read side.
//			System.out.println("After we copy the write side of pipeline registers to the read side");
//			System.out.println("-------------------------------------------------------------------");
//			Print_out_everything(IFID, IDEX, EXMEM, MEMWB, Regs, Main_mem);
		}

	}

//STAGE 1:
	//Fetch instruction and store into IFID pipeline register's write value
	public void IF_stage(int instruction, IFIDreg IFID) {
		IFID.writeInstruction(instruction);
		
	}

//STAGE 2:
	//Read from IFID pipeline value and decode instruction
	public void ID_stage(IFIDreg IFID, IDEXreg IDEX, int[] Regs) {
		//Reading instruction
		int instruct = IFID.readInstruction();
		
		//NOPs instruction. Will overwrite current values and not assume opcode is an R-Format
		if (instruct == 0) { 
			IDEX.writeRegDst(0); 	
			IDEX.writeALUSrc(0);
			IDEX.writeALUOp(0b00);	
			IDEX.writeMemRead(0);
			IDEX.writeMemWrite(0);
			IDEX.writeMemToReg(0);
			IDEX.writeRegWrite(0);	
			
			IDEX.writeReadReg1Value(0);	
			IDEX.writeReadReg2Value(0);	
			IDEX.writeSEOffset(0);				
			IDEX.writeWriteReg_20_16(0);		
			IDEX.writeWriteReg_15_11(0);		
			IDEX.writeFunction(0);				
		}

		else {
			//Format components
			int opco  = 0xFC000000; //0b 1111 1100 0000 0000 0000 0000 0000 0000;
			int reg1s = 0x03E00000; //0b 0000 0011 1110 0000 0000 0000 0000 0000;
			int reg2  = 0x001F0000; //0b 0000 0000 0001 1111 0000 0000 0000 0000;
			int reg3d = 0x0000F800; //0b 0000 0000 0000 0000 1111 1000 0000 0000;
			int func  = 0x0000003F; //0b 0000 0000 0000 0000 0000 0000 0011 1111;
			int cons  = 0x0000FFFF; //0b 0000 0000 0000 0000 1111 1111 1111 1111;
			
			//Format (R vs I): If I-format, tells you the instruction
			int tform = (instruct & opco) >>> 26;
			
			//Registers of instruction
			int treg1s; 
			int treg2;
			int treg3d; //Will not have a true value for the I-format
			
			int tfunct; //R-format FUNCTION
			short tcons; //I-format OFFSET/CONSTANT - 16 bits since it can be signed
			
			treg1s = (instruct & reg1s) >>> 21; //source
			treg2 = (instruct & reg2) >>> 16; //source
			treg3d = (instruct & reg3d) >>> 11; //destination
			tcons = (short)(instruct & cons); //I-format
			tfunct = (instruct & func); //For R-Format function number
		
			//R-format: Set Control Values (add, subtract, and, or, slt)
			if ((tform) == 0) {
				
				IDEX.writeRegDst(1); 	//Register to write is determined by bit 15-11;
				IDEX.writeALUSrc(0);
				IDEX.writeALUOp(0b10);	//Indicates this is an R-format and to write to register defined in bits 15-11
				IDEX.writeMemRead(0);
				IDEX.writeMemWrite(0);
				IDEX.writeMemToReg(0);
				IDEX.writeRegWrite(1);	//A register will get written to
				
			}
			
			//Load: Set Control Values
			else if (tform == 0x20){
				
				IDEX.writeRegDst(0); 	//Register to write is determined by bit 20-16;
				IDEX.writeALUSrc(1);
				IDEX.writeALUOp(0b00);	//00 for lw/sw (01 for subtract - branches)
				IDEX.writeMemRead(1);
				IDEX.writeMemWrite(0);
				IDEX.writeMemToReg(1);
				IDEX.writeRegWrite(1);	//A register will get written to
				
			}
			
			//Stores: Set Control Values
			else if (tform == 0x28){
			
				IDEX.writeRegDst(0); 	//Register to write is determined by bit 20-16;
				IDEX.writeALUSrc(1);
				IDEX.writeALUOp(0b00);	//00 for lw/sw (01 for subtract - branches)
				IDEX.writeMemRead(0);
				IDEX.writeMemWrite(1);
				IDEX.writeMemToReg(0);
				IDEX.writeRegWrite(0);	//A register will get written to
				
			}
			
				//Instruction Values
	
			IDEX.writeReadReg1Value(Regs[treg1s]);	//Value of register 1
			IDEX.writeReadReg2Value(Regs[treg2]);	//Value of register 2
			IDEX.writeSEOffset((int)tcons);			//Offset-Constant of I format, converted from 16 bits to 32 bits
			IDEX.writeWriteReg_20_16(treg2);		//Register destination - Load, or Register source - Store
			IDEX.writeWriteReg_15_11(treg3d);		//Register destination - R format
			IDEX.writeFunction(tfunct);				//Determines which R-Format instruction
		}	
	}
	
	
//STAGE 3:	
	public void EX_stage(IDEXreg IDEX, EXMEMreg EXMEM) {
		
		//Passing on control signals
		EXMEM.writeMemRead(IDEX.readMemRead());
		EXMEM.writeMemWrite(IDEX.readMemWrite());
		EXMEM.writeMemToReg(IDEX.readMemToReg());
		EXMEM.writeRegWrite(IDEX.readRegWrite());
		
		//Store value is placed in Register 2
		EXMEM.writeSBValue(IDEX.readReadReg2Value());
		
		//Determine WriteRegNum using RegDst to determine which bits indicate the register number
		if (IDEX.readRegDst() == 0) { //Use bits 20-16
			EXMEM.writeWriteRegNum(IDEX.readWriteReg_20_16());
		}
		
		else { //R-Format, use bits 15-11
			EXMEM.writeWriteRegNum(IDEX.readWriteReg_15_11());
		}
		
		//ALU COMPONENT
		
		int upperInput = IDEX.readReadReg1Value();
		int lowerInput;
		
		//Determining lower input of ALU via ALUSrc
		if (IDEX.readALUSrc() == 0) { //ALU takes in Register Value
			lowerInput = IDEX.readReadReg2Value();
		}
		else { //ALU takes in Sign Extended Offset
			lowerInput = IDEX.readSEOffset();
		}
		
		//Determining how to calculate via ALUOp
		int tfunct = 0; 
		
		if (IDEX.readALUOp() == 0b00) { //I-Format 
			//Load and stores use add (Branches use subtract)
			tfunct = 0x20;
		}
		
		else if (IDEX.readALUOp() == 0b10) { 
			//R-Format
			tfunct = IDEX.readFunction();
		}	
		
		//Calculates ALUResult based on function.
		int ALUResult;
		
		switch (tfunct){
			case 0x20: //add
				ALUResult = upperInput + lowerInput;
				break;
				
			case 0x22: //sub
				ALUResult = upperInput - lowerInput;
				break;
			
			case 0x24: //bitwise and
				ALUResult = (upperInput & lowerInput);
				break;
				
			case 0x25: //bitwise or
				ALUResult = (upperInput | lowerInput);
				break;
				
			case 0x2A: //slt
				if (upperInput < lowerInput) {
					ALUResult = 1;
				}
				else {
					ALUResult = 0;
				}
				break;
			
			default:
				ALUResult = -100;
				System.out.println("Error: Mistakes were made...");
		}
		EXMEM.writeALUResult(ALUResult);
	}
	
//STAGE 4:	
	public void MEM_stage(EXMEMreg EXMEM, MEMWBreg MEMWB, int[] mem) {
		//Passing values
		MEMWB.writeALUResult(EXMEM.readALUResult());
		MEMWB.writeMemToReg(EXMEM.readMemToReg());
		MEMWB.writeRegWrite(EXMEM.readRegWrite());
		MEMWB.writeWriteRegNum(EXMEM.readWriteRegNum());
		
		//LOAD: Need to read from main memory (otherwise, we can ignore)
		if (EXMEM.readMemRead() == 1) { 
			//ALUResult is a calculated address
			MEMWB.writeLBDataValue(mem[EXMEM.readALUResult()]);
		}
		
		//STORE: Need to write to main memory (otherwise, we can ignore)
		else if (EXMEM.readMemWrite() == 1) {
			//ALUResult is a calculated address																		
			mem[EXMEM.readALUResult()] = EXMEM.readSBValue();
		}

	}

//STAGE 5:
	public void WB_stage(MEMWBreg MEMWB, int[] Regs) {
		
		//Need to write to register
		if(MEMWB.readRegWrite() == 1) {
			
			//LOAD: Value from memory gets written to register
			if (MEMWB.readMemToReg() == 1) { 
				Regs[MEMWB.readWriteRegNum()] = MEMWB.readLBDataValue();
			}
			
			//R-FORMAT: Value from ALU gets written to register
			else {
				Regs[MEMWB.readWriteRegNum()] = MEMWB.readALUResult();
			}
			
		}
	}
	
	//Displays the pipeline registers and the 32 registers
	public void Print_out_everything(IFIDreg IFID, IDEXreg IDEX, EXMEMreg EXMEM, MEMWBreg MEMWB, int[] Regs, int[] mem) {
		
		//Pipeline registers
		System.out.println(IFID);
		System.out.println(IDEX);
		System.out.println(EXMEM);
		System.out.println(MEMWB);
		
		//Registers
		System.out.println("Registers: ");
		System.out.println("----------");
		
		for (int i = 0; i < Regs.length; i++){
			System.out.printf("%s%d%s%X\n","$", i , ": 0x",Regs[i]);
		}
		
		System.out.println("\n\n");

	}
	
	//Copies all data form write side of the pipeline register to the read side
	public void Copy_write_to_read(IFIDreg IFID, IDEXreg IDEX, EXMEMreg EXMEM, MEMWBreg MEMWB) {
		IFID.copyWriteToRead();
		IDEX.copyWriteToRead();
		EXMEM.copyWriteToRead();
		MEMWB.copyWriteToRead();
		
	}
}
