# ClassPrinter

The `ClassPrinter` is a simple Java utility that reads Java `.class` files and prints the bytecode in a human-readable format. This tool is helpful for exploring the structure of compiled Java classes and understanding their contents.

## Features

- Parses and reads `.class` files.
- Displays version, magic number and constant pool

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or higher.
-  A `.class` file.

### Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/your-username/class-printer.git
   cd class-printer
   ```
2. Compile the project:
    ```bash
    javac Main.java ClassPrinter.java ConstantInfo.java Test.java -d out
    ```

3. Execute it:
    ```bash
    java -cp out/ Main out/Test.class
    ```

## Limitations
Not all the content inside the .class file can be read yet
