PROBLEM STATEMENT:

It is three step process:
STEP 1: customer registration
mobile OTP is sent via an API call
name, date of birth, document type dropdown [aadhaar, voter id, pan, driving licence], document ID
All the feild need to be validated. validation of document id will happen based on type selected
Input area for opt send form the screen first.
Operator will ask otp form customer and will enter and verify it.
API call to verify otp
once the customer is verified step one completes. User can navigate back but can't edit.
STEP 2: station selection. from and to
screen one: A list of station is displayed where user can select source and destination also price is displayed
screen two: ticket booking summary. displays name document id, source, destination and price.
confirmation dialog is asked. API to generate ticket id.
once confirmed step two is completed and will become non editable.
**List of station is fetched from the api should be stored into the local database.
STEP 3: Success step with booking details
Mock all the API using either postman or https://github.com/mirrajabi/okhttp-json-mock
TOTAL APIS:
sendOtp
verifyOTP
fetchStations
confirmBooking
 

FUNCTIONAL OVERVIEW:

Added App Icon (Bus Symbol), Splash Screen (TickerBook name)

Screen 1: Registration Form:

Validations: Done using Regex
Name - empty,no Special Characters, 40 chars
DOB - min 18 years to max 100 years
DocumentID - Empty check, Selected Document ID checks
PAN - actual PAN validation
Aadhar -actual Aadhar validation
DL - actual DL validation
Voter ID - actual VoterID valdiation

API integrated sendOTP - integrated Notification for OTP

Screen 2: OTP : 245676 will be default OTP 

Validations: empty, actual OTP validation
if wrong OTP entered, Invalid OTP error shown
API integrated VerifyOTP

Screen 3: Stations

List of Stations with Prices

calls fetchStations API to get available Stations as source,destination,price array.

Two Drop Downs with Source and Destination, textview with Price is shown.

On Changing Source and Destination, the price will change.

Validation:
if source and destination are same it will show alert, source and destination cannot be same.

Screen 4: 

On Pressing book, a brief of booking details like documentId, source, destination and price is shown.
On Pressing confirm, ConfirmBooking API called, which returns ticketID.

Screen 5:
Booking summary shown with  documentId,TicketID , source, destination and price.



Solution Approach:

    okhttp json mock has been used for mocking API calls along with RXJava for adding retry feature.
    Room ORM has been used with SQLite for saving and querying Stations List.
    A single Activity with ViewPager and multiple Fragments with Step Completion indication has been added for UI flow.
    Material Design colors has been used for the app.
    Animation Fade in has been used Splash Screen.
    Custom Launcher Icon has been used in alerts and App launcher.
    All the Reusable functions have been moved to interface called Utility.
    CardView has been used as a background for each screen.  







