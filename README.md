Library for working with Yandex Calendar on CalDAV protocol

Technologies: Java 17
Compiled in: Java 17

Dependencies: apache http client

The library provides functions for CRUD operations with calendar events


Before starting work, you need to get a key from the calendar server:

1) Open the Account Management page in Yandex ID. (https://passport.yandex.ru/profile/)
2) In the Passwords and Authorization section, select Enable application passwords. Confirm the action.
3) Click Create a new password.
4) Select the Calendar application type and come up with a password name.
5) Click Create. The application password will be displayed in a pop-up window

your server credentials:
login = your yandex login + @yandex.ru
password = password generated in the algorithm above