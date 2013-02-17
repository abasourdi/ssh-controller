ssh-controller
==============

Android application to create a ssh controller

Current bugs

- On windows, directories and files with space may fail (need investigation) As we currently escape those characters with an antislash (linux style)
- You can select a controller when it's not connected
- It should disconnect you when you lose the connection to an access point.

TODO list:

- Wake on lan
- Better design
- Handling the connections in a better way
- Possibility to change the ip address, username or password
- possibility to check if the current commands are still available

Ideas:

- tail/less?
- action on release button
- action on repeat at an interval 