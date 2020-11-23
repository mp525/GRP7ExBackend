# Getting started with startcode for backend

## Setup
* Change persistence.xml to match your local database and setup connection
* In pom.xml change remote server to your droplet domain name.
* Clean/build and run to make sure everything works.

## Usage guide
* Run the main method in SetupTestUsers.java to get test users in local database.(remember to change username and password)
* LoginEndpoint.java contains endpoint to login a user and creates a token.
* DemoResource.java contains endpoints only usable if you are already logged in as either a user or admin.
* DefaultResource.java contains endpoint which fetches data from other servers' endpoints.
* FetchFacade.java contains default urls for api's, and runs multiple threads parallel to fetch the information. Change these for relevant use.
