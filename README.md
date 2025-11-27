# Java EMV Reader

A Java application for reading and analyzing EMV (chip) payment card data. This tool allows you to extract and validate information from EMV cards, including cardholder data, certificates, transaction logs, and authentication data.

## Features

- **EMV Card Reading**: Connect to smart card readers via PC/SC and read EMV card data
- **Data Analysis**: Extract and analyze card information including:
  - PAN (Primary Account Number)
  - Cardholder name
  - Expiration date
  - Certificates (Issuer, ICC)
  - Public keys
  - Transaction logs
  - Static and dynamic authentication data
- **Application Identification**: Automatically identify card applications (Visa, Mastercard, etc.)
- **Certificate Validation**: Validate digital certificates and signatures
- **Multiple Interfaces**: 
  - Graphical user interface (Swing)
  - Command-line interface
  - Emulation mode for testing
- **Terminal Support**: Support for multiple card readers/terminals
- **Database Lookups**: Built-in databases for ATRs, IINs, and RIDs

## Requirements

- **Java**: Java 17 or higher (LTS versions recommended)
- **Maven**: 3.2.1 or higher
- **Smart Card Reader**: PC/SC compliant card reader (for physical card reading)
- **Operating System**: 
  - macOS (with Smart Card Services)
  - Linux (with PC/SC Lite)
  - Windows (with Smart Card service)

## Installation

### Prerequisites

1. Install Java 17+:
   ```bash
   # macOS (using Homebrew)
   brew install openjdk@17
   
   # Or download from: https://adoptium.net/
   ```

2. Install Maven:
   ```bash
   # macOS (using Homebrew)
   brew install maven
   ```

### Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/yabadabaduca/javaemvreader.git
   cd javaemvreader
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. The JAR files will be created in the `target/` directory:
   - `javaemvreader-0.6.0-SNAPSHOT-full.jar` - Full version with all dependencies

## Usage

### Command-Line Interface

#### List available terminals:
```bash
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar --listTerminals
# or
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar -h  # for help
```

#### Run in command-line mode (no GUI):
```bash
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar --noGUI
```

#### Run with verbose output:
```bash
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar --noGUI --verbose
# or
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar --noGUI -v
```

#### Emulation mode (for testing):
```bash
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar --emulate
```

### Graphical User Interface

Simply run without any arguments:
```bash
java -jar target/javaemvreader-0.6.0-SNAPSHOT-full.jar
```

The GUI will open and automatically detect and read any inserted EMV card.

### Command-Line Options

- `-h, --help`: Print help message
- `--noGUI`: Use command-line version
- `--emulate`: Emulate communication with an EMV card
- `--listTerminals`: List all available terminals
- `-v, --verbose`: Print debug messages
- `-t, --terminal <name>`: Specify the terminal to use

## Project Structure

```
javaemvreader/
├── src/main/java/sasc/
│   ├── Main.java                    # Main entry point
│   ├── GUI.java                     # Graphical user interface
│   ├── emv/                         # EMV protocol implementation
│   │   ├── EMVSession.java          # EMV session management
│   │   ├── EMVApplication.java      # EMV application representation
│   │   └── ...
│   ├── iso7816/                     # ISO 7816 smart card commands
│   ├── terminal/                    # Terminal/reader interfaces
│   ├── smartcard/                   # Smart card handling
│   └── util/                        # Utility classes
├── src/main/resources/              # Resource files
│   ├── certificationauthorities.xml # CA certificates
│   ├── aidlist.xml                  # Application ID list
│   └── ...
└── pom.xml                          # Maven configuration
```

## Development

### Building

```bash
# Compile
mvn compile

# Run tests
mvn test

# Package
mvn package

# Create JAR with dependencies
mvn clean package
```

### Running Tests

```bash
mvn test
```

### Code Style

The project follows standard Java coding conventions. Make sure to:
- Add JavaDoc for public methods
- Follow existing code style
- Test your changes before committing

## Technologies Used

- **Java 17**: Programming language
- **Maven**: Build tool and dependency management
- **Apache Commons CLI**: Command-line argument parsing
- **SLF4J**: Logging framework
- **Swing**: GUI framework (for graphical interface)
- **PC/SC**: Smart card communication protocol
- **jCardSim**: Smart card simulation (for testing)

## Known Limitations

- Java Web Start (JNLP) support has been removed (not compatible with Java 11+)
- Some EMV features may not be fully implemented (see TODO.txt)
- PIN encipherment is not yet implemented (marked as TODO)

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

Licensed under the Apache License, Version 2.0. See [LICENSE-2_0.txt](LICENSE-2_0.txt) for details.

## Acknowledgments

- Based on EMV specifications
- Uses Ludovic Rousseau's smart card list
- Includes contributions from various developers (see CHANGELOG.txt)

## Support

For issues, questions, or contributions, please use the GitHub issue tracker.

## Version History

See [CHANGELOG.txt](CHANGELOG.txt) for detailed version history.

**Current Version**: 0.6.1-SNAPSHOT

---

**Note**: This tool is for educational and development purposes. Always follow applicable laws and regulations when working with payment card data.

