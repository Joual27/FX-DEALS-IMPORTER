FX Deals Importer

This is a Spring Boot application for importing and processing FX (Foreign Exchange) deals. It supports creating single or multiple deals in a batch, with validation and error handling to ensure data integrity.


API Documentation :

    POST /api/v1/deals: Create single or multiple deals.

Case 1: All Deals Are Valid (201 - CREATED)

    Request:
    [
      {"id": "D009", "fromCurrency": "EUR", "toCurrency": "GBP", "amount": 200},
      {"id": "D0010", "fromCurrency": "EUR", "toCurrency": "GBP", "amount": 500}
    ]

    Response:
    {
      "status": "success",
      "message": "deals created successfully !",
      "timestamps": "2025-03-21T01:11:17.494127928",
      "data": [
        {
          "id": "D009",
          "fromCurrency": "EUR",
          "toCurrency": "GBP",
          "amount": 200.0,
          "madeAt": "2025-03-21T01:11:17.432713745"
        },
        {
          "id": "D0010",
          "fromCurrency": "EUR",
          "toCurrency": "GBP",
          "amount": 500.0,
          "madeAt": "2025-03-21T01:11:17.490001993"
        }
      ]
    }

Case 2: Partial Success (207 - Multi-Status)

    Request:
    [
    {"id": null, "fromCurrency": "EUR", "toCurrency": "GBP", "amount": 200}, //ID is null (To trigger validation errors !)
    {"id": "D0012", "fromCurrency": "EUR", "toCurrency": "EUR", "amount": 200}, // SAME CURRENCY HERE TO TRIGGER CURRENCY EXCEPTION
    {"id": "D009", "fromCurrency": "EUR", "toCurrency": "USD", "amount": 200}, // HERE TEH EXAMPLE OF AN ALREADY EXISTING ID !
    {"id": "D00105", "fromCurrency": "INVALID", "toCurrency": "GBP", "amount": 500}, // INVALID CURRENCY ISO CODE 
    {"id": "D00105", "fromCurrency": "USD", "toCurrency": "GBP", "amount": 500} // VALID ONE
    ]

    Response:
    {
      "successfulDeals": [
        {
          "id": "D00105",
          "fromCurrency": "USD",
          "toCurrency": "GBP",
          "amount": 500.0,
          "madeAt": "2025-03-21T01:15:30.324692998"
        }
      ],
      "message": "some deals couldn't be processed !",
      "errors": [
        "Deal [ID: null]: ID is required",
        "Deal D0012: Currencies must be different!",
        "Deal D009: There's already an existing deal with this ID!",
        "Deal D00105: Invalid ISO currency code - null"
      ]
    }

Testing

    Created Tests for service method with different scenarios using :  
    
     - JUnit: Used for unit testing with extensive test coverage.

     - Mockito: Used for mocking dependencies in unit tests.

Dockerization

Docker Compose is used to orchestrate the deployment of two services:

- Spring Boot Application: Runs on port 8080.

- PostgreSQL Database: Runs on port 5432.

Makefile Commands

The Makefile provides shortcuts for common tasks like starting, stopping, and testing the application. Here are the available commands:

Commands :

    make up:

        Starts the application and database in detached mode (background).

        make up

    make down:

        Stops and removes the application and database containers.

        make down

    make test:

        Runs the unit tests .

        make test