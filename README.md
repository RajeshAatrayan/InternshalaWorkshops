# InternshalaWorkshops
Where students can enroll the workshops hosted by a company and can acess their dashboard to check the number of workshops he/she applied.
## Getting Started
```
Assume there is a company that conducts workshops for students. Design and develop an Android app that 
allows students to register for these workshop.
```
### Requirements
```
Screens to be developed:
‘Signup’ screen - For students to register
‘Login’ screen - For students to login
‘Available workshops’ screen - A screen which lists all the available workshops. This page must be accessible
to all users, irrespective of whether or not they are logged in. The list of available workshops and their
details must be fetched from SQLite database. You may manually hard code entries in the 
database (no need to make a form to enter workshops and their details in the database). Each workshop must have
an ‘Apply’ button. Functionality of this button-If student is not logged in, this button should redirect
the student to the login screen.
If student is logged in, this button should record this event (that the student has applied for the workshop)
Student ‘Dashboard’ screen - It should display the list of workshops the student has applied to. This should
be the default screen after student login.Student should not be allowed to register for a workshop twice.

Technical details:
a) Use of the following components is mandatory:
Activities
Fragments
Shared preferences
SQlite
b) Use of one activity and multiple fragments to fulfill the requirements is recommended.
```
### Screenshots
<div>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286147.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286157.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286169.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286199.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286220.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286236.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286250.png" width="280" heighth="410"/>
  <img src="https://github.com/RajeshAatrayan/InternshalaWorkshops/blob/master/Screenshot_1542286255.png" width="280" heighth="410"/>
  </div>
  
### Database Used

* Sqlite
* SharedPreferences
