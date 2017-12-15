# Airline Ticket Reservation System
### Software Design Final Project @ CSUMB
<details>
    <summary>
       Main GUI  
    </summary>
    <p>
        <img src="screenshot/1.png" width="200"/>
        <img src="screenshot/2.png" width="200"/>
        <img src="screenshot/3.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Create a correct account 
    </summary>
    <ol>
        <li>Select "Register"</li>
        <li>Username: Otter17</li>
        <li>Password: Otter17</li>
    </ol>
    <p>
        <img src="screenshot/4.png" width="200"/>
        <img src="screenshot/5.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Manage the system - Verify the new account
    </summary>
    <ol>
        <li>Login as Administrator</li>
        <li>Username: admin2</li>
        <li>Password: admin2</li>
    </ol>
    <p>
        <img src="screenshot/6.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Create an incorrect account
    </summary>
    <p>
        <img src="screenshot/7.png" width="200"/>
        <img src="screenshot/8.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Create an incorrect account - Duplicated username
    </summary>
    <ol>
        <li>Select "Sign Up"</li>
        <li>Username: chris21</li>
        <li>Password: CHRIS21</li>
    </ol>
    <p>
        <img src="screenshot/9.png" width="200"/>
    </p>
    <ol>
        <li>Select "Sign Up"</li>
        <li>Username: Otter17</li>
        <li>Password: Otter17</li>
    </ol>
    <p>
        <img src="screenshot/10.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Reserve seat
    </summary>
    <ol>
        <li>Login as Otter17 (pwd: Otter17)</li>
        <li>Departure: Monterey</li>
        <li>Arrival: Seattle</li>
        <li>Number of tickets: 2</li>
    </ol>
    <p>
        The System confirms it and displays<br/>
        <img src="screenshot/11.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Reserve seat - One more trial
    </summary>
    <ol>
        <li>Departure: Los Angeles</li>
        <li>Arrival: Monterey</li>
        <li>Number of tickets: 6</li>
    </ol>
    <p>
        <img src="screenshot/12.png" width="200"/>
        <img src="screenshot/13.png" width="200"/>
    </p>
    <p>
        The System confirms it and displays<br/>
        <img src="screenshot/14.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Reserve seat - Handle No Seats Available
    </summary>
    <ol>
        <li>Departure: Monterey </li>
        <li>Arrival: Los Angeles</li>
        <li>Number of tickets: 10</li>
    </ol>
    <p>
        <img src="screenshot/15.png" width="200"/>
        <img src="screenshot/16.png" width="200"/>
    </p>
    <ol>
        <li>Departure: Monterey </li>
        <li>Arrival: Los Angeles</li>
        <li>Number of tickets: 5</li>
    </ol>
    <p>
        The System confirms it and displays<br/>
        <img src="screenshot/17.png" width="200"/>
        <img src="screenshot/18.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Reserve seat - Handle No Flight Available
    </summary>
    <ol>
        <li>Departure: Seattle</li>
        <li>Arrival: Los Angeles</li>
        <li>Number of tickets: 1</li>
    </ol>
    <p>
        <img src="screenshot/19.png" width="200"/>
    </p>
</details>
<details>
    <summary>
        Cancel reservation - Handle No Reservation
    </summary>
    <ol>
        <li>Login as "alice5" (pwd: csumb100)</li>
        <li>Select "My Reservations"</li>
    </ol>
    <p>
        <img src="screenshot/20.png" width="200"/>
    </p>
</details>
