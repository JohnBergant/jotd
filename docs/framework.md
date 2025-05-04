### Java framework

Opting to go with Spring Boot as it is a well-known product with a vast amount of documentation to help with bootstrappgin a new service.
It offers robust libraries for interfacing with different storage and a strong security suite.

### Datastore

Opting to go with MongoDB for the datastore. The date will be used as the key for the document. 
Joke and description will be part of the document. Initially we won't support searching on anything other than the date.
The daily nature of the records aligns well with the key based lookup and having a document store enables flexibility for adding new features to the joke.

