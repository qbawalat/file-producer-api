# File Producer API

## Overview

Spring Boot application exposing api for file conversion.

## API Endpoints

### Convert Files

Converts a list of files based on specified parameters.

* URL: /file/conversion
* Method: POST
* Parameters:
    * file (required query parameter) - List of files to be processed. (Multi-part file upload)
    * produces (required query parameter) - The type of file to be produced.
        * json
    * consumes (required query parameter) - The type of file to be consumed.
        * csv
    * sort (optional query parameter) - Sorting direction for the output files.
        * asc
        * desc
    * sanitize (optional query parameter) - Sanitization strategy for the output files
        * SKIP_EMPTY_CELLS
        * EMPTY_CELLS_TO_NULLS
        * NO_SANITIZATION

### Request example (replace file parameters with actual csv files)

```
POST /file/conversion?file=<file1>&file=<file2>&produces=json&consumes=csv&sort=asc&sanitize=SKIP_EMPTY_CELLS
```

### Notes

- #### Project structure is a bit experimental and tries to be 'by feature with layers'
  e.g.
  ```  
  * file
      * service
        * parser
          * csv
            * service
            * util 
      * controller
      * util
  ```
- #### Code Conventions
  Underscore (_) before a variable name indicates that variable may seem unused in var scope, but it's needed.

  e.g.
  ```
   @RequestParam(name = SupportedParamName.PRODUCES) ProduceableFileType _produceableFileType,
  ```
  Even though the variable isn't directly used in controller's method body, it still describes a controller param.

## Prerequisites

Make sure you have the following software installed:
Java JDK (version 20 or higher)
Maven

## Getting Started

Clone the repository:

```
git clone <repository-url>
```

Navigate to the project directory:

```
cd file-producer-api
```

Build the project:

```
mvn clean install
```

Run the application:

```
java -jar target/file-producer-api.jar
```

The application will start on http://localhost:8080.