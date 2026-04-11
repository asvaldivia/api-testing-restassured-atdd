@bookings
Feature: Bookings

  Scenario: User can book a room
    Given Rooms and dates available
    When User books a room
    Then Room is booked successfully
    And Room and date is not available to be booked

  Scenario: A booking can be cancelled
    Given There is a booking
    When Booking is cancelled
    Then Booking is cancelled successfully
    And Room and date is available again to be booked