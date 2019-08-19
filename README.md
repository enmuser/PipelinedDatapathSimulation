# Pipelined Datapath Simulation

This is a software simulation of a pipelined data path. This simulation does not handle hazards and do not consider branches. 
Unlike actual pipeline registers, this simulation models the registers as Java objects that hold a “write” and a “read” 
version of each attribute. The “read” version of the attribute gets read at the beginning of the clock cycle. 
The “write” version of the attribute is populated as needed, but is only copied to the "read" version at the very end of each
clock cycle.

The 5 stages of a RISC pipeline:
1. IF:  Instruction fetch
2. ID:  Instruction decode
3. EX:  Instruction execute
4. MEM: Memory Access
5. WB:  Write back

///////////////////////////////////

Instead of incrementing and tracking a program counter that is used to fetch the instruction from the instruction cache in the first stage, all the instructions are hardcoded into an array. The program loops through the array and passes the instruction as an argument into the IF stage. Each loop represents one clock cycle. Within the loop, a method representing each stage of the pipeline register gets called, starting with the IF stage and ending with the WB stage. For each stage, the attributes gets read from the pipeline register's "read" attributes, does its operation, and assigns the "write" version of the attributes of the next pipeline register. When all 5 stges are complete, the program will print the attributes of the pipeline registers before copying the "write" version of the attributes to the "read" version.
