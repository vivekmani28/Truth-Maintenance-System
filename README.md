# Truth-Maintenance-System

This is a Implementation of A Simple truth maintenance system implementation in in Java.

The input of the TMS is a textFile containing a sequence of actions that will force the TMS to add and retract multiple items. The system finally prints the f inal status of the TMS knowledge base after executing the actions in the inputFile. In the textFile, "*" means logical and (^), "+" represents logical or (V), and "-" indicates logical negation (-).

### Illustration of the TMS 

![image](https://user-images.githubusercontent.com/22831490/27166297-b1185f62-5167-11e7-9356-fb2a019c73ce.png)

### File Descriptions

#### TSM.java

This file contains the implementation of the entire TSM. It populates the knowledge base with the values from the input file.

#### KBItem.java

This is the class representing a Item in the knowledge base, like its a derived information or its directly referred from the input. It also contains the dependency information.

#### Implication.java

This is a Value Object class for each rule in the input. This contains data about a given implication.

#### TSMInput.txt (Test Data)

This is a sample input file containing one implication per line. 

### Expected Input format:
``
java TSM <Input_file>
``

