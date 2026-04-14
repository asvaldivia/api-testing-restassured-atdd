@cancel-bookings
Feature: Cancell bookings auth mock

Scenario: A booking can be cancelled with auth mock
Given There is a booking auth mock
When Booking is cancelled auth mock
Then Booking is cancelled successfully auth mock
