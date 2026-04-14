@booking-reports
Feature: Booking reports

 Scenario: User requests total bookings of all rooms
   Given I have multiple bookings
   When I ask for a report of my totals of bookings
   Then I will receive a list of bookings
   And Each booking is represented by an object
   And Total of bookings are correct